<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, computed } from "vue";
import VmCloudDiskApi from "@/api/vm_cloud_disk";
import type { VmCloudDiskVO } from "@/api/vm_cloud_disk/type";
import {
  PaginationConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
import type { SimpleMap } from "@commons/api/base/type";
import { ElMessage, ElMessageBox, ElPopover } from "element-plus";
import Attach from "@/views/vm_cloud_disk/attach.vue";
import { useRouter } from "vue-router";
import _ from "lodash";
import { usePermissionStore } from "@commons/stores/modules/permission";
import Grant from "@/views/vm_cloud_server/grant.vue";
import RecycleBinsApi from "@/api/recycle_bin";
import BaseCloudAccountApi from "@commons/api/cloud_account";
import OrgTreeFilter from "@commons/components/table-filter/OrgTreeFilter.vue";

const { t } = useI18n();
const permissionStore = usePermissionStore();
const columns = ref([]);
const tableData = ref<Array<VmCloudDiskVO>>([]);
const table = ref();
const router = useRouter();
// 选中的磁盘
const multipleSelectedRowData = ref<Array<VmCloudDiskVO>>([]);
const isBatchAttach = ref(false);
const cloudAccount = ref<Array<SimpleMap<string>>>([]);
const tableLoading = ref<boolean>(false);
import { platformIcon } from "@commons/utils/platform";

/**
 * 表头：组织树筛选
 */
const orgTreeRef = ref();
const orgPopRef = ref<any>();
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
  } else {
    workspaceTreeRef.value.cancelChecked();
  }
};

//硬盘状态
const diskStatus = ref<Array<SimpleMap<string>>>([
  { text: "已删除", value: "deleted" },
  { text: "已挂载", value: "in_use" },
  { text: "可用", value: "available" },
  { text: "挂载中", value: "attaching" },
  { text: "卸载中", value: "detaching" },
  { text: "创建中", value: "creating" },
  { text: "初始化中", value: "reiniting" },
  { text: "未知", value: "unknown" },
  { text: "错误", value: "error" },
  { text: "删除中", value: "deleting" },
  { text: "扩容中", value: "enlarging" },
  { text: "待回收", value: "ToBeRecycled" },
]);
//硬盘类型
const diskTypes = ref<Array<SimpleMap<string>>>([
  { text: "高效云盘", value: "cloud_efficiency" },
  { text: "ESSD云盘", value: "cloud_essd" },
  { text: "SSD云盘", value: "cloud_ssd" },
  { text: "普通云盘", value: "cloud" },
  { text: "通用型SSD", value: "GPSSD" },
  { text: "极速型SSD", value: "ESSD" },
  { text: "超高IO", value: "SSD" },
  { text: "高IO", value: "SAS" },
  { text: "高性能云硬盘", value: "CLOUD_PREMIUM" },
  { text: "增强型SSD云硬盘", value: "CLOUD_HSSD" },
  { text: "通用型SSD云硬盘", value: "CLOUD_BSSD" },
  { text: "ESSD AutoPL云盘", value: "cloud_auto" },
  { text: "精简置备", value: "THIN" },
  { text: "厚置备置零", value: "THICK_EAGER_ZEROED" },
  { text: "厚置备延迟置零", value: "THICK_LAZY_ZEROED" },
  { text: "稀疏型", value: "SPARSE" },
  { text: "lvmdriver-1", value: "lvmdriver-1" },
  { text: "__DEFAULT__", value: "__DEFAULT__" },
  { text: "未知", value: "NA" },
  { text: "待回收", value: "ToBeRecycled" },
]);

