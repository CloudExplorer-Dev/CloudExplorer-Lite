import * as RoleTypes from "@commons/api/role/type";
import * as UserTypes from "@commons/api/user/type";

export class Role extends RoleTypes.Role {}
export class User extends UserTypes.User {}
export class UserRole extends UserTypes.UserRole {}

interface RoleInfo {
  roleId: string;
  roleType: string | null; //角色类型：系统管理员、组织管理员、工作空间用户
  organizationIds: string[];
  workspaceIds: string[];
  selectedRoleIds?: string[] | undefined; // 已选择的角色ID集合
}

interface ListUserRequest {
  pageSize: number;
  currentPage: number;
}

interface CreateUserRequest {
  username: string; // 用户ID
  name: string; // 姓名
  source: string;
  email: string;
  phone?: string;
  password: string;
  confirmPassword?: string;
  roleInfoList: RoleInfo[];
}

interface UpdateUserRequest {
  id: string; // ID
  name: string; // 姓名
  email: string;
  phone?: string;
  roleInfoList?: RoleInfo[];
}

interface UpdateUserPwdRequest {
  id: string;
  password: string;
}

interface UpdateUserStatusRequest {
  id: string;
  enabled: boolean;
}

export type {
  RoleInfo,
  ListUserRequest,
  CreateUserRequest,
  UpdateUserPwdRequest,
  UpdateUserStatusRequest,
  UpdateUserRequest,
};
