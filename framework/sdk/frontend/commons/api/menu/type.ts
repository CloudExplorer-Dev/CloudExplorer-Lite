/**
 * 菜单对象
 */
interface Menu {
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
   * 组建Path
   */
  componentPath: string;
  /**
   * 子菜单
   */
  children?: Array<Menu>;
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
export type { Menu, MenuRequest };
