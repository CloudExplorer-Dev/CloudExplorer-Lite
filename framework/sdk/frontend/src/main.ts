import { createApp } from "vue";
import ElementPlus from "element-plus";
import * as ElementPlusIcons from "@element-plus/icons-vue";
import "element-plus/dist/index.css";
import pinia from "@/stores";
import App from "./App.vue";
import common from "../commons/index";
import "nprogress/nprogress.css";
import "../commons/styles/index.scss";
import route from "./router";
import "../commons/font/iconfont.css";
const app = createApp(App);
import { setupMock } from "../commons/mock"; //mock
if (import.meta.env.MODE === "development") {
  //dev环境开启mock
  setupMock(import.meta.globEager("@/mock/*/index.ts"));
}

// 注册elementIcon
for (const [key, component] of Object.entries(ElementPlusIcons)) {
  app.component(key, component);
}
const ElementPlusIconsVue: object = ElementPlusIcons;
// 将elementIcon放到全局
app.config.globalProperties.$antIcons = ElementPlusIconsVue;
// 将elementIcon放到全局

app.use(ElementPlus);
app.use(pinia);
app.use(common);
app.use(route.router);
app.mount("#app");
