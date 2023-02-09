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
   * 需要同步的云账号以及资源
   */
  cloudAccountResources: Array<CloudAccountResource>;
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

interface SupportPlatformResourceResponse {
  /**
   * 云账号
   */
  platform: string;
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
  SupportPlatformResourceResponse,
};
