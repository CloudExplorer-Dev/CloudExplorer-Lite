import type { SimpleMap } from "@commons/api/base/type";
export class ResourceAnalysisRequest {
  accountIds: Array<string>;
  statisticalBlock: boolean;
  spreadType: string;
  monthNumber: number;
  topType: string;
  constructor(
    accountIds: Array<string>,
    statisticalBlock: boolean,
    spreadType: string,
    monthNumber: number,
    topType: string
  ) {
    this.accountIds = accountIds;
    this.statisticalBlock = statisticalBlock;
    this.spreadType = spreadType;
    this.monthNumber = monthNumber;
    this.topType = topType;
  }
}

interface VmCloudDiskVO {}

interface ListVmCloudDiskRequest {
  pageSize: number;
  currentPage: number;
}

export type { VmCloudDiskVO, ListVmCloudDiskRequest };
