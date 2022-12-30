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
import { ElMessage, ElMessageBox } from "element-plus";
import Attach from "@/views/vm_cloud_disk/attach.vue";
import { useRouter } from "vue-router";
import _ from "lodash";

const { t } = useI18n();
const columns = ref([]);
const tableData = ref<Array<VmCloudDiskVO>>([]);
const table = ref();
const router = useRouter();
// 选中的磁盘
const multipleSelectedRowData = ref<Array<VmCloudDiskVO>>([]);
const isBatchAttach = ref(false);

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
]);

// 表格头中显示的筛选状态
const diskStatusForTableSelect = ([
  { text: t("vm_cloud_disk.status.creating", "创建中"), value: "creating" },
  { text: t("vm_cloud_disk.status.in_use", "已挂载"), value: "in_use" },
  { text: t("vm_cloud_disk.status.available", "可用"), value: "available" },
  { text: t("vm_cloud_disk.status.rebooting", "扩容中"), value: "enlarging" },
  { text: t("vm_cloud_disk.status.wait_recycle", "待回收"), value: "wait_recycle" },
  { text: t("vm_cloud_disk.status.deleted", "已删除"), value: "deleted" },
]);

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
 * 页面挂载
 */
let timer: any;
onMounted(() => {
  search(new TableSearch());
  timer = setInterval(() => {
    if (tableData.value.some((s) => s.status.toUpperCase().includes("ING"))) {
      refresh();
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
const diskZone = ref();
const handleAttach = (row: VmCloudDiskVO) => {
  selectedDiskId.value = row.id;
  accountId.value = row.accountId;
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
  ElMessageBox.confirm(
    t("vm_cloud_disk.confirm.delete", ["【" + row.diskName + "】"]) + "？",
    t("commons.message_box.prompt", "提示"),
    {
      confirmButtonText: t("commons.message_box.confirm"),
      cancelButtonText: t("commons.btn.cancel"),
      type: "warning",
    }
  )
    .then(() => {
      VmCloudDiskApi.deleteDisk(row.id).then(() => {
        ElMessage.success(t("commons.msg.op_success"));
        refresh();
      });
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
    ElMessageBox.confirm(
      t("vm_cloud_disk.confirm.batch_delete", "确认批量删除"),
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
        VmCloudDiskApi.batchDeleteDisk(ids).then(() => {
          ElMessage.success(t("commons.msg.op_success"));
          refresh();
        });
      })
      .catch(() => {
        ElMessage.info(
          t("vm_cloud_disk.msg.canceled", [
            t("vm_cloud_disk.btn.delete"),
            "已取消删除",
          ])
        );
      });
  }
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
        (row) => row.status.toUpperCase() != "AVAILABLE"
      ) ||
      multipleSelectedRowData.value.some((row) =>
        notSupportPlatforms.value.includes(row.platform)
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
    show: true,
    disabled: (row: { status: string }) => {
      return row.status == "deleted";
    },
  },
  {
    label: t("vm_cloud_disk.btn.mount", "挂载"),
    icon: "",
    click: handleAttach,
    show: true,
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
    show: true,
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
    show: true,
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
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    row-key="id"
    height="100%"
    ref="table"
    @selection-change="handleSelectionChange"
  >
    <template #toolbar>
      <el-button @click="batchAttach()" :disabled="disableBatchAttach"
        >{{ t("vm_cloud_disk.btn.mount", "挂载") }}
      </el-button>
      <el-button @click="batchDetach()" :disabled="disableBatchDetach">{{
        t("vm_cloud_disk.btn.uninstall", "卸载")
      }}</el-button>
      <el-button @click="batchDelete()" :disabled="disableBatchDelete">{{
        t("commons.btn.delete", "删除")
      }}</el-button>
    </template>
    <el-table-column type="selection" />
    <el-table-column
      prop="diskName"
      :label="$t('commons.name')"
      :show-overflow-tooltip="true"
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
      :label="$t('commons.cloud_account.native')"
    ></el-table-column>
    <el-table-column
      prop="size"
      :label="$t('vm_cloud_disk.label.size') + '/G'"
      sortable
    ></el-table-column>
    <el-table-column
      prop="vmInstanceName"
      :label="$t('vm_cloud_disk.label.vm')"
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
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span>{{ filterType(scope.row.diskType) }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="organizationName"
      :label="$t('commons.org')"
      :show="false"
    ></el-table-column>
    <el-table-column
      prop="workspaceName"
      :label="$t('commons.workspace')"
      :show="false"
    ></el-table-column>
    <el-table-column
      prop="device"
      :label="$t('vm_cloud_disk.label.mount_info')"
      :show="false"
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
      :show="true"
      prop="deleteWithInstance"
      column-key="deleteWithInstance"
      :label="$t('vm_cloud_disk.label.delete_with_instance')"
      :filters="[
        { text: t('commons.btn.yes'), value: 'YES' },
        { text: t('commons.btn.no'), value: 'NO' },
      ]"
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
      prop="status"
      column-key="status"
      :label="$t('commons.status')"
      :filters="diskStatusForTableSelect"
      :show="true"
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
      prop="createTime"
      sortable
      :label="$t('commons.create_time')"
      :show="true"
    ></el-table-column>
    <el-table-column
      prop="updateTime"
      :label="$t('commons.update_time')"
      :show="true"
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
      :zone="diskZone"
      v-model:visible="attachWindowVisible"
      :isBatch="isBatchAttach"
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
</style>
