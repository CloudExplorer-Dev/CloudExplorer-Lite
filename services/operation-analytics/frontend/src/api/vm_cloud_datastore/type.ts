import type { SimpleMap } from "@commons/api/base/type";

interface VmCloudDatastoreVO {}

interface ListVmCloudDatastoreRequest {
  pageSize: number;
  currentPage: number;
}

export type { VmCloudDatastoreVO, ListVmCloudDatastoreRequest };
