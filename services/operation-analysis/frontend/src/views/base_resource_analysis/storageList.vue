<script setup lang="ts">
import PlatformIcon from "@commons/components/platform-icon/index.vue";
import { onMounted, ref } from "vue";
import VmCloudDatastoreApi from "@/api/vm_cloud_datastore";
import type { VmCloudDatastoreVO } from "@/api/vm_cloud_datastore/type";
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
const tableData = ref<Array<VmCloudDatastoreVO>>([]);
const tableLoading = ref<boolean>(false);
const loading = ref<boolean>(false);
/**
 * 查询
 * @param condition
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  VmCloudDatastoreApi.listVmCloudDatastore(
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
        value: "DatastoreName",
      },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([]),
});
const exportData = () => {
  const condition = table?.value.getTableSearch();
  const tableParams = TableSearch.toSearchParams(condition);
  VmCloudDatastoreApi.exportData(tableParams, loading);
};
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
      localKey="storageTable"
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
        prop="datastoreName"
        :label="$t('commons.name')"
      >
        <template #default="scope">
          <span class="name-span-class">
            {{ scope.row.datastoreName }}
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
      <el-table-column prop="zone" label="集群" :show="false"></el-table-column>
      <el-table-column align="right" prop="capacity" label="总容量(GB)">
        <template #default="scope">
          {{ DecimalFormat.format(scope.row.capacity) }}
        </template>
      </el-table-column>
      <el-table-column align="right" label="已使用(GB)">
        <template #default="scope">
          {{ DecimalFormat.format(scope.row.capacity - scope.row.freeSpace) }}
        </template>
      </el-table-column>
      <el-table-column align="right" prop="useRate" label="使用率">
        <template #default="scope">
          {{
            PercentFormat.format(
              (scope.row.capacity - scope.row.freeSpace) / scope.row.capacity
            )
          }}
        </template>
      </el-table-column>
      <el-table-column align="right" prop="freeSpace" label="剩余(GB)">
        <template #default="scope">
          {{ DecimalFormat.format(scope.row.freeSpace) }}
        </template>
      </el-table-column>
      <el-table-column align="right" prop="freeRate" label="剩余率">
        <template #default="scope">
          {{ PercentFormat.format(scope.row.freeSpace / scope.row.capacity) }}
        </template>
      </el-table-column>
      <el-table-column align="right" prop="allocated" label="已分配(GB)">
        <template #default="scope">
          {{ DecimalFormat.format(parseInt(scope.row.allocated)) }}
        </template>
      </el-table-column>
      <el-table-column align="right" label="分配率">
        <template #default="scope">
          {{ PercentFormat.format(scope.row.allocated / scope.row.capacity) }}
        </template>
      </el-table-column>
      <el-table-column
        align="right"
        prop="type"
        label="类型"
        :show="false"
      ></el-table-column>
      <template #buttons>
        <!-- 导出 -->
        <el-button
          :loading="loading"
          @click="exportData('xlsx')"
          style="width: 32px"
        >
          <ce-icon
            v-if="!loading"
            size="16"
            code="icon_bottom-align_outlined"
          />
          <template #loading>
            <ce-icon
              class="is-loading"
              style="margin-left: 6px"
              size="16"
              code="Loading"
            />
          </template>
        </el-button>
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
