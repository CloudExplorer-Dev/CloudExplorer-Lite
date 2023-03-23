interface VmCloudDatastoreVO {
  id: string;
  datastoreName: string;
  zone: string;
  accountId: string;
}

interface ListVmCloudDatastoreRequest {
  pageSize: number;
  currentPage: number;
}

export type { VmCloudDatastoreVO, ListVmCloudDatastoreRequest };
