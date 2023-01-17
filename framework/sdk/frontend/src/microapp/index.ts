import type { Module } from "@commons/api/module/type";
import { endsWith, trimEnd, trimStart } from "lodash";
import type { MicroApp } from "@micro-zoe/micro-app";
import type { App } from "vue";
import { store } from "@commons/stores";
import microApp from "@micro-zoe/micro-app";
import { getUrl, useModuleStore } from "@commons/stores/modules/module";
import Config from "@commons/utils/constants";
import _ from "lodash";

export class RootMicroApp {
  /**
   *获取模块信息
   */
  getModules: () => Promise<Array<Module>> | Array<Module>;
  /**
   * microApp对象
   */
  microApp: MicroApp;
  /**
   * 插件modules
   */
  modules: any = {};

  constructor(
    getModules: () => Promise<Array<Module>> | Array<Module>,
    microApp: MicroApp
  ) {
    this.getModules = getModules;
    this.microApp = microApp;
    window.rootMicroApp = this;
  }
  /**
   *解决microApp路径携带子模块名称问题
   * @param code       子模块代码
   * @param moduleName 子模块名称
   * @param moduleUrl  子模块url
   * @returns          去除模块名称的代码
   */
  resetCode(code: string, baseName: string, moduleUrl: string) {
    // 拼接模块url和模块名称
    const totalUrl =
      moduleUrl + (endsWith(moduleUrl, "/") ? "" : "/") + baseName + "/";
    const parsedName = this.regScapeStr("/" + baseName + "/");
    // 生成正则
    const regx = `(from|import)((\\s*['"])|(\\S*['"]))(${parsedName})`;

    // 将子模块的引入都替换掉
    const newCode = code.replace(new RegExp(regx, "g"), (all: string) => {
      const result = all.replace("/" + baseName + "/", totalUrl);
      return result;
    });
    return newCode;
  }

  regScapeStr(str: string): string {
    return str.replace(/[-/\\^$*+?.()|[\]{}]/g, "\\$&");
  }

  install(app?: App) {
    const getModules = this.getModules();
    if (getModules instanceof Array<Module>) {
      this.setModules(getModules);
    }
    this.microApp.start({
      plugins: { modules: this.modules },
      /*自定义fetch*/
      fetch(url, options, appName) {
        const config = {
          headers: {
            "ce-micro-app": Config.MICRO_APP_PREFIX + appName,
          },
        };
        return window.fetch(url, Object.assign(options, config)).then((res) => {
          return res.text();
        });
      },
      /*关闭虚拟路由系统*/ /*不能关，关了之后基座不能发信息给子应用*/
      /*"disable-memory-router": true,*/
      /*关闭对子应用请求的拦截*/
      "disable-patch-request": true,
    });

    //预加载
    if (getModules instanceof Array<Module>) {
      this.preFetch(getModules);
    }
  }

  /**
   * 预加载 （目前没生效，第一次访问子模块还是会重新加载）
   * @param modules
   */
  preFetch(modules: Array<Module>) {
    if (modules?.length > 0) {
      console.debug("preFetch modules!", modules);
      this.microApp.preFetch(
        _.map(modules, (m) => {
          return {
            name: m.id,
            url: getUrl(m),
            esmodule: true,
            inline: true,
            disableSandbox: true,
            baseroute: m.basePath,
          };
        })
      );
    }
  }

  /**
   * 根据接口返回数据获取运行中的模块,替换代码
   */
  async updateModule() {
    const modules = await this.getModules();
    this.setModules(modules);
    this.preFetch(modules);
  }

  /**
   * 修改模块
   * @param modules 模块数据
   */
  setModules(modules: Array<Module>) {
    modules.forEach((module) => {
      // 去掉模块名称前后斜杠/
      const baseName = trimStart(trimEnd("/" + module.name, "/"), "/");
      // eslint-disable-next-line @typescript-eslint/no-this-alias
      const _self = this;
      this.modules[baseName] = [
        {
          loader(code: string) {
            return _self.resetCode(
              code,
              baseName,
              module.basePath ? module.basePath : ""
            );
          },
        },
      ];
    });
  }
}

export function setupMicroApp(app: App<Element>) {
  const moduleStore = useModuleStore(store);
  app.use(
    new RootMicroApp(async () => {
      await moduleStore.refreshModules();
      console.debug(moduleStore.runningModules);
      return moduleStore.runningModules;
    }, microApp)
  );
}
