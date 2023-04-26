import * as RoleTypes from "@commons/api/role/type";
import * as UserTypes from "@commons/api/user/type";
import { convertUserRoleSourceList, getUserRoleList } from "@/api/user/index";
import _ from "lodash";

export class Role extends RoleTypes.Role {}
export class User extends UserTypes.User {}
export class UserRole extends UserTypes.UserRole {}

interface RoleInfo {
  roleId: string;
  roleName?: string;
  roleType: "ADMIN" | "ORGADMIN" | "USER" | string; //角色类型：系统管理员、组织管理员、工作空间用户
  organizationIds: string[];
  workspaceIds: string[];
  selectedRoleIds?: string[]; // 已选择的角色ID集合
}

interface ListUserRequest {
  pageSize: number;
  currentPage: number;
}

export interface CreateUserRequest {
  username: string; // 用户ID
  name: string; // 姓名
  email: string;
  phone?: string;
  password?: string;
  confirmPassword?: string;
  roleInfoList: RoleInfo[];
}

export class UpdateUserRequest {
  id: string; // ID
  name: string; // 姓名
  email: string;
  phone?: string;
  roleInfoList?: RoleInfo[];

  constructor(user: User) {
    this.id = user.id;
    this.name = user.name;
    this.email = user.email;
    this.phone = user.phone;

    const roleList = convertUserRoleSourceList(
      getUserRoleList(_.defaultTo(user?.roleMap, {}))
    );

    this.roleInfoList = _.defaultTo(
      _.map(roleList, (r) => {
        return {
          roleId: r.roleId,
          roleName: r.roleName,
          roleType: r.parentRole,
          organizationIds:
            r.parentRole === "ORGADMIN" ? _.map(r.list, (l) => l.source) : [],
          workspaceIds:
            r.parentRole === "USER" ? _.map(r.list, (l) => l.source) : [],
        } as RoleInfo;
      }),
      []
    );
  }
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
  UpdateUserPwdRequest,
  UpdateUserStatusRequest,
};
