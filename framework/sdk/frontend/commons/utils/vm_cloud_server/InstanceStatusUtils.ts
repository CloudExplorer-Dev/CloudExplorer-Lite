import { i18n } from "@commons/base-locales";

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
const { t } = i18n.global;

//状态
export const instanceStatusMap: Map<string, string> = new Map();
instanceStatusMap.set("Running", t("", "运行中"));
instanceStatusMap.set("Deleted", t("", "已删除"));
instanceStatusMap.set("Stopped", t("", "已关机"));
instanceStatusMap.set("Starting", t("", "启动中"));
instanceStatusMap.set("Stopping", t("", "关机中"));
instanceStatusMap.set("Rebooting", t("", "重启中"));
instanceStatusMap.set("Deleting", t("", "删除中"));
instanceStatusMap.set("Creating", t("", "创建中"));
instanceStatusMap.set("Unknown", t("", "未知"));
instanceStatusMap.set("Failed", t("", "失败"));
instanceStatusMap.set("ToBeRecycled", t("", "待回收"));
instanceStatusMap.set("WaitCreating", t("", "排队中"));
instanceStatusMap.set("ConfigChanging", t("", "配置变更中"));

export function getStatusName(status: string) {
  return instanceStatusMap.get(status);
}

const InstanceStatusUtils = {
  instanceStatusMap,
  getStatusName,
};

export default InstanceStatusUtils;
