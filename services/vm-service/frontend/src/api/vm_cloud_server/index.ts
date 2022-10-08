import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { VmCloudServerVO, ListVmCloudServerRequest } from "./type";
import type { Ref } from "vue";

export const ListVmCloudServer: (
  req: ListVmCloudServerRequest,
  loading?: Ref<boolean>
) => Promise<Result<Page<VmCloudServerVO>>> = (req) => {
  return get("api/server/page", req);
};

export type { VmCloudServerVO, ListVmCloudServerRequest };
