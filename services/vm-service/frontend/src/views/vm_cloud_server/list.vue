<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, computed } from "vue";
import VmCloudServerApi from "@/api/vm_cloud_server";
import type { VmCloudServerVO } from "@/api/vm_cloud_server/type";
import { useRouter } from "vue-router";
import {
  PaginationConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
import { ElMessage, ElMessageBox, ElPopover } from "element-plus";
import _ from "lodash";
import type { SimpleMap } from "@commons/api/base/type";
import BaseCloudAccountApi from "@commons/api/cloud_account";
import RecycleBinsApi from "@/api/recycle_bin";
import Grant from "@/views/vm_cloud_server/grant.vue";
import { usePermissionStore } from "@commons/stores/modules/permission";
import ButtonToolBar from "@commons/components/button-tool-bar/ButtonToolBar.vue";
import VmServerStatusIcon from "@/views/vm_cloud_server/VmServerStatusIcon.vue";
import {
  ButtonAction,
  type ButtonActionType,
} from "@commons/components/button-tool-bar/type";
import OrgTreeFilter from "@commons/components/table-filter/OrgTreeFilter.vue";
import AddDisk from "@/views/vm_cloud_server/AddDisk.vue";
import ChangeConfig from "@/views/vm_cloud_server/ChangeConfig.vue";
import { classifyIP } from "@commons/utils/util";
import CeIcon from "@commons/components/ce-icon/index.vue";
import PlatformIcon from "@commons/components/platform-icon/index.vue";
const { t } = useI18n();
const permissionStore = usePermissionStore();
const useRoute = useRouter();
const table = ref<any>(null);
const columns = ref([]);
const tableData = ref<Array<VmCloudServerVO>>([]);
const selectedRowData = ref<Array<VmCloudServerVO>>([]);
const tableLoading = ref<boolean>(false);
const cloudAccount = ref<Array<SimpleMap<string>>>([]);
const addDiskRef = ref<InstanceType<typeof AddDisk>>();
const changeConfigRef = ref<InstanceType<typeof ChangeConfig>>();
import InstanceStatusUtils from "@commons/utils/vm_cloud_server/InstanceStatusUtils";
import EnableStatusSwitch from "@/views/vm_cloud_server/EnableStatusSwitch.vue";

/**
 * 表头：组织树筛选
 */
const orgTreeRef = ref();
const orgPopRef = ref();
const selectedOrganizationIds = computed(() =>
  orgTreeRef.value?.getSelectedIds(false)
);

/**
 * 表头：工作空间树筛选
 */
const workspaceTreeRef = ref();
const workspacePopRef = ref();
const selectedWorkspaceIds = computed(() =>
  workspaceTreeRef.value?.getSelectedIds(true)
);

/**
 * 表头：清空组织和工作空间树的选中项
 * @param field
 */
const clearCondition = (field: string) => {
  if (field === "organizationIds") {
    orgTreeRef.value.cancelChecked();
  }
  if (field === "workspaceIds") {
    workspaceTreeRef.value.cancelChecked();
  }
};

//批量操作
const instanceOperateMap: Map<string, string> = new Map();
instanceOperateMap.set("POWER_ON", t("", "启动"));
instanceOperateMap.set("POWER_OFF", t("", "关机"));
instanceOperateMap.set("REBOOT", t("", "重启"));
instanceOperateMap.set("DELETE", t("", "删除"));

// 表格头:状态筛选项
const instanceStatusForTableSelect = computed(() => {
  return _.map(
    InstanceStatusUtils.instanceStatusListForTableSelect.value,
    (s) => {
      return {
        text: s.name?.value,
        value: s.status,
      };
    }
  );
});

// 表格头:付费类型筛选项
const chargeType = [
  { text: t("commons.charge_type.prepaid"), value: "PrePaid" },
  { text: t("commons.charge_type.postpaid"), value: "PostPaid" },
];

const filterChargeType = (value: string) => {
  let status = value;
  chargeType.forEach((v) => {
    if (v.value == value) {
      status = v.text;
      return;
    }
  });
  return status;
};

// 表格头:VMTools 筛选项
const vmToolsStatus = [
  {
    text: t("vm_cloud_server.vm_tools_status.not_Installed"),
    value: "toolsNotInstalled",
  },
  {
    text: t("vm_cloud_server.vm_tools_status.not_running"),
    value: "guestToolsNotRunning",
  },
  {
    text: t("vm_cloud_server.vm_tools_status.running"),
    value: "guestToolsRunning",
  },
];

const filterVmToolsStatus = (value: string) => {
  let status = value;
  vmToolsStatus.forEach((v) => {
    if (v.value == value) {
      status = v.text;
      return;
    }
  });
  return status;
};

/**
 * 查询
 * @param condition
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  VmCloudServerApi.listVmCloudServer(
    {
      currentPage: tableConfig.value.paginationConfig.currentPage,
      pageSize: tableConfig.value.paginationConfig.pageSize,
      ...params,
    },
    tableLoading
  ).then((res) => {
    tableData.value = res.data.records;
    tableConfig.value.paginationConfig?.setTotal(
      res.data.total,
      tableConfig.value.paginationConfig
    );
    tableConfig.value.paginationConfig?.setCurrentPage(
      res.data.current,
      tableConfig.value.paginationConfig
    );
  });
};

const refresh = () => {
  table.value.search();
};

/**
 * 是否开启了回收站
 */
const isRecycleBinOpened = ref(true);
const getRecycleBinSetting = async () => {
  await RecycleBinsApi.getRecycleEnableStatus().then((result) => {
    isRecycleBinOpened.value = result.data;
  });
};

/**
 * 页面挂载
 */
onMounted(() => {
  search(new TableSearch());
  searchCloudAccount();
  startOperateInterval();
});
onBeforeUnmount(() => {
  stopOperateInterval();
});

const searchCloudAccount = () => {
  BaseCloudAccountApi.listAll().then((result) => {
    if (result.data.length > 0) {
      result.data.forEach(function (v) {
        const ca = { text: v.name, value: v.id };
        cloudAccount.value.push(ca);
      });
    }
  });
};

const cloudServerInterval = ref<any>();
//启动定时器
const startOperateInterval = () => {
  cloudServerInterval.value = setInterval(() => {
    const list = _.map(tableData.value, (r) => r.id);
    if (list.length === 0) {
      return;
    }
    VmCloudServerApi.getVmCloudServerByIds(list).then((res) => {
      if (res) {
        for (let i = 0; i < res.data.length; i++) {
          _.forEach(tableData.value, function (vm) {
            if (vm.id === res.data[i].id) {
              _.assign(vm, res.data[i]);
            }
          });
        }
      }
    });
  }, 6000);
};
const updateInstanceStatus = (list: Array<VmCloudServerVO>) => {
  for (const vm of list) {
    VmCloudServerApi.getVmCloudServerById(vm.id).then((res) => {
      vm.instanceStatus = res.data.instanceStatus;
    });
  }
};
//停止定时器
const stopOperateInterval = () => {
  if (cloudServerInterval.value) {
    clearInterval(cloudServerInterval.value);
  }
};

/**
 * 选中的数据
 */
const handleSelectionChange = (list: Array<VmCloudServerVO>) => {
  selectedRowData.value = list;
};

/**
 * 详情
 */
const showDetail = (row: VmCloudServerVO) => {
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace(
      "/list",
      `/detail/${row.id}`
    ),
  });
};
const gotoCatalog = () => {
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace("/list", "/catalog"),
  });
};

