import { get, post, del, put } from "ce-base/commons/request";
import Result from "ce-base/commons/request/Result";
import { Page } from "ce-base/commons/request/Result";
import { ListOrganizationRequest, CloudAccount } from "./type";
const page: (
  request: ListOrganizationRequest
) => Promise<Result<Page<CloudAccount>>> = (request) => {
  return get("/cloud_account/page", request);
};
export default {
  page,
};
