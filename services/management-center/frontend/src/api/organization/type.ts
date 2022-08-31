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

interface OrganizationTree extends Organization {
  children: Array<OrganizationTree>;
}

export type { Organization, ListOrganizationRequest, OrganizationTree };
