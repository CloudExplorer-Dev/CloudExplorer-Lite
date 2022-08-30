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
   * 创建时间
   */
  createTime: number;
  /**
   *父id
   */
  pid?: string;
  /**
   *级别
   */
  level: number;
}

interface ListOrganizationRequest {
  /**
   *每页显示多少
   */
  pageSize: number;
  /**
   *当前页
   */
  currentPage: number;
}

interface OrganizationTree {
  children: Array<OrganizationTree>;
}

export type { Organization, ListOrganizationRequest, OrganizationTree };
