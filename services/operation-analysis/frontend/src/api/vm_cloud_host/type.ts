interface VmCloudHostVO {
  id: string;
  hostName: string;
  zone: string;
  accountId: string;
}

interface ListVmCloudHostRequest {
  pageSize: number;
  currentPage: number;
}

export type { VmCloudHostVO, ListVmCloudHostRequest };
