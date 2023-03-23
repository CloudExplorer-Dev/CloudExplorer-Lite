import type { CloudAccount } from "@commons/api/cloud_account/type";
export interface CloudAccountDTO extends CloudAccount {
  /**
   * 宿主机数量
   */
  hostCount: number;
  /**
   * 存储数量
   */
  datastoreCount: number;
  /**
   * 云主机数量
   */
  vmCount: number;
  /**
   * 磁盘数量
   */
  diskCount: number;
}

export interface CommonRequest {
  cloudAccountId: string;
}
