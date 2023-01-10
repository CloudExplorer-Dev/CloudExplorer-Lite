<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from "vue";
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
import { ElMessage, ElMessageBox } from "element-plus";
import _ from "lodash";
import type { SimpleMap } from "@commons/api/base/type";
import variables_server from "../../styles/vm_cloud_server/server.module.scss";
import { platformIcon } from "@commons/utils/platform";
import BaseCloudAccountApi from "@commons/api/cloud_account";
import Grant from "@/views/vm_cloud_server/grant.vue";
import { usePermissionStore } from "@commons/stores/modules/permission";
import ButtonToolBar from "@commons/components/button-tool-bar/ButtonToolBar.vue";
import { ButtonAction } from "@commons/components/button-tool-bar/type";

const { t } = useI18n();
const permissionStore = usePermissionStore();
const useRoute = useRouter();
const table = ref<any>(null);
const columns = ref([]);
const tableData = ref<Array<VmCloudServerVO>>([]);
const selectedRowData = ref<Array<VmCloudServerVO>>([]);
const tableLoading = ref<boolean>(false);
const cloudAccount = ref<Array<SimpleMap<string>>>([]);
//批量操作
const instanceOperateMap: Map<string, string> = new Map();
instanceOperateMap.set("POWER_ON", t("", "启动"));
instanceOperateMap.set("POWER_OFF", t("", "关机"));
instanceOperateMap.set("REBOOT", t("", "重启"));
instanceOperateMap.set("DELETE", t("", "删除"));
//状态
const InstanceStatus = ref<Array<SimpleMap<string>>>([
  { text: t("", "运行中"), value: "Running" },
  { text: "已删除", value: "Deleted" },
  { text: "已关机", value: "Stopped" },
  { text: "启动中", value: "Starting" },
  { text: "关机中", value: "Stopping" },
  { text: "重启中", value: "Rebooting" },
  { text: "删除中", value: "Deleting" },
  { text: "创建中", value: "Createding" },
  { text: "排队中", value: "WaitCreating" },
  { text: "创建中", value: "Creating" },
  { text: "配置变更中", value: "ConfigChanging" },
  { text: "失败", value: "Failed" },
  { text: "未知", value: "Unknown" },
]);

// 表格头中显示的筛选状态
const instanceStatusForTableSelect = [
  { text: t("vm_cloud_server.status.creating", "创建中"), value: "Creating" },
  { text: t("vm_cloud_server.status.running", "运行中"), value: "Running" },
  { text: t("vm_cloud_server.status.stopped", "已关机"), value: "Stopped" },
  { text: t("vm_cloud_server.status.rebooting", "重启中"), value: "Rebooting" },
  {
    text: t("vm_cloud_server.status.wait_recycle", "待回收"),
    value: "Wait_Recycle",
  },
  { text: t("vm_cloud_server.status.deleted", "已删除"), value: "Deleted" },
];

