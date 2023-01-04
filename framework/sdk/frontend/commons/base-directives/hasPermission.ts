import type { App } from "vue";
import _ from "lodash";
import { store } from "@commons/stores";
import type { RequiredPermissions } from "@commons/api/menu/type";
import { usePermissionStore } from "@commons/stores/modules/permission";

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
  const rolePermission = _.find(requiredPermissions, (permission) => {
    if (permission == null) {
      return false;
    }
    return permission.role === role;
  });
  if (rolePermission) {
    //只要菜单中需要权限有任意一个存在，则表示有权限访问
    return _.some(rolePermission.permissions, (permissionItem) =>
      _.some(
        permissions,
        (permission) => permission === permissionItem.simpleId
      )
    );
  }
  return false;
};

const display = async (el: any, binding: any) => {
  const permissionStore = usePermissionStore(store);
  const has = permissionStore.hasPermission(binding);
  if (!has) {
    el.style.display = "none";
  } else {
    delete el.style.display;
  }
};

export default {
  install: (app: App) => {
    app.directive("hasPermission", {
      async created(el: any, binding: any) {
        display(el, binding);
      },
      async beforeUpdate(el: any, binding: any) {
        display(el, binding);
      },
    });
  },
};
