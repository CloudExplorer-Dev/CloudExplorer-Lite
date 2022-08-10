import type { Role } from "ce-base/commons/api/role";
// 角色数据
const roles: Array<Role> = [
  {
    id: "ADMIN",
    name: "系统管理员",
    description: "",
    type: "",
    parentId: null,
  },
  {
    id: "ORG",
    name: "组织管理员",
    description: "组织管理员",
    type: "",
    parentId: null,
  },
];

// 用户角色关联数据
const userRoles = [
  {
    username: "admin",
    roleId: "ADMIN",
  },
  {
    username: "admin",
    roleId: "ORG",
  },
  {
    username: "user",
    roleId: "ORG",
  },
];
export { userRoles, roles };
