<template>
  <div>
    <ce-table
      localKey="complianceRuleTable"
      v-loading="loading"
      height="100%"
      ref="table"
      :columns="columns"
      :data="dataList"
      :tableConfig="tableConfig"
      cell-class-name="table_cell"
      row-key="id"
    >
      <template #toolbar>
        <el-button
          type="primary"
          @click="openCreateComplianceRule"
          v-hasPermission="'[security-compliance]RULE:CREATE'"
          >创建规则</el-button
        ></template
      >
      <el-table-column
        prop="name"
        show-overflow-tooltip
        label="规则名称"
        min-width="200"
      />
      <el-table-column
        prop="ruleGroupName"
        show-overflow-tooltip
        width="120"
        label="规则组"
      />
      <el-table-column
        prop="platform"
        label="云平台"
        width="150"
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
            <PlatformIcon :platform="scope.row.platform" />
            <span style="margin-left: 8px; width: calc(100% - 30px)">{{
              platformIcon[scope.row.platform].name
            }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="resourceType"
        :column-key="'resourceType'"
        label="资源类型"
        show-overflow-tooltip
        min-width="130"
        :filters="resourceTypeFilterList"
        :filter-multiple="false"
      >
        <template #default="scope">
          {{ getResourceTypeLebel(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column prop="rules" label="合规判断条件" min-width="150">
        <template #default="scope">
          <ComplianceRuleView
            :platform="scope.row.platform"
            :resource-type="scope.row.resourceType"
            :rules="scope.row.rules"
          ></ComplianceRuleView>
        </template>
      </el-table-column>
      <el-table-column prop="rules" show-overflow-tooltip label="规则类型">
        <template #default="scope">
          {{
            scope.row.rules.scanRule === "COMPLIANCE"
              ? "视为合规"
              : "视为不合规"
          }}
        </template>
      </el-table-column>
      <el-table-column
        prop="riskLevel"
        label="风险等级"
        width="100"
        :column-key="'riskLevel'"
        :filters="riskLevelFilterList"
        :filter-multiple="false"
      >
        <template #default="scope">
          <div class="risk_level">
            <el-tag
              disable-transitions
              :class="
                scope.row.riskLevel === 'HIGH'
                  ? 'high'
                  : scope.row.riskLevel === 'MIDDLE'
                  ? 'middle'
                  : 'low'
              "
            >
              {{
                scope.row.riskLevel === "HIGH"
                  ? "高风险"
                  : scope.row.riskLevel === "MIDDLE"
                  ? "中风险"
                  : "低风险"
              }}
            </el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="description"
        show-overflow-tooltip
        label="规则描述"
        min-width="200px"
      >
        <template #default="scope">
          {{ scope.row.description }}
        </template>
      </el-table-column>

      <el-table-column label="操作" fixed="right" width="100px">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <ComplianceRuleSwitch
              :compliance-rule-id="scope.row.id"
              v-model="scope.row.enable"
            ></ComplianceRuleSwitch>
            <MoreOptionsButton
              style="margin-left: 25px"
              :row="scope.row"
              :buttons="
                tableConfig.tableOperations
                  ? tableConfig.tableOperations.buttons
                  : []
              "
            ></MoreOptionsButton>
          </div>
        </template>
      </el-table-column>

      <template #buttons>
        <CeTableColumnSelect :columns="columns" />
      </template>
    </ce-table>
    <CreateComplianceRule
      ref="create_compliance_rule_ref"
      :refresh="refreshTable"
      :compliance-rule-group-list="complianceRuleGroupList"
      :risk-level-option-list="riskLevelOptionList"
      :resource-type-option-list="resourceTypeList"
    ></CreateComplianceRule>
    <UpdateComplianceRule
      ref="update_compliance_rule_ref"
      :refresh="refreshTable"
      :compliance-rule-group-list="complianceRuleGroupList"
      :risk-level-option-list="riskLevelOptionList"
      :resource-type-option-list="resourceTypeList"
    ></UpdateComplianceRule>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import PlatformIcon from "@commons/components/platform-icon/index.vue";
import type { KeyValue } from "@commons/api/base/type";
import {
  PaginationConfig,
  TableConfig,
  TableSearch,
  TableOperations,
} from "@commons/components/ce-table/type";
import MoreOptionsButton from "@commons/components/ce-table/MoreOptionsButton.vue";
import type { ComplianceRule } from "@/api/rule/type";
import complianceRuleApi from "@/api/rule";
import complianceRuleGroupApi from "@/api/rule_group";
import type { ComplianceRuleGroup } from "@/api/rule_group/type";
import { platformIcon } from "@commons/utils/platform";
import { ElMessage, ElMessageBox } from "element-plus";
import ComplianceRuleView from "@/views/rule/components/compliance_rule_view/index.vue";
import CreateComplianceRule from "@/views/rule/components/CreateComplianceRule.vue";
import UpdateComplianceRule from "@/views/rule/components/UpdateComplianceRule.vue";
import ComplianceRuleSwitch from "@/views/rule/components/ComplianceRuleSwitch.vue";
import { usePermissionStore } from "@commons/stores/modules/permission";

const permissionStore = usePermissionStore();
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
  ref<InstanceType<typeof CreateComplianceRule>>();
/**
 * 打开创建合规规则弹框
 */
const openCreateComplianceRule = () => {
  create_compliance_rule_ref.value?.open();
};
// 编辑合规规则对象
const update_compliance_rule_ref =
  ref<InstanceType<typeof UpdateComplianceRule>>();

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
  table.value.search();
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
 * 风险等级过滤下拉数据
 */
const riskLevelFilterList = computed(() => {
  return riskLevelOptionList.map((level: KeyValue<string, string>) => ({
    text: level.key,
    value: level.value,
  }));
});
// 资源类型过滤下拉数据
const resourceTypeFilterList = computed(() => {
  return resourceTypeList.value.map((resource: KeyValue<string, string>) => ({
    text: resource.key,
    value: resource.value,
  }));
});
/**
 * 获取资源类型Label
 * @param row 当前这一行
 */
const getResourceTypeLebel = (row: ComplianceRule) => {
  return (
    resourceTypeList.value.find(
      (r: KeyValue<string, string>) => r.value === row.resourceType
    )?.key || "-"
  );
};
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
      refresh();
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
<style lang="scss" scoped>
:deep(.table_cell) {
  .cell {
    white-space: nowrap;
  }
}
.risk_level {
  .high {
    background: rgba(245, 74, 69, 0.2);
    border-radius: 2px;
    color: rgba(216, 57, 49, 1);
    border: none;
  }
  .middle {
    background: rgba(255, 136, 0, 0.2);
    border-radius: 2px;
    color: rgba(222, 120, 2, 1);
    border: none;
  }
  .low {
    background: rgba(52, 199, 36, 0.2);
    border-radius: 2px;
    color: rgba(46, 161, 33, 1);
    border: none;
  }
}
</style>
