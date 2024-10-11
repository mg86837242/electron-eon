import react from '@vitejs/plugin-react-swc';
import { defineConfig, loadEnv } from 'vite';
import svgr from 'vite-plugin-svgr';

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  return {
    plugins: [react(), svgr()],
    server: {
      host: env.VITE_SERVER_HOST || 'localhost',
      port: Number(env.VITE_SERVER_PORT) || 5173,
    },
  };
});

// References:
// -- https://vitejs.dev/guide/env-and-mode.html#env-variables
// -- https://vitejs.dev/guide/env-and-mode.html#env-files
// -- https://vitejs.dev/guide/env-and-mode.html#modes: By default, the dev
//    server (`dev` command) runs in `development` mode and the `build` command
//    runs in `production` mode. This means when running vite build, it will
//    load the env variables from `.env.production` if there is one.
// -- https://vitejs.dev/config/#using-environment-variables-in-config
// -- https://vitejs.dev/config/shared-options.html#envprefix
// -- https://dev.to/whchi/how-to-use-processenv-in-vite-ho9
// -- https://vitejs.dev/config/shared-options.html#define: This will cause
//    linting error as the linter can't resolve the constants/variables defined
//    in Vite's `define` option
// -- https://thedkpatel.medium.com/dockerizing-react-application-built-with-vite-a-simple-guide-4c41eb09defa:
//    Set `host` to `0.0.0.0` in `vite.config.js` to ensure the Vite dev server
//    binds to all network interfaces inside the container, allowing access
//    from external devices like your host machine. This will be used in tandem
//    with a `.env.production` on top of a `.env`.
