import { Condition } from "@/components/complex-table";
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
  condition: Array<Condition | string>;
  /**
   *每页显示多少
   */
  pageSize: number;
  /**
   *当前页
   */
  currentPage: number;
}

export type { Organization, ListOrganizationRequest };
