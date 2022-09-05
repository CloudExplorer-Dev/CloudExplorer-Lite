import { get, post, del, put } from "ce-base/commons/request";
import type Result from "ce-base/commons/request/Result";
import type { Page } from "ce-base/commons/request/Result";
import type { ListOrganizationRequest, CloudAccount } from "./type";
const page: (
  request: ListOrganizationRequest
) => Promise<Result<Page<CloudAccount>>> = (request) => {
  return get("/api/cloud_account/page", request);
};
export default {
  page,
};
