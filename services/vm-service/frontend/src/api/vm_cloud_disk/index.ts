import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { VmCloudDiskVO, ListVmCloudDiskRequest } from "./type";
import type { Ref } from "vue";

export const ListVmCloudDisk: (
  req: ListVmCloudDiskRequest,
  loading?: Ref<boolean>
) => Promise<Result<Page<VmCloudDiskVO>>> = (req) => {
  return get("api/disk/page", req);
};

export type { VmCloudDiskVO, ListVmCloudDiskRequest };
