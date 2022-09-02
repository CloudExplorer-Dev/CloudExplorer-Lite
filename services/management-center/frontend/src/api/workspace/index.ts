import { post, del, get, put } from "ce-base/commons/request";
import Result from "ce-base/commons/request/Result";
import { Page } from "ce-base/commons/request/Result";
import { Workspace, ListWorkspaceRequest, CloudMapping } from "./type";

export const create = (req: any) => {
  return post("/workspace/create", null, req);
};

export const listWorkspace: (
  req: ListWorkspaceRequest
) => Promise<Result<Page<Workspace>>> = (req) => {
  return get("/workspace/list", req);
};

export const deleteWorkspaceById = (workspaceId: string) => {
  return del("/workspace/" + workspaceId);
};
export const deleteBatch = (organizationIds: Array<Workspace>) => {
  console.log("ids:", organizationIds);

  return del("/workspace", undefined, organizationIds);
};
export const getWorksapceById = (id: string) => {
  return get("/workspace/one", { id: id, name: "" });
};
export const update = (workspace: any) => {
  return put("/workspace/update", undefined, workspace);
};

export type { Workspace, CloudMapping };
