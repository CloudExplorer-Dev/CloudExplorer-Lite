interface VmCloudDatastoreVO {
  id: string;
}

interface ListVmCloudDatastoreRequest {
  pageSize: number;
  currentPage: number;
}

export type { VmCloudDatastoreVO, ListVmCloudDatastoreRequest };
