/**
 * 当前菜单的权限与角色
 */
interface RequiredPermissions {
  /**
   *角色
   */
  role: string;
  /**
   *权限
   */
  permissions: Array<MenuPermissionItem>;
}

interface MenuPermissionItem {
  simpleId: string;
}

interface MenuItem {
  /**
   * 标题
   */
  title: string;
  /**
   * 名称
   */
  name: string;
  /**
   * 路由path
   */
  path: string;
  /**
   * 图标
   */
  icon: string;
  /**
   *排序
   */
  order: number;
}

/**
 * 菜单对象
 */
interface Menu extends MenuItem {
  sourceMenu?: string;
  /**
   * 组建Path
   */
  componentPath: string;
  /**
   * 子菜单
   */
  children?: Array<Menu>;
  /**
   * 重定向
   */
  redirect?: string;
  /**
   *当前菜单所属权限
   */
  requiredPermissions: Array<RequiredPermissions>;
  /**
   *子菜单路由 当前路由在home下面 与当前menu在同一级别
   */
  operations?: Array<Menu>;
  /**
   *子菜单路由 属于当前路由的字路由
   */
  childOperations?: Array<Menu>;
}

/**
 * 获取菜单的请求参数
 */
interface MenuRequest {
  /**
   *模块名称
   */
  moduleName: string;
}
interface ModuleMenu {
  [propName: string]: Array<Menu>;
}
export type { Menu, MenuRequest, MenuItem, RequiredPermissions, ModuleMenu };
