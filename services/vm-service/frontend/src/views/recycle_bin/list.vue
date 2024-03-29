<script setup lang="ts">
import { ref, onMounted, computed, type Ref, onBeforeUnmount } from "vue";
import RecycleBinsApi from "@/api/recycle_bin";
import _ from "lodash";
import type { RecycleBinInfo } from "@/api/recycle_bin/type";
import { ResourceTypeConst } from "@commons/utils/constants";
import {
  PaginationConfig,
  TableConfig,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
import ButtonToolBar from "@commons/components/button-tool-bar/ButtonToolBar.vue";
import { ButtonAction } from "@commons/components/button-tool-bar/type";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { ElMessage, ElMessageBox } from "element-plus";
import type { SimpleMap } from "@commons/api/base/type";
import BaseCloudAccountApi from "@commons/api/cloud_account";
import OrgTreeFilter from "@commons/components/table-filter/OrgTreeFilter.vue";
import { ResourceType } from "@/api/recycle_bin/type";
import IpArray from "@commons/components/detail-page/IpArray.vue";
import PlatformIcon from "@commons/components/platform-icon/index.vue";
const permissionStore = usePermissionStore();
const { t } = useI18n();
const columns = ref([]);
const tableData = ref<Array<RecycleBinInfo>>();
const table = ref();
const selectedRowData = ref<Array<RecycleBinInfo>>([]);
const tableLoading = ref<boolean>(false);
const cloudAccount = ref<Array<SimpleMap<string>>>([]);

const list = (params: any, loading?: Ref<boolean>) => {
  RecycleBinsApi.listRecycleBins(
    {
      currentPage: tableConfig.value.paginationConfig.currentPage,
      pageSize: tableConfig.value.paginationConfig.pageSize,
      ...params,
    },
    loading
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
 * 查询
 * @param condition
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  list(params, tableLoading);
};

const resourceStatus = ref<Array<SimpleMap<string>>>([
  { text: "运行中", value: "Running" },
  { text: "已关机", value: "Stopped" },
  { text: "删除中", value: "Deleting" },
  { text: "已挂载", value: "in_use" },
  { text: "可用", value: "available" },
]);

const filterStatus = (value: string) => {
  let status = value;
  resourceStatus.value.forEach((v) => {
    if (v.value.toUpperCase() == value.toUpperCase()) {
      status = v.text;
      return;
    }
  });
  return status;
};

/**
 * 表头过滤选项
 */
const tableHeadFilters = ref({
  resourceType: [
    { text: t("commons.cloud_account.vm", "云主机"), value: "VM" },
    { text: t("commons.cloud_account.disk", "磁盘"), value: "DISK" },
  ],
  cloudAccount: cloudAccount,
  resourceStatus: resourceStatus,
  deleteWithInstance: [
    { text: t("commons.btn.yes", "是"), value: "YES" },
    { text: t("commons.btn.no", "否"), value: "NO" },
  ],
});

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

const isResourceLoading = (status: string | undefined) => {
  if (
    status &&
    status.toLowerCase() != "running" &&
    status.indexOf("ing") > -1
  ) {
    return true;
  } else {
    return false;
  }
};

/**
 * 页面挂载
 */
let timer: number;
onMounted(() => {
  search(new TableSearch());
  searchCloudAccount();
  timer = setInterval(() => {
    if (tableData.value?.some((s) => isResourceLoading(s.resourceStatus))) {
      list(TableSearch.toSearchParams(table.value.getTableSearch()));
    }
  }, 6000);
});

onBeforeUnmount(() => {
  if (timer) clearInterval(timer);
});

/**
 * 表单配置
 */
const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    // 查询函数
    search: search,
    quickPlaceholder: t("commons.btn.search"),
    components: [],
    searchOptions: [
      {
        label: t("recycle_bin.resource_name", "资源名称"),
        value: "resourceName",
      },
      {
        label: t("recycle_bin.relate_resource", "关联资源"),
        value: "relateResource",
      },
      {
        label: t("vm_cloud_server.label.ip_address", "IP地址"),
        value: "ipArray",
      },
      {
        label: t("recycle_bin.user_name", "操作人"),
        value: "userName",
      },
    ],
  },
  paginationConfig: new PaginationConfig(),
});

/**
 * 批量恢复
 */
const batchRecoverOperate = () => {
  if (!(selectedRowData.value && selectedRowData.value.length > 0)) {
    return;
  }
  const message = t("recycle_bin.batch_recover_tips");
  ElMessageBox.confirm(message, t("commons.message_box.prompt", "提示"), {
    confirmButtonText: t("commons.message_box.confirm", "确认"),
    cancelButtonText: t("commons.btn.cancel", "取消"),
    type: "warning",
  }).then(async () => {
    const resourceGroup = _.groupBy(selectedRowData.value, "resourceType");

    const recoverResource = async () => {
      if (resourceGroup.VM?.length > 0) {
        const req = {
          ids: _.map(resourceGroup.VM, "id"),
          resourceIds: _.map(resourceGroup.VM, "resourceId"),
          resourceType: ResourceType.VM,
        };
        await RecycleBinsApi.batchRecoverVm(req);
      }

      if (resourceGroup.DISK?.length > 0) {
        const req = {
          ids: _.map(resourceGroup.DISK, "id"),
          resourceIds: _.map(resourceGroup.DISK, "resourceId"),
          resourceType: ResourceType.DISK,
        };
        await RecycleBinsApi.batchRecoverDisk(req);
      }
    };

    await recoverResource();

    ElMessage.success(t("commons.msg.op_success"));
    refresh();
  });
};

