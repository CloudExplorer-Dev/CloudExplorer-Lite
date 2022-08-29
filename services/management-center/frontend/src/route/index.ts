import { Route, appContent, login } from "ce-base";
import { createWebHashHistory } from "vue-router";
import { moduleStore } from "ce-base/commons/stores/module";
import pinia from "@/stores";
const baseRoute = window.__MICRO_APP_BASE_APPLICATION__
  ? [
      {
        path: "/",
        name: "home",
        component: appContent,
      },
      {
        path: "/login",
        name: "login",
        component: login,
      },
    ]
  : undefined;
export default new Route(
  createWebHashHistory(),
  import.meta.glob("@/views/*/*.vue"),
  baseRoute ? baseRoute : undefined,
  async () => {
    const routes = await moduleStore(pinia).getMenu();
    return routes;
  },
  async () => {
    return {
      permissions: await moduleStore(pinia).getPermission(),
      role: (await moduleStore(pinia).getCurrentRole()).id,
    };
  }
);
