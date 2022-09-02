import { get, post, del } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { User, ListUserRequest, CreateUserRequest } from "./type";

export const listUser: (req: ListUserRequest) => Promise<Result<Page<User>>> = (
  req
) => {
  return get("/user/page", req);
};

export const createUser = (req: CreateUserRequest) => {
  return post("/user/add", "", req);
};

export const deleteUserById = (userId: string) => {
  return del("/user/" + userId);
};

export const listRole = () => {
  return get("/role");
};

export const workspaceTree = (): Promise<Result<Array<any>>> => {
  return get("/base/workspace/tree");
};

export type { User };
