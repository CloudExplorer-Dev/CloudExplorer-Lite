<template>
  <el-container>
    <ce-table
      localKey="workspaceTableInOrgManage"
      ref="table"
      :columns="columns"
      :data="tableData"
      :tableConfig="tableConfig"
      height="100%"
      table-layout="auto"
      v-loading="loading"
    >
      <template #toolbar>
        <el-button
          @click="create"
          type="primary"
          v-if="
            permissionStore.hasPermission('[management-center]WORKSPACE:CREATE')
          "
        >
          创建工作空间
        </el-button>
      </template>
      <el-table-column prop="name" :label="$t('commons.name')">
        <template #default="scope">
          <el-tooltip
            class="item"
            effect="dark"
            :content="scope.row.name"
            placement="top"
          >
            <p class="text-overflow">{{ scope.row.name }}</p>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="organizationName" :label="$t('commons.org')">
        <template #default="scope">
          <a style="color: #3370ff" @click="jumpToOrg(scope.row)">
            {{ scope.row.organizationName }}
          </a>
        </template>
      </el-table-column>
      <el-table-column
        prop="userCount"
        :label="$t('workspace.user_count')"
        sortable
      >
        <template #default="scope">
          <a style="color: #3370ff" @click="jumpToUser(scope.row)">
            {{ scope.row.userCount }}
          </a>
        </template>
      </el-table-column>

      <el-table-column prop="description" :label="$t('commons.description')">
        <template #default="scope">
          <el-tooltip
            class="item"
            effect="dark"
            :content="scope.row.description"
            placement="top"
          >
            <p class="text-overflow">{{ scope.row.description }}</p>
          </el-tooltip>
        </template>
      </el-table-column>

      <el-table-column label="操作" fixed="right">
        <template #default="scope">
          <el-button
            link
            type="primary"
            v-if="
              permissionStore.hasPermission('[management-center]WORKSPACE:EDIT')
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
                '[management-center]WORKSPACE:DELETE'
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
import WorkspaceApi from "@/api/workspace";
import type { WorkspaceDetail } from "@/api/workspace/type";

const props = defineProps<{
  orgId?: string;
}>();

const permissionStore = usePermissionStore();
const { t } = useI18n();
const table = ref();
const tableData = ref<Array<WorkspaceDetail>>();
const loading = ref<boolean>(false);
const columns = ref([]);

const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);

  if (props.orgId && props.orgId !== "CE_BASE") {
    params.organizationIds = [props.orgId];
  }

  WorkspaceApi.listWorkspace(
    {
      currentPage: tableConfig.value.paginationConfig.currentPage,
      pageSize: tableConfig.value.paginationConfig.pageSize,
      ...params,
    },
    loading
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

const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    search: search,
    quickPlaceholder: "搜索",
    components: [],
    searchOptions: [
      { label: t("user.username"), value: "username" },
      { label: t("user.name"), value: "name" },
      { label: t("user.email"), value: "email" },
    ],
  },
  paginationConfig: new PaginationConfig(),
});

const emit = defineEmits(["jumpToUser", "deleteWorkspace", "jumpToOrg"]);

function jumpToUser(row: WorkspaceDetail) {
  emit("jumpToUser", { type: "WORKSPACE", id: row.id });
}
function jumpToOrg(row: WorkspaceDetail) {
  emit("jumpToOrg", { id: row.organizationId });
}

function create() {}
function edit(row: WorkspaceDetail) {}
function deleteItem(row: WorkspaceDetail) {
  emit("deleteWorkspace", { id: row.id, type: "WORKSPACE", inTab: true });
}

function refreshList() {
  search(new TableSearch());
}

watch(
  () => props.orgId,
  (orgId) => {
    refreshList();
  },
  { immediate: true }
);

defineExpose({ refreshList });
</script>
<style lang="scss" scoped>
.text-overflow {
  max-width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
