<template>
  <breadcrumb :breadcrumbs="[{ to: {}, title: '组织管理' }]"></breadcrumb>
  <div class="table-container">
    <ce-table
      :columns="columns"
      :data="tableData"
      :tableConfig="tableConfig"
      row-key="id"
    >
      <template #toolbar>
        <el-button @click="create">创建</el-button>
        <el-button @click="deleteOrg">删除</el-button>
      </template>
      <el-table-column type="selection" />
      <el-table-column prop="name" label="组织" sortable />
      <el-table-column prop="description" label="描述" sortable />
      <el-table-column prop="createTime" label="创建时间" sortable />
      <fu-table-operations v-bind="tableConfig.tableOperations" fix />
      <template #buttons>
        <fu-table-column-select type="icon" :columns="columns" size="small" />
      </template>
    </ce-table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { listOrganization, Organization } from "@/api/organization";
import { useRouter } from "vue-router";
import {
  PaginationConfig,
  SearchConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "ce-base/commons/components/ce-table/index";
const useRoute = useRouter();
const columns = ref([]);
const tableData = ref<Array<OrganizationTree>>();

onMounted(() => {
  search(new TableSearch());
});

interface OrganizationTree extends Organization {
  children: Array<OrganizationTree>;
}

/**
 * 将数据转换为树状
 * @param organizations
 */
const resetData = (organizations: Array<Organization>) => {
  const newOrganizations: Array<OrganizationTree> = organizations.map((org) => {
    return { ...org, children: [] };
  });
  const result: Array<OrganizationTree> = [];
  newOrganizations.forEach((item) => {
    if (item.pid) {
      const parentOrganization = newOrganizations.find((org) => {
        return org.id === item.pid;
      });
      if (parentOrganization) {
        parentOrganization.children.push(item);
      }
    } else {
      result.push(item);
    }
  });
  return result;
};

const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  listOrganization({
    currentPage: tableConfig.value.paginationConfig.currentPage,
    pageSize: tableConfig.value.paginationConfig.pageSize,
    ...params,
  }).then((ok) => {
    // 扁平化数据
    console.log(ok);
    tableData.value = resetData(ok.data.records);
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
 * 创建
 */
const create = () => {
  useRoute.push({ path: useRoute.currentRoute.value.path + "/create" });
};
/**
 *删除组织
 */
const deleteOrg = () => {
  console.log("delete");
};
/**
 *修改
 */
const edit = (row: any) => {
  useRoute.push({
    path: useRoute.currentRoute.value.path + "/update",
    query: { id: row.id },
  });
};

/**
 * 表单配置
 */
const tableConfig = ref<TableConfig>({
  searchConfig: {
    search: search,
    quickPlaceholder: "按照名称搜索",
    components: [
      SearchConfig.buildComponent().DateComponent.newInstance(
        "createTime",
        "创建时间"
      ),
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([
    TableOperations.buildButtons().newInstance(
      "编辑",
      "primary",
      edit,
      "EditPen"
    ),
  ]),
});
</script>

<style lang="scss" scoped>
.table-container {
  height: calc(100% - 50px);
  width: 100%;
}
:deep(.el-table__body) {
  .el-table__placeholder {
    display: none;
  }
}
</style>
