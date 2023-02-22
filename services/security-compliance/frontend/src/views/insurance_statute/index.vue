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
      :columns="columns"
      :data="dataList"
      :tableConfig="tableConfig"
    >
      <el-table-column prop="id" label="序号" width="80px"></el-table-column>
      <el-table-column prop="securityLevel" label="安全层面" width="150px" />
      <el-table-column prop="controlPoint" label="控制点" width="150px" />
      <el-table-column prop="baseClause" label="等级保护基本要求条款" />
      <el-table-column prop="improvementProposal" label="改进建议" />
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
<style lang="scss"></style>
