import globals from 'globals';
import prettier from 'eslint-config-prettier';
import typescript from 'typescript-eslint';
import js from '@eslint/js';
import vue from 'eslint-plugin-vue';

export default typescript.config(
  {
    languageOptions: {
      globals: globals.node,
    },
  },
  {
    ignores: ['target/', 'src/test/resources/'],
  },
  js.configs.recommended,
  ...typescript.configs.recommended.map(config => (config.name === 'typescript-eslint/base' ? config : { ...config, files: ['**/*.ts'] })),
  ...vue.configs['flat/recommended'],
  {
    files: ['**/*.vue'],
    languageOptions: {
      parserOptions: { parser: '@typescript-eslint/parser' },
    },
  },
  {
    files: ['src/*/webapp/**/*.vue', 'src/*/webapp/**/*.ts'],
    rules: {
      quotes: ['error', 'single', { avoidEscape: true }],
      '@typescript-eslint/no-explicit-any': 'off',
      '@typescript-eslint/no-unused-vars': ['error'],
      'vue/multi-word-component-names': 'off',
      'vue/no-reserved-component-names': 'off',
      '@typescript-eslint/no-empty-object-type': 'off',
    },
  },
  prettier,
);
