<!--工作空间列表-->
<script setup lang="ts">
import { ref, onMounted } from "vue";
import WorkspaceApi from "@/api/workspace/index";
import type { Workspace } from "@/api/workspace/type";
import { useRouter } from "vue-router";
import { ElMessageBox } from "element-plus/es";
import {
  PaginationConfig,
  SearchConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
const { t } = useI18n();
const useRoute = useRouter();
const columns = ref([]);
const tableData = ref<Array<Workspace>>();
const tableLoading = ref<boolean>(false);
//列表选中数据
const multipleSelection = ref<Array<Workspace>>();

onMounted(() => {
  search(new TableSearch());
});
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  WorkspaceApi.listWorkspace({
    currentPage: tableConfig.value.paginationConfig.currentPage,
    pageSize: tableConfig.value.paginationConfig.pageSize,
    ...params,
  },tableLoading).then((res) => {
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

//列表选中事件
const handleSelectionChange = (val: Workspace[]) => {
  multipleSelection.value = val;
};

//批量删除
const batchDelete = () => {
  // if (!multipleSelection.value) {
  //   ElMessage.info("至少选择一个工作空间");
  //   return;
  // }
  ElMessageBox.confirm(
      t("commons.message_box.confirm_delete"),
      t("commons.message_box.prompt"),
      {
        confirmButtonText: t("commons.btn.delete"),
        cancelButtonText: t("commons.btn.cancel"),
        type: "warning",
      }
  ).then(() => {
    //执行删除操作
    WorkspaceApi.deleteBatch(multipleSelection.value ? multipleSelection.value : [],tableLoading)
        .then(() => {
          t("commons.msg.delete_success");
          search(new TableSearch());
        })
        .catch((err) => {
          console.log(err);
        });
  });
};
//单个删除
const deleteOne = (row: Workspace) => {
  ElMessageBox.confirm(
      t("commons.message_box.confirm_delete"),
      t("commons.message_box.prompt"),
      {
        confirmButtonText: t("commons.btn.delete"),
        cancelButtonText: t("commons.btn.cancel"),
        type: "warning",
      }
  ).then(() => {
    //执行删除操作
    WorkspaceApi.deleteWorkspaceById(row.id,tableLoading)
        .then(() => {
          t("commons.msg.delete_success");
          search(new TableSearch());
        })
        .catch((err) => {
          console.log(err);
        });
  });
};

//创建
const create = () => {
  console.log();
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace("/list", "/create"),
  });
};

//编辑
const edit = (row: Workspace) => {
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace("/list", "/create"),
    query: { id: row.id },
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
    components: [
      SearchConfig.buildComponent().DateComponent.newInstance(
          "createTime",
          t("commons.create_time")
      ),
      SearchConfig.buildComponent().DateComponent.newInstance(
          "updateTime",
          t("commons.update_time")
      ),
    ],
    searchOptions: [
      { label: t("commons.name"), value: "name" },
      { label: t("commons.org"), value: "organizationName" },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([
    TableOperations.buildButtons().newInstance(
        t("commons.btn.edit"),
        "primary",
        edit,
        "EditPen"
    ),
    TableOperations.buildButtons().newInstance(
        t("commons.btn.delete"),
        "primary",
        deleteOne,
        "Delete"
    ),
  ]),
});
</script>
<template>
  <ce-table v-loading="tableLoading"
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    row-key="id"
    @selection-change="handleSelectionChange"
    height="100%"
    table-layout="auto"
  >
    <template #toolbar>
      <el-button type="primary" @click="create">{{
        t("commons.btn.create")
      }}</el-button>
      <el-button @click="batchDelete">{{ t("commons.btn.delete") }}</el-button>
    </template>
    <el-table-column type="selection" />
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
    </el-table-column>
    <el-table-column
      prop="userCount"
      :label="$t('workspace.user_count')"
      sortable
    >
      <template #default="scope">
        <router-link :to="{ name: 'user', query: { id: scope.row.id } }">
          <a style="color: blue">{{ scope.row.userCount }}</a>
        </router-link>
      </template>
    </el-table-column>
    <!-- <el-table-column prop="cloud_mapping" label="云租户映射">
          <CloudMappingView
            v-for="item in testWorkspaceCloudData"
            :key="item.id"
            :cloud-name="item.cloud_account_name"
            :cloud-icon-url="item.cloud_account_icon_url"
            :project-name="item.cloud_account_project_name"
          ></CloudMappingView>
        </el-table-column> -->
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
    <el-table-column prop="createTime" :label="$t('commons.create_time')" />
    <fu-table-operations v-bind="tableConfig.tableOperations" fix />
    <template #buttons>
      <fu-table-column-select type="icon" :columns="columns" size="small" />
    </template>
  </ce-table>
</template>
<style lang="scss" scoped>
.text-overflow {
  max-width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
