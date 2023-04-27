import type { SimpleMap } from "@commons/api/base/type";

/**
 * 分账设置规则条件对象
 */
interface BillAuthorizeRuleCondition {
  /**
   *授权字段
   */
  field: string;
  /**
   * 比较器 默认EQ
   */
  compare: string;
  /**
   *授权字段值
   */
  value: Array<string>;
}
/**
 * 分账设置规则组类型
 */
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

/**
 * 分账设置规则类型
 */
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
interface BillAuthorizeRuleTree {
  /**
   * 子组件
   */
  children: Array<BillAuthorizeRuleTree>;

  /**
   *条件与或
   */
  conditionType: string;
  /**
   *  条件
   */
  conditions: Array<BillAuthorizeRuleCondition>;
}

/**
 * 分账设置对象
 */
interface BillDimensionSetting {
  /**
   *主键id
   */
  id: string;
  /**
   *分账设置规则
   */
  authorizeRule: BillAuthorizeRuleTree;
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

/**
 * 资源对象类型
 */
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
/**
 * 获取授权资源请求对象
 */
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

/**
 * 获取未授权资源请求对象
 */
interface NotAuthorizeResourcesRequest {
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
  NotAuthorizeResourcesRequest,
  BillAuthorizeRuleTree,
};
