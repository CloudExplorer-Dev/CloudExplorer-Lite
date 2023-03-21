<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb
        :breadcrumbs="[
          {
            to: {
              name: 'scan',
              path: '/scan',
            },
            title: '扫描检测',
          },
          {
            to: {
              name: 'details',
              path: '/scan/details/:compliance_rule_id/:cloud_account_id',
            },
            title: '详情',
          },
        ]"
        :auto="false"
      ></breadcrumb>
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
    <layout-container v-if="complianceInsuranceStatutes.length > 0">
      <template #header><h4>改进建议</h4></template>
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
          localKey="detailsTable"
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
          <el-table-column
            prop="cloudAccountName"
            label="云账号名称"
            :filters="
              cloudAccountList.map((item) => ({
                text: item.name,
                value: item.id,
              }))
            "
            :filter-multiple="false"
            column-key="cloudAccountId"
          >
            <template #default="scope">
              <div style="display: flex">
                <platform_icon :platform="scope.row.platform"> </platform_icon>
                {{ scope.row.cloudAccountName }}
              </div>
            </template></el-table-column
          >
          <el-table-column prop="resourceType" label="资源类型" />
          <el-table-column
            column-key="complianceStatus"
            prop="complianceStatus"
            label="检测状态"
            :filter-multiple="false"
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
            <CeTableColumnSelect :columns="columns" />
          </template>
        </ce-table>
      </template>
    </layout-container>
  </layout-content>
</template>
<script setup lang="ts">
import { useRoute } from "vue-router";
import { onMounted, ref, watch } from "vue";
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
import cloudAccountApi from "@commons/api/cloud_account";
import platform_icon from "@commons/components/platform-icon/index.vue";
import type { CloudAccount } from "@commons/api/cloud_account/type";
// 路由对象
const route = useRoute();
// 合规规则数据
const complianceRule = ref<ComplianceRule>();
// 合规规则组数据
const complianceRuleGroup = ref<ComplianceRuleGroup>();
// 表格加载器
const tableLoading = ref<boolean>(false);
// 云账号列表数据
const cloudAccountList = ref<Array<CloudAccount>>([]);
// 等保条例数据
const complianceInsuranceStatutes = ref<Array<ComplianceInsuranceStatute>>([]);
/**
 * 表格数据
 */
const dataList = ref<Array<ComplianceResourceResponse>>([]);
// 列表字段数据
const columns = ref([]);
// 表格实例对象
const table: any = ref(null);

onMounted(() => {
  // 查询合规规则数据
  complianceRuleApi
    .getComplianceRuleById(route.params.compliance_rule_id as string)
    .then((ok) => {
      complianceRule.value = ok.data;
      // 查询云账号数据
      cloudAccountApi.listAll().then((a) => {
        cloudAccountList.value = a.data.filter(
          (p) => p.platform === ok.data.platform
        );
      });

      return ok.data;
    })
    .then((data) => {
      // 查询合规规则组数据
      complianceRuleGroupApi
        .getComplianceRuleGroupById(data.ruleGroupId)
        .then((ok) => {
          complianceRuleGroup.value = ok.data;
          return ok.data;
        });
      // 查询列表
      search(new TableSearch());
    });
  // 查询等保条例数据
  complianceInsuranceStatuteApi
    .list({
      complianceRuleId: route.params.compliance_rule_id as string,
    })
    .then((ok) => {
      complianceInsuranceStatutes.value = ok.data;
    });
});

/**
 * 查询函数
 * @param condition table收集的查询数据
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  complianceScanApi
    .pageResource(
      tableConfig.value.paginationConfig.currentPage,
      tableConfig.value.paginationConfig.pageSize,
      route.params.compliance_rule_id as string,
      {
        ...params,
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
