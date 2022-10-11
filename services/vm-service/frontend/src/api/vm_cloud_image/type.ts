interface VmCloudImageVO {
  id?: string;
  accountId?: string;
  imageId?: string;
  region?: string;
  regionName?: string;
  imageName?: string;
  description?: string;
  os?: string;
  diskSize?: number;
  status?: string;
  workspaceId?: string;
  createTime?: string;
  updateTime?: string;
}
interface ListVmCloudImageRequest {
  pageSize: number;
  currentPage: number;
}
export type { VmCloudImageVO, ListVmCloudImageRequest };