const filterInstanceStatus = (value: string) => {
  let status = "";
  InstanceStatus.value.forEach((v) => {
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
    console.log("初始化定时器：" + cloudServerInterval.value);
    VmCloudServerApi.getVmCloudServerByIds(
      tableData.value.map((r) => r.id)
    ).then((res) => {
      if (res) {
        for (let i = 0; i < res.data.length; i++) {
          _.forEach(tableData.value, function (vm) {
            if (vm.id === res.data[i].id) {
              vm.instanceStatus = res.data[i].instanceStatus;
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
  console.log("即将关闭定时器：" + cloudServerInterval.value);
  if (cloudServerInterval.value) {
    console.log("关闭定时器：" + cloudServerInterval.value);
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
 * 表单配置
 */
const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    // 查询函数
    search: search,
    quickPlaceholder: t("commons.btn.search", "查找"),
    components: [],
    searchOptions: [
      {
        label: t("commons.name", "名称"),
        value: "instanceName",
      },
      {
        label: t("commons.cloud_account.name", "云账号名称"),
        value: "accountName",
      },
      {
        label: t("vm_cloud_server.label.ip_address", "IP地址"),
        value: "ipArray",
      },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([]),
});
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
  useRoute.push({ name: "add_disk", params: { id: row.id } });
};

/**
 * 配置变更
 * @param row
 */
const changeVmConfig = (row: VmCloudServerVO) => {
  ElMessageBox.confirm(
    t(
      "vm_cloud_server.message_box.confirm_config_update",
      "配置变更将会对实例执行关机操作，确认继续"
    ),
    t("commons.message_box.prompt", "提示"),
    {
      confirmButtonText: t("commons.message_box.confirm", "确认"),
      cancelButtonText: t("commons.btn.cancel", "取消"),
      type: "warning",
    }
  ).then(() => {
    useRoute.push({ name: "change_config", params: { id: row.id } });
  });
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
      return row.instanceStatus === "Deleted";
    },
  },
  {
    label: t("vm_cloud_disk.btn.create", "添加磁盘"),
    icon: "",
    click: createDisk,
    show: permissionStore.hasPermission("[vm-service]CLOUD_DISK:CREATE"),
    disabled: (row: { instanceStatus: string }) => {
      return row.instanceStatus === "Deleted";
    },
  },
  {
    label: t("vm_cloud_server.btn.change_config", "配置变更"),
    icon: "",
    click: changeVmConfig,
    show: permissionStore.hasPermission("[vm-service]CLOUD_SERVER:RESIZE"),
    disabled: (row: { instanceStatus: string }) => {
      return (
        row.instanceStatus === "Deleted" ||
        (row.instanceStatus.toLowerCase() != "running" &&
          row.instanceStatus.toLowerCase().indexOf("ing") > -1)
      );
    },
  },
]);

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
        console.log("-----" + res);
        ElMessage.success(t("commons.msg.op_success"));
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => {
        //table.value?.search();
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
      "当前虚拟机未安装VmTools或VmTools未运行，无法软关机，若继续操作则关闭电源，是否继续？"
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
          console.log("-----" + res);
          ElMessage.success(t("commons.msg.op_success"));
        })
        .catch((err) => {
          console.log(err);
        })
        .finally(() => {
          //table.value?.search();
        });
    } else {
      VmCloudServerApi.shutdownInstance(row.id as string)
        .then((res) => {
          ElMessage.success(t("commons.msg.op_success"));
        })
        .catch((err) => {
          console.log(err);
        })
        .finally(() => {
          //table.value?.search();
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
      })
      .catch((err) => {
        console.log(err);
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
      })
      .catch((err) => {
        console.log(err);
      });
  });
};

//删除
const deleteInstance = (row: VmCloudServerVO) => {
  ElMessageBox.confirm(
    t("vm_cloud_server.message_box.confirm_delete", "确认删除"),
    t("commons.message_box.prompt", "提示"),
    {
      confirmButtonText: t("commons.message_box.confirm", "确认"),
      cancelButtonText: t("commons.btn.cancel", "取消"),
      type: "warning",
    }
  ).then(() => {
    VmCloudServerApi.deleteInstance(row.id as string)
      .then(() => {
        ElMessage.success(t("commons.msg.op_success"));
      })
      .catch((err) => {
        console.log(err);
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
  ElMessageBox.confirm(
    t("vm_cloud_server.message_box.confirm_batch_operate", [
      instanceOperateMap.get(operate),
    ]),
    t("commons.message_box.prompt", "提示"),
    {
      confirmButtonText: t("commons.message_box.confirm", "确认"),
      cancelButtonText: t("commons.btn.cancel", "取消"),
      type: "warning",
    }
  ).then(() => {
    VmCloudServerApi.batchOperate(_.map(selectedRowData.value, "id"), operate)
      .then(() => {
        ElMessage.success(t("commons.msg.op_success"));
      })
      .catch((err) => {
        console.log(err);
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
const deleteBatch = () => {
  batchOperate("DELETE");
};
const moreActions = ref<Array<ButtonAction>>([
  new ButtonAction(
    t("commons.btn.create", "创建"),
    "primary",
    undefined,
    gotoCatalog,
    permissionStore.hasPermission("[vm-service]CLOUD_SERVER:CREATE")
  ),
  new ButtonAction(
    t("vm_cloud_server.btn.power_on", "启动"),
    undefined,
    "POWER_ON",
    batchOperate,
    permissionStore.hasPermission("[vm-service]CLOUD_SERVER:START")
  ),
  new ButtonAction(
    t("vm_cloud_server.btn.shutdown", "关机"),
    undefined,
    "SHUTDOWN",
    batchOperate,
    permissionStore.hasPermission("[vm-service]CLOUD_SERVER:STOP")
  ),
  new ButtonAction(
    t("vm_cloud_server.btn.reboot", "重启"),
    undefined,
    "REBOOT",
    batchOperate,
    permissionStore.hasPermission("[vm-service]CLOUD_SERVER:RESTART")
  ),
  new ButtonAction(
    t("commons.btn.grant", "授权"),
    undefined,
    undefined,
    authorizeBatch,
    permissionStore.hasPermission("[vm-service]CLOUD_SERVER:AUTH")
  ),
  new ButtonAction(
    t("commons.btn.delete", "删除"),
    undefined,
    undefined,
    deleteBatch,
    permissionStore.hasPermission("[vm-service]CLOUD_SERVER:DELETE")
  ),
]);
</script>
<template>
  <ce-table
    v-loading="tableLoading"
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    @selection-change="handleSelectionChange"
    row-key="id"
    height="100%"
    ref="table"
  >
    <template #toolbar>
      <ButtonToolBar :actions="moreActions" :ellipsis="4" />
    </template>
    <el-table-column type="selection" />
    <el-table-column
      :show-overflow-tooltip="true"
      prop="instanceName"
      column-key="instanceName"
      :label="$t('commons.name')"
    >
      <template #default="scope">
        <span @click="showDetail(scope.row)" class="name-span-class">
          {{ scope.row.instanceName }}
        </span>
      </template>
    </el-table-column>
    <el-table-column
      prop="organizationName"
      column-key="organizationName"
      :label="$t('commons.org')"
      :show="false"
    ></el-table-column>
    <el-table-column
      prop="workspaceName"
      column-key="workspaceName"
      :label="$t('commons.workspace')"
      :show="false"
    ></el-table-column>
    <el-table-column
      prop="accountName"
      column-key="accountIds"
      :label="$t('commons.cloud_account.native')"
      :filters="cloudAccount"
    >
      <template #default="scope">
        <div style="display: flex">
          <!--          <el-image
            style="margin-top: 3px; width: 16px; height: 16px"
            :src="platformIcon[scope.row.platform]?.icon"
            v-if="scope.row.platform"
          ></el-image>-->
          <component
            style="margin-top: 3px; width: 16px; height: 16px"
            :is="platformIcon[scope.row.platform]?.component"
            v-bind="platformIcon[scope.row.platform]?.icon"
            :color="platformIcon[scope.row.platform]?.color"
            size="16px"
            v-if="scope.row.platform"
          ></component>
          <span style="margin-left: 10px">{{ scope.row.accountName }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="instanceStatus"
      column-key="instanceStatus"
      :label="$t('commons.status')"
      :filters="instanceStatusForTableSelect"
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span
            :style="{
              color: variables_server[scope.row.instanceStatus],
            }"
            >{{ filterInstanceStatus(scope.row.instanceStatus) }}
          </span>
          <el-icon
            v-show="
              scope.row.instanceStatus === 'Starting' ||
              scope.row.instanceStatus === 'Stopping' ||
              scope.row.instanceStatus === 'Rebooting' ||
              scope.row.instanceStatus === 'Deleting' ||
              scope.row.instanceStatus === 'Createding' ||
              scope.row.instanceStatus === 'Creating'
            "
            class="is-loading"
            :style="{
              color: variables_server[scope.row.instanceStatus],
            }"
            ><Loading
          /></el-icon>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="instanceTypeDescription"
      column-key="instanceTypeDescription"
      :label="$t('commons.cloud_server.instance_type')"
    ></el-table-column>
    <el-table-column
      prop="ipArray"
      column-key="ipArray"
      :label="$t('vm_cloud_server.label.ip_address')"
    >
      <template #default="scope">
        <span v-show="scope.row.ipArray?.length > 2">{{
          JSON.parse(scope.row.ipArray)[0]
        }}</span>
        <el-dropdown
          :class="variables_server.dropdown_box"
          :hide-on-click="false"
          v-if="scope.row.ipArray.length > 2"
          max-height="100px"
        >
          <span>
            {{ t("commons.cloud_server.more", "更多")
            }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item
                v-for="(item, index) in JSON.parse(scope.row.ipArray)"
                :key="index"
                >{{ item }}</el-dropdown-item
              >
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>
    </el-table-column>
    <el-table-column
      prop="createUser"
      column-key="createUser"
      :label="$t('commons.cloud_server.applicant')"
      :show="false"
    ></el-table-column>
    <el-table-column
      prop="createTime"
      column-key="createTime"
      sortable
      :label="$t('commons.create_time')"
    ></el-table-column>
    <fu-table-operations
      :ellipsis="2"
      :columns="columns"
      :buttons="buttons"
      :label="$t('commons.operation')"
      fix
    />
    <template #buttons>
      <fu-table-column-select type="icon" :columns="columns" size="small" />
    </template>
  </ce-table>

  <!-- 授权页面弹出框 -->
  <el-dialog
    v-model="grantDialogVisible"
    :title="$t('commons.grant')"
    width="35%"
    destroy-on-close
    :close-on-click-modal="false"
  >
    <Grant
      :ids="selectedServerIds"
      resource-type="vm"
      v-model:visible="grantDialogVisible"
      @refresh="refresh"
    />
  </el-dialog>
</template>
<style lang="scss" scoped>
.name-span-class {
  color: var(--el-color-primary);
}
.name-span-class:hover {
  cursor: pointer;
}
</style>
