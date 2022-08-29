import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { Organization, ListOrganizationRequest } from "./type";
export const listOrganization: (
  req: ListOrganizationRequest
) => Promise<Result<Page<Organization>>> = (req) => {
  return get("organization/page", req);
};
export const listAllOrganization: () => Promise<
  Result<Array<Organization>>
> = () => {
  return get("/api/listAll/org");
};
export type { Organization };
