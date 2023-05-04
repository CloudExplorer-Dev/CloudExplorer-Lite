import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { WorkspaceTree, Workspace } from "@commons/api/workspace/type";
import { get } from "@commons/request";

export function workspaceTree(
  loading?: Ref<boolean>
): Promise<Result<Array<WorkspaceTree>>> {
  return get("/api/workspace/tree", {}, loading);
}

export function workspaceOrgTree(
  loading?: Ref<boolean>
): Promise<Result<Array<WorkspaceTree>>> {
  return get("/api/workspace/orgTree", {}, loading);
}

export function workspaces(
  loading?: Ref<boolean>
): Promise<Result<Array<Workspace>>> {
  return get("/api/workspace/list", {}, loading);
}

const BaseWorkspaceApi = {
  workspaceTree,
  workspaceOrgTree,
  workspaces,
};

export default BaseWorkspaceApi;
