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
        <el-button
          type="primary"
          @click="openCreateComplianceRuleGroup"
          v-hasPermission="'[security-compliance]RULE:CREATE'"
          >创建规则组</el-button
        ></template
      >
      <el-table-column type="selection" />
      <el-table-column
        prop="name"
        width="300px"
        label="规则组名称"
        show-overflow-tooltip
      />
      <el-table-column
        prop="description"
        label="规则描述"
        min-width="200px"
        show-overflow-tooltip
      >
        <template #default="scope">
          {{ scope.row.description }}
        </template>
      </el-table-column>
      <fu-table-operations
        fixed="right"
        v-bind="tableConfig.tableOperations"
        width="120px"
        fix
      />
      <template #buttons>
        <CeTableColumnSelect :columns="columns" />
      </template>
    </ce-table>
    <!-- 创建 -->
    <CreateCompliacneRuleGroup
      ref="create_compliacne_rule_group_ref"
      :refresh="refreshTable"
    ></CreateCompliacneRuleGroup>
    <!-- 修改 -->
    <UpdateComplianceRuleGroup
      ref="update_compliance_rule_group_ref"
      :refresh="refreshTable"
    ></UpdateComplianceRuleGroup>
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
import CreateCompliacneRuleGroup from "@/views/rule/components/CreateComplianceRuleGroup.vue";
import UpdateComplianceRuleGroup from "@/views/rule/components/UpdateComplianceRuleGroup.vue";
import { usePermissionStore } from "@commons/stores/modules/permission";
const permissionStore = usePermissionStore();
onMounted(() => {
  // 挂载查询
  search(new TableSearch());
});

const table: any = ref(null);
/**
 * 创建规则组对象
 */
const create_compliacne_rule_group_ref =
  ref<InstanceType<typeof CreateCompliacneRuleGroup>>();
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
  ref<InstanceType<typeof UpdateComplianceRuleGroup>>();
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
  table.value.search();
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
      refreshTable();
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
    searchOptions: [{ label: "规则组名称", value: "name" }],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations(
    [
      TableOperations.buildButtons().newInstance(
        "编辑",
        "primary",
        openUpdateComplianceRuleGroup,
        "EditPen",
        undefined,
        permissionStore.hasPermission("[security-compliance]RULE:EDIT")
      ),
      TableOperations.buildButtons().newInstance(
        "删除",
        "primary",
        deleteItem,
        "Delete",
        undefined,
        permissionStore.hasPermission("[security-compliance]RULE:DELETE")
      ),
    ],
    "label"
  ),
});
</script>
<style lang="scss" scoped>
:deep(.el-table__cell) {
  .cell {
    white-space: nowrap;
  }
}

:deep(.el-textarea__inner) {
  height: 100%;
}
</style>
