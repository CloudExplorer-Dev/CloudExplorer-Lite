<script setup lang="ts">
import { ref, onMounted } from "vue";
import _ from "lodash";
import type { SimpleMap } from "@commons/api/base/type";
import { RolePageRequest } from "@commons/api/role/type";
import RoleApi from "@/api/role";
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

const { t } = useI18n();

const useRoute = useRouter();
const columns = ref([]);

const table = ref<any>(null);
const tableData = ref<Array<Role>>();

const originRoles = ref<Array<Role>>();
const originRoleNameMap = ref<Array<SimpleMap<string>>>([]);

const tableLoading = ref<boolean>(false);

const typeMap = ref<Array<SimpleMap<string>>>([
  { text: "系统", value: "origin" },
  { text: "自定义", value: "inherit" },
]);

onMounted(() => {
  search(new TableSearch());
  initMaps();
});

function initMaps() {
  RoleApi.listRoles({ type: "origin" }).then((ok) => {
    originRoles.value = ok.data;

    originRoleNameMap.value = [];
    for (const role of originRoles.value) {
      originRoleNameMap.value.push({ text: role.name, value: role.id });
    }

    //console.log(originRoleNameMap);
  });
}

const handleSelectionChange = () => {
  console.log("handleSelectionChange");
};

const selectable = (row: Role, index: number) => {
  return row.type !== "origin";
};

const formatterType = (
  row: Role,
  column: any,
  cellValue: string,
  index: number
) => {
  const tempObj = _.find(typeMap.value, (o) => o.value === row.type);
  return tempObj ? tempObj.text : cellValue;
};

const formatterParentRole = (
  row: Role,
  column: any,
  cellValue: string,
  index: number
) => {
  const tempObj = _.find(
    originRoleNameMap.value,
    (o) => o.value === row.parentRoleId
  );
  return tempObj ? tempObj.text : cellValue;
};

const search = (condition: TableSearch) => {
  const params = RolePageRequest.assign(TableSearch.toSearchParams(condition));
  params.setPage(
    tableConfig.value.paginationConfig.currentPage,
    tableConfig.value.paginationConfig.pageSize
  );
  RoleApi.pageRoles(params, tableLoading).then((ok) => {
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

const openDetail = (row: Role) => {
  console.log(row);
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace(
      "/list",
      `/detail/${row.id}`
    ),
  });
  /*useRoute.push({
    path: useRoute.currentRoute.value.path.replace("/list", "/detail"),
    query: { id: row.id },
  });*/
};

const create = () => {
  useRoute.push({ name: "role_create" });
};

const editRole = (row: Role) => {
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace("/list", `/edit/${row.id}`),
  });
  console.log(row);
};

const deleteRole = (row: Role) => {
  ElMessageBox.confirm(
    t("commons.message_box.confirm_delete"),
    t("commons.message_box.prompt"),
    {
      confirmButtonText: t("commons.btn.delete"),
      cancelButtonText: t("commons.btn.cancel"),
      type: "warning",
    }
  ).then(() => {
    RoleApi.deleteRole(row.id, tableLoading)
      .then((response) => {
        console.log(response);
        ElMessage.success(t("commons.msg.delete_success"));
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => {
        table.value?.search();
      });
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
    TableOperations.buildButtons().newInstance(
      "详情",
      "primary",
      openDetail,
      "Document",
      false,
      true
    ),
    /*TableOperations.buildButtons().newInstance(
      "编辑",
      "primary",
      editRole,
      "EditPen",
      (row: Role) => {
        return row.type === "origin";
      }
    ),*/
    TableOperations.buildButtons().newInstance(
      "删除",
      "danger",
      deleteRole,
      "Delete",
      (row: Role) => {
        return row.type === "origin";
      }
    ),
  ]),
});
</script>
<template>
  <ce-table
    v-loading="tableLoading"
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
    <el-table-column type="selection" :selectable="selectable" />
    <el-table-column prop="name" label="角色" sortable />
    <el-table-column
      prop="type"
      column-key="type"
      label="类型"
      sortable
      :filters="typeMap"
      :formatter="formatterType"
    />
    <el-table-column
      prop="parentRoleId"
      column-key="parentRoleId"
      label="继承角色"
      sortable
      :filters="originRoleNameMap"
      :formatter="formatterParentRole"
    />
    <el-table-column prop="description" label="描述" sortable />
    <el-table-column
      prop="updateTime"
      label="修改时间"
      sortable
      :show="false"
    />
    <el-table-column
      prop="createTime"
      label="创建时间"
      sortable
      :show="false"
    />
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
