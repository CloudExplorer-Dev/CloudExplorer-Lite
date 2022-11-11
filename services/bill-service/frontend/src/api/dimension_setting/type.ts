import type { SimpleMap } from "@commons/api/base/type";

interface BillAuthorizeRuleCondition {
  /**
   * 唯一id
   */
  id: string;
  /**
   *授权字段
   */
  field: string;
  /**
   *授权字段值
   */
  value: Array<string>;
}
interface BillAuthorizeRuleSettingGroup {
  /**
   *唯一id
   */
  id: string;
  /**
   *账单授权规则条件
   */
  billAuthorizeRules: Array<BillAuthorizeRuleCondition>;
  /**
   *条件与或
   */
  conditionType: string;
}

interface BillAuthorizeRule {
  /**
   *分账规则组
   */
  billAuthorizeRuleSettingGroups: Array<BillAuthorizeRuleSettingGroup>;
  /**
   * 条件与或
   */
  conditionType: string;
}

interface BillDimensionSetting {
  /**
   *主键id
   */
  id: string;
  /**
   *分账设置规则
   */
  authorizeRule: BillAuthorizeRule;
  /**
   *授权id
   */
  authorizeId: string;
  /**
   *类型
   */
  type: string;
  /**
   *创建时间
   */
  createTime: string;
  /**
   * 修改时间
   */
  updateTime: string;
}

interface AuthorizeResourcesResponse {
  /**
   * 资源id
   */
  resourceId: string;
  /**
   * 资源名称
   */
  resourceName: string;
  /**
   * 企业项目id
   */
  projectId: string;
  /**
   * 企业项目名称
   */
  projectName: string;
  /**
   * 产品id
   */
  productId: string;
  /**
   * 产品名称
   */
  productName: string;
  /**
   * 标签
   */
  tags: SimpleMap<string>;
  /**
   * 云账号id
   */
  cloudAccountId: string;
  /**
   * 云账号名称
   */
  cloudAccountName: string;
}
interface AuthorizeResourcesRequest {
  /**
   * 授权对象id
   */
  authorizeId: string;
  /**
   * 授权对象类型
   */
  type: "WORKSPACE" | "ORGANIZATION";
  /**
   * 产品名称
   */
  productName?: string;
  /**
   * 资源名称
   */
  resourceName?: string;
  /**
   *企业项目名称
   */
  projectName?: string;
  /**
   * 云账号名称
   */
  cloudAccountName?: string;
}

export type {
  BillAuthorizeRuleCondition,
  BillAuthorizeRuleSettingGroup,
  BillDimensionSetting,
  BillAuthorizeRule,
  AuthorizeResourcesResponse,
  AuthorizeResourcesRequest,
};
