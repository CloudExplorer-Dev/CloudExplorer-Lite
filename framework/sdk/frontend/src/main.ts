import { createApp } from "vue";
import ElementPlus from "element-plus";
import * as ElementPlusIcons from "@element-plus/icons-vue";
import "element-plus/dist/index.css";
import "fit2cloud-ui-plus/src/styles/index.scss";
import { initRouteObj } from "@commons/router";
import { setupStore } from "@commons/stores";
import App from "./App.vue";
import common from "@commons/index";
import "nprogress/nprogress.css";
import "@commons/styles/index.scss";
import "@commons/font/iconfont.css";
import mitt from "mitt";
import { setupMicroApp } from "@/microapp";
import type { RouteObj } from "@commons/router/type";

const app = createApp(App);

//全局启用 pinia store
setupStore(app);

setupMicroApp(app);

// 注册elementIcon
for (const [key, component] of Object.entries(ElementPlusIcons)) {
  app.component(key, component);
}
const ElementPlusIconsVue: object = ElementPlusIcons;
// 将elementIcon放到全局
app.config.globalProperties.$antIcons = ElementPlusIconsVue;
// 将elementIcon放到全局
app.config.globalProperties.$bus = mitt();
app.use(ElementPlus);

app.use(common);

//设置router

let route: RouteObj | null;

initRouteObj().then((result) => {
  route = result;

  if (route) {
    route.setRouteComponent(import.meta.glob("@/views/*/*.vue"));
    app.use(route.router);
  }

  app.mount("#app");
});
