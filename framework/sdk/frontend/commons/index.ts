import type { App } from "vue";
import Route from "./router/index";
const business = import.meta.globEager("./base-*/index.ts");
const install = (app: App) => {
  Object.keys(business).forEach((key: string) => {
    const plugin: any = business[key];
    app.use(plugin.default);
  });
};
export default { install };
export { Route };
