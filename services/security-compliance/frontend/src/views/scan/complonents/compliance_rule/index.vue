<template>
  <div class="table_content">
    <ce-table
      localKey="complianceScanRuleTable"
      v-loading="loading"
      ref="table"
      :columns="columns"
      :data="tableData"
      :tableConfig="tableConfig"
      row-key="id"
    >
      <template #toolbar>
        <div class="toolbar_content">
          <div class="title">
            {{ complianceRuleGroup ? complianceRuleGroup.name : "全部规则" }}
          </div>
          <div style="flex: auto"></div>
          <div class="risk_level">
            <ce-radio
              :dataList="riskLevelOptions"
              v-model:activeValue="activeRiskLevel"
            ></ce-radio>
          </div>
        </div>
      </template>
      <el-table-column
        prop="name"
        label="规则名称"
        show-overflow-tooltip
        min-width="250"
      >
        <template #default="scope">
          <div
            class="table_content_ellipsis"
            style="cursor: pointer; color: rgba(51, 112, 255, 1)"
            @click="openUpdateRule(scope.row)"
          >
            {{ scope.row.name }}
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="resourceTyppe"
        width="120"
        label="资源类型"
        show-overflow-tooltip
        :filtered-value="resourceTypeFilters"
        :column-key="'resourceType'"
        :filter-multiple="false"
        :filters="
          resourceTypes.map((item) => ({
            text: item.key,
            value: item.value,
          }))
        "
      >
        <template #default="scope"
          >{{
            resourceTypes.find(
              (resourceType) => resourceType.value === scope.row.resourceType
            )?.key
          }}
        </template>
      </el-table-column>
      <el-table-column
        prop="platform"
        label="云平台"
        width="120"
        :column-key="'platform'"
        :filters="
          platformStore.platforms.map((platform:Platform) => ({
            text: platform.label,
            value: platform.field,
          }))
        "
        :filter-multiple="false"
      >
        <template #default="scope">
          <div style="display: flex">
            <PlatformIcon
              :platform="scope.row.platform"
              style="margin-right: 8px"
            />

            <span>{{
              platformStore.platforms.find(
                (platform: Platform) => platform.field === scope.row.platform
              ).label
            }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="riskLevel" label="风险等级" width="100">
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
      <el-table-column label="扫描状态" width="100">
        <template #default="scope">
          <div style="cursor: pointer">
            <ScanStatusColumn
              :account-job-record-list="accountJobRecordList"
              :complaince-scan-result="scope.row"
              :cloud-account-source-list="cloudAccountList"
              :open-details-job-view="openDetailsJobView"
              :cloud-account-id="cloudAccountId"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="scanStatus"
        column-key="scanStatus"
        label="检测状态"
        width="100px"
        :filter-multiple="false"
        :filters="scanStatusList"
        :filtered-value="scanStatus ? [scanStatus] : []"
      >
        <template #default="scope">
          <div class="compliance_status">
            <div
              class="icon"
              :style="{
                backgroundColor:
                  scope.row.scanStatus === 'NOT_COMPLIANCE'
                    ? '#F54A45'
                    : '#34C724',
              }"
            ></div>
            <span class="text">
              {{
                scope.row.scanStatus === "NOT_COMPLIANCE" ? "不合规" : "合规"
              }}</span
            >
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="notComplianceCount"
        label="不合规资源"
        width="120px"
        sortable
      >
        <template #default="scope">
          <div
            @click="details(scope.row)"
            style="display: flex; cursor: pointer; justify-content: flex-end"
          >
            <div style="color: #1f2329">
              {{ scope.row.notComplianceCount }}
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="最后扫描时间"
        width="170px"
        sortable
      />
      <el-table-column label="操作栏" fixed="right" width="100px">
        <template #default="scope">
          <span class="row_scan_result" @click="details(scope.row)">
            扫描结果
          </span>
        </template>
      </el-table-column>
      <template #buttons>
        <CeTableColumnSelect :columns="columns" />
      </template>
    </ce-table>
    <UpdateComplianceRule
      ref="update_compliance_rule_ref"
      :refresh="() => {}"
      :compliance-rule-group-list="complianceRuleGroupList"
      :risk-level-option-list="riskLevelOptionList"
      :resource-type-option-list="resourceTypes"
    ></UpdateComplianceRule>
    <JobDetailsView ref="jobDetailsRef" :account-job-record="currentJobRow" />
  </div>
