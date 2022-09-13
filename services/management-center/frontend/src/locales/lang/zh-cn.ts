const message = {
  user: {
    type: "用户类型",
    name: "姓名",
    email: "邮箱",
    role: "角色",
    password: "密码",
    local: "本地创建",
    extra: "第三方",
    source: "来源",
    status: "状态",
    manage: "用户管理",
    delete_role: "删除角色",
    add_role: "添加角色",
    set_role: "设置角色",
    add_org: "添加组织",
    add_workspace: "添加工作空间",
    delete_confirm: "确认删除用户",
    notify_setting: "通知设置",
    validate: {
      phone_format: "手机号码格式错误",
      email_format: "邮箱格式错误",
    },
  },
  workspace: {
    user_count: "用户数",
  },
  cloud_account: {
    base_setting: "基本设置",
    sync_setting: "同步设置",
  },
};
export default {
  ...message,
};
