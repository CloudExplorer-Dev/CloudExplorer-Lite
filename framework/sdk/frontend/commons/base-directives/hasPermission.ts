import type { App } from "vue";
import { store } from "@commons/stores";
import type { RequiredPermissions } from "@commons/api/menu/type";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useUserStore } from "@commons/stores/modules/user";

/**
 * 判断是否有角色和权限
 * @param role         角色
 * @param permissions  权限
 * @param requiredPermissions  权限
 * @returns
 */
export const hasRolePermission = (
  role: string,
  permissions: Array<string>,
  requiredPermissions?: Array<RequiredPermissions>
): boolean => {
  if (!requiredPermissions || requiredPermissions.length === 0) {
    return true;
  }
  //找到对应角色的权限
  const rolePermission = requiredPermissions.find(
    (permission) => permission.role === role
  );
  if (rolePermission) {
    //只要菜单中需要权限有任意一个存在，则表示有权限访问
    return rolePermission.permissions.some((permissionItem) =>
      permissions.some((permission) => permission === permissionItem.simpleId)
    );
  }
  return false;
};

const hasPermission = async (el: any, binding: any) => {
  const permissionStore = usePermissionStore(store);
  if (permissionStore.userPermissions?.length === 0) {
    await permissionStore.refreshPermissions();
  }
  const permissions: Array<string> = permissionStore.userPermissions;
  const role: string = useUserStore(store).currentRole;

  if (typeof binding.value === "string") {
    const hasPermission = permissions.some((item) => {
      return item === binding.value;
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
        return binding.value.includes(item);
      });
    } else {
      if (hasRolePermission(role, permissions, binding.value)) {
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
