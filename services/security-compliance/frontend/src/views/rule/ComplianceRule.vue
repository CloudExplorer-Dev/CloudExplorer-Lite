<template>
  <div>
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
        <el-button type="primary" @click="openCreateComplianceRule"
          >创建</el-button
        ></template
      >
      <el-table-column type="selection" />
      <el-table-column prop="name" label="规则名称" />
      <el-table-column prop="ruleGroupName" label="规则组" />
      <el-table-column
        prop="platform"
        label="云平台"
        :column-key="'platform'"
        :filters="
          Object.keys(platformIcon).map((key) => ({
            text: platformIcon[key].name,
            value: key,
          }))
        "
        :filter-multiple="false"
      >
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <el-image
              style="margin-right: 20%; display: flex"
              :src="platformIcon[scope.row.platform].oldIcon"
            ></el-image>
            <span>{{ platformIcon[scope.row.platform].name }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="resourceType"
        :column-key="'resourceType'"
        label="资源类型"
        :filters="
          resourceTypeList.map((resource) => ({
            text: resource.key,
            value: resource.value,
          }))
        "
        :filter-multiple="false"
      >
        <template #default="scope">
          {{
            resourceTypeList.find((r) => r.value === scope.row.resourceType)
              ?.key
          }}
        </template>
      </el-table-column>
      <el-table-column prop="rules" label="合规判断条件" width="210px">
        <template #default="scope">
          <compliance_rule_view
            :platform="scope.row.platform"
            :resource-type="scope.row.resourceType"
            :rules="scope.row.rules"
          ></compliance_rule_view>
        </template>
      </el-table-column>
      <el-table-column
        prop="riskLevel"
        label="风险等级"
        :column-key="'riskLevel'"
        :filters="
          riskLevelOptionList.map((level) => ({
            text: level.key,
            value: level.value,
          }))
        "
        :filter-multiple="false"
      >
        <template #default="scope">
          <div
            v-html="riskLevelOptionList.find((r: KeyValue<string, string>) => r.value ===
              scope.row.riskLevel) ? riskLevelOptionList.find((r: KeyValue<string, string>) => r.value ===
                scope.row.riskLevel)?.key : 'N/A'"
          ></div>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="规则描述"> </el-table-column>
      <el-table-column
        prop="enable"
        column-key="enable"
        label="是否启用"
        :filters="[
          { text: '启用', value: true },
          { text: '禁用', value: false },
        ]"
        :filter-multiple="false"
      >
        <template #default="scope">
          <compliance_rule_switch
            :compliance-rule-id="scope.row.id"
            v-model="scope.row.enable"
          ></compliance_rule_switch>
        </template>
      </el-table-column>
      <fu-table-operations v-bind="tableConfig.tableOperations" fix />
      <template #buttons>
        <fu-table-column-select type="icon" :columns="columns" size="small" />
      </template>
    </ce-table>
    <create_compliance_rule
      ref="create_compliance_rule_ref"
      :refresh="refreshTable"
      :compliance-rule-group-list="complianceRuleGroupList"
      :risk-level-option-list="riskLevelOptionList"
      :resource-type-option-list="resourceTypeList"
    ></create_compliance_rule>
    <update_compliance_rule
      ref="update_compliance_rule_ref"
      :refresh="refreshTable"
      :compliance-rule-group-list="complianceRuleGroupList"
      :risk-level-option-list="riskLevelOptionList"
      :resource-type-option-list="resourceTypeList"
    ></update_compliance_rule>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { KeyValue } from "@commons/api/base/type";
import {
  PaginationConfig,
  TableConfig,
  TableSearch,
  TableOperations,
} from "@commons/components/ce-table/type";
import type { ComplianceRule } from "@/api/rule/type";
import complianceRuleApi from "@/api/rule";
import complianceRuleGroupApi from "@/api/rule_group";
import type { ComplianceRuleGroup } from "@/api/rule_group/type";
import { platformIcon } from "@commons/utils/platform";
import { ElMessage, ElMessageBox } from "element-plus";
import compliance_rule_view from "@/views/rule/components/compliance_rules/ComplianceRuleView.vue";
import create_compliance_rule from "@/views/rule/components/CreateComplianceRule.vue";
import update_compliance_rule from "@/views/rule/components/UpdateComplianceRule.vue";
import compliance_rule_switch from "@/views/rule/components/ComplianceRuleSwitch.vue";

/**
 * 表格数据
 */
const dataList = ref<Array<ComplianceRule>>([]);
/**
 * 加载器
 */
const loading = ref<boolean>(false);

onMounted(() => {
  // 挂载查询
  search(new TableSearch());
});
// 列表字段数据
const columns = ref([]);

// 合规规则组
const complianceRuleGroupList = ref<Array<ComplianceRuleGroup>>([]);
//资源类型
const resourceTypeList = ref<Array<KeyValue<string, string>>>([]);
// 创建规则对象
const create_compliance_rule_ref =
  ref<InstanceType<typeof create_compliance_rule>>();
/**
 * 打开创建合规规则弹框
 */
const openCreateComplianceRule = () => {
  create_compliance_rule_ref.value?.open();
};
// 编辑合规规则对象
const update_compliance_rule_ref =
  ref<InstanceType<typeof update_compliance_rule>>();

/**
 * 打开编辑合规规则弹框
 * @param row 需要编辑的这一行数据
 */
const openUpdateComplianceRule = (row: ComplianceRule) => {
  update_compliance_rule_ref.value?.open();
  update_compliance_rule_ref.value?.echoData(row);
};
// 表格对象
const table = ref();

/**
 * 刷新表格数据
 */
const refreshTable = () => {
  table.value.search(table?.value.getTableSearch());
};

/**
 * 风险等级OptionList
 */
const riskLevelOptionList: Array<KeyValue<string, string>> = [
  { key: "高风险", value: "HIGH" },
  { key: "中风险", value: "MIDDLE" },
  { key: "低风险", value: "LOW" },
];

// 表单结束

/**
 * 搜索函数
 * @param condition 搜索条件
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  complianceRuleApi
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
const deleteItem = (row: ComplianceRule) => {
  ElMessageBox.confirm("确认删除", "提示", {
    confirmButtonText: "删除",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    complianceRuleApi.deleteComplianceRule(row.id).then(() => {
      table.value.search(table?.value.getTableSearch());
      ElMessage.success("删除成功");
    });
  });
};

/**
 * 表格配置
 */
const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    // 查询函数
    search: search,
    quickPlaceholder: "搜索",
    components: [],
    searchOptions: [
      { label: "规则名称", value: "name" },
      { label: "规则组名称", value: "ruleGroupName" },
      { label: "规则描述", value: "description" },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([
    TableOperations.buildButtons().newInstance(
      "编辑",
      "primary",
      openUpdateComplianceRule,
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
/**
 * 刷新列表数据
 */
const refresh = () => {
  complianceRuleGroupApi.list().then((ok) => {
    complianceRuleGroupList.value = ok.data;
  });
  complianceRuleApi.listResourceType().then((ok) => {
    resourceTypeList.value = ok.data;
  });
  refreshTable();
};
defineExpose({ refresh });
</script>
<style lang="scss"></style>
