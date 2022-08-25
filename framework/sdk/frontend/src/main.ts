import { createApp } from "vue";
import ElementPlus from "element-plus";
import * as ElementPlusIcons from "@element-plus/icons-vue";
import "element-plus/dist/index.css";
import "fit2cloud-ui-plus/src/styles/index.scss";
import route from "./router";
import pinia from "@/stores";
import App from "./App.vue";
import common from "../commons/index";
import "nprogress/nprogress.css";
import "@commons/styles/index.scss";
import "@commons/font/iconfont.css";
import mitt from "mitt";
import microApp from "@micro-zoe/micro-app";
import { setupMock } from "@commons/mock"; //mock
import { servicesStore } from "@commons/stores/services";
import { RootMicroapp } from "./microapp";

if (import.meta.env.MODE === "development") {
  //dev环境开启mock
  setupMock(import.meta.glob("@/mock/*/index.ts", { eager: true }));
}
const app = createApp(App);

app.use(
  new RootMicroapp(async () => {
    await servicesStore(pinia).init();
    return servicesStore(pinia).runingModules;
  }, microApp)
);
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
app.use(pinia);
app.use(common);
app.use(route.router);
console.log("app", app);
app.mount("#app");
