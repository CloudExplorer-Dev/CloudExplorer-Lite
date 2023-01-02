import type { SimpleMap } from "@commons/api/base/type";
export class ResourceAnalysisRequest {
  accountIds: Array<string>;
  clusterIds: Array<string>;
  hostIds: Array<string>;
  datastoreIds: Array<string>;
  resourceIds: Array<string>;
  vmStatus: string;
  entityType: string;
  metricName: string;
  startTime: number;
  endTime: number;
  constructor(
    accountIds: Array<string>,
    clusterIds: Array<string>,
    hostIds: Array<string>,
    datastoreIds: Array<string>,
    resourceIds: Array<string>,
    vmStatus: string,
    entityType: string,
    metricName: string,
    startTime: number,
    endTime: number
  ) {
    this.accountIds = accountIds;
    this.clusterIds = clusterIds;
    this.hostIds = hostIds;
    this.datastoreIds = datastoreIds;
    this.resourceIds = resourceIds;
    this.vmStatus = vmStatus;
    this.entityType = entityType;
    this.metricName = metricName;
    this.startTime = startTime;
    this.endTime = endTime;
  }
}
