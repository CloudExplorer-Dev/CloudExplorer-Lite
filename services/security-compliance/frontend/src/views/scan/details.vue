<template>
  <div>
    <layout-auto-height-content>
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
      <base-container style="height: auto" :contentBorder="false">
        <template #header><span>规则信息</span></template>
        <template #content>
          <el-form :inline="true" label-position="top" v-loading="infoLoading">
            <el-row style="width: 100%">
              <el-col :span="19">
                <el-form-item style="width: 100%" label="规则名称">{{
                  complianceRule?.name
                }}</el-form-item></el-col
              >
              <el-col :span="5">
                <el-form-item style="width: 100%" label="风险等级">
                  <el-tag
                    disable-transitions
                    :class="
                      complianceRule?.riskLevel === 'HIGH'
                        ? 'high'
                        : complianceRule?.riskLevel === 'MIDDLE'
                        ? 'middle'
                        : 'low'
                    "
                  >
                    {{
                      complianceRule?.riskLevel === "HIGH"
                        ? "高风险"
                        : complianceRule?.riskLevel === "MIDDLE"
                        ? "中风险"
                        : "低风险"
                    }}</el-tag
                  >
                </el-form-item></el-col
              >
            </el-row>
            <el-row style="width: 100%">
              <el-col :span="19">
                <el-form-item style="width: 100%" label="规则描述">{{
                  complianceRule?.description
                }}</el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item style="width: 100%" label="规则组">{{
                  complianceRuleGroup?.name
                }}</el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </template>
      </base-container>
      <base-container
        style="height: auto"
        v-if="complianceInsuranceStatutes.length > 0"
        :contentBorder="false"
      >
        <template #header><span>改进建议</span></template>
        <template #content>
          <el-descriptions :column="1">
            <el-descriptions-item
              v-for="(item, index) in complianceInsuranceStatutes"
              :key="item.id"
              :label="index + 1 + '.'"
            >
              <span style="color: rgb(31, 35, 41)">
                {{ item.improvementProposal }}</span
              ></el-descriptions-item
            >
          </el-descriptions>
        </template>
      </base-container>
      <base-container style="height: auto" :contentBorder="false">
        <template #header><span>资源检测结果</span></template>
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
              :filtered-value="filterCloudAccountId"
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
                <div style="display: flex; align-items: center">
                  <PlatformIcon :platform="scope.row.platform" />
                  &nbsp;&nbsp;{{ scope.row.cloudAccountName }}
                </div>
              </template></el-table-column
            >
            <el-table-column
              prop="resourceType"
              min-width="100"
              label="资源类型"
            >
              <template #default="scope"
                >{{
                  resourceTypes.find(
                    (resourceType) =>
                      resourceType.value === scope.row.resourceType
                  )?.key
                }}
              </template>
            </el-table-column>
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
                <div class="compliance_status">
                  <div
                    class="icon"
                    :style="{
                      backgroundColor:
                        scope.row.complianceStatus === 'NOT_COMPLIANCE'
                          ? '#F54A45'
                          : '#34C724',
                    }"
                  ></div>
                  <span class="text">
                    {{
                      scope.row.complianceStatus === "NOT_COMPLIANCE"
                        ? "不合规"
                        : "合规"
                    }}</span
                  >
                </div>
              </template>
            </el-table-column>
            <template #buttons>
              <CeTableColumnSelect :columns="columns" />
            </template>
          </ce-table>
        </template>
      </base-container>
    </layout-auto-height-content>
  </div>
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
import ruleApi from "@/api/rule";
import {
  PaginationConfig,
  TableConfig,
  TableSearch,
} from "@commons/components/ce-table/type";
import cloudAccountApi from "@commons/api/cloud_account";
import PlatformIcon from "@commons/components/platform-icon/index.vue";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import type { KeyValue } from "@commons/api/base/type";
// 路由对象
const route = useRoute();
// 合规规则数据
const complianceRule = ref<ComplianceRule>();
// 合规规则组数据
const complianceRuleGroup = ref<ComplianceRuleGroup>();
// 表格加载器
const tableLoading = ref<boolean>(false);
// 基本信息加载器
const infoLoading = ref<boolean>(false);
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
// 资源类型列表数据
const resourceTypes = ref<Array<KeyValue<string, string>>>([]);
// 过滤的值
const filterCloudAccountId = ref<Array<string>>([]);
const getCloudAccountParams = () => {
  if (filterCloudAccountId.value.length > 0) {
    const t = cloudAccountList.value.find(
      (cloudAccount) => cloudAccount.id === filterCloudAccountId.value[0]
    );
    if (t) {
      return {
        cloudAccountId: {
          field: "cloudAccountId",
          label: "云账号名称",
          value: t.id,
          valueLabel: t.name,
        },
      };
    }
  }
  return undefined;
};
onMounted(() => {
  if (
    route.params.cloud_account_id &&
    route.params.cloud_account_id !== "all"
  ) {
    filterCloudAccountId.value = [route.params.cloud_account_id as string];
  }

  // 查询所有资源类型
  ruleApi.listResourceType().then((ok) => {
    resourceTypes.value = ok.data;
  });
  // 查询合规规则数据
  complianceRuleApi
    .getComplianceRuleById(
      route.params.compliance_rule_id as string,
      infoLoading
    )
    .then((ok) => {
      complianceRule.value = ok.data;
      return ok.data;
    })
    .then((data) => {
      // 查询云账号数据
      cloudAccountApi.listAll().then((a) => {
        cloudAccountList.value = a.data.filter(
          (p) => p.platform === data.platform
        );
        table.value?.getTableSearch(getCloudAccountParams());
        console.log("xxx", getCloudAccountParams());
        // 查询列表
        table.value?.search();
      });
      // 查询合规规则组数据
      complianceRuleGroupApi
        .getComplianceRuleGroupById(data.ruleGroupId)
        .then((ok) => {
          complianceRuleGroup.value = ok.data;
          return ok.data;
        });
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
<style lang="scss" scoped>
:deep(tbody) {
  .el-table__cell {
    .cell {
      color: rgb(31, 35, 41);
    }
  }
}
.compliance_status {
  display: flex;
  align-items: center;
  .icon {
    height: 6px;
    width: 6px;
    border-radius: 50%;
  }
  .text {
    margin-left: 8px;
    color: rgba(31, 35, 41, 1);
    font-weight: 400;
    font-size: 14px;
    height: 22px;
  }
}
</style>
