const permissions = [
  {
    id: "USER_MENU_VIEW",
    name: "菜单显示",
    resourceId: "USER",
    parentRoles: ["ADMIN"],
  },
  {
    id: "ROLE_MENU_VIEW",
    name: "菜单显示",
    resourceId: "ROLE",
    parentRoles: ["ADMIN"],
  },
  {
    id: "ORG_MENU_VIEW",
    name: "菜单显示",
    resourceId: "ORG",
    parentRoles: ["ADMIN"],
  },
  {
    id: "WORKSPACE_MENU_VIEW",
    name: "菜单显示",
    resourceId: "WORKSPACE",
    parentRoles: ["ADMIN"],
  },
];
const rolePermissions = [
  {
    roleId: "ADMIN",
    permissionId: "USER_MENU_VIEW",
  },
  {
    roleId: "ADMIN",
    permissionId: "ROLE_MENU_VIEW",
  },
  {
    roleId: "ADMIN",
    permissionId: "ORG_MENU_VIEW",
  },
  {
    roleId: "ADMIN",
    permissionId: "WORKSPACE_MENU_VIEW",
  },
  {
    roleId: "ORG",
    permissionId: "USER_MENU_VIEW",
  },
  {
    roleId: "ORG",
    permissionId: "ROLE_MENU_VIEW",
  },
  {
    roleId: "ORG",
    permissionId: "WORKSPACE_MENU_VIEW",
  },
];

export { rolePermissions, permissions };
