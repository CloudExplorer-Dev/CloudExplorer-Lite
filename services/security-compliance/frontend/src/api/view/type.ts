interface ComplianceViewCountResponse {
  /**
   * 合规数量
   */
  complianceCount: number;
  /**
   * 资源总数
   */
  total: number;
  /**
   * 不合规数量
   */
  notComplianceCount: number;
  /**
   * 不合规资源占比
   */
  notCompliancePercentage: number;

  [propName: string]: any;
}

interface ComplianceViewGroupResponse {
  /**
   * 分支值
   */
  groupValue: string;
  /**
   * 分组名称
   */
  groupName: string;
  /**
   * 分组类型
   */
  groupType: string;
  /**
   * 合规数量
   */
  complianceCount: number;
  /**
   * 总数
   */
  total: number;
  /**
   * 不合规数量
   */
  notComplianceCount: number;
}
interface ComplianceCountRequest {
  /**
   * 云账号id
   */
  cloudAccountId?: string;
}

interface ComplianceGroupRequest {
  /**
   * 云账号id
   */
  cloudAccountId?: string;
  /**
   * 分组类型
   */
  groupType?: "CLOUD_ACCOUNT" | "RESOURCE_TYPE" | "RULE_GROUP" | "RULE";
}
export type {
  ComplianceViewCountResponse,
  ComplianceViewGroupResponse,
  ComplianceGroupRequest,
  ComplianceCountRequest,
};
