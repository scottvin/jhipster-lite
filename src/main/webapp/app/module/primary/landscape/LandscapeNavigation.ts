import { ModuleSlug } from '@/module/domain/ModuleSlug';
import { LandscapeElement } from '@/module/domain/landscape/LandscapeElement';
import { LandscapeElementId } from '@/module/domain/landscape/LandscapeElementId';
import { LandscapeLevel } from '@/module/domain/landscape/LandscapeLevel';
import { LandscapeModule } from '@/module/domain/landscape/LandscapeModule';

export class LandscapeNavigation {
  private currentModule: number;
  private currentElement: number;
  private currentLevel: number;
  private landscapeElements: Map<string, HTMLElement>;
  private levels: LandscapeLevel[];

  constructor(landscapeElements: Map<string, HTMLElement>, levels: LandscapeLevel[]) {
    [this.currentLevel, this.currentElement, this.currentModule] = this.setValues(0, 0, 0);
    this.landscapeElements = landscapeElements;
    this.levels = levels;
  }

  private level(): LandscapeLevel {
    return this.levels[this.currentLevel];
  }

  private getLevelByIndex(index: number): LandscapeLevel {
    return this.levels[index];
  }

  private element(): LandscapeElement {
    return this.level().elements[this.currentElement];
  }

  private module(): LandscapeModule {
    return this.element().allModules()[this.currentModule];
  }

  public getSlug(): ModuleSlug {
    return this.module().slug();
  }

  public goUp(): void {
    if (this.currentModule == 0) {
      this.currentElement = this.decrease(this.currentElement);
      this.currentModule = this.element().allModules().length - 1;
    } else {
      this.currentModule = this.decrease(this.currentModule);
    }
  }

  public goDown(): void {
    if (this.currentModule == this.element().allModules().length - 1) {
      this.currentElement = this.increase(this.currentElement, this.level().elements.length - 1);
      this.currentModule = 0;
    } else {
      this.currentModule = this.increase(this.currentModule, this.element().allModules().length - 1);
    }
  }

  public goLeft(): void {
    const current_module = this.module().slug();

    this.currentLevel = this.decrease(this.currentLevel);

    const [elementIndex, moduleIndex] = this.calculatePosition(current_module);

    this.updateCursor(this.currentLevel, elementIndex, moduleIndex);
  }

  public goRight(): void {
    const current_module = this.module().slug();

    this.currentLevel = this.increase(this.currentLevel, this.levels.length - 1);

    const [elementIndex, moduleIndex] = this.calculatePosition(current_module);

    this.updateCursor(this.currentLevel, elementIndex, moduleIndex);
  }

  public goToDependency(): void {
    if (this.module().dependencies().length == 0) {
      return;
    }

    const [levelIndex, elementIndex, moduleIndex] = this.findDependencyPosition();

    this.updateCursor(levelIndex, elementIndex, moduleIndex);
  }

  public goToDependent(): void {
    const [levelIndex, elementIndex, moduleIndex] = this.findDependentPostion();
    this.updateCursor(levelIndex, elementIndex, moduleIndex);
  }

  private isInModuleDependencies(dependencies: LandscapeElementId[]): boolean {
    return dependencies.findIndex(depenecie => depenecie.get() == this.module().slug().get()) >= 0;
  }

  private findModuleIndexInDependencies(modules: LandscapeModule[]): number {
    return modules.findIndex(module => {
      return this.isInModuleDependencies(module.dependencies());
    });
  }

  private isInDependencies(slug: string): boolean {
    return (
      this.module()
        .dependencies()
        .findIndex(dependency => dependency.get() == slug) >= 0
    );
  }

  private findModuleIndexWithSlug(modules: LandscapeModule[]): number {
    return modules.findIndex(module => this.isInDependencies(module.slug().get()));
  }

  private findDependencyElementPosition(elements: LandscapeElement[]): [number, number] {
    let moduleIndex = 0;
    const elementIndex = elements.findIndex(element => {
      if (this.isInDependencies(element.slug().get())) {
        moduleIndex = 0;
        return true;
      } else {
        moduleIndex = this.findModuleIndexWithSlug(element.allModules());
        return moduleIndex >= 0;
      }
    });

    return [elementIndex, moduleIndex];
  }

  private findDependencyPosition(): [number, number, number] {
    const levelIndex = this.currentLevel - 1;
    const [elementIndex, moduleIndex] = this.findDependencyElementPosition(this.getLevelByIndex(levelIndex).elements);

    return [levelIndex, elementIndex, moduleIndex];
  }

  private findDependentElementPosition(elements: LandscapeElement[]): [number, number] {
    let moduleIndex = 0;
    const elementIndex = elements.findIndex(element => {
      moduleIndex = this.findModuleIndexInDependencies(element.allModules());
      return moduleIndex >= 0;
    });

    return [elementIndex, moduleIndex];
  }

  private findDependentPostion(): [number, number, number] {
    let [levelIndex, elementIndex, moduleIndex] = this.setValues(this.currentLevel, this.currentElement, this.currentModule);

    while (levelIndex < this.levels.length - 1) {
      levelIndex += 1;

      [elementIndex, moduleIndex] = this.findDependentElementPosition(this.getLevelByIndex(levelIndex).elements);
      if (elementIndex >= 0) {
        break;
      }
    }

    if (elementIndex == -1) {
      return [this.currentLevel, this.currentElement, this.currentModule];
    }

    return [levelIndex, elementIndex, moduleIndex];
  }

  private getHtmlElement(moduleSlug: string): HTMLElement {
    return this.landscapeElements.get(moduleSlug) as HTMLElement;
  }

  private calculateDistance(moduleHtmlElement: HTMLElement, currentModuleHtml: HTMLElement) {
    return Math.abs(moduleHtmlElement.getBoundingClientRect().y - currentModuleHtml.getBoundingClientRect().y);
  }

  private calculatePosition(current_module: ModuleSlug): [number, number] {
    const currentModuleHtml = this.getHtmlElement(current_module.get());
    let [elementIndex, moduleIndex, smallestDistance] = this.setValues(0, 0, Infinity);

    this.level().elements.forEach((element, element_index) => {
      element.allModules().forEach((module, module_index) => {
        const moduleHtmlElement = this.getHtmlElement(module.slug().get());
        const distance = this.calculateDistance(moduleHtmlElement, currentModuleHtml);
        if (distance < smallestDistance) {
          [smallestDistance, elementIndex, moduleIndex] = this.setValues(distance, element_index, module_index);
        }
      });
    });

    return [elementIndex, moduleIndex];
  }

  private setValues(i1: number, i2: number, i3: number): [number, number, number] {
    return [i1, i2, i3];
  }

  private increase(value: number, max: number): number {
    if (value < max) {
      return value + 1;
    } else {
      return max;
    }
  }

  private decrease(value: number): number {
    if (value > 0) {
      return value - 1;
    } else {
      return 0;
    }
  }

  private updateCursor(currentLevel: number, currentElement: number, currentModule: number): void {
    [this.currentLevel, this.currentElement, this.currentModule] = this.setValues(currentLevel, currentElement, currentModule);
  }
}
