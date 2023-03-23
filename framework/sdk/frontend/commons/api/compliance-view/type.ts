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

interface ComplianceCountRequest {
  /**
   * 云账号id
   */
  cloudAccountId?: string;
}

export type { ComplianceViewCountResponse, ComplianceCountRequest };
