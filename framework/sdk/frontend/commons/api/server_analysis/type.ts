export class ResourceAnalysisRequest {
  accountIds?: Array<string>;
  hostIds?: Array<string>;
  spreadType?: string;
  dayNumber?: number;
  metricName?: string;
  startTime?: number;
  endTime?: number;
  analysisWorkspace?: boolean;
  constructor(
    accountIds?: Array<string>,
    hostIds?: Array<string>,
    spreadType?: string,
    dayNumber?: number,
    metricName?: string,
    startTime?: number,
    endTime?: number,
    analysisWorkspace?: boolean
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
