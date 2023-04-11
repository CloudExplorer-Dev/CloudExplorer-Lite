import { fileURLToPath, URL } from "node:url";
import DefineOptions from "unplugin-vue-define-options/vite";
import type { ProxyOptions } from "vite";
import { defineConfig, loadEnv } from "vite";
import vue from "@vitejs/plugin-vue";

import { createHtmlPlugin } from "vite-plugin-html";

const envDir = "./env";

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  const ENV = loadEnv(mode, envDir);
  const proxyConf: Record<string, string | ProxyOptions> = {};
  proxyConf[ENV.VITE_BASE_PATH + "api"] =
    "http://localhost:" + Number(ENV.VITE_BASE_API_PORT);
  proxyConf[ENV.VITE_BASE_PATH + "login"] =
    "http://localhost:" + Number(ENV.VITE_BASE_API_PORT);

  return {
    server: {
      cors: true,
      host: "0.0.0.0",
      port: Number(ENV.VITE_APP_PORT),
      strictPort: true,
      proxy: proxyConf,
    },
    base: ENV.VITE_BASE_PATH,
    envDir: envDir,
    plugins: [
      vue(),
      DefineOptions(),
      createHtmlPlugin({
        minify: true,
        inject: {
          data: {
            //将环境变量 VITE_APP_TITLE 赋值给 title 方便 html页面使用 title 获取系统标题
            VITE_APP_TITLE: ENV.VITE_APP_TITLE,
            VITE_APP_NAME: ENV.VITE_APP_NAME,
          },
        },
      }),
    ],
    resolve: {
      alias: {
        "@": fileURLToPath(new URL("./src", import.meta.url)),
        "@commons": fileURLToPath(
          new URL("../../../framework/sdk/frontend/commons", import.meta.url)
        ),
      },
    },
  };
});
