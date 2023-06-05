import IconCloseFilled from "@commons/assets/img/status-icon/IconCloseFilled.vue";
import IconWait from "@commons/assets/img/status-icon/IconWait.vue";
import IconSucceedFilled from "@commons/assets/img/status-icon/IconSucceedFilled.vue";
import IconTesting from "@commons/assets/img/status-icon/IconTesting.vue";
import IconWarningFilled from "@commons/assets/img/status-icon/IconWarningFilled.vue";
import IconInfoFilled from "@commons/assets/img/status-icon/IconInfoFilled.vue";

export const getStatusIcon = (status: string) => {
  return status === "FAILED"
    ? IconCloseFilled
    : status === "INIT"
    ? IconWait
    : status === "SUCCESS"
    ? IconSucceedFilled
    : status === "SYNCING"
    ? IconTesting
    : status === "TIME_OUT"
    ? IconWarningFilled
    : IconInfoFilled;
};

export const getColorByAccountStatus = (status: string) => {
  return status === "FAILED"
    ? "#F54A45"
    : status === "INIT"
    ? "var(--el-color-warning)"
    : status === "SUCCESS"
    ? "#62D256"
    : status === "SYNCING"
    ? "#3370FF"
    : status === "TIME_OUT"
    ? "var(--el-color-danger-dark-2)"
    : "var(--el-color-info)";
};

export default {
  getStatusIcon,
  getColorByAccountStatus,
};
