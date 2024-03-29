<template>
  <layout-auto-height-content>
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <ce-table
      localKey="insuranceStatuteTable"
      v-loading="loading"
      height="100%"
      ref="table"
      class="ce-table"
      :columns="columns"
      :data="dataList"
      :tableConfig="tableConfig"
    >
      <el-table-column prop="id" label="序号" min-width="40"></el-table-column>
      <el-table-column
        prop="securityLevel"
        label="安全层面"
        min-width="100px"
      />
      <el-table-column prop="controlPoint" label="控制点" min-width="80px" />
      <el-table-column
        prop="baseClause"
        min-width="300px"
        label="等级保护基本要求条款"
      >
        <template #default="scope">
          <el-popover
            placement="top-start"
            title="等级保护基本要求条款"
            :width="400"
            :popper-style="{
              '--el-popover-title-font-size': '12px',
              '--el-popover-title-text-color': '#646a73',
              color: '#1F2329',
              'font-size': '12px',
            }"
            trigger="hover"
            :content="scope.row.improvementProposal"
          >
            <template #reference>
              <span style="cursor: pointer">
                {{ scope.row.improvementProposal }}</span
              ></template
            >
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column
        prop="improvementProposal"
        min-width="300px"
        label="改进建议"
      >
        <template #default="scope">
          <el-popover
            placement="top-start"
            title="改进建议"
            :width="400"
            :popper-style="{
              '--el-popover-title-font-size': '12px',
              '--el-popover-title-text-color': '#646a73',
              color: '#1F2329',
              'font-size': '12px',
            }"
            trigger="hover"
            :content="scope.row.improvementProposal"
          >
            <template #reference>
              <span style="cursor: pointer">{{
                scope.row.improvementProposal
              }}</span>
            </template>
          </el-popover>
        </template>
      </el-table-column>
    </ce-table>
  </layout-auto-height-content>
</template>
<script setup lang="ts">
import {
  PaginationConfig,
  TableConfig,
  TableSearch,
  TableOperations,
} from "@commons/components/ce-table/type";
import { ref, onMounted } from "vue";
import complianceInsuranceStatuteApi from "@/api/compliance_insurance_statute";
import type { ComplianceInsuranceStatute } from "@/api/compliance_insurance_statute/type";
// 表格实例
const table: any = ref(null);
// 表格数据
const dataList = ref<Array<ComplianceInsuranceStatute>>([]);
// 表格加载器
const loading = ref<boolean>(false);
// 字段
const columns = ref([]);
/**
 * 表格查询函数
 * @param condition 表格查询参数
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  complianceInsuranceStatuteApi
    .page(
      tableConfig.value.paginationConfig.currentPage,
      tableConfig.value.paginationConfig.pageSize,
      params,
      loading
    )
    .then((ok) => {
      dataList.value = ok.data.records;
      tableConfig.value.paginationConfig?.setTotal(
        ok.data.total,
        tableConfig.value.paginationConfig
      );
      tableConfig.value.paginationConfig?.setCurrentPage(
        ok.data.current,
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
    quickPlaceholder: "搜索",
    components: [],
    searchOptions: [
      { label: "安全层面", value: "securityLevel" },
      { label: "控制点", value: "controlPoint" },
      { label: "基本要求条款", value: "baseClause" },
      { label: "改进建议", value: "improvementProposal" },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([]),
});
onMounted(() => {
  search(new TableSearch());
});
</script>
<style lang="scss" scoped>
:deep(.el-table__cell) {
  .cell {
    white-space: nowrap;
  }
}
</style>