/**
 * 添加磁盘
 * @param row
 */
const createDisk = (row: VmCloudServerVO) => {
  addDiskRef.value?.open(row.id);
};

/**
 * 配置变更
 * @param row
 */
const changeVmConfig = (row: VmCloudServerVO) => {
  changeConfigRef.value?.open(row.id);
};

/**
 * 验证VMTools状态
 * @param vm
 */
const checkVmToolsStatus = (vm: VmCloudServerVO) => {
  if (vm.platform === "fit2cloud_vsphere_platform") {
    return vm.vmToolsStatus == "guestToolsRunning";
  }
  return true;
};
/**
 * 开机
 * @param row
 */
const powerOn = (row: VmCloudServerVO) => {
  ElMessageBox.confirm(
    t("vm_cloud_server.message_box.confirm_power_on", "确认启动"),
    t("commons.message_box.prompt", "提示"),
    {
      confirmButtonText: t("commons.message_box.confirm", "确认"),
      cancelButtonText: t("commons.btn.cancel", "取消"),
      type: "warning",
    }
  ).then(() => {
    VmCloudServerApi.powerOn(row.id as string)
      .then((res) => {
        ElMessage.success(t("commons.msg.op_success"));
        refresh();
      })
      .catch((err) => {
        ElMessage.error(err.response.data.message);
      });
  });
};
//关机
const shutdown = (row: VmCloudServerVO) => {
  let label = t("vm_cloud_server.message_box.confirm_shutdown", "确认关机");
  let powerOff = false;
  if (!checkVmToolsStatus(row)) {
    label = t(
      "vm_cloud_server.message_box.check_vm_tools_status_confirm_shutdown",
      "当前云主机未安装VmTools或VmTools未运行，无法软关机，若继续操作则关闭电源，是否继续？"
    );
    powerOff = true;
  }
  ElMessageBox.confirm(label, t("commons.message_box.prompt", "提示"), {
    confirmButtonText: t("commons.message_box.confirm", "确认"),
    cancelButtonText: t("commons.btn.cancel", "取消"),
    type: "warning",
  }).then(() => {
    if (powerOff) {
      VmCloudServerApi.powerOff(row.id as string)
        .then((res) => {
          ElMessage.success(t("commons.msg.op_success"));
          refresh();
        })
        .catch((err) => {
          ElMessage.error(err.response.data.message);
        });
    } else {
      VmCloudServerApi.shutdownInstance(row.id as string)
        .then((res) => {
          ElMessage.success(t("commons.msg.op_success"));
          refresh();
        })
        .catch((err) => {
          ElMessage.error(err.response.data.message);
        });
    }
  });
};
//关闭电源
const powerOff = (row: VmCloudServerVO) => {
  ElMessageBox.confirm(
    t("vm_cloud_server.message_box.confirm_power_off", "确认关闭电源"),
    t("commons.message_box.prompt", "提示"),
    {
      confirmButtonText: t("commons.message_box.confirm", "确认"),
      cancelButtonText: t("commons.btn.cancel", "取消"),
      type: "warning",
    }
  ).then(() => {
    VmCloudServerApi.powerOff(row.id as string)
      .then(() => {
        ElMessage.success(t("commons.msg.op_success"));
        refresh();
      })
      .catch((err) => {
        ElMessage.error(err.response.data.message);
      });
  });
};
//重启
const reboot = (row: VmCloudServerVO) => {
  ElMessageBox.confirm(
    t("vm_cloud_server.message_box.confirm_reboot", "确认重启"),
    t("commons.message_box.prompt", "提示"),
    {
      confirmButtonText: t("commons.message_box.confirm", "确认"),
      cancelButtonText: t("commons.btn.cancel", "取消"),
      type: "warning",
    }
  ).then(() => {
    VmCloudServerApi.reboot(row.id as string)
      .then(() => {
        ElMessage.success(t("commons.msg.op_success"));
        refresh();
      })
      .catch((err) => {
        ElMessage.error(err.response.data.message);
      });
  });
};

