import type { SimpleMap } from "@commons/api/base/type";

interface VmCloudHostVO {}

interface ListVmCloudHostRequest {
  pageSize: number;
  currentPage: number;
}

export type { VmCloudHostVO, ListVmCloudHostRequest };
