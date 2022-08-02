import { createApp } from "vue";
import ElementPlus from "element-plus";
import * as ElementPlusIcons from "@element-plus/icons-vue";
import "element-plus/dist/index.css";
import { createPinia } from "pinia";
import App from "./App.vue";
import theme from "../commons/index";
import { Route } from "../commons/index";
import { createWebHistory } from "vue-router";
const app = createApp(App);
// 注册elementIcone
for (const [key, component] of Object.entries(ElementPlusIcons)) {
  app.component(key, component);
}
// 将elementIcone放到全局
app.use(ElementPlus);
app.use(createPinia());
console.log(theme);
app.use(theme);
app.use(new Route(createWebHistory(), import.meta.glob("@/views/*/*.vue")));
app.mount("#app");