//删除
const deleteInstance = async (row: VmCloudServerVO) => {
  if (row.instanceStatus === "Failed") {
    deleteFailedRecord(row.id);
    return;
  }
  await getRecycleBinSetting();

  const message = isRecycleBinOpened.value
    ? t(
        "vm_cloud_server.message_box.confirm_recycle",
        "回收站已开启，云主机将关机并放入回收站中"
      )
    : t(
        "vm_cloud_server.message_box.confirm_to_delete",
        "回收站已关闭，云主机将立即删除"
      );
  ElMessageBox.confirm(message, t("commons.message_box.prompt", "提示"), {
    confirmButtonText: t("commons.message_box.confirm", "确认"),
    cancelButtonText: t("commons.btn.cancel", "取消"),
    type: "warning",
  }).then(() => {
    if (isRecycleBinOpened.value) {
      VmCloudServerApi.recycleInstance(row.id as string)
        .then(() => {
          ElMessage.success(t("commons.msg.op_success"));
          refresh();
        })
        .catch((err) => {
          ElMessage.error(err.response.data.message);
        });
    } else {
      VmCloudServerApi.deleteInstance(row.id as string)
        .then(() => {
          ElMessage.success(t("commons.msg.op_success"));
          refresh();
        })
        .catch((err) => {
          ElMessage.error(err.response.data.message);
        });
    }
  });
};

