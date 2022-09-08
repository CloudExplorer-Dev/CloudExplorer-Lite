<script setup lang="ts">
import { platformIcon } from "@/utils/platform";
import { ref, onMounted } from "vue";
import {
  PaginationConfig,
  SearchConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import cloudAccountApi from "@/api/cloud_account/index";
import type { CloudAccount } from "@/api/cloud_account/type";
import { useRouter } from "vue-router";
const router = useRouter();
const clouAccountList = ref<Array<CloudAccount>>();
const tableSearch = ref<TableSearch>();

// 获得云平台过滤数据
const platformFilters = Object.keys(platformIcon).map((platform: string) => {
  return { text: platformIcon[platform].name, value: platform };
});
const edit = () => {
  router.push({ name: "cloud_account_update" });
  console.log("修改");
};
const deleteItem = () => {
  console.log("删除");
};

const check = () => {
  console.log("校验");
};
const sync = () => {
  console.log("同步");
};
const create = () => {
  router.push({ name: "cloud_account_create" });
  console.log("create");
};
const handleSelectionChange = () => {
  console.log("handleSelectionChange");
};
const batchDelete = () => {
  console.log("batchDelete");
};

const columns = ref([]);

const search = (condition: TableSearch) => {
  tableSearch.value = condition;
  const params = TableSearch.toSearchParams(condition);
  cloudAccountApi
    .page({
      currentPage: tableConfig.value.paginationConfig.currentPage,
      pageSize: tableConfig.value.paginationConfig.pageSize,
      ...params,
    })
    .then((ok) => {
      clouAccountList.value = ok.data.records;
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

onMounted(() => {
  search(new TableSearch());
});
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
      "校验",
      "primary",
      check,
      "Search"
    ),
    TableOperations.buildButtons().newInstance(
      "同步",
      "primary",
      sync,
      "Refresh"
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
    :columns="columns"
    :data="clouAccountList"
    :tableConfig="tableConfig"
    @selection-change="handleSelectionChange"
    row-key="id"
  >
    <template #toolbar>
      <el-button type="primary" @click="create">创建</el-button>
      <el-button @click="batchDelete">删除</el-button>
    </template>
    <el-table-column type="selection" />
    <el-table-column prop="name" label="名称" sortable />
    <el-table-column
      column-key="platform"
      :filters="platformFilters"
      prop="platform"
      label="云平台"
      sortable
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span>{{ platformIcon[scope.row.platform].name }}</span>
          <el-image
            style="margin-left: 20px; display: flex"
            :src="platformIcon[scope.row.platform].icon"
          ></el-image>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="state"
      label="云账号状态"
      column-key="state"
      sortable
      :filters="[
        { text: '有效', value: true },
        { text: '无效', value: false },
      ]"
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span>{{ scope.row.state ? "有效" : "无效" }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      column-key="status"
      prop="status"
      label="同步状态"
      :filters="[
        { text: '同步成功', value: 0 },
        { text: '同步失败', value: 1 },
        { text: '同步中', value: 2 },
      ]"
      sortable
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span :style="{ color: scope.row.status === 1 ? 'red' : '' }">{{
            scope.row.status === 0
              ? "同步成功"
              : scope.row.status === 1
              ? "同步失败"
              : "同步中"
          }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column prop="updateTime" label="最近同步时间" sortable />
    <el-table-column prop="createTime" label="创建时间" sortable />
    <fu-table-operations v-bind="tableConfig.tableOperations" fix />
    <template #buttons>
      <fu-table-column-select type="icon" :columns="columns" size="small" />
    </template>
  </ce-table>
</template>

<style lang="scss"></style>
