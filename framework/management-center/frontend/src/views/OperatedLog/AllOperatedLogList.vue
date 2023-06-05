<!--操作日志列表-->
<template>
  <ce-table
    localKey="allOperatedLogTable"
    v-loading="tableLoading"
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    row-key="id"
    height="100%"
  >
    <template #toolbar>
      <!--      <el-button
        type="primary"
        @click="clearPolicy"
        v-hasPermission="'[management-center]OPERATED_LOG:CLEAR_POLICY'"
      >
        {{ t("log_manage.btn.clear_policy") }}
      </el-button>-->
    </template>
    <el-table-column
      prop="user"
      :label="$t('log_manage.operator')"
      fixed
      min-width="150px"
    ></el-table-column>
    <el-table-column
      prop="module"
      :label="$t('log_manage.module')"
      min-width="150px"
    ></el-table-column>
    <el-table-column
      prop="resourceType"
      :label="$t('log_manage.menu')"
      min-width="150px"
    ></el-table-column>
    <el-table-column
      prop="content"
      :label="$t('log_manage.content')"
      min-width="150px"
    >
      <template #default="scope">
        <el-tooltip>
          <template #content>
            <div style="max-width: 500px">
              {{
                scope.row.content ? scope.row.content : scope.row.operatedName
              }}
            </div>
          </template>
          <div
            style="
              white-space: nowrap;
              overflow: hidden;
              text-overflow: ellipsis;
            "
          >
            {{ scope.row.content ? scope.row.content : scope.row.operatedName }}
          </div></el-tooltip
        >
      </template>
    </el-table-column>
    <el-table-column
      :show-overflow-tooltip="true"
      prop="resourceId"
      :label="$t('log_manage.resource')"
      min-width="200px"
    >
      <template #default="scope">
        <el-tooltip>
          <template #content>
            <div style="max-width: 500px">{{ scope.row.resourceName }}</div>
          </template>
          <div
            style="
              white-space: nowrap;
              overflow: hidden;
              text-overflow: ellipsis;
            "
          >
            {{ scope.row.resourceName }}
          </div></el-tooltip
        >
      </template>
    </el-table-column>
    <el-table-column
      prop="sourceIp"
      :label="$t('log_manage.ip')"
      min-width="150px"
    ></el-table-column>
    <el-table-column
      min-width="200px"
      prop="date"
      :label="$t('commons.operate_time')"
      sortable="desc"
    />
    <el-table-column
      width="100px"
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
    <fu-table-operations v-bind="tableConfig.tableOperations" fixed="right" />
    <template #buttons>
      <CeTableColumnSelect :columns="columns" />
    </template>
  </ce-table>
  <LogDetail ref="logInfoRef" />
  <el-dialog
    v-model="clearLogConfigDialogVisible"
    title="清空策略"
    width="25%"
    destroy-on-close
    :close-on-click-modal="false"
    class="custom-dialog"
    style="min-width: 600px"
  >
    <ClearLogConfig
      :paramValue="paramValue"
      :paramKey="'log.keep.api.months'"
      v-model:visible="clearLogConfigDialogVisible"
    />
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import OperatedLogApi from "@/api/operated_log/index";
import type { OperatedLogVO } from "@/api/operated_log/type";
import ClearLogConfig from "@/views/OperatedLog/ClearLogConfig.vue";
import {
  PaginationConfig,
  TableConfig,
  TableOperations,
  TableSearch,
  Order,
} from "@commons/components/ce-table/type";
import LogDetail from "./LogDetail.vue";
import { useI18n } from "vue-i18n";
const { t } = useI18n();

const columns = ref([]);
const logInfoRef = ref();
const tableLoading = ref<boolean>(false);
const showLogInfoDialog = (v: OperatedLogVO) => {
  logInfoRef.value.dialogVisible = true;
  logInfoRef.value.logInfo = v;
};
const table = ref<any>(null);
const tableData = ref<Array<OperatedLogVO>>();
const paramValue = ref<string>();
const clearLogConfigDialogVisible = ref<boolean>(false);

const showClearLogConfigDialog = () => {
  paramValue.value = "3";
  clearLogConfigDialogVisible.value = true;
};
onMounted(() => {
  const defaultCondition = new TableSearch();
  defaultCondition.order = new Order("date", false);
  search(defaultCondition);
});
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  params.type = "allLog";
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
      { label: t("log_manage.resource", "操作对象"), value: "resourceName" },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations(
    [
      TableOperations.buildButtons().newInstance(
        t("log_manage.view_details", "查看详情"),
        "primary",
        showLogInfoDialog
      ),
    ],
    "label"
  ),
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
