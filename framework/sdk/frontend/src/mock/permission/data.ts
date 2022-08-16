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
const runingPermissions = {
  "management-center": [
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
  ],
  "service-directory": [
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
  ],
  "server-service": [
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
  ],
  "message-service": [
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
  ],
};
const runingRolePermission = {
  "management-center": [
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
  ],
  "service-directory": [
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
  ],
  "server-service": [
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
  ],
  "message-service": [
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
  ],
};
export {
  rolePermissions,
  permissions,
  runingPermissions,
  runingRolePermission,
};
