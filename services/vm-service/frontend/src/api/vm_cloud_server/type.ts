interface VmCloudServerVO {
  id: string;
  instanceUuid?: string;
  workspaceId?: string;
  projectId?: string;
  accountId?: string;
  instanceId?: string;
  instanceName?: string;
  imageId?: string;
  instanceStatus?: string;
  instanceType?: string;
  instanceTypeDescription?: string;
  region?: string;
  zone?: string;
  host?: string;
  remoteIp?: string;
  ipArray?: [];
  os?: string;
  osVersion?: string;
  cpu?: number;
  memory?: number;
  disk?: number;
  hostname?: string;
  managementIp?: string;
  managementPort?: number;
  osInfo?: string;
  remark?: string;
  network?: string;
  vpcId?: string;
  subnetId?: string;
  networkInterfaceId?: string;
  managementIpv6?: string;
  remoteIpv6?: string;
  localIpv6?: string;
  ipType?: string;
  snapShot?: number;
  createTime?: string;
  updateTime?: string;
  platform?: string;
  vmToolsStatus?: boolean;
}
interface ListVmCloudServerRequest {
  pageSize: number;
  currentPage: number;
}
//操作枚举
//TODO 想要处理国际化
export enum InstanceOperateEnum {
  POWER_ON = <any>"开机",
  POWER_OFF = <any>"关闭电源",
  SHUTDOWN = <any>"关机",
  REBOOT = <any>"重启",
  DELETE = <any>"删除",
}

export type { VmCloudServerVO, ListVmCloudServerRequest };
