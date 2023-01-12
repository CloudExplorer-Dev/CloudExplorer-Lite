<script setup lang="ts">
import { platformIcon } from "@commons/utils/platform";
import { onMounted, ref } from "vue";
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
      },
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
  <div class="log-table">
    <ce-table
      v-loading="tableLoading"
      :columns="columns"
      :data="tableData"
      :tableConfig="tableConfig"
      row-key="id"
      height="100%"
      ref="table"
    >
      <template #toolbar> </template>
      <el-table-column type="selection" />
      <el-table-column
        :show-overflow-tooltip="true"
        prop="hostName"
        :label="$t('commons.name')"
        min-width="120"
      >
        <template #default="scope">
          <span class="name-span-class">
            {{ scope.row.hostName }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        prop="accountName"
        column-key="accountIds"
        :label="$t('commons.cloud_account.native')"
        min-width="120"
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
        :show="false"
      ></el-table-column>
      <el-table-column prop="zone" label="集群" :show="false"></el-table-column>
      <el-table-column
        prop="hostIp"
        label="IP地址"
        :show="false"
      ></el-table-column>
      <el-table-column
        prop="numCpuCores"
        label="CPU总量(核)"
        width="120"
      ></el-table-column>
      <el-table-column prop="vmCpuCores" label="CPU已分配(核)" min-width="150">
      </el-table-column>
      <el-table-column prop="memoryTotal" label="内存总量(GB)" min-width="150">
        <template #default="scope">
          {{ (scope.row.memoryTotal / 1024).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="memoryAllocated"
        label="内存已分配(GB)"
        min-width="150"
      >
        <template #default="scope">
          {{ (scope.row.memoryAllocated / 1024).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="vmTotal"
        label="云主机总数"
        min-width="150"
      ></el-table-column>
      <el-table-column
        prop="vmRunning"
        label="运行中云主机"
        min-width="150"
      ></el-table-column>
      <template #buttons>
        <fu-table-column-select type="icon" :columns="columns" size="small" />
      </template>
    </ce-table>
  </div>
</template>
<style lang="scss" scoped>
.log-table {
  width: 100%;
  height: calc(100vh - 270px);
}
</style>
