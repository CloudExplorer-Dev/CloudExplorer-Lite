export interface JobInfo {
  id?: string;
  type?: string;
  status?: string;
  description?: string;
  params?: string;
  createTime?: string;
  updateTime?: string;
  result?: string;
}
export interface ListJobRequest {
  pageSize: number;
  currentPage: number;

  [propName: string]: any;
}
