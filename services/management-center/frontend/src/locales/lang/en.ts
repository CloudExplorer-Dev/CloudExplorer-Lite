const message = {
  workspace: {
    user_count: "Number of users",
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
};

export default {
  ...message,
};
