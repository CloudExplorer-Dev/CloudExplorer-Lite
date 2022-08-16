import { Menu } from "../menu/type";
import { Permission } from "../permission/type";
interface Module {
  /**
   * 标题
   */
  title: string;
  /**
   * 服务名称
   */
  name: string;
  /**
   * 服务图标
   */
  icon: string;
  /**
   * 服务排序
   */
  order: number;
  /**
   *服务地址
   */
  url: string;
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

export type { Module, ServerInfo };