// 删除创建失败机器的记录，只删除数据库记录，不调用云平台接口
const deleteFailedRecord = (cloudServerId: string) => {
  const message = t(
    "vm_cloud_server.message_box.confirm_delete_record",
    "确认删除失败记录？"
  );
  ElMessageBox.confirm(message, t("commons.message_box.prompt", "提示"), {
    confirmButtonText: t("commons.message_box.confirm", "确认"),
    cancelButtonText: t("commons.btn.cancel", "取消"),
    type: "warning",
  }).then(() => {
    VmCloudServerApi.deleteFailedRecord(cloudServerId)
      .then(() => {
        ElMessage.success(t("commons.msg.op_success"));
        refresh();
      })
      .catch((err) => {
        ElMessage.error(err.response.data.message);
      });
  });
};

/**
 * 批量操作
 */
const batchOperate = (operate: string) => {
  if (!(selectedRowData.value && selectedRowData.value.length > 0)) {
    return;
  }
  let message = t("vm_cloud_server.message_box.confirm_batch_operate", [
    instanceOperateMap.get(operate),
  ]);
  if (
    operate.toUpperCase() === "DELETED" ||
    operate.toUpperCase() === "RECYCLE_SERVER"
  ) {
    message = isRecycleBinOpened.value
      ? t(
          "vm_cloud_server.message_box.confirm_recycle",
          "回收站已开启，云主机将关机并放入回收站中"
        )
      : t(
          "vm_cloud_server.message_box.confirm_to_delete",
          "回收站已关闭，云主机将立即删除"
        );
    if (
      selectedRowData.value.every(
        (vmCloudServer) => vmCloudServer.instanceStatus === "Failed"
      )
    ) {
      message = t(
        "vm_cloud_server.message_box.confirm_batch_delete_record",
        "确认批量删除失败记录"
      );
    }
  }
  ElMessageBox.confirm(message, t("commons.message_box.prompt", "提示"), {
    confirmButtonText: t("commons.message_box.confirm", "确认"),
    cancelButtonText: t("commons.btn.cancel", "取消"),
    type: "warning",
  }).then(() => {
    VmCloudServerApi.batchOperate(_.map(selectedRowData.value, "id"), operate)
      .then(() => {
        ElMessage.success(t("commons.msg.op_success"));
        refresh();
      })
      .catch((err) => {
        ElMessage.error(err.response.data.message);
      });
  });
};
/**
 * 更多操作
 */
//授权
const authorizeBatch = () => {
  if (!(selectedRowData.value && selectedRowData.value.length > 0)) {
    ElMessage.warning(t("commons.msg.at_least_select_one", "至少选择一条数据"));
    return;
  }
  showGrantDialog();
};
const grantDialogVisible = ref<boolean>(false);
const selectedServerIds = ref<string[]>();
const showGrantDialog = () => {
  selectedServerIds.value = _.map(selectedRowData.value, "id");
  grantDialogVisible.value = true;
};

//删除
const deleteBatch = async () => {
  await getRecycleBinSetting();
  if (isRecycleBinOpened.value) {
    batchOperate("RECYCLE_SERVER");
  } else {
    batchOperate("DELETE");
  }
};

/**
 * 禁用批量启动
 */
const disableBatch = computed<boolean>(() => {
  return selectedRowData.value.length === 0
    ? true
    : selectedRowData.value.length > 0 &&
        selectedRowData.value.some(
          (row) => row.instanceStatus === "ToBeRecycled"
        );
});
const createAction = ref<Array<ButtonActionType>>([
  new ButtonAction(
    t("commons.btn.create", "创建") +
      t("vm_cloud_server.label.cloudVm", "云主机"),
    "primary",
    undefined,
    gotoCatalog,
    permissionStore.hasPermission("[vm-service]CLOUD_SERVER:CREATE")
  ),
]);
const moreActions = ref<Array<ButtonActionType>>([
  new ButtonAction(
    t("vm_cloud_server.btn.power_on", "启动"),
    undefined,
    "POWER_ON",
    batchOperate,
    permissionStore.hasPermission("[vm-service]CLOUD_SERVER:START"),
    disableBatch
  ),
  new ButtonAction(
    t("commons.btn.grant", "授权"),
    undefined,
    undefined,
    authorizeBatch,
    permissionStore.hasPermission("[vm-service]CLOUD_SERVER:AUTH"),
    disableBatch
  ),
  new ButtonAction(
    t("vm_cloud_server.btn.shutdown", "关机"),
    undefined,
    "SHUTDOWN",
    batchOperate,
    permissionStore.hasPermission("[vm-service]CLOUD_SERVER:STOP"),
    disableBatch
  ),
  new ButtonAction(
    t("vm_cloud_server.btn.reboot", "重启"),
    undefined,
    "REBOOT",
    batchOperate,
    permissionStore.hasPermission("[vm-service]CLOUD_SERVER:RESTART"),
    disableBatch
  ),
  new ButtonAction(
    t("commons.btn.delete", "删除"),
    undefined,
    undefined,
    deleteBatch,
    permissionStore.hasPermission("[vm-service]CLOUD_SERVER:DELETE"),
    disableBatch
  ),
]);

