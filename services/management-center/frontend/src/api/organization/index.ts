import { get, post, del, put } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { CreateOrgFrom } from "@/views/OrgManage/type";
import type {
  Organization,
  ListOrganizationRequest,
  OrganizationTree,
} from "./type";
export const listOrganization: (
  req: ListOrganizationRequest
) => Promise<Result<Page<Organization>>> = (req) => {
  return get("/api/organization/page", req);
};
export const listAllOrganization: () => Promise<
  Result<Array<Organization>>
> = () => {
  return get("/api/listAll/org");
};

export const tree: (
  treeType?: string
) => Promise<Result<Array<OrganizationTree>>> = (treeType) => {
  const type: string = treeType === undefined ? "ORGANIZATION" : treeType;
  return get("/api/base/organization/tree/" + type);
};

export const batch = (data: CreateOrgFrom) => {
  return post("/api/organization/batch", null, data);
};
export const deleteOrg = (id: string) => {
  return del("/api/organization/" + id);
};

export const deleteBatchOrg = (organizations: Array<Organization>) => {
  return del("/api/organization", undefined, organizations);
};
export const getOrgById = (id: string) => {
  return get("/api/organization/one", { id: id, name: "" });
};
export const updateOrg = (organization: Organization) => {
  return put("/api/organization", undefined, organization);
};
export type { Organization, OrganizationTree };
