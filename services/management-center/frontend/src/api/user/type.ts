interface User {
  id: string;
  username: string;
  name: string;
  email: string;
  createTime: number;
  source: string;
  password: string;
  enabled: boolean;
  phone: string;
  roles: Role[];
}

interface Role {
  id: string;
  name: string;
  type: string;
  parentRoleId: string;
}

interface RoleInfo {
  roleId: string;
  roleType: string;
  organizationIds: [];
  workspaceIds: [];
  selects?: string[] | undefined;
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

export type { User, Role, RoleInfo, ListUserRequest, CreateUserRequest };
