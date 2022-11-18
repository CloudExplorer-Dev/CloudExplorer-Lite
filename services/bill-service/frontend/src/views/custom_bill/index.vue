<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <ce-table
      v-loading="loading"
      height="100%"
      ref="table"
      :columns="columns"
      :data="dataList"
      :tableConfig="tableConfig"
      row-key="id"
    >
      <template #toolbar>
        <el-button type="primary" @click="addBillRule">新增</el-button>
      </template>
      <el-table-column type="selection" />
      <el-table-column prop="name" label="规则名称"> </el-table-column>
      <el-table-column prop="name" label="分组维度">
        <template #default="scope">
          <span>{{
            scope.row.groups && scope.row.groups.length > 0
              ? scope.row.groups.map((g: Group) => g.name).join(",")
              : "N/A"
          }}</span>
        </template>
      </el-table-column>
      <fu-table-operations v-bind="tableConfig.tableOperations" fix />
      <template #buttons>
        <fu-table-column-select type="icon" :columns="columns" size="small" />
      </template>
    </ce-table>
  </layout-content>
  <el-dialog
    v-model="billRuleDialogVisible"
    :title="billRuleFormType === 'ADD' ? '添加规则' : '编辑规则'"
    width="60%"
  >
    <el-form :model="billRuleForm" label-width="120px">
      <el-form-item label="规则名称">
        <el-input v-model="billRuleForm.name" />
      </el-form-item>
      <el-form-item label="规则名称">
        <billRuleGroup></billRuleGroup>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="billRuleDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="billRuleDialogVisible = false">
          保存
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import { ref, onMounted } from "vue";
import {
  PaginationConfig,
  TableConfig,
  TableSearch,
  TableOperations,
} from "@commons/components/ce-table/type";
import billRuleApi from "@/api/bill_rule";
import type { BillRule, Filter, Group } from "@/api/bill_rule/type";
import billRuleGroup from "@/components/bill_rule_group/index.vue";

/**
 *加载器
 */
const loading = ref<boolean>(false);

/**
 * 数据列表
 */
const dataList = ref<Array<BillRule>>([]);

/**
 * 表格对象
 */
const table = ref<any>();

/**
 * 账单对话框是否展示
 */
const billRuleDialogVisible = ref<boolean>(false);

/**
 * 账单规则对话框类型
 */
const billRuleFormType = ref<"ADD" | "EDIT">("ADD");

/**
 * 账单规则表单
 */
const billRuleForm = ref<any>({});

/**
 * 查询函数
 * @param condition 查询条件
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  billRuleApi
    .pageBillRules(
      tableConfig.value.paginationConfig.currentPage,
      tableConfig.value.paginationConfig.pageSize,
      params,
      loading
    )
    .then((ok) => {
      dataList.value = ok.data.records;
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

// 列表字段数据
const columns = ref([]);
// 编辑
const editBillRule = () => {
  billRuleDialogVisible.value = true;
  billRuleFormType.value = "EDIT";
};

// 删除
const deleteBillRule = () => {
  console.log("删除账单规则");
};
const addBillRule = () => {
  billRuleDialogVisible.value = true;
  billRuleFormType.value = "ADD";
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
    components: [],
    searchOptions: [{ label: "规则名称", value: "name" }],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([
    TableOperations.buildButtons().newInstance(
      "编辑",
      "primary",
      editBillRule,
      "EditPen"
    ),
    TableOperations.buildButtons().newInstance(
      "删除",
      "primary",
      deleteBillRule,
      "Delete"
    ),
  ]),
});

onMounted(() => {
  /**
   * 组件挂载查询数据
   */
  search(table.value?.getTableSearch());
});
</script>
<style lang="scss"></style>
