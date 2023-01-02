export interface Workspace {
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
  createTime?: string;
  /**
   *修改时间
   */
  updateTime?: string;
}

export interface WorkspaceTree {
  id: string;
  pid?: string;
  name: string;
  children?: Array<WorkspaceTree>;
}