/**
 * 表单配置
 */
const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    // 查询函数
    search: search,
    quickPlaceholder: t("commons.btn.search", "搜索"),
    components: [],
    searchOptions: [
      {
        label: t("commons.name", "名称"),
        value: "instanceName",
      },
      {
        label: t("vm_cloud_server.label.ip_address", "IP地址"),
        value: "ipArray",
      },
      {
        label: t("commons.os", "操作系统"),
        value: "osInfo",
      },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([
    TableOperations.buildButtons().newInstance(
      t("vm_cloud_server.btn.power_off", "关闭电源"),
      "primary",
      powerOff,
      undefined,
      (row: { instanceStatus: string }) => {
        return row.instanceStatus !== "Running";
      },
      permissionStore.hasPermission("[vm-service]CLOUD_SERVER:STOP")
    ),
    TableOperations.buildButtons().newInstance(
      t("vm_cloud_server.btn.reboot", "重启"),
      "primary",
      reboot,
      undefined,
      (row: { instanceStatus: string }) => {
        return row.instanceStatus !== "Running";
      },
      permissionStore.hasPermission("[vm-service]CLOUD_SERVER:RESTART")
    ),
    TableOperations.buildButtons().newInstance(
      t("commons.btn.delete", "删除"),
      "primary",
      deleteInstance,
      undefined,
      (row: { instanceStatus: string }) => {
        return (
          row.instanceStatus.toUpperCase() === "ToBeRecycled".toUpperCase() ||
          row.instanceStatus.toUpperCase() === "Deleted".toUpperCase() ||
          (row.instanceStatus.toUpperCase() !== "Running".toUpperCase() &&
            row.instanceStatus.toUpperCase().indexOf("ING") > -1)
        );
      },
      permissionStore.hasPermission("[vm-service]CLOUD_SERVER:DELETE"),
      "#F54A45"
    ),
    TableOperations.buildButtons().newInstance(
      t("vm_cloud_disk.btn.create", "添加磁盘"),
      "primary",
      createDisk,
      undefined,
      (row: { instanceStatus: string }) => {
        return (
          row.instanceStatus.toUpperCase() === "ToBeRecycled".toUpperCase() ||
          row.instanceStatus.toUpperCase() === "Deleted".toUpperCase() ||
          (row.instanceStatus.toUpperCase() !== "Running".toUpperCase() &&
            row.instanceStatus.toUpperCase().indexOf("ING") > -1)
        );
      },
      permissionStore.hasPermission("[vm-service]CLOUD_DISK:CREATE")
    ),
    TableOperations.buildButtons().newInstance(
      t("vm_cloud_server.btn.change_config", "配置变更"),
      "primary",
      changeVmConfig,
      undefined,
      (row: { instanceStatus: string }) => {
        return (
          row.instanceStatus === "ToBeRecycled" ||
          row.instanceStatus === "Deleted" ||
          (row.instanceStatus.toLowerCase() != "running" &&
            row.instanceStatus.toLowerCase().indexOf("ing") > -1)
        );
      },
      permissionStore.hasPermission("[vm-service]CLOUD_SERVER:RESIZE")
    ),
  ]),
});
const getFirstIp = (list: Array<any>) => {
  if (list) {
    const publicIpItem = _.find(list, ["isPublicIp", true]);
    if (publicIpItem) {
      return publicIpItem.ip + "(公)";
    }
    return list[0].ip;
  } else {
    return "";
  }
};
/**
 * 操作按钮
 */
