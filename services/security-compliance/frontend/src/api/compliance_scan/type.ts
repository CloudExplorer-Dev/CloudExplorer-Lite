import type { KeyValue } from "@commons/api/base/type";
import type { CloudAccount } from "@commons/api/cloud_account/type";

interface ComplianceResourceResponse {
  /**
   * 主键id
   */
  id: string;
  /**
   * 供应商
   */
  platform: string;
  /**
   * 云账号id
   */
  cloudAccountId: string;
  /**
   * 云账号名称
   */
  cloudAccountName: string;
  /**
   * 资源类型
   */
  resourceType: string;
  /**
   * 资源名称
   */
  resourceName: string;
  /**
   * 资源id
   */
  resourceId: string;
  /**
   * 扫描状态
   */
  scanStatus: string;
  /**
   * 实例对象
   */
  instance: any;
}
interface ComplianceResourceRequest {
  /**
   * 资源类型
   */
  resourceType?: string;
  /**
   * 云账号id
   */
  cloudAccountId?: string;
  /**
   * 扫描状态
   */
  complianceStatus?: string;
  /**
   * 资源名称
   */
  resourceName?: string;
}
interface CloudAccountResource {
  /**
   * 云账号
   */
  cloudAccountId: string;

  /**
   * 资源类型
   */
  resourceType: Array<string>;
}
interface SyncScanResourceRequest {
  /**
   *云账号id
   */
  cloudAccountIds: Array<string>;
  /**
   * 规则组id
   */
  ruleGroupIds: Array<string>;
}
interface SupportCloudAccountResourceResponse {
  /**
   * 云账号
   */
  cloudAccount: CloudAccount;
  /**
   * 资源类型
   */
  resourceTypes: Array<KeyValue<string, string>>;
}

export type {
  ComplianceResourceResponse,
  ComplianceResourceRequest,
  SyncScanResourceRequest,
  CloudAccountResource,
  SupportCloudAccountResourceResponse,
};
