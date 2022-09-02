import { Route, appContent, login } from "@commons/index";
import { createWebHashHistory } from "vue-router";
import { store } from "@commons/stores";
import { useModuleStore } from "@commons/stores/modules/module";
import { useUserStore } from "@commons/stores/modules/user";
import { usePermissionStore } from "@commons/stores/modules/permission";

const baseRoute = window.__MICRO_APP_BASE_APPLICATION__
  ? [
      {
        path: "/",
        name: "home",
        component: appContent,
      },
      {
        path: "/signin",
        name: "signin",
        component: login,
      },
    ]
  : undefined;
export default new Route(
  createWebHashHistory(),
  import.meta.glob("@/views/*/*.vue"),
  baseRoute ? baseRoute : undefined,
  async () => {
    const moduleStore = useModuleStore(store);

    await moduleStore.refreshModules();
    console.log(moduleStore.currentModuleMenu);

    return moduleStore.currentModuleMenu;
  },
  async () => {
    const userStore = useUserStore(store);
    const permissionStore = usePermissionStore(store);
    await permissionStore.refreshPermissions();
    console.log(userStore.isLogin);
    console.log(permissionStore.userPermissions);
    return {
      permissions: permissionStore.userPermissions,
      role: userStore.currentRole,
    };
  }
);
