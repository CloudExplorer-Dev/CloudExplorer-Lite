import { post, del, get, put } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { CreateWorkspaceForm } from "@/views/WorkspaceManage/type";
import type { Workspace, ListWorkspaceRequest, CloudMapping } from "./type";
import type { Ref } from "vue";

export const create = (req: any) => {
  return post("api/workspace/create", null, req);
};

export const listWorkspace: (
  req: ListWorkspaceRequest,
  loading?: Ref<boolean>
) => Promise<Result<Page<Workspace>>> = (req) => {
  return get("api/workspace/list", req);
};

export const deleteWorkspaceById = (workspaceId: string) => {
  return del("api/workspace/" + workspaceId);
};
export const deleteBatch = (organizationIds: Array<Workspace>) => {
  console.log("ids:", organizationIds);

  return del("api/workspace", undefined, organizationIds);
};
export const getWorksapceById = (id: string) => {
  return get("api/workspace/one", { id: id, name: "" });
};
export const update = (workspace: any) => {
  return put("api/workspace/update", undefined, workspace);
};
export const batch = (data: CreateWorkspaceForm) => {
  console.log("formdata:", data);

  return post("/api/workspace/batch", null, data);
};

export type { Workspace, CloudMapping };
