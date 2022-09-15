import { get, post, del, put } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { CreateOrgFrom } from "@/views/OrgManage/type";
import type {
  Organization,
  ListOrganizationRequest,
  OrganizationTree,
} from "./type";
/**
 *  分页查询组织
 * @param req 组织请求参数
 * @returns
 */
const pageOrganization: (
  req: ListOrganizationRequest,
  loading?: Ref<boolean>
) => Promise<Result<Page<Organization>>> = (req, loading) => {
  return get("/api/organization/page", req, loading);
};
/**
 * 查询到所有的组织
 * @returns 所有组织
 */
const listAllOrganization: (
  loading?: Ref<boolean>
) => Promise<Result<Array<Organization>>> = (loading) => {
  return get("/api/listAll/org", null, loading);
};

export const tree: (
  treeType?: string
) => Promise<Result<Array<OrganizationTree>>> = (treeType) => {
  const type: string = treeType === undefined ? "ORGANIZATION" : treeType;
  return get("/api/base/organization/tree/" + type);
};

const batchSave = (data: CreateOrgFrom) => {
  return post("/api/organization/batch", null, data);
};
const deleteOrg = (id: string) => {
  return del("/api/organization/" + id);
};

const deleteBatchOrg = (organizations: Array<Organization>) => {
  return del("/api/organization", undefined, organizations);
};
const getOrgById = (id: string) => {
  return get("/api/organization/" + id, null);
};
const updateOrg = (organization: Organization) => {
  return put("/api/organization", undefined, organization);
};
export default {
  pageOrganization,
  listAllOrganization,
  tree,
  batchSave,
  deleteOrg,
  deleteBatchOrg,
  getOrgById,
  updateOrg,
};
