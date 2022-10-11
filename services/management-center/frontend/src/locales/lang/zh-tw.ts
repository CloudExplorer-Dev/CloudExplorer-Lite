const message = {
  user: {
    type: "用戶類型",
    name: "姓名",
    email: "郵箱",
    phone: "手機號碼",
    wechatAccount: "企業微信賬號",
    role: "角色",
    password: "密碼",
    local: "本地創建",
    extra: "第三方",
    source: "來源",
    status: "狀態",
    manage: "用戶管理",
    delete_role: "刪除角色",
    add_role: "添加角色",
    set_role: "設置角色",
    has_role: "擁有角色",
    add_org: "添加組織",
    add_workspace: "添加工作空間",
    delete_confirm: "確認刪除用戶",
    notify_setting: "通知設置",
    notify_tips:
      "郵箱、手機號設置後將與用戶基本信息關聯。手機號將做為釘釘平臺推送標識。企業微信賬號參考",
    validate: {
      phone_format: "手機號碼格式錯誤",
      email_format: "郵箱格式錯誤",
      selected: "請選擇用戶",
      role_empty: "角色信息不能為空",
      param: "參數不合法",
      user_type_empty: "用戶類型不能為空",
      org: "請選擇組織",
      workspace: "請選擇工作空間",
    },
  },
  workspace: {
    user_count: "用戶數",
    workspace_name: "工作空間名稱",
    org: "父級組織",
    validate: {
      repeat: "{0}重複",
    },
  },
  cloud_account: {
    base_setting: "基本設定",
    sync_setting: "同步設定",
  },
  log_manage: {
    "login": "登入日誌",
    "vm": "虛擬機器操作日誌",
    "disk": "磁片操作日誌",
    "platform": "平臺管理日誌",
    "operator": "操作人",
    "module": "模塊",
    "menu": "選單",
    "type": "操作類型",
    "resource": "操作對象",
    "ip": "操作IP",
    "status": "操作狀態",
    "view_details": "查看詳情",
    "belong_vm": "所屬虛擬機器",
    btn: {
      "clear_policy": "清空策略"
    }
  }
};

export default {
  ...message,
};
