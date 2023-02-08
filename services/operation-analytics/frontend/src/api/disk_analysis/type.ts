export class ResourceAnalysisRequest {
  accountIds: Array<string>;
  statisticalBlock: boolean;
  spreadType: string;
  monthNumber: number;
  topType: string;
  analysisWorkspace: boolean;
  constructor(
    accountIds: Array<string>,
    statisticalBlock: boolean,
    spreadType: string,
    monthNumber: number,
    topType: string,
    analysisWorkspace: boolean
  ) {
    this.accountIds = accountIds;
    this.statisticalBlock = statisticalBlock;
    this.spreadType = spreadType;
    this.monthNumber = monthNumber;
    this.topType = topType;
    this.analysisWorkspace = analysisWorkspace;
  }
}

interface VmCloudDiskVO {}

interface ListVmCloudDiskRequest {
  pageSize: number;
  currentPage: number;
}

export type { VmCloudDiskVO, ListVmCloudDiskRequest };
