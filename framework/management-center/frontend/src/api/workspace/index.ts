import { post, del, get, put } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  WorkspaceDetail,
  ListWorkspaceRequest,
  CreateWorkspaceForm,
  WorkspaceCreate,
} from "./type";
import type { Ref } from "vue";

/**
 * 创建工作空间
 * @param workspace
 * @param loading
 */
export function create(
  workspace: any,
  loading?: Ref<boolean>
): Promise<Result<WorkspaceDetail>> {
  return post("api/workspace/create", null, workspace, loading);
}

/**
 * 查询工作空间
 * @param req
 * @param loading
 */
export function listWorkspace(
  req: ListWorkspaceRequest,
  loading?: Ref<boolean>
): Promise<Result<Page<WorkspaceDetail>>> {
  return get("api/workspace/page", req, loading);
}

/**
 * 单个删除工作空间
 * @param workspaceId
 * @param loading
 */
export function deleteWorkspaceById(
  workspaceId: string,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return del("api/workspace/" + workspaceId, loading);
}

/**
 * 批量删除工作空间
 * @param organizationIds
 * @param loading
 */
export function deleteBatch(
  organizationIds: Array<WorkspaceDetail>,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return del("api/workspace", undefined, organizationIds, loading);
}

/**
 * 根据ID查询工作空间
 * @param id
 * @param loading
 */
export function getWorkspaceById(
  id: string,
  loading?: Ref<boolean>
): Promise<Result<WorkspaceDetail>> {
  return get("api/workspace/one", { id: id, name: "" }, loading);
}

/**
 * 更新工作空间
 * @param workspace
 * @param loading
 */
export function update(
  workspace: WorkspaceCreate,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return put("api/workspace/update", undefined, workspace, loading);
}

/**
 * 批量创建
 * @param data
 * @param loading
 */
export function batch(
  data: CreateWorkspaceForm,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return post("/api/workspace/batch", null, data, loading);
}

const WorkspaceApi = {
  create,
  listWorkspace,
  deleteWorkspaceById,
  deleteBatch,
  getWorkspaceById,
  update,
  batch,
};

export default WorkspaceApi;
