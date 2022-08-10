interface Role {
  /**
   * 角色主键id
   */
  id: string;
  /**
   * 角色名称
   */
  name: string;
  /**
   *描述
   */
  description: string;
  /**
   *角色类型
   */
  type: string;
  /**
   *角色父id
   */
  parentId: string | null | undefined;
}
export type { Role };
