interface ComplianceRuleGroup {
  /**
   * 主键id
   */
  id: string;
  /**
   * 规则组名称
   */
  name: string;
  /**
   * 规则组描述
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

interface ComplianceRuleGroupRequest {
  /**
   * 规则组名称
   */
  name: string;
  /**
   * 规则组描述
   */
  description: string;
}

interface UpdateComplianeRuleGroupRequest extends ComplianceRuleGroupRequest {
  /**
   * 规则组id
   */
  id: string;
}

export type {
  ComplianceRuleGroup,
  ComplianceRuleGroupRequest,
  UpdateComplianeRuleGroupRequest,
};
