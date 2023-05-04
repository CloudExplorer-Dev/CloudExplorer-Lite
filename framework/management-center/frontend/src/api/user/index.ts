import { get, post, del } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  ListUserRequest,
  CreateUserRequest,
  User,
  UpdateUserPwdRequest,
  UpdateUserStatusRequest,
  UpdateUserRequest,
} from "./type";
import type { Ref } from "vue";
import type { UserRole } from "./type";
import _ from "lodash";
import type { SimpleMap } from "@commons/api/base/type";

export const listUser: (
  req: ListUserRequest,
  loading?: Ref<boolean>
) => Promise<Result<Page<User>>> = (req, loading) => {
  return get("/api/user/page", req, loading);
};

export const createUser = (req: CreateUserRequest, loading?: Ref<boolean>) => {
  return post("/api/user/add", "", req, loading);
};

export const updateUser = (req: UpdateUserRequest, loading?: Ref<boolean>) => {
  return post("/api/user/update", "", req, loading);
};

export const updatePwd = (
  req: UpdateUserPwdRequest,
  loading?: Ref<boolean>
) => {
  return post("/api/user/updatePwd", "", req, loading);
};

export const deleteUserById = (userId: string, loading?: Ref<boolean>) => {
  return del("/api/user/" + userId, undefined, undefined, loading);
};

export const getRoleInfo = (userId: string) => {
  return get("/api/user/role/info/" + userId);
};

export function getUser(
  userId: string,
  loading?: Ref<boolean>
): Promise<Result<User>> {
  return get("/api/user/" + userId, undefined, loading);
}

/**
 * 启停用户
 */
export const changeUserStatus = (
  req: UpdateUserStatusRequest,
  loading?: Ref<boolean>
) => {
  return post("/api/user/changeStatus", "", req, loading);
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

export function removeUserRole(
  userId: string,
  roleId: string,
  sourceId?: string,
  loading?: Ref<boolean>
) {
  return del(
    `/api/user/removeRole`,
    undefined,
    {
      userId: userId,
      roleId: roleId,
      sourceId: sourceId,
    },
    loading
  );
}

export function getUserRoleList(
  map: SimpleMap<Array<UserRole>>
): Array<UserRole> {
  const _list: Array<UserRole> = [];
  _.forEach(map["ADMIN"], (value) => {
    _list.push(value);
  });
  _.forEach(map["ORGADMIN"], (value) => {
    _list.push(value);
  });
  _.forEach(map["USER"], (value) => {
    _list.push(value);
  });
  return _list;
}

export function getUserRoleSourceList(list: Array<UserRole>) {
  const _list: Array<any> = [];
  _.forEach(list, (userRole) => {
    _.forEach(userRole.roles, (role) => {
      _list.push({
        source: userRole.source,
        roleId: role.id,
        roleName: role.name,
        parentRole: role.parentRoleId,
      });
    });
  });
  return _list;
}

export function convertUserRoleSourceList(list: Array<UserRole>) {
  const _list: Array<any> = getUserRoleSourceList(list);

  const map = _.groupBy(_list, "roleId");

  const result: Array<any> = [];
  _.forIn(map, function (value, key) {
    result.push({
      roleId: key,
      roleName: value[0].roleName,
      parentRole: value[0].parentRole,
      list: value,
    });
  });

  return result;
}

export default {
  listUser,
  createUser,
  updateUser,
  updatePwd,
  deleteUserById,
  getRoleInfo,
  getUser,
  changeUserStatus,
  userNotificationSetting,
  findUserNotification,
  userAddRole,
  removeUserRole,
  getUserRoleList,
  getUserRoleSourceList,
};
