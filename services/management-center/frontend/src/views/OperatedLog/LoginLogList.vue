<!--操作日志列表-->
<template>
  <ce-table
    v-loading="tableLoading"
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    row-key="id"
    height="100%"
  >
    <template #toolbar>
      <el-button
        type="primary"
        @click="clearPolicy"
        v-hasPermission="'[management-center]OPERATED_LOG:CLEAR_POLICY'"
      >
        {{ t("log_manage.btn.clear_policy") }}
      </el-button>
    </template>
    <el-table-column
      prop="user"
      :label="$t('log_manage.operator')"
    ></el-table-column>
    <el-table-column
      prop="operatedName"
      :label="$t('commons.operation')"
    ></el-table-column>
    <el-table-column
      prop="sourceIp"
      :label="$t('log_manage.ip')"
    ></el-table-column>
    <el-table-column
      prop="date"
      :label="$t('commons.create_time')"
      sortable="desc"
    />
    <el-table-column
      prop="status"
      :label="$t('log_manage.status')"
      column-key="status"
    >
      <template #default="scope">
        <div
          style="display: flex; align-items: center"
          :style="{ color: scope.row.status === 1 ? '' : 'red' }"
        >
          <span>{{
            scope.row.status === 1
              ? t("commons.msg.success", [""])
              : t("commons.msg.fail", [""])
          }}</span>
        </div>
      </template></el-table-column
    >
    <fu-table-operations v-bind="tableConfig.tableOperations" fix />
  </ce-table>
  <el-dialog
    v-model="clearLogConfigDialogVisible"
    title="保存日志策略"
    width="25%"
    destroy-on-close
    :close-on-click-modal="false"
  >
    <ClearLogConfig
      :paramValue="paramValue"
      :paramKey="'log.keep.login.months'"
      v-model:visible="clearLogConfigDialogVisible"
    />
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import OperatedLogApi from "@/api/operated_log/index";
import type { OperatedLogVO } from "@/api/operated_log/type";
import {
  PaginationConfig,
  TableConfig,
  TableOperations,
  TableSearch,
  Order,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
import LogDetail from "./LogDetail.vue";
import ClearLogConfig from "@/views/OperatedLog/ClearLogConfig.vue";
const { t } = useI18n();
const columns = ref([]);
const tableLoading = ref<boolean>(false);
const tableData = ref<Array<OperatedLogVO>>();

const paramValue = ref<string>();
const clearLogConfigDialogVisible = ref<boolean>(false);

const showClearLogConfigDialog = () => {
  paramValue.value = "3";
  clearLogConfigDialogVisible.value = true;
};

onMounted(() => {
  LogDetail.dialogFormVisible = true;
  const defaultCondition = new TableSearch();
  defaultCondition.order = new Order("date", false);
  search(defaultCondition);
});
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  params.type = "loginLog";
  OperatedLogApi.listOperatedLog(
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
    quickPlaceholder: t("commons.btn.search"),
    components: [],
    searchOptions: [
      { label: t("log_manage.operator", "操作人"), value: "user" },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([]),
});

const clearPolicy = () => {
  showClearLogConfigDialog();
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
