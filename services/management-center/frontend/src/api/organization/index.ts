import { get, post, del } from "@commons/request";
import Result from "@commons/request/Result";
import { Page } from "@commons/request/Result";
import { CreateOrgFrom } from "@/views/OrgManage/type";
import {
  Organization,
  ListOrganizationRequest,
  OrganizationTree,
} from "./type";
import { da } from "element-plus/es/locale";
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

export const tree: () => Promise<Result<Array<OrganizationTree>>> = () => {
  return get("/organization/tree");
};
export const batch = (data: CreateOrgFrom) => {
  return post("/organization/batch", null, data);
};
export const deleteOrg = (id: string) => {
  return del("/organization/" + id);
};

export const deleteBatchOrg = (organizations: Array<Organization>) => {
  return del("/organization", undefined, organizations);
};
export type { Organization, OrganizationTree };
