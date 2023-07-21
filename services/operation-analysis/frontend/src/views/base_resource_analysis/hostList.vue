<script setup lang="ts">
import PlatformIcon from "@commons/components/platform-icon/index.vue";
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
import PercentFormat from "@commons/utils/percentFormat";
import DecimalFormat from "@commons/utils/decimalFormat";

const { t } = useI18n();
const table = ref<any>(null);
const columns = ref([]);
const tableData = ref<Array<VmCloudHostVO>>([]);
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
      localKey="hostTable"
      v-loading="tableLoading"
      :columns="columns"
      :data="tableData"
      :tableConfig="tableConfig"
      row-key="id"
      height="100%"
      ref="table"
    >
      <template #toolbar></template>
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
            <PlatformIcon
              style="margin-top: 3px; width: 16px; height: 16px"
              :platform="scope.row.platform"
            ></PlatformIcon>
            <span style="margin-left: 10px">{{ scope.row.accountName }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="region"
        label="数据中心"
        :show="false"
      ></el-table-column>
      <el-table-column prop="zone" label="集群" :show="false">
      </el-table-column>
      <el-table-column
        prop="hostIp"
        label="IP地址"
        :show="false"
      ></el-table-column>
      <el-table-column
        align="right"
        prop="numCpuCores"
        label="CPU总量(核)"
        width="120"
      >
        <template #default="scope">
          {{ DecimalFormat.format(scope.row.numCpuCores) }}
        </template>
      </el-table-column>
      <el-table-column
        align="right"
        prop="vmCpuCores"
        label="CPU已分配(核)"
        min-width="150"
      >
        <template #default="scope">
          {{ DecimalFormat.format(scope.row.vmCpuCores) }}
        </template>
      </el-table-column>
      <el-table-column
        :show="false"
        align="right"
        prop="memoryTotal"
        label="CPU分配率"
        min-width="150"
      >
        <template #default="scope">
          {{
            PercentFormat.format(scope.row.vmCpuCores / scope.row.numCpuCores)
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="right"
        prop="cpuTotal"
        label="CPU总量(MHz)"
        min-width="150"
      >
        <template #default="scope">
          {{ DecimalFormat.format(scope.row.cpuTotal) }}
        </template>
      </el-table-column>
      <el-table-column
        align="right"
        prop="cpuUsed"
        label="CPU已使用(MHz)"
        min-width="150"
      >
        <template #default="scope">
          {{
            scope.row.cpuUsed ? DecimalFormat.format(scope.row.cpuUsed) : "-"
          }}
        </template>
      </el-table-column>
      <el-table-column
        :show="false"
        align="right"
        label="CPU使用率"
        min-width="150"
      >
        <template #default="scope">
          {{
            scope.row.cpuUsed
              ? PercentFormat.format(scope.row.cpuUsed / scope.row.cpuTotal)
              : "-"
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="right"
        prop="memoryTotal"
        label="内存总量(GB)"
        min-width="150"
      >
        <template #default="scope">
          {{ DecimalFormat.format(scope.row.memoryTotal / 1024) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="memoryAllocated"
        label="内存已分配(GB)"
        min-width="150"
        align="right"
      >
        <template #default="scope">
          {{ DecimalFormat.format(scope.row.memoryAllocated / 1024) }}
        </template>
      </el-table-column>
      <el-table-column
        :show="false"
        align="right"
        prop="memoryTotal"
        label="内存分配率"
        min-width="150"
      >
        <template #default="scope">
          {{
            PercentFormat.format(
              scope.row.memoryAllocated / scope.row.memoryTotal
            )
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="right"
        prop="memoryUsed"
        label="内存已使用(GB)"
        min-width="150"
      >
        <template #default="scope">
          {{
            scope.row.memoryUsed
              ? DecimalFormat.format(scope.row.memoryUsed / 1024)
              : "-"
          }}
        </template>
      </el-table-column>
      <el-table-column
        :show="false"
        align="right"
        label="内存使用率"
        min-width="150"
      >
        <template #default="scope">
          {{
            scope.row.memoryUsed
              ? PercentFormat.format(
                  scope.row.memoryUsed / scope.row.memoryTotal
                )
              : "-"
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="right"
        prop="vmTotal"
        label="云主机总数"
        min-width="150"
      >
        <template #default="scope">
          {{ DecimalFormat.format(scope.row.vmTotal) }}
        </template>
      </el-table-column>
      <el-table-column
        align="right"
        prop="vmRunning"
        label="运行中云主机"
        min-width="150"
      >
        <template #default="scope">
          {{ DecimalFormat.format(scope.row.vmRunning) }}
        </template>
      </el-table-column>
      <template #buttons>
        <CeTableColumnSelect :columns="columns" />
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
