import { ApplicationListener } from '@/shared/alert/infrastructure/primary/ApplicationListener';
import { ModulesRepository } from '@/module/domain/ModulesRepository';
import { AppVue } from '@/root/infrastructure/primary';
import { flushPromises, mount, VueWrapper } from '@vue/test-utils';
import { stubAlertBus } from '../shared/alert/domain/AlertBus.fixture';
import {
  applicationBaseNamePropertyDefinition,
  defaultPresets,
  moduleSlug,
  ModulesRepositoryStub,
  optionalBooleanPropertyDefinition,
  stubModulesRepository,
} from '../module/domain/Modules.fixture';
import { ProjectFoldersRepositoryStub, stubProjectFoldersRepository } from '../module/domain/ProjectFolders.fixture';
import { ModuleParametersRepositoryStub, stubModuleParametersRepository } from '../module/domain/ModuleParameters.fixture';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { BodyCursorUpdater } from '@/module/primary/landscape/BodyCursorUpdater';
import { LandscapeScroller } from '@/module/primary/landscape/LandscapeScroller';
import { ModuleParametersRepository } from '@/module/domain/ModuleParametersRepository';
import { APPLICATION_LISTENER, CURSOR_UPDATER, GLOBAL_WINDOW, provide } from '@/injections';
import { ALERT_BUS, ALERT_LISTENER } from '@/shared/alert/application/AlertProvider';
import {
  LANDSCAPE_SCROLLER,
  MANAGEMENT_REPOSITORY,
  MODULE_PARAMETERS_REPOSITORY,
  MODULES_REPOSITORY,
  PROJECT_FOLDERS_REPOSITORY,
  THEMES_REPOSITORY,
} from '@/module/application/ModuleProvider';
import { stubWindow } from '../module/primary/GlobalWindow.fixture';
import { createRouter, createWebHistory, Router } from 'vue-router';
import { routes } from '@/router';
import { AlertListener } from '@/shared/alert/domain/AlertListener';
import { stubAlertListener } from '../shared/alert/domain/AlertListener.fixure';
import { stubTimeout } from '../common/primary/timeout/Timeout.fixture';
import { TIMEOUT } from '@/shared/toast/application/ToastProvider';
import { TimeoutListener } from '@/shared/toast/domain/Timeout';
import { ManagementRepositoryStub, stubLocalManagementRepository } from '../module/domain/ManagementRepository.fixture';
import { LocalWindowThemeRepositoryStub, stubLocalWindowThemeRepository } from '../module/domain/ThemeRepository.fixture';
import { ProjectFoldersRepository } from '@/module/domain/ProjectFoldersRepository';
import { Modules } from '@/module/domain/Modules';
import { defaultLandscape } from '../module/domain/landscape/Landscape.fixture';

interface ApplicationListenerStub extends ApplicationListener {
  addEventListener: vi.fn;
  removeEventListener: vi.fn;
}

interface BodyCursorUpdaterStub extends BodyCursorUpdater {
  set: vi.fn;
  reset: vi.fn;
}

const stubBodyCursorUpdater = (): BodyCursorUpdaterStub =>
  ({
    set: vi.fn(),
    reset: vi.fn(),
  }) as BodyCursorUpdaterStub;

const stubApplicationListener = (): ApplicationListenerStub => ({
  addEventListener: vi.fn(),
  removeEventListener: vi.fn(),
});

const stubLandscapeScroller = (): any => ({
  scroll: vi.fn(),
});

const managementRepositoryStubResolves = (): ManagementRepositoryStub => {
  const management = stubLocalManagementRepository();
  management.getInfo.resolves({ git: { build: { version: '1.0.0' } } });

  return management;
};

interface WrapperOptions {
  alertListener: AlertListener;
  toastTimeout: TimeoutListener;
  cursorUpdater: BodyCursorUpdater;
  landscapeScroller: LandscapeScroller;
  modules: ModulesRepository;
  applicationListener: ApplicationListener;
  moduleParameters: ModuleParametersRepository;
  management: ManagementRepositoryStub;
  theme: LocalWindowThemeRepositoryStub;
  projectFolders: ProjectFoldersRepository;
}

const alertBus = stubAlertBus();

let router: Router;
beforeEach(async () => {
  router = createRouter({
    history: createWebHistory(),
    routes: routes,
  });
});

