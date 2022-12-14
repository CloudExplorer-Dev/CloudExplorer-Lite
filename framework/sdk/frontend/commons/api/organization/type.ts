import type { Workspace } from "@commons/api/workspace/type";

export interface Organization {
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
  /**
   * 创建时间
   */
  createTime: string;
  /**
   * 修改时间
   */
  updateTime: string;
}

export interface OrganizationTree extends Organization {
  /**
   * 子组织
   */
  children?: Array<OrganizationTree>;
  /**
   * 工作空间
   */
  workspaces?: Array<Workspace>;
}
