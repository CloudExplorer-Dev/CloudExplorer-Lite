const message = {
  user: {
    type: "User Type",
    name: "Name",
    email: "Email",
    phone: "Phone",
    wechatAccount: "Wechat Account",
    role: "Role",
    password: "Password",
    local: "Local",
    extra: "Extra",
    source: "Source",
    status: "Status",
    manage: "User Manage",
    delete_role: "Delete Role",
    add_role: "Add Role",
    set_role: "Set Role",
    has_role: "Has Role",
    add_org: "Add Organization",
    add_workspace: "Add Workspace",
    delete_confirm: "Sure To Delete",
    notify_setting: "Notify Setting",
    notify_tips:
      "The email address and mobile phone number are associated with the user's basic information. The mobile phone number will be used as the push identifier of Dingding platform. Enterprise wechat account for reference",
    validate: {
      phone_format: "The format of the mobile phone number is incorrect",
      email_format: "Email Format Error",
      selected: "Please Select User",
      role_empty: "Role information cannot be empty",
      param: "Parameter Invalid",
      user_type_empty: "User type cannot be empty",
      org: "Please Select Organization",
      workspace: "Please Select Workspace",
    },
  },
  workspace: {
    user_count: "Number of users",
    workspace_name: "Workspace Name",
    org: "Parent Organization",
    validate: {
      repeat: "{0} duplicate",
    },
  },
  // 云账号相关国际化
  cloud_account: {
    name: "Cloud account name",
    name_placeholder: "Please enter the name of your cloud account",
    base_setting: "Basic settings",
    sync_setting: "Sync Settings",
    cloud_account_size: "You must select one cloud account",
    verification: "check",
    sync_message: "synchronization",
    edit_job_message: "Edit Scheduled Task",
    platform: "Cloud platform",
    native_state_valid_message: "The cloud account is valid",
    native_state_invalid_message: "Invalid cloud account",
    native_state: "Cloud account status",
    native_state_valid: "Valid",
    native_state_invalid: "invalid",
    native_sync_status: "Cloud account synchronization status",
    native_sync: {
      init: "initialization",
      success: "success",
      failed: "fail",
      syncing: "Synchronizing",
      unknown: "unknown",
    },
    last_sync_time: "Last Sync Time",
    please_select_platform_message: "Please select a cloud platform",
    account_information_message: "account information",
    field_is_not_null: "Field cannot be empty",
    name_is_not_empty: "The cloud account name cannot be empty",
    platform_is_not_empty: "Cloud platform cannot be empty",
    sync: {
      once: "Synchronize once",
      region: "region",
      range: "Synchronization range",
      timing: "Timing synchronization",
      interval: "interval",
      interval_time_unit: {
        millisecond: "millisecond",
        second: "second",
        minute: "minute",
        hour: "hour",
        day: "day",
      },
    },
  },
  // 组织相关国际化
  org_manage: {
    affiliated_organization: "Affiliated organization",
    organization_name_is_not_empty: "Organization name cannot be empty",
    organization_description_is_not_empty:
      "Organization description cannot be empty",
  },
  log_manage: {
    "login": "Login Log",
    "vm": "Virtual machine operation log",
    "disk": "Disk Operation Log",
    "platform": "Platform Management Log",
    "operator": "Operator",
    "module": "Module",
    "menu": "Menu",
    "type": "Operation Type",
    "resource": "Operation object",
    "ip": "Operation IP",
    "status": "Operation status",
    "view_details": "View details",
    "belong_vm": "Virtual machine",
    btn: {
      "clear_policy": "Clear Policy"
    }
  }
};

export default {
  ...message,
};
