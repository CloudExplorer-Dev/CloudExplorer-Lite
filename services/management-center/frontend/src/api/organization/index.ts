import { get } from "ce-base/commons/request";
import Result from "ce-base/commons/request/Result";
import { Page } from "ce-base/commons/request/Result";
import { Organization, ListOrganizationRequest } from "./type";
export const listOrganization: (
  req: ListOrganizationRequest
) => Promise<Result<Page<Organization>>> = (req) => {
  return get("/api/list/org", req);
};
export type { Organization };
