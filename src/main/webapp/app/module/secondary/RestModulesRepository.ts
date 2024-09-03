import { AxiosHttp } from '@/shared/http/infrastructure/secondary/AxiosHttp';
import { AxiosResponse, RawAxiosRequestConfig } from 'axios';
import { Modules } from '../domain/Modules';
import { ModulesRepository } from '../domain/ModulesRepository';
import { ModuleToApply } from '../domain/ModuleToApply';
import { ModuleSlug } from '../domain/ModuleSlug';
import { ProjectFolder } from '../domain/ProjectFolder';
import { Project } from '../domain/Project';
import { Landscape } from '../domain/landscape/Landscape';
import { mapToModules, RestModules } from './RestModules';
import { RestModuleToApply, toRestModuleToApply } from './RestModuleToApply';
import { mapToModuleHistory, RestProjectHistory } from './RestProjectHistory';
import { mapToLandscape, RestLandscape } from './RestLandscape';
import { ProjectHistory } from '../domain/ProjectHistory';
import { ModulesToApply } from '../domain/ModulesToApply';
import { RestModulesToApply, toRestModulesToApply } from './RestModulesToApply';
import { Optional } from '@/shared/optional/domain/Optional';
import { mapToPresets, RestPresets } from '@/module/secondary/RestPresets';
import { Presets } from '@/module/domain/Presets';

export class RestModulesRepository implements ModulesRepository {
  constructor(private axiosInstance: AxiosHttp) {}

  list(): Promise<Modules> {
    return this.axiosInstance.get<RestModules>('/api/modules').then(mapToModules);
  }

  landscape(): Promise<Landscape> {
    return this.axiosInstance.get<RestLandscape>('/api/modules-landscape').then(mapToLandscape);
  }

  async apply(module: ModuleSlug, moduleToApply: ModuleToApply): Promise<void> {
    await this.axiosInstance.post<void, RestModuleToApply>(`/api/modules/${module.get()}/apply-patch`, toRestModuleToApply(moduleToApply));
  }

  async applyAll(modulesToApply: ModulesToApply): Promise<void> {
    await this.axiosInstance.post<void, RestModulesToApply>('/api/apply-patches', toRestModulesToApply(modulesToApply));
  }

  history(folder: ProjectFolder): Promise<ProjectHistory> {
    return this.axiosInstance.get<RestProjectHistory>(`/api/projects?path=${encodeURI(folder)}`).then(mapToModuleHistory);
  }

  async format(folder: ProjectFolder): Promise<void> {
    await this.axiosInstance.post<void, RestModuleToApply>(`/api/format-project?path=${encodeURI(folder)}`);
  }

  download(folder: ProjectFolder): Promise<Project> {
    const config: RawAxiosRequestConfig = {
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
    };

    return this.axiosInstance.get<ArrayBuffer>(`/api/projects?path=${encodeURI(folder)}`, config).then(mapToProject);
  }

  preset(): Promise<Presets> {
    return this.axiosInstance.get<RestPresets>('/api/presets').then(mapToPresets);
  }
}

const mapToProject = (response: AxiosResponse<ArrayBuffer>): Project => ({
  filename: Optional.ofUndefinable(response.headers['x-suggested-filename']).orElseThrow(
    () => new Error('Impossible to download file without filename'),
  ),
  content: response.data,
});
