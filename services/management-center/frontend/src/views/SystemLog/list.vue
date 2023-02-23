<!--系统日志列表-->
<template>
  <ce-table
    localKey="systemLogTable"
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
        v-hasPermission="'[management-center]SYS_LOG:CLEAR_POLICY'"
      >
        清空策略
      </el-button>
    </template>
    <el-table-column prop="module" label="模块"></el-table-column>
    <el-table-column
      prop="level"
      label="级别"
      column-key="level"
      :filters="[
        { text: 'INFO', value: 'INFO' },
        { text: 'WARN', value: 'WARN' },
        { text: 'DEBUG', value: 'DEBUG' },
        { text: 'ERROR', value: 'ERROR' },
      ]"
    >
      <template #default="scope">
        <div
          style="display: flex; align-items: center"
          :style="{ color: scope.row.level === 'ERROR' ? 'red' : '' }"
        >
          <span>{{ scope.row.level }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="createTime"
      :label="$t('commons.create_time')"
      sortable
    />
    <el-table-column prop="message" label="日志详情" width="300px">
      <template #default="scope">
        <p class="text-overflow">{{ scope.row.message }}</p>
        <a @click="showLogInfoDialog(scope.row)">更多详情</a>
      </template>
    </el-table-column>
  </ce-table>
  <LogDetail ref="logInfoRef" />
  <el-dialog
    v-model="clearLogConfigDialogVisible"
    title="保存日志策略"
    width="25%"
    destroy-on-close
    :close-on-click-modal="false"
  >
    <ClearLogConfig
      :paramValue="paramValue"
      :paramKey="'log.keep.system.months'"
      v-model:visible="clearLogConfigDialogVisible"
    />
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import SysLogApi from "@/api/sys_log/index";
import type { SystemLogVO } from "@/api/sys_log/type";
import ClearLogConfig from "@/views/OperatedLog/ClearLogConfig.vue";
import {
  PaginationConfig,
  TableConfig,
  TableOperations,
  TableSearch,
  Order,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
import type { OperatedLogVO } from "@/api/operated_log/type";
import LogDetail from "./LogDetail.vue";
const { t } = useI18n();

const columns = ref([]);
const tableData = ref<Array<SystemLogVO>>();
const logInfoRef = ref();
const tableLoading = ref<boolean>(false);
const paramValue = ref<string>();
const clearLogConfigDialogVisible = ref<boolean>(false);

const showClearLogConfigDialog = () => {
  paramValue.value = "3";
  clearLogConfigDialogVisible.value = true;
};
const showLogInfoDialog = (v: OperatedLogVO) => {
  logInfoRef.value.dialogVisible = true;
  logInfoRef.value.logInfo = v;
};
onMounted(() => {
  const defaultCondition = new TableSearch();
  defaultCondition.order = new Order("createTime", false);
  search(defaultCondition);
});
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  SysLogApi.listSystemLog(
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
      { label: "日志详情", value: "message" },
      { label: "模块", value: "module" },
      { label: "级别", value: "level" },
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
  max-width: 300px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
