import { get, post, del, put } from "ce-base/commons/request";
import Result from "ce-base/commons/request/Result";
import { Page } from "ce-base/commons/request/Result";
import { CreateOrgFrom } from "@/views/OrgManage/type";
import {
  Organization,
  ListOrganizationRequest,
  OrganizationTree,
} from "./type";
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
  return get("/base/organization/tree");
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
export const getOrgById = (id: string) => {
  return get("/organization/one", { id: id, name: "" });
};
export const updateOrg = (organization: Organization) => {
  return put("organization", undefined, organization);
};
export type { Organization, OrganizationTree };