</template>
<script setup lang="ts">
import ruleApi from "@/api/rule";
import ScanStatusColumn from "./ScanStatusColumn.vue";
import JobDetailsView from "@/views/scan/complonents/compliance_rule/JobDetailsView.vue";
import type { KeyValue } from "@commons/api/base/type";
import type { ComplianceScanResultResponse } from "@/api/compliance_scan_result/type";
import complianceScanResultApi from "@/api/compliance_scan_result";
import { onMounted, ref, watch, onBeforeUnmount } from "vue";
import complianceScanApi from "@/api/compliance_scan";
import _ from "lodash";
import complianceRuleGroupApi from "@/api/rule_group";
import type { ComplianceRuleGroup } from "@/api/rule_group/type";
import {
  PaginationConfig,
  TableConfig,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useRouter } from "vue-router";
import type {
  AccountJobRecord,
  CloudAccount,
} from "@commons/api/cloud_account/type";
import type { ComplianceRuleGroupCountResponse } from "@/api/compliance_scan_result/type";
import bus from "@commons/bus";
import UpdateComplianceRule from "@/views/rule/components/UpdateComplianceRule.vue";
import { useRoute } from "vue-router";
import { usePlatformStore } from "@commons/stores/modules/platform";
import type { Platform } from "@commons/api/cloud_account/type";
import PlatformIcon from "@commons/components/platform-icon/index.vue";
const platformStore = usePlatformStore();
const route = useRoute();
let jobInterval: number;
// 路由对象
const router = useRouter();
const complianceRuleGroup = ref<ComplianceRuleGroupCountResponse>();
const resourceTypeFilters = ref<Array<string>>([]);

// 编辑合规规则对象
const update_compliance_rule_ref =
  ref<InstanceType<typeof UpdateComplianceRule>>();
// 合规规则组
const complianceRuleGroupList = ref<Array<ComplianceRuleGroup>>([]);

// 轮询时间
const pollTime = 10000;
const props = defineProps<{
  cloudAccountList: Array<CloudAccount>;
  cloudAccountId: string;
}>();

const scanStatus = ref<string>();
const scanStatusList = ref([
  { text: "合规", value: "COMPLIANCE" },
  { text: "不合规", value: "NOT_COMPLIANCE" },
]);

onMounted(() => {
  // 查询所有资源类型
  ruleApi.listResourceType().then((ok) => {
    resourceTypes.value = ok.data;
    // 查询列表数据
    if (table.value) {
      table.value.getTableSearch(getPathSearchParams());
      table.value?.search();
    }
  });

  complianceRuleGroupApi.list().then((ok) => {
    complianceRuleGroupList.value = ok.data;
  });
  if (route.query.resourceType) {
    resourceTypeFilters.value = [route.query.resourceType as string];
  }
  if (route.query.ruleGroup) {
    activeComplianceRuleGroupId.value = route.query.ruleGroup as string;
  }
  if (route.query.scanStatus) {
    scanStatus.value = route.query.scanStatus as string;
  }
  if (route.query.riskLevel) {
    activeRiskLevel.value = parseInt(route.query.riskLevel as string);
  }

  // 监控合规规则组变化
  bus.on(
    "update:compliance_rule_group",
    (ruleGroup: ComplianceRuleGroupCountResponse) => {
      complianceRuleGroup.value = ruleGroup;
      table.value?.search();
    }
  );

  complianceScanApi.listJobRecord().then((ok) => {
    accountJobRecordList.value = ok.data;
  });
  jobInterval = setInterval(() => {
    pollJobRecord();
  }, pollTime);
});

/**
 * 打开编辑规则抽屉
 * @param row
 */
const openUpdateRule = (row: ComplianceScanResultResponse) => {
  update_compliance_rule_ref.value?.open();
  update_compliance_rule_ref.value?.echo(row.id);
};
/**
 * 获取扫描状态
 */
const pollJobRecord = () => {
  complianceScanApi.listJobRecord().then((ok) => {
    accountJobRecordList.value = ok.data;
    currentJobRow.value = accountJobRecordList.value.find(
      (item) =>
        item.resourceType === currentJobRow.value?.resourceType &&
        item.resourceId === currentJobRow.value?.resourceId
    );
    bus.emit(
      "sync_status",
      ok.data
        .filter((item) =>
          props.cloudAccountList.some(
            (account) => account.id === item.resourceId
          )
        )
        .map((item) => ({
          resourceType: item.resourceType,
          status: item.status,
          cloudAccountId: item.resourceId,
          platform: props.cloudAccountList.find(
            (account) => account.id === item.resourceId
          )?.platform,
        }))
    );
  });
};

onBeforeUnmount(() => {
  if (jobInterval) {
    clearInterval(jobInterval);
  }
});

/**
 * 风险等级OptionList
 */
