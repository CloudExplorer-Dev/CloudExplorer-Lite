import { createApp } from "vue";
import ElementPlus from "element-plus";
import * as ElementPlusIcons from "@element-plus/icons-vue";
import "element-plus/dist/index.css";
import { createPinia } from "pinia";
import App from "./App.vue";
import common from "../commons/index";
import "nprogress/nprogress.css";
import "../commons/styles/common.scss";
import route from "./router";

const app = createApp(App);
import { setupMock } from "../commons/mock"; //mock
if (import.meta.env.MODE === "development") {
  //dev环境开启mock
  setupMock(import.meta.globEager("@/mock/*/index.ts"));
}

// 注册elementIcone
for (const [key, component] of Object.entries(ElementPlusIcons)) {
  app.component(key, component);
}
const ElementPlusIconsVue: object = ElementPlusIcons;
// 将elementIcone放到全局
app.config.globalProperties.$antIcons = ElementPlusIconsVue;
// 将elementIcone放到全局

app.use(ElementPlus);
app.use(createPinia());
console.log(common);
app.use(route);
app.mount("#app");
