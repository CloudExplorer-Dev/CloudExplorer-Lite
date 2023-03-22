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

  constructor(status: string, tableSelect?: boolean, order?: number) {
    this.status = status;
    this.tableSelect = !!tableSelect;
    this.order = order === undefined ? -1 : order;
    this.name = computed<string>(() => {
      return t(`commons.cloud_server.status.${this.status}`, this.status);
    });
  }
}

const instanceStatusList = computed<Array<InstanceStatus>>(() => [
  new InstanceStatus("Running", true, 2),
  new InstanceStatus("Deleted", true, 6),
  new InstanceStatus("Stopped", true, 3),
  new InstanceStatus("Starting"),
  new InstanceStatus("Stopping"),
  new InstanceStatus("Rebooting", true, 4),
  new InstanceStatus("Deleting"),
  new InstanceStatus("Creating", true, 1),
  new InstanceStatus("Unknown"),
  new InstanceStatus("Failed", true, 7),
  new InstanceStatus("ToBeRecycled", true, 5),
  new InstanceStatus("WaitCreating"),
  new InstanceStatus("ConfigChanging"),
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

const InstanceStatusUtils = {
  instanceStatusList,
  instanceStatusListForTableSelect,
  getStatusName,
};

export default InstanceStatusUtils;
