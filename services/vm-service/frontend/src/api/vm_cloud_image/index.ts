import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { VmCloudImageVO, ListVmCloudImageRequest } from "./type";
import type { Ref } from "vue";

export const ListVmCloudImage: (
  req: ListVmCloudImageRequest,
  loading?: Ref<boolean>
) => Promise<Result<Page<VmCloudImageVO>>> = (req) => {
  return get("api/image/page", req);
};

export type { VmCloudImageVO, ListVmCloudImageRequest };
