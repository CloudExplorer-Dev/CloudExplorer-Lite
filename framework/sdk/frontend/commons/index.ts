import type { App } from "vue";
import Route from "./router/index";
import appContent from "./components/layout/appContent/index.vue";
import login from "./business/login/index.vue";
const business = import.meta.globEager("./base-*/index.ts");
const install = (app: App) => {
  Object.keys(business).forEach((key: string) => {
    const plugin: any = business[key];
    app.use(plugin.default);
  });
};
export default { install };
export { Route, appContent, login };
