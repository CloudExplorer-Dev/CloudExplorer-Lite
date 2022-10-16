import type { App } from "vue";
import type { SimpleMap } from "@commons/api/base/type";
import CeForm from "@commons/components/ce-form/index.vue";
const components: SimpleMap<any> = import.meta.glob(
  "@commons/components/ce-form/*/*.vue",
  {
    eager: true,
  }
);
const install = (app: App) => {
  Object.keys(components).forEach((key: string) => {
    const commentName: string = key
      .substring(key.lastIndexOf("/") + 1, key.length)
      .replace(".vue", "");
    app.component(commentName, components[key].default);
  });
  app.component("CeForm", CeForm);
};
export default { install };
