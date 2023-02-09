interface ComplianceScanResultResponse {
  /**
   * 主键id
   */
  id: string;
  /**
   * 规则名称
   */
  name: string;
  /**
   * 云账户id
   */
  cloudAccountId: string;
  /**
   * 云账户名称
   */
  cloudAccountName: string;
  /**
   * 供应商
   */
  platform: string;
  /**
   * 风险等级
   */
  riskLevel: string;
  /**
   * 扫描状态
   */
  scanStatus: string;
  /**
   * 合规数量
   */
  complianceCount: number;
  /**
   * 不合规数量
   */
  notComplianceCount: number;
  /**
   * 最后一次同步时间
   */
  lastSyncTime: string;
}

interface ComplianceScanResultRequest {
  /**
   * 云账户id
   */
  cloudAccountId?: string;
  /**
   * 资源类型
   */
  resourceType?: string;
  /**
   * 合规规则组id
   */
  complianceRuleGroupId?: string;
}

interface ComplianceScanResultRuleGroupResponse {
  /**
   * 规则组名称
   */
  ruleGroupName: string;
  /**
   * 合规规则数量
   */
  complianceRuleCount: number;
  /**
   * 不合规规则数量
   */
  notComplianceRuleCount: number;
  /**
   * 高风险
   */
  high: number;
  /**
   * 中风险
   */
  middle: number;
  /**
   * 低风险
   */
  low: number;
}

export type {
  ComplianceScanResultResponse,
  ComplianceScanResultRequest,
  ComplianceScanResultRuleGroupResponse,
};