const wrap = (options?: Partial<WrapperOptions>): VueWrapper => {
  const {
    alertListener,
    toastTimeout,
    applicationListener,
    cursorUpdater,
    landscapeScroller,
    modules,
    moduleParameters,
    management,
    theme,
    projectFolders,
  }: WrapperOptions = {
    alertListener: stubAlertListener(),
    toastTimeout: stubTimeout(),
    cursorUpdater: stubBodyCursorUpdater(),
    landscapeScroller: stubLandscapeScroller(),
    modules: repositoryWithLandscape(),
    applicationListener: stubApplicationListener(),
    moduleParameters: repositoryWithModuleParameters(),
    management: managementRepositoryStubResolves(),
    theme: stubLocalWindowThemeRepository(),
    projectFolders: repositoryWithProjectFolders(),
    ...options,
  };

  provide(ALERT_LISTENER, alertListener);
  provide(TIMEOUT, toastTimeout);
  provide(ALERT_BUS, alertBus);
  provide(APPLICATION_LISTENER, applicationListener);
  provide(CURSOR_UPDATER, cursorUpdater);
  provide(LANDSCAPE_SCROLLER, landscapeScroller);
  provide(MODULES_REPOSITORY, modules);
  provide(MODULE_PARAMETERS_REPOSITORY, moduleParameters);
  provide(GLOBAL_WINDOW, stubWindow());
  provide(PROJECT_FOLDERS_REPOSITORY, repositoryWithProjectFolders());
  provide(MANAGEMENT_REPOSITORY, management);
  provide(THEMES_REPOSITORY, theme);
  provide(PROJECT_FOLDERS_REPOSITORY, projectFolders);

  return mount(AppVue, {
    global: {
      plugins: [router],
    },
  });
};

export const defaultModules = (): Modules =>
  new Modules([
    {
      name: 'Spring',
      modules: [
        {
          slug: moduleSlug('spring-cucumber'),
          description: 'Add cucumber to the application',
          properties: [
            applicationBaseNamePropertyDefinition(),
            optionalBooleanPropertyDefinition(),
            {
              type: 'INTEGER',
              mandatory: false,
              key: 'optionalInteger',
              order: 100,
            },
          ],
          tags: ['server'],
        },
        {
          slug: moduleSlug('banner'),
          description: 'Add a banner to the application',
          properties: [],
          tags: [],
        },
      ],
    },
  ]);

const repositoryWithLandscape = (): ModulesRepositoryStub => {
  const modules = stubModulesRepository();
  modules.landscape.resolves(defaultLandscape());
  modules.preset.resolves(defaultPresets());

  return modules;
};

const repositoryWithModules = (): ModulesRepositoryStub => {
  const modules = stubModulesRepository();
  modules.list.resolves(defaultModules());

  return modules;
};

const repositoryWithProjectFolders = (): ProjectFoldersRepositoryStub => {
  const projectFolders = stubProjectFoldersRepository();
  projectFolders.get.resolves('/tmp/jhlite/1234');

  return projectFolders;
};

const repositoryWithModuleParameters = (): ModuleParametersRepositoryStub => {
  const moduleParameters = stubModuleParametersRepository();
  moduleParameters.store.resolves(undefined);
  moduleParameters.storeCurrentFolderPath.resolves('');
  moduleParameters.getCurrentFolderPath.returns('');
  moduleParameters.get.returns(new Map());
  return moduleParameters;
};

describe('Router', () => {
  describe('Navigation on LandscapeVue', () => {
    it('should navigate on LandscapeVue when the URL is /', async () => {
      router.push('/');

      await router.isReady();

      const wrapper = wrap();

      expect(wrapper.html()).toContain('jhlite-landscape-loader');
    });

    it('should navigate on LandscapeVue when the URL is /landscape', async () => {
      router.push('/landscape');

      await router.isReady();

      const wrapper = wrap();

      expect(wrapper.html()).toContain('jhlite-landscape-loader');
    });
  });

  describe('Navigation on ModulesVue', () => {
    it('should navigate on ModulesVue when the URL is /patches', async () => {
      router.push('/patches');

      await router.isReady();

      const modules = repositoryWithModules();
      modules.apply.resolves(null);
      const wrapper = wrap({ modules });

      await flushPromises();

      expect(wrapper.html()).toContain('jhipster-modules-patch');
    });
  });
});
