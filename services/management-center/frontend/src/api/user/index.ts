import { get, post, del } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  ListUserRequest,
  CreateUserRequest,
  User,
  UpdateUserPwdRequest,
  UpdateUserStatusRequest,
} from "./type";

export const listUser: (req: ListUserRequest) => Promise<Result<Page<User>>> = (
  req
) => {
  return get("/api/user/page", req);
};

export const createUser = (req: CreateUserRequest) => {
  return post("/api/user/add", "", req);
};

export const updateUser = (req: CreateUserRequest) => {
  return post("/api/user/update", "", req);
};

export const updatePwd = (req: UpdateUserPwdRequest) => {
  return post("/api/user/updatePwd", "", req);
};

export const deleteUserById = (userId: string) => {
  return del("/api/user/" + userId);
};

export const getRoleInfo = (userId: string) => {
  return get("/api/user/role/info/" + userId);
};

export const workspaceTree = (): Promise<Result<Array<any>>> => {
  return get("/api/base/workspace/tree");
};

/**
 * 启停用户
 * @param req
 */
export const changeUserStatus = (req: UpdateUserStatusRequest) => {
  return post("/api/user/changeStatus", "", req);
};

/**
 * 设置用户通知信息
 * @param req
 */
export const userNotificationSetting = (req: any) => {
  return post("/api/user/notificationSetting", "", req);
};

export const findUserNotification = (userId: string) => {
  return get("/api/user/findUserNotification/" + userId);
};

export const userAddRole = (req: any) => {
  return post("/api/user/addRole", "", req);
};