const riskLevelOptionList: Array<KeyValue<string, string>> = [
  { key: "高风险", value: "HIGH" },
  { key: "中风险", value: "MIDDLE" },
  { key: "低风险", value: "LOW" },
];
// 风险等级数据
const riskLevelOptions = ref<Array<{ label: string; value: number }>>([
  {
    label: "全部",
    value: -2,
  },
  {
    label: "高风险",
    value: 1,
  },
  {
    label: "中风险",
    value: 0,
  },
  {
    label: "低风险",
    value: -1,
  },
]);
// 风险等级level
const activeRiskLevel = ref<number>(-2);
const accountJobRecordList = ref<Array<AccountJobRecord>>([]);
// 列表数据
const tableData = ref<Array<ComplianceScanResultResponse>>([]);
// 选中的合规规则组id
const activeComplianceRuleGroupId = ref<string>("");
// 资源类型列表数据
const resourceTypes = ref<Array<KeyValue<string, string>>>([]);
// 当前这一行任务数据
const currentJobRow = ref<AccountJobRecord>();
// 任务详情组件
const jobDetailsRef = ref<InstanceType<typeof JobDetailsView>>();
/**
 * 路由到详情页面
 * @param row 当前行数据
 */
const details = (row: ComplianceScanResultResponse) => {
  router.push({
    name: "details",
    params: {
      compliance_rule_id: row.id,
      cloud_account_id: props.cloudAccountId,
    },
  });
};

/**
 * 表格实例对象
 */
const table: any = ref(null);
// 列表字段数据
const columns = ref([]);
/**
 * 加载器
 */
const loading = ref<boolean>(false);

function getPathSearchParams() {
  const param1 = getResourceTypeParams();
  const param2 = getScanStatusParams();
  return { ...param1, ...param2 };
}

const getResourceTypeParams = () => {
  if (resourceTypeFilters.value.length > 0) {
    const t = resourceTypes.value.find(
      (type) => type.value === resourceTypeFilters.value[0]
    );
    if (t) {
      return {
        resourceType: {
          field: "resourceType",
          label: t.key,
          value: t.value,
          valueLabel: t.key,
        },
      };
    }
  }
  return undefined;
};
const getScanStatusParams = () => {
  if (scanStatus.value) {
    const t = _.find(
      scanStatusList.value,
      (type) => type.value === scanStatus.value
    );
    if (t) {
      return {
        scanStatus: {
          field: "scanStatus",
          label: t.text,
          value: t.value,
          valueLabel: t.text,
        },
      };
    }
  }
  return undefined;
};
watch(activeRiskLevel, () => {
  table.value?.search();
});

watch(
  () => props.cloudAccountId,
  () => {
    table.value?.search();
  }
);

/**
 
/**
 * 打开查看同步详情
 * @param row 当前行
 */
const openDetailsJobView = (row: AccountJobRecord) => {
  currentJobRow.value = row;
  jobDetailsRef.value?.open();
};
/**
 * 查询列表函数
 * @param condition 查询条件
 */
const search = (condition: TableSearch) => {
  //
  const params = TableSearch.toSearchParams(condition);

  complianceScanResultApi
    .page(
      tableConfig.value.paginationConfig.currentPage,
      tableConfig.value.paginationConfig.pageSize,
      {
        ...params,
        cloudAccountId:
          props.cloudAccountId === "all" ? undefined : props.cloudAccountId,
        complianceRuleGroupId: complianceRuleGroup.value
          ? complianceRuleGroup.value?.id
          : undefined,
        riskLevel:
          activeRiskLevel.value === -2 ? undefined : activeRiskLevel.value,
      },
      loading
    )
    .then((ok) => {
      tableData.value = ok.data.records;
      tableConfig.value.paginationConfig?.setTotal(
        ok.data.total,
        tableConfig.value.paginationConfig
      );
      tableConfig.value.paginationConfig?.setCurrentPage(
        ok.data.current,
        tableConfig.value.paginationConfig
      );
      pollJobRecord();
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
    searchOptions: [{ label: "规则名称", value: "complianceRuleName" }],
  },
  paginationConfig: new PaginationConfig(),
});
</script>
<style lang="scss" scoped>
:deep(.el-table__cell) {
  .cell {
    white-space: nowrap;
  }
}
.active {
  color: var(--el-color-primary);
  background-color: var(--el-menu-hover-bg-color);
}
.table_content {
  margin-top: 20px;
  .toolbar_content {
    display: flex;
    width: 100%;
    .title {
      color: rgba(31, 35, 41, 1);
      font-weight: 500;
      font-size: 16px;
      height: 24px;
      height: 24px;
    }
    .risk_level {
      margin-right: 8px;
    }
  }
  .row_scan_result {
    color: rgba(51, 112, 255, 1);
    font-weight: 400;
    font-size: 14px;
    line-height: 22px;
    height: 22px;
    cursor: pointer;
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
}
:deep(.table_cell) {
  .cell {
    white-space: nowrap;
  }
}
</style>
