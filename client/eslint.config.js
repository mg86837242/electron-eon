import { FlatCompat } from '@eslint/eslintrc';
import js from '@eslint/js';
import pluginQuery from '@tanstack/eslint-plugin-query';
import eslintConfigPrettier from 'eslint-config-prettier';
import reactHooks from 'eslint-plugin-react-hooks';
import reactRefresh from 'eslint-plugin-react-refresh';
import reactJsxRuntime from 'eslint-plugin-react/configs/jsx-runtime.js';
import reactRecommended from 'eslint-plugin-react/configs/recommended.js';
import simpleImportSort from 'eslint-plugin-simple-import-sort';
import globals from 'globals';
import path from 'path';
import { fileURLToPath } from 'url';

// mimic CommonJS variables -- not needed if using CommonJS
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const compat = new FlatCompat({
  baseDirectory: __dirname,
});

// "... the later object taking precedence over any previous objects": https://eslint.org/docs/latest/use/configure/rules#using-configuration-files
// "... as the new config system is based on chaining": https://github.com/jsx-eslint/eslint-plugin-react?tab=readme-ov-file#configuring-shared-settings
export default [
  // https://eslint.org/docs/latest/use/configure/migration-guide#predefined-and-shareable-configs
  js.configs.recommended,
  // https://github.com/jsx-eslint/eslint-plugin-react?tab=readme-ov-file#configuring-shared-settings
  reactRecommended,
  reactJsxRuntime,
  // https://tanstack.com/query/latest/docs/eslint/eslint-plugin-query
  ...pluginQuery.configs['flat/recommended'],
  // "make sure to put it (`eslint-config-prettier`) last": https://github.com/prettier/eslint-config-prettier?tab=readme-ov-file#installation
  eslintConfigPrettier,
  // https://eslint.org/docs/latest/use/configure/configuration-files#globally-ignoring-files-with-ignores
  {
    ignores: [
      'dist',
      '.eslintrc.cjs',
      'eslint.config.js',
      // customized:
      '**/__*/**',
      'tailwind.config.cjs',
    ],
  },
  {
    // props are ordered by: https://eslint.org/docs/latest/use/configure/configuration-files#configuration-objects
    // file extensions coverage: https://github.com/jsx-eslint/eslint-plugin-react?tab=readme-ov-file#flat-configs
    files: ['**/*.{js,mjs,cjs,jsx,mjsx,ts,tsx,mtsx}'],
    languageOptions: {
      ecmaVersion: 2022,
      sourceType: 'module',
      globals: {
        ...globals.node,
        ...globals.browser,
        ...globals.es2021,
      },
    },
    plugins: {
      // https://github.com/facebook/react/issues/28313#issuecomment-2363692765
      'react-hooks': reactHooks,
      // https://github.com/ArnaudBarre/eslint-plugin-react-refresh (PS RRD6.4+ will likely violate this plugin's rules)
      'react-refresh': reactRefresh,
      // https://github.com/lydell/eslint-plugin-simple-import-sort
      'simple-import-sort': simpleImportSort,
    },
    rules: {
      'react/jsx-no-target-blank': 'off',
      'react-refresh/only-export-components': [
        'warn',
        { allowConstantExport: true },
      ],
      // https://github.com/facebook/react/issues/28313#issuecomment-2363692765
      ...reactHooks.configs.recommended.rules,
      // https://dev.to/julioxavierr/sorting-your-imports-with-eslint-3ped && `override` in flat config: https://eslint.org/docs/latest/use/configure/migration-guide#glob-based-configs
      'simple-import-sort/imports': [
        'warn',
        {
          groups: [
            // Packages `react` related packages come first.
            ['^react', '^@?\\w'],
            // Internal packages.
            ['^(@|components)(/.*|$)'],
            // Side effect imports.
            ['^\\u0000'],
            // Parent imports. Put `..` last.
            ['^\\.\\.(?!/?$)', '^\\.\\./?$'],
            // Other relative imports. Put same-folder imports and `.` last.
            ['^\\./(?=.*/)(?!/?$)', '^\\.(?!/?$)', '^\\./?$'],
            // Style imports.
            ['^.+\\.?(css)$'],
          ],
        },
      ],
      // customized:
      // `react/prop-types` is from `eslint-plugin-react`: https://github.com/jsx-eslint/eslint-plugin-react/issues/1966 && https://old.reddit.com/r/typescript/comments/12b1m46/is_it_ok_to_globally_disable_the_prop_types_rule/
      'react/prop-types': 'off',
      'no-unused-vars': 'off',
    },
    settings: {
      react: {
        // linter will show warning w/o this line: https://github.com/jsx-eslint/eslint-plugin-react
        version: 'detect',
      },
    },
  },
];
