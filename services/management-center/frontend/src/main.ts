import { createApp } from "vue";
import ElementPlus from "element-plus";
import * as ElementPlusIcons from "@element-plus/icons-vue";
import "element-plus/dist/index.css";
import { createPinia } from "pinia";
import App from "./App.vue";
import ceBase from "ce-base";
import { Route } from "ce-base";
import { createWebHashHistory } from "vue-router";
const app = createApp(App);
// 注册elementIcone
for (const [key, component] of Object.entries(ElementPlusIcons)) {
  app.component(key, component);
}
app.use(ceBase);
app.use(new Route(createWebHashHistory(), import.meta.glob("@/views/*/*.vue")));
// 将elementIcone放到全局
app.use(ElementPlus);
app.use(createPinia());

app.mount("#app");
