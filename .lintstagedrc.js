export default {
  '*.{js,cjs,ts,tsx,vue}': ['eslint --fix', 'prettier --write'],
  '*.{css,scss}': ['stylelint --fix', 'prettier --write'],
  '*.pug': ['eslint --fix', 'prettier --write'],
  '*.{md,json,yml,html,java,xml,feature,sh}': ['prettier --write'],
};
