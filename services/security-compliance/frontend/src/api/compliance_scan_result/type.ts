import type { ComplianceRuleGroup } from "@/api/rule_group/type";
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
  updateTime: string;
  /**
   * 资源类型
   */
  resourceType: string;
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
   * 风险等级
   */
  riskLevel?: string;
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

interface ComplianceRuleGroupCountResponse extends ComplianceRuleGroup {
  /**
   * 低风险数量
   */
  low: number;
  /**
   * 中风险数量
   */
  middle: number;
  /**
   * 高风险数量
   */
  high: number;
  /**
   * 总数
   */
  total: number;
  /**
   *资源类型
   */
  resourceType: Array<string>;
  /**
   * 云平台
   */
  platform: Array<string>;
}
export type {
  ComplianceScanResultResponse,
  ComplianceScanResultRequest,
  ComplianceScanResultRuleGroupResponse,
  ComplianceRuleGroupCountResponse,
};
