module.exports = {
  root: true,
  parser: '@typescript-eslint/parser',
  plugins: ['cypress', '@typescript-eslint'],
  env: {
    mocha: true,
    'cypress/globals': true,
  },
};
