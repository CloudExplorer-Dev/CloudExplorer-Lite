<!--操作日志列表-->
<template>
  <ce-table
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    row-key="id"
    table-layout="auto"
  >
    <template #toolbar>
      <el-button type="primary" @click="clearPolicy">清空策略</el-button>
    </template>
    <el-table-column prop="user" label="操作人"></el-table-column>
    <el-table-column prop="operatedName" label="操作"></el-table-column>
    <el-table-column prop="sourceIp" label="操作IP"></el-table-column>
    <el-table-column prop="date" label="操作时间" sortable="desc" />
    <el-table-column prop="status" label="登录状态" column-key="status">
      <template #default="scope">
        <div
          style="display: flex; align-items: center"
          :style="{ color: scope.row.status === 1 ? '' : 'red' }"
        >
          <span>{{ scope.row.status === 1 ? "成功" : "失败" }}</span>
        </div>
      </template></el-table-column
    >
    <fu-table-operations v-bind="tableConfig.tableOperations" fix />
  </ce-table>
  <LogDetail ref="logInfoRef" />
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { ListOperatedLog } from "@/api/operated_log";
import type { OperatedLogVO } from "@/api/operated_log";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus/es";
import {
  PaginationConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
import LogDetail from "./LogDetail.vue";
const { t } = useI18n();
const useRoute = useRouter();
const columns = ref([]);

const logInfoRef = ref();
const showLogInfoDialog = (v: OperatedLogVO) => {
  logInfoRef.value.dialogVisible = true;
  logInfoRef.value.logInfo = v;
};

const tableData = ref<Array<OperatedLogVO>>();
onMounted(() => {
  LogDetail.dialogFormVisible = true;
  search(new TableSearch());
});
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  params.type = "loginLog";
  ListOperatedLog({
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
    searchOptions: [{ label: "操作人", value: "user" }],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([
    TableOperations.buildButtons().newInstance(
      "查看详情",
      "primary",
      showLogInfoDialog,
      "InfoFilled"
    ),
  ]),
});

const clearPolicy = () => {
  //
  ElMessage.success("敬请期待！");
};
</script>

<style lang="scss" scoped>
.text-overflow {
  max-width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
