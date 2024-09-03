import { LandscapeElementId } from './LandscapeElementId';

export class LandscapeUnselectionTree {
  public static readonly EMPTY: LandscapeUnselectionTree = new LandscapeUnselectionTree([]);

  constructor(public readonly elements: LandscapeElementId[]) {}

  public includes(element: LandscapeElementId): boolean {
    return this.elements.some(unselectedElement => unselectedElement.get() === element.get());
  }
}
