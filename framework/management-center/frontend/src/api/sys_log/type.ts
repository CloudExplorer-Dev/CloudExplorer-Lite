interface SystemLogVO {
  name?: string;
  msg?: string;
  module?: string;
  level?: string;
  message?: string;
  type?: string;
  createTime?: number;
}

interface ListSystemLogRequest {
  pageSize: number;
  currentPage: number;
}

export type { SystemLogVO, ListSystemLogRequest };
