<script setup lang="ts">
import { ref, onMounted } from "vue";
import VmCloudServerApi from "@/api/vm_cloud_server";
import type {
  VmCloudServerVO,
  CloudServerJobRecord,
} from "@/api/vm_cloud_server/type";
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
import { platformIcon } from "@/utils/platform";

const { t } = useI18n();
const useRoute = useRouter();
const table = ref<any>(null);
const columns = ref([]);
const tableData = ref<Array<VmCloudServerVO>>([]);
const selectedRowData = ref<Array<VmCloudServerVO>>();
const tableLoading = ref<boolean>(false);
//批量操作
const instanceOperateMap: Map<string, string> = new Map();
instanceOperateMap.set("POWER_ON", t("", "启动"));
instanceOperateMap.set("HARD_SHUTDOWN", t("", "关机"));
instanceOperateMap.set("HARD_REBOOT", t("", "重启"));
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
  { text: "未知", value: "Unknown" },
]);

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

/**
 * 页面挂载
 */
onMounted(() => {
  search(new TableSearch());
});

//启动定时器
const startOperateInterval = (list: Array<VmCloudServerVO>) => {
  for (const vm of list) {
    VmCloudServerApi.getVmCloudServerById(vm.id).then((res) => {
      vm.instanceStatus = res.data.instanceStatus;
    });
  }
  let cloudServerInterval: any;
  const isOk = ref<boolean>(false);
  cloudServerInterval = setInterval(() => {
    console.log("初始化定时器：" + cloudServerInterval);
    VmCloudServerApi.getServerJobRecord(list.map((r) => r.id))
      .then((serverJobs) => {
        for (const vm of list) {
          const jobs = serverJobs.data[vm.id];
          if (jobs) {
            if (
              jobs.some((job) => job.status === "FAILED") ||
              jobs.every((job) => job.status === "SUCCESS")
            ) {
              VmCloudServerApi.getVmCloudServerById(vm.id).then((res) => {
                vm.instanceStatus = res.data.instanceStatus;
                isOk.value = true;
                console.log(":" + vm.instanceStatus);
              });
            }
          }
        }
        if (isOk.value) {
          stopOperateInterval(cloudServerInterval);
        }
      })
      .catch((err) => {
        console.log("出错了");
        updateInstanceStatus(list);
        stopOperateInterval(cloudServerInterval);
        console.log(err);
      });
  }, 6000);
};
//停止定时器
const stopOperateInterval = (cloudServerInterval: any) => {
  if (cloudServerInterval) {
    console.log("关闭定时器：" + cloudServerInterval);
    clearInterval(cloudServerInterval);
  }
};

const updateInstanceStatus = (list: Array<VmCloudServerVO>) => {
  for (const vm of list) {
    VmCloudServerApi.getVmCloudServerById(vm.id).then((res) => {
      vm.instanceStatus = res.data.instanceStatus;
    });
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
    path: useRoute.currentRoute.value.path.replace("/list", "/detail"),
    query: { id: row.id },
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
 * 操作按钮
 */
const buttons = ref([
  {
    label: t("vm_cloud_server.btn.power_on", "启动"),
    icon: "",
    click: (row: VmCloudServerVO) => {
      powerOn(row);
    },
    show: true,
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
    show: true,
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
    show: true,
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
    show: true,
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
    show: true,
    disabled: (row: { instanceStatus: string }) => {
      return row.instanceStatus === "Deleted";
    },
  },
  {
    label: t("vm_cloud_disk.btn.create", "添加磁盘"),
    icon: "",
    click: createDisk,
    show: true,
    disabled: (row: { instanceStatus: string }) => {
      return row.instanceStatus === "Deleted";
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
        startOperateInterval([row]);
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
    VmCloudServerApi.shutdownInstance(row.id as string, powerOff)
      .then((res) => {
        startOperateInterval([row]);
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
        startOperateInterval([row]);
        ElMessage.success(t("commons.msg.op_success"));
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => {});
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
        startOperateInterval([row]);
        ElMessage.success(t("commons.msg.op_success"));
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => {});
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
        startOperateInterval([row]);
        ElMessage.success(t("commons.msg.op_success"));
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => {});
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
        startOperateInterval(selectedRowData.value as Array<VmCloudServerVO>);
        ElMessage.success(t("commons.msg.op_success"));
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => {});
  });
};
/**
 * 更多操作
 */
//授权
const authorizeBatch = () => {
  ElMessage.info("等等");
};
//删除
const deleteBatch = () => {
  batchOperate("DELETE");
};
const moreActions = ref([
  { text: t("commons.btn.grant", "授权"), arg: "", fn: authorizeBatch },
  { text: t("commons.btn.delete", "删除"), arg: "", fn: deleteBatch },
]);
//触发事件
const handleAction = (actionObj: any) => {
  const { arg, fn } = actionObj;
  if (fn) {
    fn(arg);
  }
};
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
      <el-button @click="gotoCatalog()" type="primary">{{
        t("commons.btn.create", "创建")
      }}</el-button>
      <el-button @click="batchOperate('POWER_ON')">{{
        t("vm_cloud_server.btn.power_on", "启动")
      }}</el-button>
      <el-button @click="batchOperate('HARD_SHUTDOWN')">{{
        t("vm_cloud_server.btn.shutdown", "关机")
      }}</el-button>
      <el-button @click="batchOperate('HARD_REBOOT')">{{
        t("vm_cloud_server.btn.reboot", "重启")
      }}</el-button>
      <el-dropdown
        @command="handleAction"
        trigger="click"
        style="margin-left: 12px"
      >
        <el-button>
          {{ t("commons.btn.more_actions", "更多操作")
          }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item
              v-for="(item, index) in moreActions"
              :key="index"
              :command="{ arg: item.arg, fn: item.fn }"
              >{{ item.text }}</el-dropdown-item
            >
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </template>
    <el-table-column type="selection" />
    <el-table-column
      :show-overflow-tooltip="true"
      prop="instanceName"
      :label="$t('commons.name')"
    >
      <template #default="scope">
        <el-link type="primary" @click="showDetail(scope.row)">
          <span>
            {{ scope.row.instanceName }}
          </span>
        </el-link>
      </template>
    </el-table-column>
    <el-table-column
      prop="organizationName"
      :label="$t('commons.org')"
    ></el-table-column>
    <el-table-column
      prop="workspaceName"
      :label="$t('commons.workspace')"
    ></el-table-column>
    <el-table-column
      prop="accountName"
      :label="$t('commons.cloud_account.native')"
    >
      <template #default="scope">
        <div style="display: flex">
          <el-image
            style="margin-top: 3px; width: 16px; height: 16px"
            :src="platformIcon[scope.row.platform].icon"
            v-if="scope.row.platform"
          ></el-image>
          <span style="margin-left: 10px">{{ scope.row.accountName }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="instanceStatus"
      column-key="instanceStatus"
      :label="$t('commons.status')"
      :filters="InstanceStatus"
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
              scope.row.instanceStatus === 'Createding'
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
      :label="$t('commons.cloud_server.instance_type')"
    ></el-table-column>
    <el-table-column
      prop="ipArray"
      :label="$t('vm_cloud_server.label.ip_address')"
    >
      <template #default="scope">
        <span v-show="scope.row.ipArray.length > 2">{{
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
      :label="$t('commons.cloud_server.applicant')"
      :show="false"
    ></el-table-column>
    <el-table-column
      prop="createTime"
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
</template>
<style lang="scss" scoped></style>
