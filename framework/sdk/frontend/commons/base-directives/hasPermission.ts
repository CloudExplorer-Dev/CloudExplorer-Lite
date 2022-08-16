import type { APP } from "vue";
import pinia from "@/stores";
import { moduleStore } from "../stores/module";
import { Permission } from "../api/permission";
const moduleStoreObj = moduleStore(pinia);
export default {
  install: (app: APP) => {
    app.directive("hasPermission", async (el: any, binding: any) => {
      const permissions: Array<Permission> =
        await moduleStoreObj.getPermission();
      if (typeof binding.value === "string") {
        const hasPermission = permissions.some((item) => {
          return item.id === binding.value;
        });
        if (!hasPermission) {
          el.style.display = "none";
        }
      }
      if (binding.value instanceof Array) {
        const hasPermission = permissions.some((item) => {
          return binding.value.includes(item.id);
        });
        if (!hasPermission) {
          el.style.display = "none";
        }
      }
    });
  },
};
