import { createApp } from "vue";
import type { App as AppInstance } from "vue";
import ElementPlus from "element-plus";
import * as ElementPlusIcons from "@element-plus/icons-vue";
import "element-plus/dist/index.css";
import pinia from "@/stores";
import App from "./App.vue";
import ceBase from "ce-base";
import "ce-base/lib/style.css";
import "ce-base/commons/styles/index.scss";
import { AppMicroapp } from "./microapp";
import route from "./route";
import { setupMock } from "ce-base/commons/mock"; //mock
if (import.meta.env.MODE === "development") {
  //dev环境开启mock
  setupMock(import.meta.globEager("@/mock/*/index.ts"));
}
const app = createApp(App);
const mount = () => {
  // 注册elementIcone
  for (const [key, component] of Object.entries(ElementPlusIcons)) {
    app.component(key, component);
  }
  app.use(ceBase);
  app.use(route.router);
  // 将elementIcone放到全局
  app.use(ElementPlus);
  app.use(pinia);
  app.mount(`#${import.meta.env.VITE_APP_NAME}`);
  return app;
};

app.use(new AppMicroapp(mount, import.meta.env.VITE_APP_NAME, route.history));
