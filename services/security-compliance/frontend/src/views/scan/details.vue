<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <layout-container>
      <template #header><h4>规则信息</h4></template>
      <template #content>
        <el-descriptions :column="2">
          <el-descriptions-item label="规则名称">{{
            complianceRule?.name
          }}</el-descriptions-item>
          <el-descriptions-item label="风险等级">{{
            complianceRule?.riskLevel === "HIGH"
              ? "高风险"
              : complianceRule?.riskLevel === "MIDDLE"
              ? "中风险"
              : "低风险"
          }}</el-descriptions-item>
          <el-descriptions-item label="规则描述">{{
            complianceRule?.description
          }}</el-descriptions-item>
          <el-descriptions-item label="规则组">{{
            complianceRuleGroup?.name
          }}</el-descriptions-item>
        </el-descriptions>
      </template>
    </layout-container>
    <layout-container>
      <template #header><h4>改进意见</h4></template>
      <template #content>
        <el-descriptions :column="1">
          <el-descriptions-item
            v-for="(item, index) in complianceInsuranceStatutes"
            :key="item.id"
            :label="index + 1 + '.'"
          >
            {{ item.improvementProposal }}</el-descriptions-item
          >
        </el-descriptions>
      </template>
    </layout-container>
    <layout-container>
      <template #header><h4>资源检测结果</h4></template>
      <template #content>
        <ce-table
          v-loading="tableLoading"
          height="100%"
          ref="table"
          :columns="columns"
          :data="dataList"
          :tableConfig="tableConfig"
          row-key="id"
        >
          <el-table-column type="expand">
            <template #default="props">
              <el-descriptions :column="2" style="padding: 0 20px">
                <el-descriptions-item
                  v-for="key in Object.keys(props.row.instance)"
                  :key="key"
                  :label="key + ':'"
                  >{{ props.row.instance[key] }}</el-descriptions-item
                >
              </el-descriptions>
            </template>
          </el-table-column>
          <el-table-column prop="resourceId" label="资源id" />
          <el-table-column prop="resourceName" label="资源名称" />
          <el-table-column prop="cloudAccountName" label="云账号名称" />
          <el-table-column prop="resourceType" label="资源类型" />
          <el-table-column
            column-key="complianceStatus"
            prop="complianceStatus"
            label="检测状态"
            :filters="[
              { text: '合规', value: 'COMPLIANCE' },
              { text: '不合规', value: 'NOT_COMPLIANCE' },
            ]"
          >
            <template #default="scope">
              <span
                :style="{
                  color:
                    scope.row.complianceStatus === 'COMPLIANCE'
                      ? '#70B603'
                      : '#D9001B',
                }"
                >{{
                  scope.row.complianceStatus === "COMPLIANCE"
                    ? "合规"
                    : "不合规"
                }}</span
              >
            </template>
          </el-table-column>

          <template #buttons>
            <fu-table-column-select
              type="icon"
              :columns="columns"
              size="small"
            />
          </template>
        </ce-table>
      </template>
    </layout-container>
  </layout-content>
</template>
<script setup lang="ts">
import { useRoute } from "vue-router";
import { onMounted, ref } from "vue";
import complianceRuleApi from "@/api/rule";
import complianceRuleGroupApi from "@/api/rule_group";
import complianceScanApi from "@/api/compliance_scan";
import type { ComplianceRule } from "@/api/rule/type";
import type { ComplianceRuleGroup } from "@/api/rule_group/type";
import type { ComplianceResourceResponse } from "@/api/compliance_scan/type";
import complianceInsuranceStatuteApi from "@/api/compliance_insurance_statute";
import type { ComplianceInsuranceStatute } from "@/api/compliance_insurance_statute/type";
import {
  PaginationConfig,
  TableConfig,
  TableSearch,
} from "@commons/components/ce-table/type";
const route = useRoute();
const complianceRule = ref<ComplianceRule>();
const complianceRuleGroup = ref<ComplianceRuleGroup>();
const tableLoading = ref<boolean>(false);
const complianceInsuranceStatutes = ref<Array<ComplianceInsuranceStatute>>([]);
/**
 * 表格数据
 */
const dataList = ref<Array<ComplianceResourceResponse>>([]);
// 列表字段数据
const columns = ref([]);
const table: any = ref(null);
onMounted(() => {
  complianceRuleApi
    .getComplianceRuleById(route.params.compliance_rule_id as string)
    .then((ok) => {
      complianceRule.value = ok.data;
      search(new TableSearch());
      return ok.data;
    })
    .then((data) => {
      complianceRuleGroupApi
        .getComplianceRuleGroupById(data.ruleGroupId)
        .then((ok) => {
          complianceRuleGroup.value = ok.data;
        });
    });
  complianceInsuranceStatuteApi
    .list({
      complianceRuleId: route.params.compliance_rule_id as string,
    })
    .then((ok) => {
      complianceInsuranceStatutes.value = ok.data;
    });
});

const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  complianceScanApi
    .pageResource(
      tableConfig.value.paginationConfig.currentPage,
      tableConfig.value.paginationConfig.pageSize,
      route.params.compliance_rule_id as string,
      {
        ...params,
        cloudAccountId: route.params.cloud_account_id,
        resourceType: complianceRule.value?.resourceType,
      },
      tableLoading
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
 * 表单配置
 */
const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    // 查询函数
    search: search,
    quickPlaceholder: "搜索",
    components: [],
    searchOptions: [{ label: "资源名称", value: "resourceName" }],
  },
  paginationConfig: new PaginationConfig(),
});
</script>
<style lang="scss"></style>