/**
 * 批量彻底删除
 */
const batchDeleteOperate = () => {
  if (!(selectedRowData.value && selectedRowData.value.length > 0)) {
    return;
  }
  const message = t("recycle_bin.batch_delete_tips");
  ElMessageBox.confirm(message, t("commons.message_box.prompt", "提示"), {
    confirmButtonText: t("commons.message_box.confirm", "确认"),
    cancelButtonText: t("commons.btn.cancel", "取消"),
    type: "warning",
  }).then(async () => {
    const resourceGroup = _.groupBy(selectedRowData.value, "resourceType");

    const deleteResource = async () => {
      if (resourceGroup.VM?.length > 0) {
        const req = {
          ids: _.map(resourceGroup.VM, "id"),
          resourceIds: _.map(resourceGroup.VM, "resourceId"),
          resourceType: ResourceType.VM,
        };
        await RecycleBinsApi.batchDeleteVm(req);
      }

      if (resourceGroup.DISK?.length > 0) {
        const req = {
          ids: _.map(resourceGroup.DISK, "id"),
          resourceIds: _.map(resourceGroup.DISK, "resourceId"),
          resourceType: ResourceType.DISK,
        };
        await RecycleBinsApi.batchDeleteDisk(req);
      }
    };

    await deleteResource();

    ElMessage.success(t("commons.msg.op_success"));
    refresh();
  });
};

/**
 * 恢复
 */
const recoverOperate = (row: RecycleBinInfo) => {
  const message = t("recycle_bin.recover_tips", [row.resourceName]);
  ElMessageBox.confirm(message, t("commons.message_box.prompt", "提示"), {
    confirmButtonText: t("commons.message_box.confirm", "确认"),
    cancelButtonText: t("commons.btn.cancel", "取消"),
    type: "warning",
  }).then(() => {
    const req = {
      id: row.id,
      resourceId: row.resourceId,
      resourceType: row.resourceType,
    };
    switch (row.resourceType) {
      case ResourceType.VM:
        RecycleBinsApi.recoverVm(req)
          .then(() => {
            ElMessage.success(t("commons.msg.op_success"));
            refresh();
          })
          .catch((err: any) => {
            console.log(err);
          });
        break;
      case ResourceType.DISK:
        RecycleBinsApi.recoverDisk(req)
          .then(() => {
            ElMessage.success(t("commons.msg.op_success"));
            refresh();
          })
          .catch((err: any) => {
            console.log(err);
          });
        break;
      default:
        ElMessage.warning("不支持的资源类型");
    }
  });
};

/**
 * 彻底删除
 */
const deleteOperate = (row: RecycleBinInfo) => {
  const message = t("recycle_bin.delete_tips", [row.resourceName]);
  ElMessageBox.confirm(message, t("commons.message_box.prompt", "提示"), {
    confirmButtonText: t("commons.message_box.confirm", "确认"),
    cancelButtonText: t("commons.btn.cancel", "取消"),
    type: "warning",
  }).then(() => {
    const req = {
      id: row.id,
      resourceId: row.resourceId,
      resourceType: row.resourceType,
    };
    switch (row.resourceType) {
      case ResourceType.VM:
        RecycleBinsApi.deleteVm(req)
          .then(() => {
            ElMessage.success(t("commons.msg.op_success"));
            refresh();
          })
          .catch((err: any) => {
            console.log(err);
          });
        break;
      case ResourceType.DISK:
        RecycleBinsApi.deleteDisk(req)
          .then(() => {
            ElMessage.success(t("commons.msg.op_success"));
            refresh();
          })
          .catch((err: any) => {
            console.log(err);
          });
        break;
      default:
        ElMessage.warning("不支持的资源类型");
    }
  });
};

const disableBatch = computed<boolean>(() => {
  return (
    selectedRowData.value.length > 0 &&
    selectedRowData.value.some(
      (row) => row.relateResource !== "-" && row.resourceType == "DISK"
    )
  );
});

const moreActions = ref<Array<ButtonAction>>([
  new ButtonAction(
    t("recycle_bin.recover"),
    "primary",
    undefined,
    batchRecoverOperate,
    permissionStore.hasPermission("[vm-service]RECYCLE_BIN:RECOVER"),
    disableBatch
  ),
  new ButtonAction(
    t("recycle_bin.delete"),
    undefined,
    "POWER_ON",
    batchDeleteOperate,
    permissionStore.hasPermission("[vm-service]RECYCLE_BIN:DELETE"),
    disableBatch
  ),
]);

/**
 * 操作按钮
 */
