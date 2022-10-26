<script setup lang="ts">
import { ref, onMounted } from "vue";
import organizationApi from "@/api/organization";
import type { Organization } from "@/api/organization/type";
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

// 树形组织对象
interface OrganizationTree extends Organization {
  children: Array<OrganizationTree>;
}

// 初始化国际化函数对象
const { t } = useI18n();
// 获取路由对象
const router = useRouter();
// 表格所需字段
const columns = ref([]);
// 表格数据
const tableData = ref<Array<OrganizationTree>>();
// 组织数据
const organizations = ref<Array<Organization>>();
// 需要展开的列表id
const expandRowKeys = ref<Array<string>>();
//选中的组织对象
const multipleSelection = ref<Array<Organization>>();
// table组建
const table: any = ref(null);

onMounted(() => {
  search(new TableSearch());
});
/**
 * 查找需要展开的组织
 * @param organizations 组织数据
 * @param field         字段
 * @param value         字段值
 */
const findOpenTree = (
  organizations: Array<Organization>,
  field: string,
  value: string
) => {
  if (!field && !value) {
    return;
  }
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

/**
 * 查找当前树上所有的组织树
 * @param organizations 需要查找的组织s
 * @param result        结果
 * @param pid           父id
 */
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

/**
 * 查询
 * @param condition 查询条件
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  organizationApi
    .pageOrganization({
      currentPage: tableConfig.value.paginationConfig.currentPage,
      pageSize: tableConfig.value.paginationConfig.pageSize,
      ...params,
    })
    .then((ok) => {
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
  router.push({
    path: router.currentRoute.value.path.replace("/list", "/create"),
  });
};
/**
 *删除组织
 */
const batchDelete = () => {
  ElMessageBox.confirm(
    t("commons.message_box.confirm_delete", "确认删除"),
    t("commons.message_box.prompt", "提交"),
    {
      confirmButtonText: t("commons.btn.delete", "删除"),
      cancelButtonText: t("commons.btn.cancel", "取消"),
      type: "warning",
    }
  ).then(() => {
    organizationApi
      .deleteBatchOrg(multipleSelection.value ? multipleSelection.value : [])
      .then(() => {
        table.value?.search();
      });
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
const edit = (row: Organization) => {
  router.push({
    path: router.currentRoute.value.path.replace("/list", "/update"),
    query: { id: row.id },
  });
};

/**
 * 删除一个组织
 * @param row
 */
const deleteItem = (row: Organization) => {
  ElMessageBox.confirm(
    t("commons.message_box.confirm_delete", "确认删除"),
    t("commons.message_box.prompt", "提示"),
    {
      confirmButtonText: t("commons.btn.delete", "删除"),
      cancelButtonText: t("commons.btn.cancel", "取消"),
      type: "warning",
    }
  ).then(() => {
    organizationApi.deleteOrg(row.id).then(() => {
      if (table.value) {
        table.value?.search();
        ElMessage.success(t("commons.msg.delete_success", "删除成功"));
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
        t("commons.create_time", "创建时间")
      ),
      SearchConfig.buildComponent().DateComponent.newInstance(
        "updateTime",
        t("commons.update_time", "修改时间")
      ),
    ],
    searchOptions: [{ label: "组织", value: "name" }],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([
    TableOperations.buildButtons().newInstance(
      t("commons.btn.edit", "编辑"),
      "primary",
      edit,
      "EditPen"
    ),
    TableOperations.buildButtons().newInstance(
      t("commons.btn.delete", "删除"),
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
      <el-button type="primary" @click="create">{{
        t("commons.btn.create", "创建")
      }}</el-button>
      <el-button @click="batchDelete">{{
        t("commons.btn.delete", "删除")
      }}</el-button>
    </template>
    <el-table-column type="selection" />
    <el-table-column prop="name" :label="t('commons.org', '组织')" sortable />
    <el-table-column
      prop="description"
      :label="t('commons.description', '描述')"
      sortable
    />
    <el-table-column
      prop="updateTime"
      :label="t('commons.update_time', '修改时间')"
      sortable
    />
    <el-table-column
      prop="createTime"
      :label="t('commons.create_time', '创建时间')"
      sortable
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
