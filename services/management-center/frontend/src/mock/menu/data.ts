export default [
  {
    name: "user_manage",
    title: "用户与租户",
    componentPath: "",
    path: "/user_manage",
    icon: "yonghuguanli_huaban",
    order: 1,
    children: [
      {
        name: "user",
        title: "用户管理",
        icon: "yonghuguanli_huaban",
        path: "/user",
        componentPath: "/src/views/UserManage/index.vue",

        operations: [
          {
            name: "create",
            title: "创建",
            path: "/create",
            componentPath: "/src/views/UserManage/create.vue",
            requiredPermissions: [
              { role: "ADMIN", logical: "OR", permissions: ["USER_MENU_VIEW"] },
            ],
          },
        ],
        requiredPermissions: [
          { role: "ADMIN", logical: "OR", permissions: ["USER_MENU_VIEW"] },
        ],
        order: 1,
      },
      {
        name: "role",
        title: "角色管理",
        icon: "jurassic_user",
        path: "/role",
        componentPath: "/src/views/RoleManage/index.vue",
        requiredPermissions: [
          { role: "ADMIN", logical: "OR", permissions: ["ROLE_MENU_VIEW"] },
        ],
        order: 2,
      },
      {
        name: "org",
        title: "组织管理",
        icon: "zuzhijiagou",
        path: "/org",
        componentPath: "/src/views/OrgManage/index.vue",
        operations: [
          {
            name: "orgCreate",
            title: "创建",
            path: "/create",
            componentPath: "/src/views/OrgManage/create.vue",
            requiredPermissions: [
              { role: "ADMIN", logical: "OR", permissions: ["ORG_MENU_VIEW"] },
            ],
          },
          {
            name: "orgUpdate",
            title: "创建",
            path: "/update",
            componentPath: "/src/views/OrgManage/update.vue",
            requiredPermissions: [
              { role: "ADMIN", logical: "OR", permissions: ["ORG_MENU_VIEW"] },
            ],
          },
        ],
        requiredPermissions: [
          { role: "ADMIN", logical: "OR", permissions: ["ORG_MENU_VIEW"] },
        ],
        order: 3,
      },
      {
        name: "workspace",
        title: "工作空间",
        icon: "project_space",
        path: "/workspace",
        props: true,
        componentPath: "/src/views/WorkspaceManage/index.vue",
        operations: [
          {
            name: "worksapceCreate",
            title: "创建",
            path: "/create",
            componentPath: "/src/views/WorkspaceManage/Create.vue",
            props: true,
            requiredPermissions: [
              {
                role: "ADMIN",
                logical: "OR",
                permissions: ["WORKSPACE_MENU_VIEW"],
              },
            ],
          },
        ],
        requiredPermissions: [
          {
            role: "ADMIN",
            logical: "OR",
            permissions: ["WORKSPACE_MENU_VIEW"],
          },
        ],
        order: 4,
      },
    ],
  },
  {
    name: "cloud_account",
    title: "云账号",
    componentPath: "/src/views/CloudAccount/index.vue",
    path: "/cloud_account",
    icon: "yonghuguanli_huaban",
    order: 2,
    redirect: "/cloud_account/list",
    childOperations: [
      {
        name: "cloud_account_list",
        title: "创建",
        path: "/list",
        componentPath: "/src/views/CloudAccount/list.vue",
        requiredPermissions: [
          { role: "ADMIN", logical: "OR", permissions: ["USER_MENU_VIEW"] },
        ],
      },
    ],
  },
  {
    title: "系统设置",
    icon: "xitongshezhi",
    name: "system_setting",
    path: "/system_setting",
    order: 3,
    children: [
      {
        title: "系统设置",
        icon: "shezhi",
        name: "xitongshezhi",
        componentPath: "/src/views/SystemModule/index.vue",
        path: "/system_module",
        requiredPermissions: [
          {
            role: "ADMIN",
            logical: "OR",
            permissions: [],
          },
        ],
        order: 1,
      },
      {
        title: "显示设置",
        icon: "xianshi",
        name: "view_setting",
        componentPath: "/src/views/ViewSetting/index.vue",
        path: "/view_setting",
        requiredPermissions: [
          {
            role: "ADMIN",
            logical: "OR",
            permissions: [],
          },
        ],
        order: 2,
      },
      {
        title: "消息通知",
        icon: "shouye",
        name: "message",
        componentPath: "/src/views/Message/index.vue",
        path: "/message",
        requiredPermissions: [
          {
            role: "ADMIN",
            logical: "OR",
            permissions: [],
          },
        ],
        order: 3,
      },
      {
        title: "系统日志",
        icon: "zhuanxierizhi",
        name: "system_log",
        componentPath: "/src/views/SystemLog/index.vue",
        path: "/system_log",
        requiredPermissions: [
          {
            role: "ADMIN",
            logical: "OR",
            permissions: [],
          },
        ],
        order: 4,
      },
      {
        title: "关于",
        icon: "guanyu",
        name: "about",
        componentPath: "/src/views/About/index.vue",
        path: "/about",
        requiredPermissions: [
          {
            role: "ORG",
            logical: "OR",
            permissions: [],
          },
        ],
        order: 5,
      },
    ],
  },
];
export const runingModulesMenu = {
  "management-center": [
    {
      name: "user_manage",
      title: "用户与租户",
      componentPath: "",
      path: "/user_manage",
      icon: "yonghuguanli_huaban",
      order: 1,
      children: [
        {
          name: "user",
          title: "用户管理",
          icon: "yonghuguanli_huaban",
          path: "/user",
          componentPath: "/src/views/UserManage/index.vue",
          requiredPermissions: [
            { role: "ADMIN", logical: "OR", permissions: ["USER_MENU_VIEW"] },
          ],
          order: 1,
        },
        {
          name: "role",
          title: "角色管理",
          icon: "jurassic_user",
          path: "/role",
          componentPath: "/src/views/RoleManage/index.vue",
          requiredPermissions: [
            { role: "ADMIN", logical: "OR", permissions: ["ROLE_MENU_VIEW"] },
          ],
          order: 2,
        },
        {
          name: "org",
          title: "组织管理",
          icon: "zuzhijiagou",
          path: "/org",
          componentPath: "/src/views/OrgManage/index.vue",
          requiredPermissions: [
            { role: "ADMIN", logical: "OR", permissions: ["ORG_MENU_VIEW"] },
          ],
          order: 3,
        },
        {
          name: "workspace",
          title: "工作空间",
          icon: "project_space",
          path: "/workspace",
          componentPath: "/src/views/WorkspaceManage/index.vue",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: ["WORKSPACE_MENU_VIEW"],
            },
          ],
          order: 4,
        },
      ],
    },
    {
      title: "系统设置",
      icon: "xitongshezhi",
      name: "system_setting",
      path: "/system_setting",
      order: 2,
      children: [
        {
          title: "系统设置",
          icon: "shezhi",
          name: "xitongshezhi",
          componentPath: "/src/views/SystemModule/index.vue",
          path: "/system_module",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 1,
        },
        {
          title: "显示设置",
          icon: "xianshi",
          name: "view_setting",
          componentPath: "/src/views/ViewSetting/index.vue",
          path: "/view_setting",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 2,
        },
        {
          title: "消息通知",
          icon: "shouye",
          name: "message",
          componentPath: "/src/views/Message/index.vue",
          path: "/message",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 3,
        },
        {
          title: "系统日志",
          icon: "rizhi",
          name: "system_log",
          componentPath: "/src/views/SystemLog/index.vue",
          path: "/system_log",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 4,
        },
        {
          title: "关于",
          icon: "guanyu",
          name: "about",
          componentPath: "/src/views/About/index.vue",
          path: "/about",
          requiredPermissions: [
            {
              role: "ORG",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 5,
        },
      ],
    },
  ],
  "service-directory": [
    {
      name: "user_manage",
      title: "用户与租户",
      componentPath: "",
      path: "/user_manage",
      icon: "yonghuguanli_huaban",
      order: 1,
      children: [
        {
          name: "user",
          title: "用户管理",
          icon: "yonghuguanli_huaban",
          path: "/user",
          componentPath: "/src/views/UserManage/index.vue",
          requiredPermissions: [
            { role: "ADMIN", logical: "OR", permissions: ["USER_MENU_VIEW"] },
          ],
          order: 1,
        },
        {
          name: "role",
          title: "角色管理",
          icon: "jurassic_user",
          path: "/role",
          componentPath: "/src/views/RoleManage/index.vue",
          requiredPermissions: [
            { role: "ADMIN", logical: "OR", permissions: ["ROLE_MENU_VIEW"] },
          ],
          order: 2,
        },
        {
          name: "org",
          title: "组织管理",
          icon: "zuzhijiagou",
          path: "/org",
          componentPath: "/src/views/OrgManage/index.vue",
          requiredPermissions: [
            { role: "ADMIN", logical: "OR", permissions: ["ORG_MENU_VIEW"] },
          ],
          order: 3,
        },
        {
          name: "workspace",
          title: "工作空间",
          icon: "project_space",
          path: "/workspace",
          componentPath: "/src/views/WorkspaceManage/index.vue",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: ["WORKSPACE_MENU_VIEW"],
            },
          ],
          order: 4,
        },
      ],
    },
    {
      title: "系统设置",
      icon: "xitongshezhi",
      name: "system_setting",
      path: "/system_setting",
      order: 2,
      children: [
        {
          title: "系统设置",
          icon: "shezhi",
          name: "xitongshezhi",
          componentPath: "/src/views/SystemModule/index.vue",
          path: "/system_module",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 1,
        },
        {
          title: "显示设置",
          icon: "xianshi",
          name: "view_setting",
          componentPath: "/src/views/ViewSetting/index.vue",
          path: "/view_setting",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 2,
        },
        {
          title: "消息通知",
          icon: "shouye",
          name: "message",
          componentPath: "/src/views/Message/index.vue",
          path: "/message",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 3,
        },
        {
          title: "系统日志",
          icon: "rizhi",
          name: "system_log",
          componentPath: "/src/views/SystemLog/index.vue",
          path: "/system_log",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 4,
        },
        {
          title: "关于",
          icon: "guanyu",
          name: "about",
          componentPath: "/src/views/About/index.vue",
          path: "/about",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 5,
        },
      ],
    },
  ],
  "server-service": [
    {
      name: "user_manage",
      title: "用户与租户",
      componentPath: "",
      path: "/user_manage",
      icon: "yonghuguanli_huaban",
      order: 1,
      children: [
        {
          name: "user",
          title: "用户管理",
          icon: "yonghuguanli_huaban",
          path: "/user",
          componentPath: "/src/views/UserManage/index.vue",
          requiredPermissions: [
            { role: "ADMIN", logical: "OR", permissions: ["USER_MENU_VIEW"] },
          ],
          order: 1,
        },
        {
          name: "role",
          title: "角色管理",
          icon: "jurassic_user",
          path: "/role",
          componentPath: "/src/views/RoleManage/index.vue",
          requiredPermissions: [
            { role: "ADMIN", logical: "OR", permissions: ["ROLE_MENU_VIEW"] },
          ],
          order: 2,
        },
        {
          name: "org",
          title: "组织管理",
          icon: "zuzhijiagou",
          path: "/org",
          componentPath: "/src/views/OrgManage/index.vue",
          requiredPermissions: [
            { role: "ADMIN", logical: "OR", permissions: ["ORG_MENU_VIEW"] },
          ],
          order: 3,
        },
        {
          name: "workspace",
          title: "工作空间",
          icon: "project_space",
          path: "/workspace",
          componentPath: "/src/views/WorkspaceManage/index.vue",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: ["WORKSPACE_MENU_VIEW"],
            },
          ],
          order: 4,
        },
      ],
    },
    {
      title: "系统设置",
      icon: "xitongshezhi",
      name: "system_setting",
      path: "/system_setting",
      order: 2,
      children: [
        {
          title: "系统设置",
          icon: "shezhi",
          name: "xitongshezhi",
          componentPath: "/src/views/SystemModule/index.vue",
          path: "/system_module",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 1,
        },
        {
          title: "显示设置",
          icon: "xianshi",
          name: "view_setting",
          componentPath: "/src/views/ViewSetting/index.vue",
          path: "/view_setting",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 2,
        },
        {
          title: "消息通知",
          icon: "shouye",
          name: "message",
          componentPath: "/src/views/Message/index.vue",
          path: "/message",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 3,
        },
        {
          title: "系统日志",
          icon: "rizhi",
          name: "system_log",
          componentPath: "/src/views/SystemLog/index.vue",
          path: "/system_log",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 4,
        },
        {
          title: "关于",
          icon: "guanyu",
          name: "about",
          componentPath: "/src/views/About/index.vue",
          path: "/about",
          requiredPermissions: [
            {
              role: "ORG",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 5,
        },
      ],
    },
  ],
  "message-service": [
    {
      name: "user_manage",
      title: "用户与租户",
      componentPath: "",
      path: "/user_manage",
      icon: "yonghuguanli_huaban",
      order: 1,
      children: [
        {
          name: "user",
          title: "用户管理",
          icon: "yonghuguanli_huaban",
          path: "/user",
          componentPath: "/src/views/UserManage/index.vue",
          requiredPermissions: [
            { role: "ADMIN", logical: "OR", permissions: ["USER_MENU_VIEW"] },
          ],
          order: 1,
        },
        {
          name: "role",
          title: "角色管理",
          icon: "jurassic_user",
          path: "/role",
          componentPath: "/src/views/RoleManage/index.vue",
          requiredPermissions: [
            { role: "ADMIN", logical: "OR", permissions: ["ROLE_MENU_VIEW"] },
          ],
          order: 2,
        },
        {
          name: "org",
          title: "组织管理",
          icon: "zuzhijiagou",
          path: "/org",
          componentPath: "/src/views/OrgManage/index.vue",
          requiredPermissions: [
            { role: "ADMIN", logical: "OR", permissions: ["ORG_MENU_VIEW"] },
          ],
          order: 3,
        },
        {
          name: "workspace",
          title: "工作空间",
          icon: "project_space",
          path: "/workspace",
          componentPath: "/src/views/WorkspaceManage/index.vue",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: ["WORKSPACE_MENU_VIEW"],
            },
          ],
          order: 4,
        },
      ],
    },
    {
      title: "系统设置",
      icon: "xitongshezhi",
      name: "system_setting",
      path: "/system_setting",
      order: 2,
      children: [
        {
          title: "系统设置",
          icon: "shezhi",
          name: "xitongshezhi",
          componentPath: "/src/views/SystemModule/index.vue",
          path: "/system_module",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 1,
        },
        {
          title: "显示设置",
          icon: "xianshi",
          name: "view_setting",
          componentPath: "/src/views/ViewSetting/index.vue",
          path: "/view_setting",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 2,
        },
        {
          title: "消息通知",
          icon: "shouye",
          name: "message",
          componentPath: "/src/views/Message/index.vue",
          path: "/message",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 3,
        },
        {
          title: "系统日志",
          icon: "rizhi",
          name: "system_log",
          componentPath: "/src/views/SystemLog/index.vue",
          path: "/system_log",
          requiredPermissions: [
            {
              role: "ADMIN",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 4,
        },
        {
          title: "关于",
          icon: "guanyu",
          name: "about",
          componentPath: "/src/views/About/index.vue",
          path: "/about",
          requiredPermissions: [
            {
              role: "ORG",
              logical: "OR",
              permissions: [],
            },
          ],
          order: 5,
        },
      ],
    },
  ],
};
