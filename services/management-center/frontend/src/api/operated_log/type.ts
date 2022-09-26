interface OperatedLogVO {
  name?: string;
  createTime?: number;
  requestId?: string;
  module?: string;
  operated?: string;
  operatedName?: string;
  resourceId?: string;
  resourceType?: string;
  joinResourceId?: string;
  user?: string;
  userId?: string;
  url?: string;
  content?: string;
  method?: string;
  params?: string;
  status?: number;
  code?: string;
  sourceIp?: string;
  time?: number;
  response?: string;
}

interface ListOperatedLogRequest {
  pageSize: number;
  currentPage: number;
}

export type { OperatedLogVO, ListOperatedLogRequest };
