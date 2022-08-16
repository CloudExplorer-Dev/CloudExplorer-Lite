import { defineStore } from "pinia";
import { getCurrentModule, Module } from "../api/module";
import { getCurrentMenus, Menu, RequiredPermissions } from "../api/menu";
import { getPermission } from "../api/permission";
import { getCurrentRole } from "../api/role";
import type { Role } from "../api/role";
import type { Permission } from "../api/permission";
interface RouteItem {
  /**
   *路由名称
   */
  name: string;
  /**
   * 理由路径
   */
  path: string;
  /**
   * 组建路径
   */
  componentPath: string;
  /**
   *重定向
   */
  redirect?: string | any;
}

interface Route {
  [propName: string]: Array<RouteItem>;
}
/**
 * 扁平化后菜单
 */
interface fMenu extends Menu {
  parentName: string;
}
/**
 * 扁平化菜单转换为路由
 * @param menus 菜单
 * @param newArr 空数组
 * @param prentPath 父path
 * @returns 扁平化后的数组
 */
const flatMenuToRoute = (
  menus: Array<Menu>,
  prentPath: string,
  currentRole: Role,
  permissions: Array<Permission>
) => {
  return flatMenu(menus, [], prentPath)
    .filter((menu) => {
      return hasPermission(menu, currentRole, permissions);
    })
    .map((menu) => {
      const newRoute: RouteItem = {
        path: menu.path,
        name: menu.name,
        componentPath: menu.componentPath,
        redirect: menu.redirect ? { name: menu.redirect } : undefined,
      };

      return newRoute;
    });
};

export const hasPermission = (
  menu: Menu,
  currentRole: Role,
  permissions: Array<Permission>
) => {
  const requiredPermissions: Array<RequiredPermissions> =
    menu.requiredPermissions;
  if (!menu.requiredPermissions || requiredPermissions.length === 0) {
    return true;
  }
  for (let i = 0; i < requiredPermissions.length; i++) {
    const roleOk = requiredPermissions[i].role === currentRole.id;
    const permissionOk = permissions.some((item) => {
      if (
        requiredPermissions[i].permissions ||
        requiredPermissions[i].permissions.length > 0
      ) {
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

/**
 * 过来有权限的菜单
 * @param menus       需要过滤的菜单
 * @param currentRole 当前角色
 * @param permissions 当前权限
 * @returns
 */
const filterMenu = (
  menus: Array<Menu>,
  currentRole: Role,
  permissions: Array<Permission>
) => {
  const flatMenus = flatMenu(menus, []);
  const filterMenu = flatMenus.filter((item) => {
    return hasPermission(item, currentRole, permissions);
  });
  return revertMenu(filterMenu);
};
/**
 * 扁平化菜单
 * @param menus
 * @param newMenus
 * @param prentPath
 * @returns
 */
export const flatMenu = (
  menus: Array<Menu>,
  newMenus: Array<fMenu>,
  prentPath?: string,
  prentName = "rootPrentName"
) => {
  menus.forEach((item) => {
    const newMenu: fMenu = {
      ...item,
      parentName: prentName,
      children: [],
      path:
        prentPath === undefined || prentPath === null
          ? item.path
          : prentPath + item.path,
    };
    newMenus.push(newMenu);
    if (item.children != null && item.children.length > 0) {
      flatMenu(
        item.children,
        newMenus,
        prentPath === undefined || prentPath === null
          ? prentPath
          : newMenu.path,
        item.name
      );
    }
  });
  return newMenus;
};

/**
 * 将菜单对象转换回去
 * @param flatMenus
 * @returns
 */
const revertMenu = (flatMenus: Array<fMenu>) => {
  const menus: Array<Menu> = [];
  flatMenus = JSON.parse(JSON.stringify(flatMenus));
  flatMenus.sort((s1) => {
    if (s1.parentName === "rootPrentName") {
      return -1;
    }
    return 1;
  });

  flatMenus.forEach((item) => {
    if (item.parentName === "rootPrentName") {
      menus.push(item);
      menus.sort((s1, s2) => {
        return s1.order - s2.order;
      });
    } else {
      const parentMenu = flatMenus.find((menu) => {
        return menu.name === item.parentName;
      });
      if (parentMenu) {
        parentMenu.children?.push(item);
        parentMenu.children?.sort((s1, s2) => {
          return s1.order - s2.order;
        });
      }
    }
  });
  return menus;
};

/**
 * 重写菜单Path
 * @param menus     菜单
 * @param prentPath 父菜单path
 * @returns         将子菜单path与父亲菜单拼接
 */
const resetMenuPath = (menus: Array<Menu>, prentPath: string) => {
  menus.forEach((menu) => {
    menu.path = prentPath + menu.path;
    if (menu.children != null && menu.children.length > 0) {
      resetMenuPath(menu.children, menu.path);
    }
  });
  return menus;
};

export const moduleStore = defineStore({
  id: "module",
  state: () => ({
    /**
     *当前模块信息
     */
    currentModule: <Module | undefined>{},
    /**
     *当前模块菜单
     */
    currentMenus: <Array<Menu>>[],
    /**
     *当前角色
     */
    currentRole: <Role>{},
    /**
     *权限
     */
    permissions: <Array<Permission>>[],
    /**
     *是否初始化过状态
     */
    initState: <boolean>false,
  }),
  getters: {
    modules(state: any) {
      if (!this.initModule) {
        state.initModule();
        return [];
      }
      return state.runingModules;
    },
    route(state: any) {
      if (!this.currentMenus || this.currentMenus.length === 0) {
        state.initModule();
        return [];
      } else {
        const route: Route = {
          home: flatMenuToRoute(
            this.currentMenus,
            "",
            this.currentRole,
            this.permissions
          ),
        };
        return route;
      }
    },
    menus(state: any) {
      if (!this.currentMenus || this.currentMenus.length === 0) {
        state.initModule();
        return [];
      } else {
        return resetMenuPath(this.currentMenus, "");
      }
    },
  },
  actions: {
    async initModule() {
      // 获取当前角色
      this.currentRole = (await getCurrentRole()).data;
      // 运行模块
      this.currentModule = (await getCurrentModule()).data;
      // 权限
      this.permissions = (await getPermission()).data;
      // 获取当前模块菜单
      const currentMenus = (await getCurrentMenus()).data;
      // 根据权限过滤菜单
      this.currentMenus = filterMenu(
        currentMenus,
        this.currentRole,
        this.permissions
      );
      this.initState = true;
    },
    async getRoute() {
      if (!this.initState) {
        await this.initModule();
      }
      if (this.currentMenus) {
        const route: Route = {
          home: flatMenuToRoute(
            this.currentMenus,
            "",
            this.currentRole,
            this.permissions
          ),
        };
        return route;
      }
      return [];
    },
    /**
     * 获取菜单
     * @returns
     */
    async getMenu() {
      if (!this.initState) {
        await this.initModule();
      }
      return flatMenu(this.currentMenus, [], "");
    },
    async getCurrentRole() {
      if (!this.initState) {
        this.currentRole = (await getCurrentRole()).data;
      }

      return this.currentRole;
    },
    async getPermission() {
      if (!this.initState) {
        await this.initModule();
      }
      return this.permissions;
    },
  },
});
