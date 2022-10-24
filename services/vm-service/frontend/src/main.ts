import { createApp } from "vue";
import ElementPlus from "element-plus";
import * as ElementPlusIcons from "@element-plus/icons-vue";
import "element-plus/dist/index.css";
import Fit2CloudPlus from "fit2cloud-ui-plus";
import "fit2cloud-ui-plus/src/styles/index.scss";
import { setupStore } from "@commons/stores";
import App from "./App.vue";
import common from "@commons/index";
import { i18n } from "@commons/index";

import "@commons/styles/index.scss";
import { AppMicroApp } from "@commons/microapp";
import { initRouteObj, getRoute } from "@commons/router";

let app = null;

const mount = async () => {
  app = createApp(App);
  // 注册elementIcon
  for (const [key, component] of Object.entries(ElementPlusIcons)) {
    app.component(key, component);
  }
  // 将elementIcon放到全局
  app.config.globalProperties.$antIcons = ElementPlusIcons;
  // 将elementIcon放到全局
  app.use(ElementPlus, {
    locale: i18n.global.messages.value[i18n.global.locale.value],
  });
  app.use(common);

  //设置router
  await initRouteObj();
  const route = getRoute();
  if (route) {
    route.setRouteComponent(import.meta.glob("@/views/**/**.vue"));

    app.use(route.router);
  }

  app.use(Fit2CloudPlus);

  //全局启用 pinia store
  setupStore(app);

  app.mount(`#${import.meta.env.VITE_APP_NAME}`);
  return app;
};

new AppMicroApp(mount, import.meta.env.VITE_APP_NAME).install();
