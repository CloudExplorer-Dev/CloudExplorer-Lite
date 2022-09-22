import { fileURLToPath, URL } from "node:url";
import DefineOptions from "unplugin-vue-define-options/vite";
import type { ConfigEnv } from "vite";
import { defineConfig, loadEnv } from "vite";
import type { ProxyOptions } from "vite";
import vue from "@vitejs/plugin-vue";
import vueSetupExtend from "vite-plugin-vue-setup-extend";

import dts from "vite-plugin-dts"; //生成d.ts

/**
 * 公共模块打包配置
 */
const commonBuild = {
  build: {
    outDir: fileURLToPath(new URL("./lib", import.meta.url)),
    rollupOptions: {
      external: [
        "vue",
        "pinia",
        "vue-router",
        "@/config/modules.config",
        "@/config/basePathConfig",
        "@/locales/lang/zh-cn",
        "@/locales/lang/zh-tw",
        "@/locales/lang/en",
        "@/stores",
      ],
      output: {
        globals: {
          vue: "Vue",
          pinia: "pinia",
          "vue-router": "vue-router",
          "@/config/modules.config": "@/config/modules.config",
          "@/config/basePathConfig": "@/config/basePathConfig",
          "@/locales/lang/zh-cn": "@/locales/lang/zh-cn",
          "@/locales/lang/zh-tw": "@/locales/lang/zh-tw",
          "@/locales/lang/en": "@/locales/lang/en",
          "@/stores": "@/stores",
        },
      },
    },
    lib: {
      entry: fileURLToPath(new URL("./commons/index.ts", import.meta.url)),
      name: "ceBase",
      fileName: (format: string) => `ce-base.${format}.js`,
    },
  },
};

const envDir = "./env";

/**
 * 当前模块打包
 */
const thisBuild = {
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

// 根据mode 判断打包依赖包还是当前项目
export default defineConfig(({ mode }: ConfigEnv) => {
  const ENV = loadEnv(mode, envDir);
  let config = { ...thisBuild };
  if (mode === "lib") {
    config = { ...config, ...commonBuild };
    //生成d.ts
    config.plugins.push(
      dts({
        outputDir: fileURLToPath(new URL("./lib", import.meta.url)),
        tsConfigFilePath: "./tsconfig.json",
      })
    );
  }

  const proxyConf: Record<string, string | ProxyOptions> = {};
  proxyConf[ENV.VITE_BASE_PATH + "api"] =
    "http://localhost:" + Number(ENV.VITE_BASE_API_PORT);
  proxyConf[ENV.VITE_BASE_PATH + "login"] =
    "http://localhost:" + Number(ENV.VITE_BASE_API_PORT);
  proxyConf[ENV.VITE_BASE_PATH + "management-center"] = "http://127.0.0.1:5001";
  proxyConf[ENV.VITE_BASE_PATH + "vm-service"] = "http://127.0.0.1:5002";

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
