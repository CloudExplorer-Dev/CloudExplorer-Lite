<script setup lang="ts">
import { ref, onMounted } from "vue";
import {
  listOrganization,
  type Organization,
  deleteOrg,
  deleteBatchOrg,
} from "@/api/organization";
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
const { t } = useI18n();
const useRoute = useRouter();
const columns = ref([]);
const tableData = ref<Array<OrganizationTree>>();
const organizations = ref<Array<Organization>>();
const expandRowKeys = ref<Array<string>>();

/**
 * 选中的组织对象
 */
const multipleSelection = ref<Array<Organization>>();
const table: any = ref(null);
onMounted(() => {
  search(new TableSearch());
});

interface OrganizationTree extends Organization {
  children: Array<OrganizationTree>;
}

const findOpenTree = (
  organizations: Array<Organization>,
  field: string,
  value: string
) => {
  const expandKeys = new Set<string>();
  const filterOrganizations = organizations.filter((item: any) => {
    return item[field].indexOf(value) >= 0;
  });
  filterOrganizations.forEach((filterItem) => {
    findUpOrgTree(organizations, [], filterItem.pid).forEach((id: string) => {
      expandKeys.add(id);
    });
  });
  expandRowKeys.value = [...expandKeys];
};
const findUpOrgTree = (
  organizations: Array<Organization>,
  result: Array<string>,
  pid?: string
) => {
  if (pid) {
    const pOrg = organizations.find((org) => {
      return org.id === pid;
    });
    if (pOrg) {
      result.push(pOrg.id);
      findUpOrgTree(organizations, result, pOrg.pid);
    }
  }
  return result;
};
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
    organizations.value = ok.data.records;
    // 扁平化数据
    tableData.value = resetData(ok.data.records);
    if (condition.search) {
      const searchValue: unknown = condition.search.value;
      findOpenTree(
        ok.data.records,
        condition.search.field,
        searchValue as string
      );
    }

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
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace("/list", "/create"),
  });
};
/**
 *删除组织
 */
const batchDelete = () => {
  ElMessageBox.confirm(
    t("commons.message_box.confirm_delete"),
    t("commons.message_box.prompt"),
    {
      confirmButtonText: t("commons.btn.delete"),
      cancelButtonText: t("commons.btn.cancel"),
      type: "warning",
    }
  ).then(() => {
    deleteBatchOrg(multipleSelection.value ? multipleSelection.value : []).then(
      () => {
        table.value?.search();
      }
    );
  });
};

/**
 * 选中
 * @param val
 */
const handleSelectionChange = (val: OrganizationTree[]) => {
  multipleSelection.value = val;
};

/**
 *修改
 */
const edit = (row: any) => {
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace("/list", "/update"),
    query: { id: row.id },
  });
};

const deleteItem = (row: any) => {
  ElMessageBox.confirm(
    t("commons.message_box.confirm_delete"),
    t("commons.message_box.prompt"),
    {
      confirmButtonText: t("commons.btn.delete"),
      cancelButtonText: t("commons.btn.cancel"),
      type: "warning",
    }
  ).then(() => {
    deleteOrg(row.id).then((ok) => {
      if (table.value) {
        table.value?.search();
        ElMessage.success(t("commons.msg.delete_success"));
      }
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
    searchOptions: [{ label: "组织", value: "name" }],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([
    TableOperations.buildButtons().newInstance(
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
    ),
  ]),
});
</script>
<template>
  <ce-table
    height="100%"
    ref="table"
    :expand-row-keys="expandRowKeys"
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
