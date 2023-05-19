import { i18n } from "@commons/base-locales";
import { computed, type ComputedRef } from "vue";
import _ from "lodash";

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
const { t } = i18n.global;

class InstanceStatus {
  status: string;
  tableSelect: boolean;
  name?: ComputedRef<string>;

  order?: number;
  icon?: string;
  color?: string;

  constructor(
    status: string,
    tableSelect?: boolean,
    order?: number,
    icon?: string,
    color?: string
  ) {
    this.status = status;
    this.tableSelect = !!tableSelect;
    this.order = order === undefined ? -1 : order;
    this.name = computed<string>(() => {
      return t(`commons.cloud_server.status.${this.status}`, this.status);
    });
    this.icon = icon === undefined ? "" : icon;
    this.color = color === undefined ? "" : color;
  }
}

const instanceStatusList = computed<Array<InstanceStatus>>(() => [
  new InstanceStatus("Running", true, 2, "icon_skip_outlined", "#34C724"),
  new InstanceStatus("Deleted", true, 6, "icon_deleting", "#6E748E"),
  new InstanceStatus("Stopped", true, 3, "guanji-", "#000000"),
  new InstanceStatus("Starting", false, -1, "shuaxin4", "#34C724"),
  new InstanceStatus("Stopping", false, -1, "shuaxin4", "#000000"),
  new InstanceStatus("Rebooting", true, 4, "shuaxin4", "#34C724"),
  new InstanceStatus("Deleting", false, -1, "shuaxin4", "#6E748E"),
  new InstanceStatus("Creating", true, 1, "icon_testing", "#3370FF"),
  new InstanceStatus("Unknown", false, -1, "icon_disable", "#6E748E"),
  new InstanceStatus("Failed", true, 7, "shibai", "#F54A45"),
  new InstanceStatus("ToBeRecycled", true, 5, "icon_retrieve", "#FF8802"),
  new InstanceStatus("WaitCreating", false, -1, "shuaxin4", "#3370FF"),
  new InstanceStatus("ConfigChanging", false, -1, "shuaxin4", "#34C724"),
]);

const instanceStatusListForTableSelect = computed<Array<InstanceStatus>>(() => {
  return _.orderBy(
    _.filter(instanceStatusList.value, (s) => s.tableSelect),
    (s) => s.order
  );
});

function getStatusName(status: string) {
  const o = _.find(instanceStatusList.value, (s) => s.status === status);
  if (o) {
    return _.defaultTo(o.name?.value, status);
  } else {
    return status;
  }
}

function getInstanceStatus(status: string) {
  const o = _.find(instanceStatusList.value, (s) => s.status === status);
  if (o) {
    return o;
  } else {
    return undefined;
  }
}

const InstanceStatusUtils = {
  instanceStatusList,
  instanceStatusListForTableSelect,
  getStatusName,
  getInstanceStatus,
};

export default InstanceStatusUtils;
