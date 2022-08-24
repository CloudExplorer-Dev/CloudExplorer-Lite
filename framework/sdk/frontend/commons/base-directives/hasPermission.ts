import type {App} from "vue";
import pinia from "@/stores";
import {moduleStore} from "@commons/stores/module";
import type {Permission} from "@commons/api/permission";
import type {RequiredPermissions} from "@commons/api/menu";

const moduleStoreObj = moduleStore(pinia);

const hasRolePermission = (
  requiredPermissions: Array<RequiredPermissions>,
  role: string,
  permissions: Array<Permission>
) => {
  if (!requiredPermissions || requiredPermissions.length === 0) {
    return true;
  }
  for (let i = 0; i < requiredPermissions.length; i++) {
    const roleOk = requiredPermissions[i].role === role;
    const permissionOk = permissions.some((item) => {
      if (requiredPermissions[i].permissions) {
        return requiredPermissions[i].permissions.includes(item.id);
      }
      return true;
    });
    if (requiredPermissions[i].logical === "OR") {
      return roleOk || permissionOk;
    } else {
      return roleOk && permissionOk;
    }
  }
};

const hasPermission = async (el: any, binding: any) => {
  const permissions: Array<Permission> = await moduleStoreObj.getPermission();
  const role: string = (await moduleStoreObj.getCurrentRole()).id;
  if (typeof binding.value === "string") {
    const hasPermission = permissions.some((item) => {
      return item.id === binding.value;
    });
    if (!hasPermission) {
      el.style.display = "none";
    } else {
      delete el.style.display;
    }
  }
  if (binding.value instanceof Array && binding.value) {
    let isPermission = false;
    if (typeof binding.value[0] === "string") {
      isPermission = permissions.some((item) => {
        return binding.value.includes(item.id);
      });
    } else {
      if (hasRolePermission(binding.value, role, permissions)) {
        isPermission = true;
      }
    }
    if (!isPermission) {
      el.style.display = "none";
    } else {
      delete el.style.display;
    }
  }
};
export default {
  install: (app: App) => {
    app.directive("hasPermission", {
      async created(el: any, binding: any) {
        hasPermission(el, binding);
      },
      async beforeUpdate(el: any, binding: any) {
        hasPermission(el, binding);
      },
    });
  },
};
