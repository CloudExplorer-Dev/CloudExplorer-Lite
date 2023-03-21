<template>
  <div>
    <ce-table
      localKey="complianceRuleGroupTable"
      v-loading="loading"
      height="100%"
      ref="table"
      :columns="columns"
      :data="dataList"
      :tableConfig="tableConfig"
      row-key="id"
    >
      <template #toolbar>
        <el-button type="primary" @click="openCreateComplianceRuleGroup"
          >创建</el-button
        ></template
      >
      <el-table-column type="selection" />
      <el-table-column prop="name" label="规则组名称" />

      <el-table-column prop="description" label="规则描述" />
      <fu-table-operations v-bind="tableConfig.tableOperations" fix />
      <template #buttons>
        <CeTableColumnSelect :columns="columns" />
      </template>
    </ce-table>
    <!-- 创建 -->
    <create_compliacne_rule_group
      ref="create_compliacne_rule_group_ref"
      :refresh="refreshTable"
    ></create_compliacne_rule_group>
    <!-- 修改 -->
    <update_compliance_rule_group
      ref="update_compliance_rule_group_ref"
      :refresh="refreshTable"
    ></update_compliance_rule_group>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  PaginationConfig,
  TableConfig,
  TableSearch,
  TableOperations,
} from "@commons/components/ce-table/type";
import type { ComplianceRuleGroup } from "@/api/rule_group/type";
import complianceRuleGroupApi from "@/api/rule_group";
import create_compliacne_rule_group from "@/views/rule/components/CreateComplianceRuleGroup.vue";
import update_compliance_rule_group from "@/views/rule/components/UpdateComplianceRuleGroup.vue";
onMounted(() => {
  // 挂载查询
  search(new TableSearch());
});

const table: any = ref(null);
/**
 * 创建规则组对象
 */
const create_compliacne_rule_group_ref =
  ref<InstanceType<typeof create_compliacne_rule_group>>();
/**
 * 打开创建规则组弹出框
 */
const openCreateComplianceRuleGroup = () => {
  create_compliacne_rule_group_ref.value?.open();
};
/**
 *修改规则组组件对象
 */
const update_compliance_rule_group_ref =
  ref<InstanceType<typeof update_compliance_rule_group>>();
/**
 * 打开修改规则组弹出框
 * @param row 规则组对象
 */
const openUpdateComplianceRuleGroup = (row: ComplianceRuleGroup) => {
  update_compliance_rule_group_ref.value?.echoData(row);
  update_compliance_rule_group_ref.value?.open();
};
/**
 * 刷新表格数据
 */
const refreshTable = () => {
  table.value.search(table?.value.getTableSearch());
};

/**
 * 表格数据
 */
const dataList = ref<Array<ComplianceRuleGroup>>([]);

const loading = ref<boolean>(false);
// 列表字段数据
const columns = ref([]);
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  complianceRuleGroupApi
    .page(
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

/**
 * 删除
 * @param row 需要删除的这一行
 */
const deleteItem = (row: ComplianceRuleGroup) => {
  ElMessageBox.confirm("确认删除", "提示", {
    confirmButtonText: "删除",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    complianceRuleGroupApi.deleteById(row.id).then(() => {
      ElMessage.success("删除成功");
      table.value.search(table?.value.getTableSearch());
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
    components: [],
    searchOptions: [{ label: "规则名称", value: "name" }],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([
    TableOperations.buildButtons().newInstance(
      "编辑",
      "primary",
      openUpdateComplianceRuleGroup,
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
<style lang="scss" scoped>
:deep(.el-textarea__inner) {
  height: 100%;
}
</style>
