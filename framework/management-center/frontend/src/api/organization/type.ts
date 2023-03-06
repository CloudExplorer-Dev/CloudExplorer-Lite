import type {
  Organization,
  OrganizationTree,
} from "@commons/api/organization/type";

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

/**
 * 组织详情
 */
interface OrgDetails {
  /**
   * 组织名称
   */
  name: string;
  /**
   *组织描述
   */
  description: string;
}
/**
 * 创建组织对象
 */
interface CreateOrgFrom {
  /**
   *所属组织
   */
  pid?: string;
  /**
   * 组织详细信息
   */
  orgDetails: Array<OrgDetails>;
}
/**
 * 更新组织对象
 */
interface OrgUpdateForm {
  /**
   *主键id
   */
  id: string;
  /**
   * 父id
   */
  pid?: string;
  /**
   *组织名称
   */
  name: string;
  /**
   *描述
   */
  description: string;
}

export type {
  Organization,
  ListOrganizationRequest,
  OrganizationTree,
  CreateOrgFrom,
  OrgUpdateForm,
};