const buttons = ref([
  {
    label: t("vm_cloud_server.btn.power_on", "启动"),
    icon: "",
    click: (row: VmCloudServerVO) => {
      powerOn(row);
    },
    show: permissionStore.hasPermission("[vm-service]CLOUD_SERVER:START"),
    disabled: (row: { instanceStatus: string }) => {
      return row.instanceStatus !== "Stopped";
    },
  },
  {
    label: t("vm_cloud_server.btn.shutdown", "关机"),
    icon: "",
    click: (row: VmCloudServerVO) => {
      shutdown(row);
    },
    show: permissionStore.hasPermission("[vm-service]CLOUD_SERVER:STOP"),
    disabled: (row: { instanceStatus: string }) => {
      return row.instanceStatus !== "Running";
    },
  },
  {
    label: t("vm_cloud_server.btn.power_off", "关闭电源"),
    icon: "",
    click: (row: VmCloudServerVO) => {
      powerOff(row);
    },
    show: permissionStore.hasPermission("[vm-service]CLOUD_SERVER:STOP"),
    disabled: (row: { instanceStatus: string }) => {
      return row.instanceStatus !== "Running";
    },
  },
  {
    label: t("vm_cloud_server.btn.reboot", "重启"),
    icon: "",
    click: (row: VmCloudServerVO) => {
      reboot(row);
    },
    show: permissionStore.hasPermission("[vm-service]CLOUD_SERVER:RESTART"),
    disabled: (row: { instanceStatus: string }) => {
      return row.instanceStatus !== "Running";
    },
  },
  {
    label: t("commons.btn.delete", "删除"),
    icon: "",
    click: (row: VmCloudServerVO) => {
      deleteInstance(row);
    },
    show: permissionStore.hasPermission("[vm-service]CLOUD_SERVER:DELETE"),
    disabled: (row: { instanceStatus: string }) => {
      return (
        row.instanceStatus.toUpperCase() === "ToBeRecycled".toUpperCase() ||
        row.instanceStatus.toUpperCase() === "Deleted".toUpperCase() ||
        (row.instanceStatus.toUpperCase() !== "Running".toUpperCase() &&
          row.instanceStatus.toUpperCase().indexOf("ING") > -1)
      );
    },
  },
  {
    label: t("vm_cloud_disk.btn.create", "添加磁盘"),
    icon: "",
    click: createDisk,
    show: permissionStore.hasPermission("[vm-service]CLOUD_DISK:CREATE"),
    disabled: (row: { instanceStatus: string }) => {
      return (
        row.instanceStatus.toUpperCase() === "ToBeRecycled".toUpperCase() ||
        row.instanceStatus.toUpperCase() === "Deleted".toUpperCase() ||
        (row.instanceStatus.toUpperCase() !== "Running".toUpperCase() &&
          row.instanceStatus.toUpperCase().indexOf("ING") > -1)
      );
    },
  },
  {
    label: t("vm_cloud_server.btn.change_config", "配置变更"),
    icon: "",
    click: changeVmConfig,
    show: permissionStore.hasPermission("[vm-service]CLOUD_SERVER:RESIZE"),
    disabled: (row: { instanceStatus: string }) => {
      return (
        row.instanceStatus === "ToBeRecycled" ||
        row.instanceStatus === "Deleted" ||
        (row.instanceStatus.toLowerCase() != "running" &&
          row.instanceStatus.toLowerCase().indexOf("ing") > -1)
      );
    },
  },
]);
</script>
<template>
  <ce-table
    localKey="vmCloudServerTable"
    v-loading="tableLoading"
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    :show-selected-count="true"
    @selection-change="handleSelectionChange"
    @clearCondition="clearCondition"
    row-key="id"
    height="100%"
    ref="table"
  >
    <template #toolbar>
      <ButtonToolBar :actions="createAction || []" :ellipsis="4" />
      <ButtonToolBar :actions="moreActions || []" :ellipsis="2" />
    </template>
    <el-table-column type="selection" />
    <el-table-column
      :show-overflow-tooltip="true"
      prop="instanceName"
      column-key="instanceName"
      :label="$t('commons.name')"
      fixed
      min-width="200px"
    >
      <template #default="scope">
        <span @click="showDetail(scope.row)" class="name-span-class">
          {{ scope.row.instanceName }}
        </span>
      </template>
    </el-table-column>
    <el-table-column
      prop="remark"
      column-key="remark"
      :label="$t('vm_cloud_server.label.remark', '备注')"
      :show="false"
      min-width="180px"
    ></el-table-column>
    <el-table-column
      prop="ipArray"
      column-key="ipArray"
      :label="$t('vm_cloud_server.label.ip_address')"
      min-width="210px"
    >
      <template #default="scope">
        <div
          class="role_display"
          :data-var="
            (scope._list = classifyIP(scope.row.ipArray, scope.row.remoteIp))
          "
        >
          <span v-if="JSON.parse(scope.row.ipArray)?.length > 1">
            {{ getFirstIp(scope._list) }}
          </span>
          <span v-if="JSON.parse(scope.row.ipArray)?.length === 1">
            {{ getFirstIp(scope._list) }}
          </span>
          <el-popover
            placement="right"
            :width="200"
            trigger="hover"
            v-if="scope._list?.length > 1"
          >
            <template #reference>
              <span class="role_numbers"> +{{ scope._list?.length - 1 }} </span>
            </template>
            <div v-for="(item, index) in scope._list" :key="index">
              <span>{{ item.ip }}</span>
              <span v-if="item.isPublicIp"> (公) </span>
            </div>
          </el-popover>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="instanceStatus"
      column-key="instanceStatus"
      :label="$t('commons.status')"
      :filters="instanceStatusForTableSelect"
      :filter-multiple="false"
      min-width="120px"
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <VmServerStatusIcon
            :status="scope.row.instanceStatus"
          ></VmServerStatusIcon>
          <span style="margin-left: 7px"
            >{{ InstanceStatusUtils.getStatusName(scope.row.instanceStatus) }}
          </span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="accountName"
      column-key="accountIds"
      :label="$t('commons.cloud_account.native', '云账号')"
      :filters="cloudAccount"
      min-width="180px"
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <PlatformIcon
            style="height: 16px; width: 16px"
            :platform="scope.row.platform"
          ></PlatformIcon>
          <span style="margin-left: 10px">{{ scope.row.accountName }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="region"
      column-key="region"
      :label="$t('vm_cloud_server.label.region', '区域/数据中心')"
      :show="false"
      min-width="180px"
    ></el-table-column>
    <el-table-column
      prop="zone"
      column-key="zone"
      :label="$t('vm_cloud_server.label.zone', '可用区/集群')"
      :show="false"
      min-width="180px"
    ></el-table-column>
    <el-table-column
      prop="organizationName"
      column-key="organizationIds"
      :label="$t('commons.org', '组织')"
      :show="false"
      min-width="180px"
    >
      <template #header>
        <span :class="{ highlight: selectedOrganizationIds?.length > 0 }">{{
          $t("commons.org", "组织")
        }}</span>
        <el-popover
          ref="orgPopRef"
          placement="bottom"
          :width="200"
          trigger="click"
          :show-arrow="false"
        >
          <template #reference>
            <span class="el-table__column-filter-trigger"
              ><el-icon><ArrowDown /></el-icon
            ></span>
          </template>
          <OrgTreeFilter
            tree-type="org"
            ref="orgTreeRef"
            field="organizationIds"
            :label="$t('commons.org', '组织')"
            :popover-ref="orgPopRef"
            :table-ref="table"
          />
        </el-popover>
      </template>
      <template #default="scope">
        <span v-html="scope.row.organizationName"></span>
      </template>
    </el-table-column>
    <el-table-column
      prop="workspaceName"
      column-key="workspaceIds"
      :label="$t('commons.workspace', '工作空间')"
      :show="false"
      min-width="180px"
    >
      <template #header>
        <span :class="{ highlight: selectedWorkspaceIds?.length > 0 }">{{
          $t("commons.workspace", "工作空间")
        }}</span>
        <el-popover
          ref="workspacePopRef"
          placement="bottom"
          :width="200"
          trigger="click"
          :show-arrow="false"
        >
          <template #reference>
            <span class="el-table__column-filter-trigger"
              ><el-icon><ArrowDown /></el-icon
            ></span>
          </template>
          <OrgTreeFilter
            tree-type="workspace"
            ref="workspaceTreeRef"
            field="workspaceIds"
            :leaf-only="true"
            :label="$t('commons.workspace', '工作空间')"
            :popover-ref="workspacePopRef"
            :table-ref="table"
          />
        </el-popover>
      </template>
      <template #default="scope">
        <span v-html="scope.row.workspaceName"></span>
      </template>
    </el-table-column>
    <el-table-column
      prop="instanceTypeDescription"
      column-key="instanceTypeDescription"
      :label="$t('commons.cloud_server.instance_type')"
      min-width="160px"
    >
      <template #default="scope">
        <span style="display: flex">{{
          scope.row.instanceTypeDescription
        }}</span>
        <span
          style="display: flex"
          v-if="scope.row.instanceType !== scope.row.instanceTypeDescription"
          >{{ scope.row.instanceType }}</span
        >
      </template>
    </el-table-column>
    <el-table-column
      prop="osInfo"
      column-key="osInfo"
      :label="$t('commons.os_version', '操作系统版本')"
      min-width="180px"
      :show="false"
    />
    <el-table-column
      prop="instanceChargeType"
      column-key="instanceChargeType"
      :label="$t('cloud_server.label.charge_type', '付费类型')"
      :filters="chargeType"
      min-width="120px"
      :show="false"
    >
      <template #default="scope">
        {{ filterChargeType(scope.row.instanceChargeType) }}
      </template>
    </el-table-column>
    <el-table-column
      prop="host"
      column-key="host"
      :label="$t('commons.cloud_account.host', '宿主机')"
      :show="false"
      min-width="120px"
    ></el-table-column>
    <el-table-column
      prop="vmToolsStatus"
      column-key="vmToolsStatus"
      :label="$t('cloud_server.label.vm_tools_status', 'VMTools状态')"
      :filters="vmToolsStatus"
      :show="false"
      min-width="180px"
    >
      <template #default="scope">
        {{ filterVmToolsStatus(scope.row.vmToolsStatus) }}
      </template>
    </el-table-column>
    <el-table-column
      prop="expiredTime"
      column-key="expiredTime"
      sortable
      :label="$t('cloud_server.label.expired_time', '到期时间')"
      min-width="180px"
      :show="false"
    ></el-table-column>
    <el-table-column
      prop="createTime"
      column-key="createTime"
      sortable
      :label="$t('commons.create_time', '创建时间')"
      min-width="180px"
    ></el-table-column>
    <el-table-column
      prop="applyUser"
      column-key="applyUser"
      :label="$t('commons.cloud_server.creator', '创建人')"
      min-width="120px"
      :show="false"
    ></el-table-column>
    <fu-table-operations
      :ellipsis="2"
      :columns="columns"
      :buttons="buttons"
      :label="$t('commons.operation')"
      fixed="right"
    />
    <!--    <el-table-column min-width="110px" label="操作" fixed="right">-->
    <!--      <template #default="scope">-->
    <!--        <el-space wrap>-->
    <!--          <EnableStatusSwitch-->
    <!--            :instance-status="scope.row.instanceStatus"-->
    <!--            :final-function="refresh"-->
    <!--            :function-props="scope.row"-->
    <!--          />-->
    <!--          <MoreOptionsButton-->
    <!--            :buttons="tableConfig.tableOperations.buttons"-->
    <!--            :row="scope.row"-->
    <!--          />-->
    <!--        </el-space>-->
    <!--      </template>-->
    <!--    </el-table-column>-->
    <template #buttons>
      <CeTableColumnSelect :columns="columns" />
    </template>
    <!--    <template #bottomToolBar>-->
    <!--      <ButtonToolBar :actions="moreActions || []" :ellipsis="2" />-->
    <!--    </template>-->
  </ce-table>

  <!-- 授权页面弹出框 -->
  <el-dialog
    v-model="grantDialogVisible"
    :title="$t('commons.grant')"
    width="35%"
    destroy-on-close
  >
    <Grant
      :ids="selectedServerIds || []"
      resource-type="vm"
      v-model:dialogVisible="grantDialogVisible"
      @refresh="refresh"
    />
  </el-dialog>
  <AddDisk ref="addDiskRef"></AddDisk>
  <ChangeConfig ref="changeConfigRef"></ChangeConfig>
</template>
<style lang="scss" scoped>
.name-span-class {
  color: var(--el-color-primary);
}
.name-span-class:hover {
  cursor: pointer;
}
.highlight {
  color: var(--el-color-primary);
}
.role_display {
  height: 24px;
  line-height: 24px;
  display: flex;
  .role_numbers {
    cursor: pointer;
    margin-left: 8px;
    border-radius: 2px;
    padding: 0 6px;
    height: 24px;
    font-size: 14px;
    background-color: rgba(31, 35, 41, 0.1);
  }
  .role_numbers:hover {
    background-color: #ebf1ff;
    color: #3370ff;
  }
}
</style>
