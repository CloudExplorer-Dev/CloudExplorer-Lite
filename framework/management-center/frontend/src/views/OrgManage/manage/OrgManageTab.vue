<template>
  <el-container>
    <ce-table
      localKey="orgManageTable"
      ref="table"
      :columns="columns"
      :data="tableData"
      :tableConfig="tableConfig"
      :expand-row-keys="expandRowKeys"
      height="100%"
      row-key="id"
      v-loading="loading"
    >
      <template #toolbar>
        <el-button
          @click="create"
          type="primary"
          v-if="
            permissionStore.hasPermission(
              '[management-center]ORGANIZATION:CREATE'
            )
          "
        >
          添加组织
        </el-button>
      </template>
      <el-table-column prop="name" :label="t('commons.org', '组织')" sortable />
      <el-table-column
        prop="description"
        :label="t('commons.description', '描述')"
        sortable
      />
      <el-table-column label="工作空间">
        <template #default="scope">
          <a style="color: #3370ff" @click="jumpToWorkspace(scope.row)">
            {{ scope.row.workspaceCount }}
          </a>
        </template>
      </el-table-column>
      <el-table-column label="用户">
        <template #default="scope">
          <a style="color: #3370ff" @click="jumpToUser(scope.row)">
            {{ scope.row.userCount }}
          </a>
        </template>
      </el-table-column>

      <el-table-column label="操作" fixed="right">
        <template #default="scope">
          <el-button
            link
            type="primary"
            v-if="
              permissionStore.hasPermission(
                '[management-center]ORGANIZATION:EDIT'
              )
            "
            @click="edit(scope.row)"
          >
            {{ t("commons.btn.edit", "编辑") }}
          </el-button>
          <el-button
            link
            type="primary"
            v-if="
              permissionStore.hasPermission(
                '[management-center]ORGANIZATION:DELETE'
              )
            "
            @click="deleteItem(scope.row)"
          >
            {{ t("commons.btn.delete", "删除") }}
          </el-button>
        </template>
      </el-table-column>
    </ce-table>
  </el-container>
</template>
<script setup lang="ts">
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useI18n } from "vue-i18n";
import { ref, watch } from "vue";

import {
  PaginationConfig,
  TableConfig,
  TableSearch,
} from "@commons/components/ce-table/type";
import organizationApi from "@/api/organization";
import type { Organization } from "@/api/organization/type";

const props = defineProps<{
  id?: string;
}>();

// 树形组织对象
interface OrganizationTree extends Organization {
  children: Array<OrganizationTree>;
}

const permissionStore = usePermissionStore();
const { t } = useI18n();
const table = ref();
// 表格数据
const tableData = ref<Array<OrganizationTree>>();
// 组织数据
const organizations = ref<Array<Organization>>();
const loading = ref<boolean>(false);
const columns = ref([]);
// 需要展开的列表id
const expandRowKeys = ref<Array<string>>();

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
      } else {
        result.push(item);
      }
    } else {
      result.push(item);
    }
  });
  return result;
};

const sort = (tableData: Array<OrganizationTree>) => {
  tableData.sort((pre, next) => pre.createTime.localeCompare(next.createTime));
  tableData.forEach((data) => {
    if (data.children && data.children.length > 0) {
      sort(data.children);
    }
  });
};

const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  if (props.id !== "CE_BASE") {
    params.rootId = props.id;
  }

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
      if (!condition.order) {
        // 使用默认排序 默认是根据创建时间顺序
        sort(tableData.value);
      }
      if (condition.search) {
        const searchValue: any = condition.search.name;
        findOpenTree(
          ok.data.records,
          searchValue.field as string,
          searchValue.value as string
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

function create() {}

function edit(row: Organization) {}

const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    search: search,
    quickPlaceholder: "搜索",
    components: [],
    searchOptions: [{ label: "组织", value: "name" }],
  },
  paginationConfig: new PaginationConfig(),
});

const emit = defineEmits(["jumpToWorkspace", "jumpToUser", "deleteOrg"]);

function jumpToWorkspace(row: Organization) {
  emit("jumpToWorkspace", { type: "ORGANIZATION", id: row.id });
}
function jumpToUser(row: Organization) {
  emit("jumpToUser", { type: "ORGANIZATION", id: row.id });
}

/**
 * 删除一个组织
 * @param row
 */
const deleteItem = (row: Organization) => {
  emit("deleteOrg", {
    id: row.id,
    type: "ORGANIZATION",
    name: row.name,
    inTab: true,
  });
};

function refreshList() {
  search(new TableSearch());
}

watch(
  () => props.id,
  (id) => {
    console.log(id);
    refreshList();
  },
  { immediate: true }
);

defineExpose({ refreshList });
</script>
<style lang="scss" scoped>
:deep(.el-table__body) {
  .el-table__placeholder {
    display: none;
  }
}
</style>