const buttons = ref([
  {
    label: t("recycle_bin.recover"),
    icon: "",
    click: (row: RecycleBinInfo) => {
      recoverOperate(row);
    },
    show: permissionStore.hasPermission("[vm-service]RECYCLE_BIN:RECOVER"),
    disabled: (row: { relateResource: string; resourceType: string }) => {
      return row.relateResource !== "-" && row.resourceType == "DISK";
    },
  },
  {
    label: t("recycle_bin.delete"),
    icon: "",
    click: (row: RecycleBinInfo) => {
      deleteOperate(row);
    },
    show: permissionStore.hasPermission("[vm-service]RECYCLE_BIN:DELETE"),
    disabled: (row: { relateResource: string; resourceType: string }) => {
      return row.relateResource !== "-" && row.resourceType == "DISK";
    },
  },
]);

/**
 * 选中的数据
 */
const handleSelectionChange = (list: Array<RecycleBinInfo>) => {
  selectedRowData.value = list;
};

// 刷新列表
const refresh = () => {
  table.value.search();
};
</script>
<template>
  <ce-table
    localKey="recycleBinTable"
    v-loading="tableLoading"
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    @clearCondition="clearCondition"
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
      prop="resourceName"
      column-key="resourceName"
      :label="t('recycle_bin.resource_name')"
      fixed
      min-width="200px"
    >
    </el-table-column>
    <el-table-column
      prop="resourceType"
      column-key="resourceType"
      :label="t('recycle_bin.resource_type', '资源类型')"
      :filters="tableHeadFilters.resourceType"
      :filter-multiple="false"
      min-width="120px"
    >
      <template #default="scope">
        {{
          _.get(
            ResourceTypeConst,
            scope.row.resourceType,
            scope.row.resourceType
          )
        }}
      </template>
    </el-table-column>
    <el-table-column
      prop="organizationName"
      column-key="organizationName"
      :label="t('recycle_bin.organization_name', '组织')"
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
      column-key="workspaceName"
      :label="t('recycle_bin.workspace_name', '工作空间')"
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
      prop="accountName"
      column-key="accountIds"
      :label="$t('commons.cloud_account.native', '云账号')"
      :filters="tableHeadFilters.cloudAccount"
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
      prop="resourceStatus"
      column-key="resourceStatus"
      label="状态"
      :filters="tableHeadFilters.resourceStatus"
      :filter-multiple="false"
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span>{{ filterStatus(scope.row.resourceStatus) }} </span>
          <el-icon
            v-show="isResourceLoading(scope.row.resourceStatus)"
            class="is-loading"
            ><Loading
          /></el-icon>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="resourceConfig"
      column-key="resourceConfig"
      :label="t('recycle_bin.resource_config')"
      min-width="180px"
    />
    <el-table-column
      prop="ipArray"
      column-key="ipArray"
      :label="t('recycle_bin.ip_array')"
      min-width="180px"
    >
      <template #default="scope">
        <div v-if="scope.row.resourceType === ResourceType.VM">
          <IpArray
            :remote-ip="scope.row.remoteIp"
            :ip-array="scope.row.ipArray"
          />
        </div>
        <span v-if="scope.row.resourceType === ResourceType.DISK">
          {{ scope.row.ipArray }}
        </span>
      </template>
    </el-table-column>

    <el-table-column
      :show="true"
      prop="deleteWithInstance"
      column-key="deleteWithInstance"
      :label="$t('vm_cloud_disk.label.delete_with_instance', '随实例删除')"
      :filters="tableHeadFilters.deleteWithInstance"
      :filter-multiple="false"
      min-width="130px"
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span v-if="scope.row.resourceType === 'DISK'">{{
            scope.row.deleteWithInstance === "YES"
              ? t("commons.btn.yes")
              : t("commons.btn.no")
          }}</span>
          <span v-else>{{ scope.row.deleteWithInstance }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="relateResource"
      column-key="relateResource"
      :label="t('recycle_bin.relate_resource')"
      min-width="200px"
    >
      <template #default="scope">
        <el-tooltip
          class="item"
          effect="dark"
          :content="scope.row.relateResource"
          placement="top"
        >
          <p class="text-overflow">
            {{ scope.row.relateResource }}
          </p>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column
      prop="userName"
      column-key="userName"
      :label="t('recycle_bin.user_name')"
      min-width="120px"
    />
    <el-table-column
      prop="createTime"
      :label="t('recycle_bin.put_into_recycle_bin_time')"
      min-width="180px"
    />
    <el-table-column
      prop="resourceCreateTime"
      :label="t('recycle_bin.create_time')"
      min-width="180px"
    />
    <fu-table-operations
      :buttons="buttons"
      :label="$t('commons.operation')"
      fixed="right"
    />
    <template #buttons>
      <CeTableColumnSelect :columns="columns" />
    </template>
  </ce-table>
</template>
<style lang="scss" scoped>
.text-overflow {
  max-width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.name-span-class {
  color: var(--el-color-primary);
}
.name-span-class:hover {
  cursor: pointer;
}
.text-overflow {
  max-width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
