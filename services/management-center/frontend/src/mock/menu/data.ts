export default {
  "management-center": [
    {
      name: "user_manage",
      title: "用户与租户",
      componentPath: "",
      path: "user_manage",
      icon: "corporate_fare",
      children: [
        {
          name: "user",
          title: "用户管理",
          icon: "yonghuguanli",
          path: "/user",
          componentPath: "",
        },
        {
          name: "role",
          title: "角色管理",
          icon: "jiaoseguanli",
          path: "/role",
          componentPath: "",
        },
        {
          name: "org",
          title: "组织管理",
          icon: "zuzhiguanli",
          path: "/org",
          componentPath: "",
        },
        {
          name: "workspace",
          title: "工作空间",
          icon: "gongzuokongjian",
          path: "/workspace",
          componentPath: "",
        },
      ],
    },
  ],
  "service-directory": [{}],
  "server-service": [{}],
  "message-service": [{}],
};
