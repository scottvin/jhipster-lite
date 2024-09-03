import { defineConfig } from 'vitest/config';
import path from 'path';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src/main/webapp/app'),
    },
  },
  plugins: [
    vue({
      template: {
        compilerOptions: {
          isCustomElement: tag => /^x-/.test(tag),
        },
      },
    }),
  ],
  test: {
    include: ['src/test/webapp/unit/**/*.spec.ts'],
    logHeapUsage: true,
    poolOptions: {
      threads: {
        minThreads: 1,
        maxThreads: 2,
      },
    },
    environment: 'jsdom',
    cache: false,
    reporters: ['default', 'vitest-sonar-reporter'],
    outputFile: {
      'vitest-sonar-reporter': 'target/test-results/TESTS-results-sonar.xml',
    },
    coverage: {
      all: false,
      clean: true,
      thresholds: {
        perFile: true,
        autoUpdate: true,
        100: true,
      },
      exclude: [
        'src/main/webapp/**/*.component.ts',
        'src/main/webapp/app/main.ts',
        'src/main/webapp/app/router/index.ts',
        'src/main/webapp/app/**/application/*Provider.ts',
        'src/main/webapp/app/shared/alert/infrastructure/primary/WindowApplicationListener.ts',
        'src/main/webapp/app/module/secondary/RestManagementRepository.ts',
        'src/main/webapp/app/injections.ts',
        '**/*.d.ts',
        'src/test/**/*',
      ],
      provider: 'istanbul',
      reportsDirectory: 'target/test-results/',
      reporter: ['html', 'json-summary', 'text', 'text-summary', 'lcov', 'clover'],
      watermarks: {
        statements: [100, 100],
        branches: [100, 100],
        functions: [100, 100],
        lines: [100, 100],
      },
    },
  },
});
