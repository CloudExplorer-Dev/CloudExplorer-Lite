import type { App } from "vue";
import { i18n } from "./base-locales/index";
import type { SimpleMap } from "@commons/api/base/type";
import type { RootMicroApp } from "@/microapp";
import type { MicroApp } from "@micro-zoe/micro-app";

declare global {
  interface Window {
    __MICRO_APP_NAME__: string;
    __MICRO_APP_ENVIRONMENT__: string;
    __MICRO_APP_BASE_APPLICATION__: string;
    rootMicroApp: RootMicroApp;
    /**
     * 子应用获取MicroApp对象
     **/
    microApp: MicroApp;
    [index: string]: any;
  }
}

const components: SimpleMap<any> = import.meta.glob(
  [
    "./components/**/index.vue",
    "./components/ce-table/CeTableColumnSelect.vue",
  ],
  {
    eager: true,
  }
);

const componentsUses: SimpleMap<any> = import.meta.glob(
  "./components/**/index.ts",
  {
    eager: true,
  }
);

const business = import.meta.glob("./base-*/index.ts", { eager: true });

const install = (app: App) => {
  Object.keys(business).forEach((key: string) => {
    const plugin: any = business[key];
    app.use(plugin.default);
  });

  Object.keys(components).forEach((key: string) => {
    const replaceKey = key.replace(".vue", "").replace("/index", "");
    app.component(
      replaceKey.substring(replaceKey.lastIndexOf("/") + 1, replaceKey.length),
      components[key].default
    );
  });
  Object.keys(componentsUses).forEach((key: string) => {
    const component: any = componentsUses[key];
    app.use(component.default);
  });
};
export default { install };
export { i18n };
