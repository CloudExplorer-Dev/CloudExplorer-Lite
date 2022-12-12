
<script setup lang="ts">
import { platformIcon } from "@commons/utils/platform";
import {onMounted, ref} from "vue";
import VmCloudHostApi from "@/api/vm_cloud_host";
import type { VmCloudHostVO } from "@/api/vm_cloud_host/type";
import {
  PaginationConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
const { t } = useI18n();
const table = ref<any>(null);
const columns = ref([]);
const tableData = ref<Array<VmCloudHostVO>>([]);
const selectedRowData = ref<Array<VmCloudHostVO>>([]);
const tableLoading = ref<boolean>(false);

/**
 * 查询
 * @param condition
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  VmCloudHostApi.listVmCloudHost(
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
        value: "hostName",
      }
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([]),
});

/**
 * 页面挂载
 */
onMounted(() => {
  search(new TableSearch());
});
</script>
<template>
  <ce-table
      v-loading="tableLoading"
      :columns="columns"
      :data="tableData"
      :tableConfig="tableConfig"
      row-key="id"
      height="100%"
      ref="table"
  >
    <template #toolbar>

    </template>
    <el-table-column type="selection" />
    <el-table-column
        :show-overflow-tooltip="true"
        prop="hostName"
        :label="$t('commons.name')"
    >
      <template #default="scope">
        <span @click="" class="name-span-class">
          {{ scope.row.hostName }}
        </span>
      </template>
    </el-table-column>
    <el-table-column
        prop="accountName"
        column-key="accountIds"
        :label="$t('commons.cloud_account.native')"
    >
      <template #default="scope">
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
        label="数据中心"
    ></el-table-column>
    <el-table-column
        prop="zone"
        label="集群"
    ></el-table-column>
    <el-table-column
        prop="hostIp"
        label="IP地址"
    ></el-table-column>
    <el-table-column
        prop="cpuTotal"
        label="CPU总量(MHz)"
    ></el-table-column>
    <el-table-column
        prop="cpuAllocated"
        label="CPU已分配(MHz)"
    >
    </el-table-column>
    <el-table-column
        prop="memoryTotal"
        label="内存总量(GB)"
    >
      <template #default="scope">

        {{ (scope.row.memoryTotal/1024).toFixed(2) }}
      </template>
    </el-table-column>
    <el-table-column
        prop="memoryAllocated"
        label="内存已分配(GB)"
    >
      <template #default="scope">

        {{ (scope.row.memoryAllocated/1024).toFixed(2) }}
      </template>
    </el-table-column>
    <el-table-column
        prop="vmTotal"
        label="虚拟机总数"
    ></el-table-column>
    <el-table-column
        prop="vmRunning"
        label="运行中虚拟机"
    ></el-table-column>
    <template #buttons>
      <fu-table-column-select type="icon" :columns="columns" size="small" />
    </template>
  </ce-table>
</template>
<style lang="scss" scoped>
.name-span-class {
  color: var(--el-color-primary);
}
.name-span-class:hover {
  cursor: pointer;
}
</style>
