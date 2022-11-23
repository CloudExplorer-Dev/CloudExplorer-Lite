interface Organization {
  /**
   *主键id
   */
  id: string;
  /**
   *组织名称
   */
  name: string;
  /**
   *描述
   */
  description?: string;
  /**
   *父id
   */
  pid?: string;
}
interface Workspace {
  /**
   *主键id
   */
  id: string;
  /**
   *工作空间名称
   */
  name: string;
  /**
   *工作空间描述
   */
  description: string;
  /**
   *组织id
   */
  organizationId: string;
  /**
   *创建时间
   */
  createTime: string;
  /**
   *修改时间
   */
  updateTime: string;
}

interface OrganizationTree extends Organization {
  /**
   * 子组织
   */
  children: Array<OrganizationTree>;
  /**
   * 工作空间
   */
  workspaces: Array<Workspace>;
}

interface OrganizationWorkspace {
  /**
   *主键id
   */
  id: string;
  /**
   *名称
   */
  name: string;
  /**
   * 组织或者工作空间
   */
  type: "WORKSPACE" | "ORGANIZATION";
}
/**
 * 组织或者工作空间树
 */
interface OrganizationWorkspaceTree extends OrganizationWorkspace {
  children?: Array<OrganizationWorkspaceTree>;
}
export type {
  Organization,
  OrganizationTree,
  Workspace,
  OrganizationWorkspaceTree,
  OrganizationWorkspace,
};
