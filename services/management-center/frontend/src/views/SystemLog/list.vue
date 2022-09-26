<!--系统日志列表-->
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
    <el-table-column prop="message" label="日志详情">
      <template #default="scope">
        <el-tooltip
          class="item"
          effect="dark"
          :content="scope.row.message"
          placement="top"
        >
          <p class="text-overflow">{{ scope.row.message }}</p>
        </el-tooltip>
      </template>
    </el-table-column>
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
      sortable="desc"
    />
  </ce-table>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { ListSystemLog } from "@/api/sys_log";
import type { SystemLogVO } from "@/api/sys_log";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus/es";
import {
  PaginationConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
const { t } = useI18n();
const useRoute = useRouter();
const columns = ref([]);
const tableData = ref<Array<SystemLogVO>>();
onMounted(() => {
  search(new TableSearch());
});
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  ListSystemLog({
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