// 表格头中显示的筛选状态
const diskStatusForTableSelect = [
  { text: t("vm_cloud_disk.status.creating", "创建中"), value: "creating" },
  { text: t("vm_cloud_disk.status.in_use", "已挂载"), value: "in_use" },
  { text: t("vm_cloud_disk.status.available", "可用"), value: "available" },
  { text: t("vm_cloud_disk.status.rebooting", "扩容中"), value: "enlarging" },
  {
    text: t("vm_cloud_disk.status.wait_recycle", "待回收"),
    value: "ToBeRecycled",
  },
  { text: t("vm_cloud_disk.status.deleted", "已删除"), value: "deleted" },
];

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

/**
 * 不支持磁盘单独管理的云平台
 */
const notSupportPlatforms = ref(["fit2cloud_vsphere_platform"]);

const filterType = (value: string) => {
  let status = value;
  diskTypes.value.forEach((v) => {
    if (v.value == value) {
      status = v.text;
      return;
    }
  });
  return status;
};

const filterStatus = (value: string) => {
  let status = "";
  diskStatus.value.forEach((v) => {
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
  VmCloudDiskApi.listVmCloudDisk(
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
 * 是否开启了回收站
 */
const isRecycleBinOpened = ref(true);
const getRecycleBinSetting = () => {
  RecycleBinsApi.getRecycleEnableStatus().then((result) => {
    isRecycleBinOpened.value = result.data;
  });
};

/**
 * 页面挂载
 */
let timer: any;
onMounted(() => {
  searchCloudAccount();
  getRecycleBinSetting();
  search(new TableSearch());
  timer = setInterval(() => {
    if (tableData.value.some((s) => s.status.toUpperCase().includes("ING"))) {
      refreshWithoutLoading();
    }
  }, 6000);
});

onBeforeUnmount(() => {
  if (timer) clearInterval(timer);
});

// 刷新列表
const refresh = () => {
  table.value.search();
};

// 刷新列表不展示 loading 图标
const refreshWithoutLoading = () => {
  const params = TableSearch.toSearchParams(table.value.getTableSearch());
  VmCloudDiskApi.listVmCloudDisk({
    currentPage: tableConfig.value.paginationConfig.currentPage,
    pageSize: tableConfig.value.paginationConfig.pageSize,
    ...params,
  }).then((res) => {
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
      { label: t("commons.name", "名称"), value: "diskName" },
      {
        label: t("commons.cloud_account.name", "云账号名称"),
        value: "accountName",
      },
      {
        label: t("vm_cloud_disk.label.vm", "所属虚拟机"),
        value: "vmInstanceName",
      },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([]),
});

/**
 * 扩容磁盘
 * @param row
 */
const handleEnlarge = (row: VmCloudDiskVO) => {
  router.push({ name: "enlarge", params: { id: row.id } });
};

/**
 * 挂载磁盘
 * @param row
 */
const selectedDiskId = ref();
const attachWindowVisible = ref(false);
const accountId = ref();
const platform = ref();
const diskZone = ref();
const handleAttach = (row: VmCloudDiskVO) => {
  selectedDiskId.value = row.id;
  accountId.value = row.accountId;
  platform.value = row.platform;
  diskZone.value = row.zone;
  attachWindowVisible.value = true;
};

/**
 * 卸载磁盘
 * @param row
 */
const handleDetach = (row: VmCloudDiskVO) => {
  ElMessageBox.confirm(
    t("vm_cloud_disk.confirm.detach", [
      "【" + row.diskName + "】",
      "【" + row.vmInstanceName + "】",
    ]) + "？",
    t("commons.message_box.prompt", "提示"),
    {
      confirmButtonText: t("commons.message_box.confirm"),
      cancelButtonText: t("commons.btn.cancel"),
      type: "warning",
    }
  )
    .then(() => {
      VmCloudDiskApi.detach(row.id).then(() => {
        ElMessage.success(t("commons.msg.op_success"));
        refresh();
      });
    })
    .catch(() => {
      ElMessage.info(
        t("vm_cloud_disk.msg.canceled", [
          t("vm_cloud_disk.btn.uninstall"),
          "已取消卸载",
        ])
      );
    });
};

/**
 * 删除磁盘
 * @param row
 */
const handleDelete = (row: VmCloudDiskVO) => {
  const message = isRecycleBinOpened.value
    ? t("vm_cloud_disk.confirm.recycle", ["【" + row.diskName + "】"]) + "？"
    : t("vm_cloud_disk.confirm.delete", ["【" + row.diskName + "】"]) + "？";

  ElMessageBox.confirm(message, t("commons.message_box.prompt", "提示"), {
    confirmButtonText: t("commons.message_box.confirm"),
    cancelButtonText: t("commons.btn.cancel"),
    type: "warning",
  })
    .then(() => {
      if (isRecycleBinOpened.value) {
        VmCloudDiskApi.recycleDisk(row.id as string)
          .then(() => {
            ElMessage.success(t("commons.msg.op_success"));
            refresh();
          })
          .catch((err) => {
            console.log(err);
          });
      } else {
        VmCloudDiskApi.deleteDisk(row.id).then(() => {
          ElMessage.success(t("commons.msg.op_success"));
          refresh();
        });
      }
    })
    .catch(() => {
      ElMessage.info(
        t("vm_cloud_disk.msg.canceled", [
          t("vm_cloud_disk.btn.delete"),
          "已取消删除",
        ])
      );
    });
};

/**
 * 选中的数据
 */
const handleSelectionChange = (list: Array<VmCloudDiskVO>) => {
  multipleSelectedRowData.value = list;
};

/**
 * 校验是否勾选了数据
 */
const validateSelectedData = computed(() => {
  return (
    multipleSelectedRowData.value && multipleSelectedRowData.value.length > 0
  );
});

/**
 * 批量挂载
 */
const selectedDiskIds = ref<string[]>([]);
const batchAttach = () => {
  if (!validateSelectedData.value) {
    ElMessage.warning(t("vm_cloud_disk.msg.select_one", "至少选择一条数据"));
    return;
  } else {
    isBatchAttach.value = true;
    selectedDiskIds.value = Array.from(
      multipleSelectedRowData.value,
      ({ id }) => id
    );
    accountId.value = _.cloneDeep(multipleSelectedRowData.value[0].accountId);
    diskZone.value = _.cloneDeep(multipleSelectedRowData.value[0].zone);
    attachWindowVisible.value = true;
  }
};

/**
 * 批量卸载
 */
const batchDetach = () => {
  if (!validateSelectedData.value) {
    ElMessage.warning(t("vm_cloud_disk.msg.select_one", "至少选择一条数据"));
    return;
  } else {
    ElMessageBox.confirm(
      t("vm_cloud_disk.confirm.batch_detach", "确认批量卸载"),
      t("commons.message_box.prompt", "提示"),
      {
        confirmButtonText: t("commons.message_box.confirm", "确认"),
        cancelButtonText: t("commons.btn.cancel", "取消"),
        type: "warning",
      }
    )
      .then(() => {
        const ids: string[] = Array.from(
          multipleSelectedRowData.value,
          ({ id }) => id
        );
        VmCloudDiskApi.batchDetach(ids).then(() => {
          ElMessage.success(t("commons.msg.op_success"));
          refresh();
        });
      })
      .catch(() => {
        ElMessage.info(
          t("vm_cloud_disk.msg.canceled", [
            t("vm_cloud_disk.btn.uninstall"),
            "已取消卸载",
          ])
        );
      });
  }
};

/**
 * 批量删除
 */
const batchDelete = () => {
  if (!validateSelectedData.value) {
    ElMessage.warning(t("vm_cloud_disk.msg.select_one", "至少选择一条数据"));
    return;
  } else {
    const message = isRecycleBinOpened.value
      ? t("vm_cloud_disk.confirm.batch_recycle") + "？"
      : t("vm_cloud_disk.confirm.batch_delete") + "？";
    ElMessageBox.confirm(message, t("commons.message_box.prompt", "提示"), {
      confirmButtonText: t("commons.message_box.confirm", "确认"),
      cancelButtonText: t("commons.btn.cancel", "取消"),
      type: "warning",
    })
      .then(() => {
        const ids: string[] = Array.from(
          multipleSelectedRowData.value,
          ({ id }) => id
        );

        if (isRecycleBinOpened.value) {
          VmCloudDiskApi.batchRecycleDisks(ids).then(() => {
            ElMessage.success(t("commons.msg.op_success"));
            refresh();
          });
        } else {
          VmCloudDiskApi.batchDeleteDisk(ids).then(() => {
            ElMessage.success(t("commons.msg.op_success"));
            refresh();
          });
        }
      })
      .catch(() => {
        ElMessage.info(
          t("vm_cloud_disk.msg.canceled", [
            t("vm_cloud_disk.btn.delete"),
            "已取消",
          ])
        );
      });
  }
};

/**
 * 批量授权
 */
const batchAuthorize = () => {
  if (!validateSelectedData.value) {
    ElMessage.warning(t("vm_cloud_disk.msg.select_one", "至少选择一条数据"));
    return;
  }
  showGrantDialog();
};
const grantDialogVisible = ref<boolean>(false);
const showGrantDialog = () => {
  selectedDiskIds.value = selectedDiskIds.value = Array.from(
    multipleSelectedRowData.value,
    ({ id }) => id
  );
  grantDialogVisible.value = true;
};

/**
 * 禁用批量挂载
 */
const disableBatchAttach = computed(() => {
  if (multipleSelectedRowData.value.length == 0) {
    return false;
  } else {
    return (
      multipleSelectedRowData.value.some(
        (row) => row.status === "ToBeRecycled"
      ) ||
      multipleSelectedRowData.value.some(
        (row) => row.status.toUpperCase() != "AVAILABLE"
      ) ||
      multipleSelectedRowData.value.every(
        (row) => row.platform != multipleSelectedRowData.value[0].platform
      ) ||
      multipleSelectedRowData.value.every(
        (row) => row.zone != multipleSelectedRowData.value[0].zone
      ) ||
      multipleSelectedRowData.value.some((row) =>
        notSupportPlatforms.value.includes(row.platform)
      )
    );
  }
});

/**
 * 禁用批量卸载
 */
const disableBatchDetach = computed(() => {
  if (multipleSelectedRowData.value.length == 0) {
    return false;
  } else {
    return (
      multipleSelectedRowData.value.some(
        (row) => row.status === "ToBeRecycled"
      ) ||
      multipleSelectedRowData.value.some(
        (row) => row.status.toUpperCase() != "IN_USE"
      ) ||
      multipleSelectedRowData.value.some((row) =>
        notSupportPlatforms.value.includes(row.platform)
      )
    );
  }
});

/**
 * 禁用批量删除
 */
const disableBatchDelete = computed(() => {
  if (multipleSelectedRowData.value.length == 0) {
    return false;
  } else {
    return (
      multipleSelectedRowData.value.some(
        (row) => row.status === "ToBeRecycled"
      ) ||
      multipleSelectedRowData.value.some(
        (row) => row.status.toUpperCase() != "AVAILABLE"
      ) ||
      multipleSelectedRowData.value.some((row) =>
        notSupportPlatforms.value.includes(row.platform)
      )
    );
  }
});

/**
 * 禁用批量授权
 */
const disableBatchAuthorize = computed(() => {
  if (multipleSelectedRowData.value.length == 0) {
    return false;
  } else {
    return (
      multipleSelectedRowData.value.some(
        (row) => row.status === "ToBeRecycled"
      ) ||
      multipleSelectedRowData.value.some(
        (row) =>
          (!_.isEmpty(row.instanceUuid) && !_.isNull(row.instanceUuid)) ||
          row.status.toUpperCase() === "DELETED"
      )
    );
  }
});

/**
 * 展示磁盘详情
 * @param row
 */
const showDiskDetail = (row: VmCloudDiskVO) => {
  router.push({ name: "disk_detail", params: { id: row.id } });
};

/**
 * 操作按钮
 */
const buttons = ref([
  {
    label: t("vm_cloud_disk.btn.enlarge", "扩容"),
    icon: "",
    click: handleEnlarge,
    show: permissionStore.hasPermission("[vm-service]CLOUD_DISK:RESIZE"),
    disabled: (row: { status: string }) => {
      return row.status == "ToBeRecycled" || row.status == "deleted";
    },
  },
  {
    label: t("vm_cloud_disk.btn.mount", "挂载"),
    icon: "",
    click: handleAttach,
    show: permissionStore.hasPermission("[vm-service]CLOUD_DISK:ATTACH"),
    disabled: (row: VmCloudDiskVO) => {
      return (
        row.status !== "available" ||
        notSupportPlatforms.value.includes(row.platform)
      );
    },
  },
  {
    label: t("vm_cloud_disk.btn.uninstall", "卸载"),
    icon: "",
    click: handleDetach,
    show: permissionStore.hasPermission("[vm-service]CLOUD_DISK:DETACH"),
    disabled: (row: VmCloudDiskVO) => {
      return (
        row.status !== "in_use" ||
        notSupportPlatforms.value.includes(row.platform)
      );
    },
  },
  {
    label: t("commons.btn.delete", "删除"),
    icon: "",
    click: handleDelete,
    show: permissionStore.hasPermission("[vm-service]CLOUD_DISK:DELETE"),
    disabled: (row: VmCloudDiskVO) => {
      return (
        row.status !== "available" ||
        notSupportPlatforms.value.includes(row.platform)
      );
    },
  },
]);
</script>
<template>
  <ce-table
    localKey="vmCloudDiskTable"
    v-loading="tableLoading"
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    row-key="id"
    height="100%"
    ref="table"
    @selection-change="handleSelectionChange"
    @clearCondition="clearCondition"
  >
    <template #toolbar>
      <el-button
        @click="batchAttach()"
        :disabled="disableBatchAttach"
        v-hasPermission="'[vm-service]CLOUD_DISK:ATTACH'"
      >
        {{ t("vm_cloud_disk.btn.mount", "挂载") }}
      </el-button>
      <el-button
        @click="batchDetach()"
        :disabled="disableBatchDetach"
        v-hasPermission="'[vm-service]CLOUD_DISK:DETACH'"
      >
        {{ t("vm_cloud_disk.btn.uninstall", "卸载") }}
      </el-button>
      <el-button
        @click="batchAuthorize()"
        :disabled="disableBatchAuthorize"
        v-hasPermission="'[vm-service]CLOUD_DISK:AUTH'"
      >
        {{ t("commons.btn.gran", "授权") }}
      </el-button>
      <el-button
        @click="batchDelete()"
        :disabled="disableBatchDelete"
        v-hasPermission="'[vm-service]CLOUD_DISK:DELETE'"
      >
        {{ t("commons.btn.delete", "删除") }}
      </el-button>
    </template>
    <el-table-column type="selection" />
    <el-table-column
      prop="diskName"
      :label="$t('commons.name')"
      :show-overflow-tooltip="true"
      min-width="200px"
      fixed
    >
      <template #default="scope">
        <span
          @click="showDiskDetail(scope.row)"
          style="cursor: pointer; color: var(--el-color-primary)"
        >
          {{ scope.row.diskName }}
        </span>
      </template>
    </el-table-column>
    <el-table-column
      prop="accountName"
      column-key="accountIds"
      :label="$t('commons.cloud_account.native')"
      :filters="cloudAccount"
      min-width="180px"
      ><template #default="scope">
        <div style="display: flex">
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
      ><template #header>
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
      :label="$t('commons.workspace')"
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
      prop="vmInstanceName"
      :label="$t('vm_cloud_disk.label.vm')"
      min-width="200px"
    >
      <template #default="scope">
        <el-tooltip
          class="item"
          effect="dark"
          :content="scope.row.vmInstanceName"
          placement="top"
        >
          <p class="text-overflow">
            {{ scope.row.vmInstanceName }}
          </p>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column
      prop="bootableText"
      column-key="bootable"
      :label="$t('vm_cloud_disk.label.disk_attribute')"
      :filters="[
        { text: t('vm_cloud_disk.label.system_disk'), value: 1 },
        { text: t('vm_cloud_disk.label.data_disk'), value: 0 },
      ]"
      min-width="120px"
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span>{{ scope.row.bootableText }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="diskType"
      column-key="diskType"
      :label="$t('vm_cloud_disk.label.disk_type')"
      :filters="diskTypes"
      min-width="120px"
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span>{{ filterType(scope.row.diskType) }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="size"
      :label="$t('vm_cloud_disk.label.size') + '(GB)'"
      sortable
      min-width="160px"
    ></el-table-column>
    <el-table-column
      prop="status"
      column-key="status"
      :label="$t('commons.status')"
      :filters="diskStatusForTableSelect"
      :show="true"
      :filter-multiple="false"
      min-width="100px"
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span
            :style="{
              color: scope.row.status === 'deleted' ? 'red' : '',
            }"
            >{{ filterStatus(scope.row.status) }}</span
          >
          <ce-icon
            v-if="scope.row.status.toUpperCase().includes('ING')"
            code="Loading"
            class="is-loading"
            style="margin-left: 5px; color: var(--el-color-primary)"
          ></ce-icon>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      :show="true"
      prop="deleteWithInstance"
      column-key="deleteWithInstance"
      :label="$t('vm_cloud_disk.label.delete_with_instance')"
      :filters="[
        { text: t('commons.btn.yes'), value: 'YES' },
        { text: t('commons.btn.no'), value: 'NO' },
      ]"
      min-width="130px"
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span>{{
            scope.row.deleteWithInstance === "YES"
              ? t("commons.btn.yes")
              : t("commons.btn.no")
          }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="device"
      :label="$t('vm_cloud_disk.label.mount_info')"
      :show="false"
      min-width="120px"
    >
      <template #default="scope">
        <el-tooltip
          class="item"
          effect="dark"
          :content="scope.row.device"
          placement="top"
        >
          <p class="text-overflow">
            {{ scope.row.device }}
          </p>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column
      prop="createTime"
      sortable
      :label="$t('commons.create_time')"
      :show="true"
      min-width="180px"
    ></el-table-column>
    <fu-table-operations
      :ellipsis="2"
      :columns="columns"
      :buttons="buttons"
      :label="$t('commons.operation')"
      fixed="right"
    />
    <template #buttons>
      <fu-table-column-select type="icon" :columns="columns" size="small" />
    </template>
  </ce-table>

  <!-- 挂载磁盘弹出框 -->
  <el-dialog
    v-model="attachWindowVisible"
    :title="$t('vm_cloud_disk.label.select_vm')"
    width="25%"
    destroy-on-close
  >
    <Attach
      :id="selectedDiskId"
      :ids="selectedDiskIds"
      :accountId="accountId"
      :platform="platform"
      :zone="diskZone"
      v-model:visible="attachWindowVisible"
      :isBatch="isBatchAttach"
      @refresh="refresh"
    />
  </el-dialog>

  <!-- 批量授权页面弹出框 -->
  <el-dialog
    v-model="grantDialogVisible"
    :title="$t('commons.grant')"
    width="35%"
    destroy-on-close
    :close-on-click-modal="false"
  >
    <Grant
      :ids="selectedDiskIds"
      resource-type="disk"
      v-model:visible="grantDialogVisible"
      @refresh="refresh"
    />
  </el-dialog>
</template>
<style lang="scss" scoped>
.text-overflow {
  max-width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.highlight {
  color: var(--el-color-primary);
}
</style>
