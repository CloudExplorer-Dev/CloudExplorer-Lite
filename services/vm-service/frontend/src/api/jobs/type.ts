import type { VmCloudDiskVO } from "@/api/vm_cloud_disk/type";
import type { VmCloudServerVO } from "@/api/vm_cloud_server/type";

export interface JobInfo {
  id?: string;
  type?: string;
  status?: string;
  description?: string;
  params?: string;
  createTime?: string;
  finishTime?: string;
  updateTime?: string;
  result?: string;
  servers?: Array<VmCloudServerVO>;
  disks?: Array<VmCloudDiskVO>;
  operateUserId?: string;
  operateUserName?: string;
  resourceId?: string;
  resourceName?: string;
  resourceType?: string;
}
export interface ListJobRequest {
  pageSize: number;
  currentPage: number;

  [propName: string]: any;
}
