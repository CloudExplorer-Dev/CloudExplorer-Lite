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

export type {
  BillAuthorizeRuleCondition,
  BillAuthorizeRuleSettingGroup,
  BillDimensionSetting,
  BillAuthorizeRule,
};
