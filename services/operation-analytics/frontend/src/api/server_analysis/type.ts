export class ResourceAnalysisRequest {
  accountIds: Array<string>;
  hostIds: Array<string>;
  spreadType: string;
  dayNumber: number;
  metricName: string;
  startTime: number;
  endTime: number;
  analysisWorkspace: boolean;
  constructor(
    accountIds: Array<string>,
    hostIds: Array<string>,
    spreadType: string,
    dayNumber: number,
    metricName: string,
    startTime: number,
    endTime: number,
    analysisWorkspace: boolean
  ) {
    this.accountIds = accountIds;
    this.hostIds = hostIds;
    this.spreadType = spreadType;
    this.dayNumber = dayNumber;
    this.metricName = metricName;
    this.startTime = startTime;
    this.endTime = endTime;
    this.analysisWorkspace = analysisWorkspace;
  }
}

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
  vmToolsStatus?: string;
  accountName?: string;
  workspaceName?: string;
  organizationName?: string;
  showLoading?: boolean;
  securityGroupIds?: [];
  instanceChargeType?: string;
}

interface ListVmCloudServerRequest {
  pageSize: number;
  currentPage: number;
}

export type { VmCloudServerVO, ListVmCloudServerRequest };
