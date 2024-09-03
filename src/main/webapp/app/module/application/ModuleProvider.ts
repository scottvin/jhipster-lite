import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
import { key } from 'piqure';
import { ProjectFoldersRepository } from '@/module/domain/ProjectFoldersRepository';
import { provide } from '@/injections';
import { RestProjectFoldersRepository } from '@/module/secondary/RestProjectFoldersRepository';
import { ManagementRepository } from '@/module/domain/ManagementRepository';
import { RestManagementRepository } from '@/module/secondary/RestManagementRepository';
import { ModulesRepository } from '@/module/domain/ModulesRepository';
import { RestModulesRepository } from '@/module/secondary/RestModulesRepository';
import { ThemeRepository } from '@/module/domain/ThemeRepository';
import { LocalWindowThemeRepository } from '@/module/secondary/LocalWindowThemeRepository';
import { ModuleParametersRepository } from '@/module/domain/ModuleParametersRepository';
import { LocalStorageModuleParametersRepository } from '@/module/secondary/LocalStorageModuleParametersRepository';
import { LandscapeScroller } from '@/module/primary/landscape/LandscapeScroller';

export const PROJECT_FOLDERS_REPOSITORY = key<ProjectFoldersRepository>('ProjectFoldersRepository');
export const MANAGEMENT_REPOSITORY = key<ManagementRepository>('ManagementRepository');
export const MODULES_REPOSITORY = key<ModulesRepository>('ModulesRepository');
export const THEMES_REPOSITORY = key<ThemeRepository>('ThemesRepository');
export const MODULE_PARAMETERS_REPOSITORY = key<ModuleParametersRepository>('ParamsRepository');
export const LANDSCAPE_SCROLLER = key<LandscapeScroller>('LandscapeScroller');

export const provideForModule = (rest: AxiosHttp): void => {
  provide(PROJECT_FOLDERS_REPOSITORY, new RestProjectFoldersRepository(rest));
  provide(MANAGEMENT_REPOSITORY, new RestManagementRepository(rest));
  provide(MODULES_REPOSITORY, new RestModulesRepository(rest));
  provide(THEMES_REPOSITORY, new LocalWindowThemeRepository(window, localStorage));
  provide(MODULE_PARAMETERS_REPOSITORY, new LocalStorageModuleParametersRepository(localStorage));
  provide(LANDSCAPE_SCROLLER, new LandscapeScroller());
};
