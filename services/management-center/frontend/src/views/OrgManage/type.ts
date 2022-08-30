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
export type { CreateOrgFrom, OrgDetails };
