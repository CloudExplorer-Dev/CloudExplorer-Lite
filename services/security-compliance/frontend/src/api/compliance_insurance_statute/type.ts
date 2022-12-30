interface ComplianceInsuranceStatuteRequest {
  /**
   * 等级保护基本要求条款
   */
  baseClause?: string;
  /**
   * 安全层面
   */
  securityLevel?: string;
  /**
   * 控制点
   */
  controlPoint?: string;
  /**
   * 改进建议
   */
  improvementProposal?: string;
  /**
   * 合规规则id
   */
  complianceRuleId?: string;
}

interface ComplianceInsuranceStatute {
  /**
   * 主键id 序号
   */
  id: number;
  /**
   * 等级保护基本要求条款
   */
  baseClause: string;
  /**
   * 安全层面
   */
  securityLevel: string;
  /**
   * 控制点
   */
  controlPoint: string;
  /**
   * 改进建议
   */
  improvementProposal: string;
  /**
   * 创建时间
   */
  createTime: string;
  /**
   * 修改时间
   */
  updateTime: string;
}

export type { ComplianceInsuranceStatute, ComplianceInsuranceStatuteRequest };
