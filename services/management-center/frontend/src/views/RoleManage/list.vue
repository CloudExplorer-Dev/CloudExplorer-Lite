<script setup lang="ts">
import { ref, onMounted } from "vue";
import { RolePageRequest } from "@commons/api/role/type";
import BaseRoleApi from "@commons/api/role";
import { useRouter } from "vue-router";
import {
  PaginationConfig,
  SearchConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { ElMessage, ElMessageBox } from "element-plus";
import { useI18n } from "vue-i18n";

import type { Role } from "@commons/api/role/type";
import { listOrganization } from "@/api/organization";

const { t } = useI18n();
const useRoute = useRouter();
const columns = ref([]);

const tableData = ref<Array<Role>>();

onMounted(() => {
  search(new TableSearch());
});

const handleSelectionChange = () => {
  console.log("handleSelectionChange");
};

const search = (condition: TableSearch) => {
  const params = RolePageRequest.assign(TableSearch.toSearchParams(condition));
  params.setPage(
    tableConfig.value.paginationConfig.currentPage,
    tableConfig.value.paginationConfig.pageSize
  );
  BaseRoleApi.pageRoles(params).then((ok) => {
    tableData.value = ok.data.records;

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
    components: [
      SearchConfig.buildComponent().DateComponent.newInstance(
        "createTime",
        "创建时间"
      ),
      SearchConfig.buildComponent().DateComponent.newInstance(
        "updateTime",
        "修改时间"
      ),
    ],
    searchOptions: [{ label: "角色", value: "name" }],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([
    /*TableOperations.buildButtons().newInstance(
      "编辑",
      "primary",
      edit,
      "EditPen"
    ),
    TableOperations.buildButtons().newInstance(
      "删除",
      "primary",
      deleteItem,
      "Delete"
    ),*/
  ]),
});
</script>
<template>
  <ce-table
    height="100%"
    ref="table"
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    @selection-change="handleSelectionChange"
    row-key="id"
  >
    <template #toolbar>
      <el-button type="primary" @click="create">创建</el-button>
      <el-button @click="batchDelete">删除</el-button>
    </template>
    <el-table-column type="selection" />
    <el-table-column prop="name" label="组织" sortable />
    <el-table-column prop="description" label="描述" sortable />
    <el-table-column prop="updateTime" label="修改时间" sortable />
    <el-table-column prop="createTime" label="创建时间" sortable />
    <fu-table-operations v-bind="tableConfig.tableOperations" fix />
    <template #buttons>
      <fu-table-column-select type="icon" :columns="columns" size="small" />
    </template>
  </ce-table>
</template>

<style lang="scss" scoped>
:deep(.el-table__body) {
  .el-table__placeholder {
    display: none;
  }
}
</style>
