import { post } from "ce-base/commons/request";
import Result from "ce-base/commons/request/Result";
import { Page } from "ce-base/commons/request/Result";
import { User, ListUserRequest } from "./type";

export const listUser: (req: ListUserRequest) => Promise<Result<Page<User>>> = (
  req
) => {
  return post("/api/list/user", req);
};

export const deleteUserById = (userId: string) => {
  // return post("/api/user/delete/?userId=" + userId);
};

export type { User };
