interface VmCloudHostVO {
  id: string;
}

interface ListVmCloudHostRequest {
  pageSize: number;
  currentPage: number;
}

export type { VmCloudHostVO, ListVmCloudHostRequest };
