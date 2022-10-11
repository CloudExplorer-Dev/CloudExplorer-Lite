interface VmCloudDiskVO {
  id?: string;
  region?: string;
  zone?: string;
  diskId?: string;
  diskName?: string;
  diskType?: string;
  category?: string;
  status?: string;
  diskChargeType?: string;
  description?: string;
  size?: number;
  device?: string;
  accountId?: string;
  datastoreId?: string;
  instanceUuid?: string;
  workspaceId?: string;
  projectId?: string;
  bootable?: boolean;
  imageId?: string;
  deleteWithInstance?: string;
  createTime?: string;
  updateTime?: string;
}
interface ListVmCloudDiskRequest {
  pageSize: number;
  currentPage: number;
}
export type { VmCloudDiskVO, ListVmCloudDiskRequest };
