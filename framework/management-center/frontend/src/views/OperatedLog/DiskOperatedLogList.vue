<!--操作日志列表-->
<template>
  <ce-table
    localKey="diskOperatedLogTable"
    v-loading="tableLoading"
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    row-key="id"
    table-layout="auto"
    height="100%"
  >
    <template #toolbar>
      <!-- <el-button type="primary" @click="clearPolicy">清空策略</el-button> -->
    </template>
    <el-table-column
      prop="user"
      :label="$t('log_manage.operator')"
      min-width="200px"
      fixed
    ></el-table-column>
    <el-table-column
      prop="content"
      :label="$t('log_manage.content')"
      min-width="150px"
    >
      <template #default="scope">
        <el-tooltip>
          <template #content>
            <div style="max-width: 500px">{{ scope.row.content?scope.row.content:scope.row.operatedName }}</div>
          </template>
          <div class="text-overflow">
            {{ scope.row.content?scope.row.content:scope.row.operatedName }}
          </div>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column
      prop="resourceId"
      :label="$t('log_manage.resource')"
      min-width="200px"
    >
      <template #default="scope">
        <el-tooltip>
          <template #content>
            <div style="max-width: 500px">{{ scope.row.resourceName }}</div>
          </template>
          <div class="text-overflow">
            {{ scope.row.resourceName }}
          </div>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column
      prop="joinResourceId"
      :label="$t('log_manage.belong_vm')"
      min-width="200px"
    >
      <template #default="scope">
        <el-tooltip class="box-item" effect="dark" placement="top-start">
          <template #content>
            <div style="max-width: 500px">
              {{
                scope.row.joinResourceName != null
                  ? scope.row.joinResourceName
                  : scope.row.joinResourceId
              }}
            </div>
          </template>
          <div class="table_content_ellipsis">
            {{
              scope.row.joinResourceName != null
                ? scope.row.joinResourceName
                : scope.row.joinResourceId
            }}
          </div></el-tooltip
        >
      </template>
    </el-table-column>
    <el-table-column
      prop="sourceIp"
      :label="$t('log_manage.ip')"
      min-width="200px"
    ></el-table-column>
    <el-table-column
      prop="date"
      :label="$t('commons.operate_time')"
      sortable="desc"
      min-width="200px"
    />
    <el-table-column
      prop="status"
      :label="$t('log_manage.status')"
      column-key="status"
      min-width="200px"
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
      <CeTableColumnSelect :columns="columns"/>
    </template>
  </ce-table>
  <LogDetail ref="logInfoRef" />
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import OperatedLogApi from "@/api/operated_log/index";
import type { OperatedLogVO } from "@/api/operated_log/type";
import { ElMessage } from "element-plus/es";
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
const tableData = ref<Array<OperatedLogVO>>();
onMounted(() => {
  const defaultCondition = new TableSearch();
  defaultCondition.order = new Order("date", false);
  search(defaultCondition);
});
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  params.type = "diskOperateLog";
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
        showLogInfoDialog,
        "InfoFilled"
      ),
    ],
    "label"
  ),
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
