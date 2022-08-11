const permissions = [
  {
    id: "TEST",
    name: "菜单显示",
    resourceId: "USER",
    parentRoles: ["ADMIN"],
  },
];
const rolePermissions = [
  {
    roleId: "ADMIN",
    permissionId: "TEST",
  },
];

export { rolePermissions, permissions };
