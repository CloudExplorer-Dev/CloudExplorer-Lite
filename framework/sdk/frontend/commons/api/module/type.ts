import type { Menu, RequiredPermissions } from "../menu/type";
import type { Permission } from "../permission/type";

export interface RouteModule {
  /**
   *路由名称
   */
  name: string;
  /**
   *路由path
   */
  path: string;
  /**
   * 组建path
   */
  componentPath: string;
  props: {
    /**
     * 模块名称
     */
    moduleName: string;
    /**
     * 模块名称
     */
    name: string;
    /**
     * 模块地址
     */
    url: string;
    /**
     * 模块路由地址
     */
    baseRoute: string;
  };

  requiredPermissions?: Array<RequiredPermissions>;
}

export class Module {
  id: string;
  name: string;
  icon: string;
  order: number;
  basePath?: string;

  requiredPermissions?: Array<RequiredPermissions>;

  constructor(
    id: string,
    name: string,
    icon: string,
    order: number,
    basePath: string,

    requiredPermissions?: Array<RequiredPermissions>
  ) {
    this.id = id;
    this.name = name;
    this.icon = icon;
    this.order = order;
    this.basePath = basePath;

    this.requiredPermissions = requiredPermissions;
  }
}

interface ServerInfo {
  /**
   *模块信息
   */
  moduleInfo: Module;
  /**
   *菜单
   */
  menus: Menu;
  /**
   * 所属权限
   */
  permissions: Array<Permission>;
}

export type { ServerInfo };
