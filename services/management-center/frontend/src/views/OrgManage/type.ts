interface OrgDetails {
  name: string;
  description: string;
}
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
interface UpdateForm {
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
export type { CreateOrgFrom, OrgDetails, UpdateForm };
