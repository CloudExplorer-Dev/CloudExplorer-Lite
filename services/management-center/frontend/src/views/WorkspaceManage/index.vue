<!--工作空间列表-->
<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb :breadcrumbs="[{ to: {}, title: '工作空间' }]"></breadcrumb>
    </template>
    <ce-table
      :columns="columns"
      :data="tableData"
      :tableConfig="tableConfig"
      row-key="id"
      @selection-change="handleSelectionChange"
      table-layout="auto"
    >
      <template #toolbar>
        <el-button type="primary" @click="create">创建</el-button>
        <el-button @click="batchDelete">删除</el-button>
      </template>
      <el-table-column type="selection" />
      <el-table-column prop="name" label="名称">
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
      <el-table-column prop="organizationName" label="组织"> </el-table-column>
      <el-table-column prop="userCount" label="用户数" sortable>
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
      <el-table-column prop="description" label="描述">
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
      <el-table-column prop="createTime" label="创建时间" />
      <fu-table-operations v-bind="tableConfig.tableOperations" fix />
      <template #buttons>
        <fu-table-column-select type="icon" :columns="columns" size="small" />
      </template>
    </ce-table>
  </layout-content>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import {
  Workspace,
  listWorkspace,
  deleteWorkspaceById,
  deleteBatch,
} from "@/api/workspace";
import { useRouter } from "vue-router";
import { ElMessageBox, ElMessage } from "element-plus/es";
import {
  PaginationConfig,
  SearchConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "ce-base/commons/components/ce-table/index";
const useRoute = useRouter();
const columns = ref([]);
const tableData = ref<Array<Workspace>>();
//列表选中数据
const multipleSelection = ref<Array<Workspace>>();

onMounted(() => {
  search(new TableSearch());
});
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  listWorkspace({
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

//列表选中事件
const handleSelectionChange = (val: Workspace[]) => {
  multipleSelection.value = val;
};

//批量删除
const batchDelete = () => {
  ElMessageBox.confirm("确认删除工作空间", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    //执行删除操作
    deleteBatch(multipleSelection.value ? multipleSelection.value : [])
      .then(() => {
        search(new TableSearch());
      })
      .catch((err) => {
        ElMessage.info("删除失败", err);
      });
  });
};
//单个删除
const deleteOne = (row: Workspace) => {
  ElMessageBox.confirm("确认删除工作空间", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    //执行删除操作
    deleteWorkspaceById(row.id)
      .then(() => {
        search(new TableSearch());
      })
      .catch((err) => {
        ElMessage.info("删除失败", err);
      });
  });
};

//创建
const create = () => {
  console.log();
  useRoute.push({ path: useRoute.currentRoute.value.path + "/create" });
};

//编辑
const edit = (row: Workspace) => {
  useRoute.push({
    name: "worksapceCreate",
    params: { id: row.id },
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
    searchOptions: [{ label: "名称", value: "name" }],
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
      deleteOne,
      "Delete"
    ),
  ]),
});
</script>

<style lang="scss" scoped>
.text-overflow {
  max-width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
