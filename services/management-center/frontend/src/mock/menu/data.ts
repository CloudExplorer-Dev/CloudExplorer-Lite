export default {
  "management-center": [
    {
      name: "user_manage",
      title: "用户与租户",
      componentPath: "",
      path: "/user_manage",
      icon: "corporate_fare",
      children: [
        {
          name: "user",
          title: "用户管理",
          icon: "yonghuguanli",
          path: "/user",
          componentPath: "/src/views/UserManage/index.vue",
        },
        {
          name: "role",
          title: "角色管理",
          icon: "jiaoseguanli",
          path: "/role",
          componentPath: "/src/views/RoleManage/index.vue",
        },
        {
          name: "org",
          title: "组织管理",
          icon: "zuzhiguanli",
          path: "/org",
          componentPath: "/src/views/OrgManage/index.vue",
        },
        {
          name: "workspace",
          title: "工作空间",
          icon: "gongzuokongjian",
          path: "/workspace",
          componentPath: "/src/views/WorkspaceManage/index.vue",
        },
      ],
    },
    {
      title: "系统设置",
      icon: "shezhi",
      name: "system_setting",
      path: "/system_setting",
      children: [
        {
          title: "系统设置",
          icon: "shezhi",
          name: "system_module",
          componentPath: "/src/views/SystemModule/index.vue",
          path: "/system_module",
        },
        {
          title: "显示设置",
          icon: "xianshi",
          name: "view_setting",
          componentPath: "/src/views/ViewSetting/index.vue",
          path: "/view_setting",
        },
        {
          title: "消息通知",
          icon: "xiaoxi",
          name: "message",
          componentPath: "/src/views/Message/index.vue",
          path: "/message",
        },
        {
          title: "系统日志",
          icon: "zhuanxierizhi",
          name: "system_log",
          componentPath: "/src/views/SystemLog/index.vue",
          path: "/system_log",
        },
        {
          title: "关于",
          icon: "guanyu",
          name: "about",
          componentPath: "/src/views/About/index.vue",
          path: "/about",
        },
      ],
    },
  ],
};
