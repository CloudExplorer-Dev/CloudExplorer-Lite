interface Permission {
  /**
   *权限唯一标示
   */
  id: string;
  /**
   *权限名称
   */
  name: string;
  /**
   *所属权限,用于分组
   */
  resourceId: string;
  /**
   *所属角色
   */
  parentRoles: Array<string>;
}
interface ModulePermission {
  [propName: string]: Permission;
}

export type { Permission, ModulePermission };
