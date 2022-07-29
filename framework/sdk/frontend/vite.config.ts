import { fileURLToPath, URL } from "node:url";
import DefineOptions from "unplugin-vue-define-options/vite";
import { defineConfig, loadEnv } from "vite";
import type { ConfigEnv } from "vite";
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
        "element-plus",
        "@/config/basePathConfig",
      ],
      output: {
        globals: {
          vue: "Vue",
          pinia: "pinia",
          "vue-router": "vue-router",
          "@/config/modules.config": "@/config/modules.config",
          "element-plus": "element-plus",
          "@/config/basePathConfig": "@/config/basePathConfig",
        },
      },
    },
    lib: {
      entry: fileURLToPath(new URL("./packages/index.ts", import.meta.url)),
      name: "frontendPublic",
      fileName: (format: string) => `frontend-public.${format}.js`,
    },
  },
};
/**
 * 当前模块打包
 */
const thisBuild = {
  plugins: [
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
    },
  },
  server: {
    host: "0.0.0.0",
    port: 5502,
    proxy: {
      "/login": {
        target: "http://127.0.0.1:6611", //实际请求地址
        changeOrigin: true,
      },
      "/api/module/runing": {
        target: "http://127.0.0.1:6611", //实际请求地址
        changeOrigin: true,
      },
      "/api/module/current": {
        target: "http://127.0.0.1:6611", //实际请求地址
        changeOrigin: true,
      },
    },
  },
};

// 根据mode 判断打包依赖包还是当前项目
export default defineConfig(({ mode }: ConfigEnv) => {
  const env = loadEnv(mode, "./env");
  console.log(env);
  if (mode === "lib") {
    const config = { ...thisBuild, ...commonBuild };
    //生成d.ts
    config.plugins.push(
      dts({
        outputDir: fileURLToPath(new URL("./lib", import.meta.url)),
        tsConfigFilePath: "./tsconfig.json",
      })
    );
    return config;
  }
  return { ...thisBuild, ...env };
});
