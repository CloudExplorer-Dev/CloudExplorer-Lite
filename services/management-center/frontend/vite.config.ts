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
      // 自定义插件
      /*(function () {
              let basePath = "";

              return {
                name: "vite:micro-app",
                apply: "build",
                configResolved(config) {
                  basePath = `${config.base}${config.build.assetsDir}/`;
                },
                writeBundle(options: OutputOptions, bundle: OutputBundle) {
                  for (const chunkName in bundle) {
                    if (Object.prototype.hasOwnProperty.call(bundle, chunkName)) {
                      const chunk = bundle[chunkName];
                      if (chunk.fileName && chunk.fileName.endsWith(".js")) {
                        if ((chunk as OutputChunk).code) {
                          (chunk as OutputChunk).code = (
                            chunk as OutputChunk
                          ).code.replace(
                            /(from|import\()(\s*['"])(\.\.?\/)/g,
                            (all, $1, $2, $3) => {
                              return all.replace(
                                $3,
                                new URL($3, basePath).toString()
                              );
                            }
                          );
                          const fullPath = options.dir
                            ? join(options.dir, chunk.fileName)
                            : chunk.fileName;

                          //console.log((chunk as OutputChunk).code);
                          writeFileSync(fullPath, (chunk as OutputChunk).code);
                        }
                      }
                    }
                  }
                },
              };
            })(),*/
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
