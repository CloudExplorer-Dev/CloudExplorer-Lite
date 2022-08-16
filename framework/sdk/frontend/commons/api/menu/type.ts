/**
 * 当前菜单的权限与角色
 */
interface RequiredPermissions {
  /**
   *角色
   */
  role: string;
  /**
   * 关系
   */
  logical: string;

  /**
   *权限
   */
  permissions: Array<string>;
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
