import type { App } from "vue";
import Route from "@/../commons/router/index";
import appContent from "@/../commons/components/layout/app-content/index.vue";
import login from "@/../commons/business/login/index.vue";
import { i18n } from "./base-locales/index";

const components = import.meta.globEager("./components/**/index.vue");

const business = import.meta.globEager("./base-*/index.ts");

const install = (app: App) => {
  Object.keys(business).forEach((key: string) => {
    const plugin: any = business[key];
    app.use(plugin.default);
  });

  Object.keys(components).forEach((key: string) => {
    const replaceKey = key.replace("/index.vue", "");
    app.component(
      replaceKey.substring(replaceKey.lastIndexOf("/") + 1, replaceKey.length),
      components[key].default
    );
  });
};
export default { install };
export { Route, appContent, login, i18n };
