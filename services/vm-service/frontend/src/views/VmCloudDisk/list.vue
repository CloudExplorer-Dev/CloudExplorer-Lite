<script setup lang="ts">
import { ref, onMounted } from "vue";
import VmCloudDiskApi from "@/api/vm_cloud_disk";
import type { VmCloudDiskVO } from "@/api/vm_cloud_disk/type";
import { useRouter } from "vue-router";
import {
  PaginationConfig,
  TableConfig,
  SearchConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
import { ElMessage } from "element-plus";
import type { SimpleMap } from "@commons/api/base/type";
const { t } = useI18n();
const useRoute = useRouter();
const columns = ref([]);
const tableData = ref<Array<VmCloudDiskVO>>();
//硬盘状态
const diskStatus = ref<Array<SimpleMap<string>>>([
  { text: "deleted", value: "deleted" },
  { text: "in_use", value: "in_use" },
  { text: "available", value: "available" },
  { text: "attaching", value: "attaching" },
  { text: "detaching", value: "detaching" },
  { text: "creating", value: "creating" },
  { text: "reiniting", value: "reiniting" },
  { text: "unknown", value: "unknown" },
  { text: "error", value: "error" },
]);
//硬盘类型
const diskTypes = ref<Array<SimpleMap<string>>>([
  { text: "cloud_efficiency", value: "cloud_efficiency" },
  { text: "cloud_essd", value: "cloud_essd" },
  { text: "GPSSD", value: "GPSSD" },
  { text: "cloud_auto", value: "cloud_auto" },
]);
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
onMounted(() => {
  search(new TableSearch());
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
 * 操作按钮
 */
const buttons = ref([
  {
    label: t("vm_cloud_disk.btn.uninstall", "卸载"),
    icon: "",
    click: (row: VmCloudDiskVO) => {
      //
    },
    show: true,
    disabled: (row: { status: string }) => {
      return row.status !== "in_use";
    },
  },
  {
    label: t("vm_cloud_disk.btn.mount", "挂载"),
    icon: "",
    click: (row: VmCloudDiskVO) => {
      //
    },
    show: true,
    disabled: (row: { status: string }) => {
      return row.status !== "available";
    },
  },
  {
    label: t("commons.btn.delete", "删除"),
    icon: "",
    click: (row: VmCloudDiskVO) => {
      //
    },
    show: true,
    disabled: (row: { status: string }) => {
      return row.status !== "available";
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
  >
    <template #toolbar>
      <el-button>{{ t("vm_cloud_disk.btn.mount", "挂载") }}</el-button>
      <el-button>{{ t("vm_cloud_disk.btn.uninstall", "卸载") }}</el-button>
      <el-button>{{ t("commons.btn.delete", "删除") }}</el-button>
    </template>
    <el-table-column type="selection" />
    <el-table-column prop="diskName" :label="$t('commons.name')">
      <template #default="scope">
        <el-tooltip
          class="item"
          effect="dark"
          :content="scope.row.diskName"
          placement="top"
        >
          <p class="text-overflow">
            {{ scope.row.diskName }}
          </p>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column
      prop="accountName"
      :label="$t('commons.cloud_account.native')"
    ></el-table-column>
    <el-table-column
      prop="size"
      :label="$t('vm_cloud_disk.label.size')"
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
      prop="diskType"
      :label="$t('vm_cloud_disk.label.disk_category')"
      :filters="diskTypes"
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span>{{ scope.row.diskType }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="diskType"
      :label="$t('vm_cloud_disk.label.disk_type')"
      :filters="diskTypes"
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span>{{ scope.row.diskType }}</span>
        </div>
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
      prop="device"
      :label="$t('vm_cloud_disk.label.mount_info')"
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
      prop="deleteWithInstance"
      :label="$t('vm_cloud_disk.label.delete_with_instance')"
      :filters="[
        { text: t('commons.btn.yes'), value: 'YES' },
        { text: t('commons.btn.no'), value: 'NO' },
      ]"
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span
            :style="{
              color: scope.row.deleteWithInstance === 'YES' ? 'red' : '',
            }"
            >{{
              scope.row.deleteWithInstance === "YES"
                ? t("commons.btn.yes")
                : t("commons.btn.no")
            }}</span
          >
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="status"
      :label="$t('commons.status')"
      :filters="diskStatus"
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span>{{ scope.row.status }}</span>
        </div>
      </template>
    </el-table-column>
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
<style lang="scss" scoped>
.text-overflow {
  max-width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
