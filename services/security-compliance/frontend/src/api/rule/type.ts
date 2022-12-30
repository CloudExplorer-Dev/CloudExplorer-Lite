import type { KeyValue, SimpleMap } from "@commons/api/base/type";

interface ComplianceRule {
  /**
   * 主键id
   */
  id: string;
  /**
   * 规则名称
   */
  name: string;
  /**
   * 规则组id
   */
  ruleGroupId: string;
  /**
   * 云平台
   */
  platform: string;
  /**
   * 资源类型
   */
  resourceType: string;
  /**
   * 规则组
   */
  rules: Array<Rule>;
  /**
   * 风险等级
   */
  riskLevel: "HIGH" | "MIDDLE" | "LOW";
  /**
   * 是否启用
   */
  enable: boolean;
  /**
   * 描述
   */
  description: string;
  /**
   * 创建时间
   */
  createTime: string;
  /**
   * 修改时间
   */
  updateTime: string;
}
interface Rule {
  /**
   * 字段
   */
  field: string;
  /**
   * 比较
   */
  compare: string;
  /**
   * 值
   */
  value: any;
}

interface InstanceSearchField {
  /**
   * 字段
   */
  field: string;
  /**
   * 提示
   */
  label: string;
  /**
   * 字段类型
   */
  fieldType: string;
  /**
   * 比较器
   */
  compares: Array<KeyValue<string, string>>;
  /**
   * 选择
   */
  options?: Array<KeyValue<string, string>>;
}

interface SaveComplianceRuleRequest {
  /**
   * 名称
   */
  name: string;
  /**
   * 规则组id
   */
  ruleGroupId: string;
  /**
   * 供应商
   */
  platform: string;
  /**
   * 资源类型
   */
  resourceType: string;
  /**
   * 规则
   */
  rules: Array<Rule>;
  /**
   * 风险等级
   */
  riskLevel: "HIGH" | "MIDDLE" | "LOW";
  /**
   * 等保条例
   */
  insuranceStatuteIds: Array<number>;
  /**
   * 描述
   */
  description: string;
}
interface UpdateComplianceRuleRequest {
  /**
   * 主键id
   */
  id: string;
  /**
   * 名称
   */
  name?: string;
  /**
   * 规则组id
   */
  ruleGroupId?: string;
  /**
   * 供应商
   */
  platform: string;
  /**
   * 资源类型
   */
  resourceType: string;
  /**
   * 规则
   */
  rules: Array<Rule>;
  /**
   * 风险等级
   */
  riskLevel?: "HIGH" | "MIDDLE" | "LOW";
  /**
   * 等保条例
   */
  insuranceStatuteIds: Array<number>;
  /**
   * 描述
   */
  description?: string;
  /**
   * 是否开启
   */
  enable?: boolean;
}
export type {
  Rule,
  ComplianceRule,
  InstanceSearchField,
  SaveComplianceRuleRequest,
  UpdateComplianceRuleRequest,
};
