import { fileURLToPath, URL } from "node:url";
import DefineOptions from "unplugin-vue-define-options/vite";
import type { ConfigEnv } from "vite";
import { defineConfig, loadEnv } from "vite";
import type { ProxyOptions } from "vite";
import vue from "@vitejs/plugin-vue";
import vueSetupExtend from "vite-plugin-vue-setup-extend";

import { createHtmlPlugin } from "vite-plugin-html";

const commonBuild = {
  build: {
    rollupOptions: {
      output: {
        manualChunks(id: string) {
          if (id.includes("node_modules")) {
            return id
              .toString()
              .split("node_modules/")[1]
              .split("/")[0]
              .toString();
          }
        },
      },
    },
  },
};

const envDir = "./env";

/**
 * 当前模块打包
 */
// 根据mode 判断打包依赖包还是当前项目
export default defineConfig(({ mode }: ConfigEnv) => {
  const ENV = loadEnv(mode, envDir);
  const config = {
    plugins: [
      //alias(),
      vue({
        template: {
          // 添加以下内容
          compilerOptions: {
            isCustomElement: (tag) => /^micro-app/.test(tag), //排除<micro-app>标签
          },
        },
      }),
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
      vueSetupExtend(),
    ],
    resolve: {
      alias: {
        "@": fileURLToPath(new URL("./src", import.meta.url)),
        "@commons": fileURLToPath(new URL("./commons", import.meta.url)),
      },
    },
    envDir: envDir,
    server: {},
  };

  //config = { ...config, ...commonBuild };

  const proxyConf: Record<string, string | ProxyOptions> = {};
  proxyConf[ENV.VITE_BASE_PATH + "api"] =
    "http://localhost:" + Number(ENV.VITE_BASE_API_PORT);
  proxyConf[ENV.VITE_BASE_PATH + "login"] =
    "http://localhost:" + Number(ENV.VITE_BASE_API_PORT);

  //management-center
  proxyConf[ENV.VITE_BASE_PATH + "management-center/api"] =
    "http://localhost:" + Number(ENV.VITE_BASE_API_PORT);
  proxyConf[ENV.VITE_BASE_PATH + "management-center?management-center"] =
    "http://localhost:" + Number(ENV.VITE_BASE_API_PORT);
  proxyConf[ENV.VITE_BASE_PATH + "management-center"] = "http://127.0.0.1:5001";

  //vm-service
  proxyConf[ENV.VITE_BASE_PATH + "vm-service/api"] =
    "http://localhost:" + Number(ENV.VITE_BASE_API_PORT);
  proxyConf[ENV.VITE_BASE_PATH + "vm-service"] = "http://127.0.0.1:5002";

  //finance-management
  proxyConf[ENV.VITE_BASE_PATH + "finance-management/api"] =
    "http://localhost:" + Number(ENV.VITE_BASE_API_PORT);
  proxyConf[ENV.VITE_BASE_PATH + "finance-management"] =
    "http://127.0.0.1:5003";

  //security-compliance
  proxyConf[ENV.VITE_BASE_PATH + "security-compliance/api"] =
    "http://localhost:" + Number(ENV.VITE_BASE_API_PORT);
  proxyConf[ENV.VITE_BASE_PATH + "security-compliance"] =
    "http://127.0.0.1:5004";

  //operation-analysis
  proxyConf[ENV.VITE_BASE_PATH + "operation-analysis/api"] =
    "http://localhost:" + Number(ENV.VITE_BASE_API_PORT);
  proxyConf[ENV.VITE_BASE_PATH + "operation-analysis"] =
    "http://127.0.0.1:5005";

  //https://cn.vitejs.dev/config/server-options.html#server-host
  config.server = {
    cors: true,
    host: "0.0.0.0",
    port: Number(ENV.VITE_APP_PORT),
    strictPort: true,
    proxy: proxyConf,
  };

  return config;
});
