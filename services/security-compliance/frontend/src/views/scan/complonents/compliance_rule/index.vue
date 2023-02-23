<template>
  <div class="compliance_rule_wapper">
    <div class="left_wapper">
      <div
        class="item"
        :class="activeResourceType === undefined ? 'active' : ''"
        @click="selectResourceType(undefined)"
      >
        全部
      </div>
      <div
        class="item"
        v-for="resourceType in supportResourceTypes"
        :key="resourceType.value"
        :class="activeResourceType === resourceType.value ? 'active' : ''"
        @click="selectResourceType(resourceType.value)"
      >
        <div style="display: flex; width: 100%">
          <div>
            {{ resourceType.key }}
          </div>
          <div style="flex: auto"></div>
        </div>
      </div>
    </div>
    <div class="right_wapper">
      <ce-table
        localKey="complianceScanRuleTable"
        v-loading="loading"
        height="100%"
        ref="table"
        :columns="columns"
        :data="tableData"
        :tableConfig="tableConfig"
        row-key="id"
      >
        <template #toolbar>
          <el-select
            v-model="activeCloudAccount"
            class="m-2"
            placeholder="Select"
          >
            <el-option
              v-for="item in cloudAccountList"
              :key="item.value"
              :label="item.key"
              :value="item.value"
            />
          </el-select>
        </template>
        <el-table-column prop="name" label="规则名称" width="120">
          <template #default="scope">
            <el-tooltip class="box-item" effect="dark" placement="top-start">
              <template #content>
                <div style="max-width: 500px">{{ scope.row.description }}</div>
              </template>
              <div class="table_content_ellipsis">
                {{ scope.row.name }}
              </div></el-tooltip
            >
          </template>
        </el-table-column>
        <el-table-column prop="resourceTyppe" label="资源类型" width="120px">
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
              <platform_icon :platform="scope.row.platform"></platform_icon>
              <span>{{ platformIcon[scope.row.platform].name }}</span>
            </div>
          </template></el-table-column
        >
        <el-table-column prop="riskLevel" label="风险等级" width="120" sortable>
          <template #default="scope">
            {{
              scope.row.riskLevel === "HIGH"
                ? "高风险"
                : scope.row.riskLevel === "MIDDLE"
                ? "中风险"
                : "低风险"
            }}
          </template>
        </el-table-column>
        <el-table-column
          prop="scanStatus"
          column-key="scanStatus"
          label="检测状态"
          width="100px"
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
                  scope.row.scanStatus === 'NOT_COMPLIANCE'
                    ? '#d9001b'
                    : '#70b603',
              }"
            >
              {{
                scope.row.scanStatus === "NOT_COMPLIANCE" ? "不合规" : "合规"
              }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="notComplianceCount"
          label="不合规/合规资源"
          width="150px"
          sortable
        >
          <template #default="scope">
            <div
              @click="details(scope.row)"
              style="display: flex; cursor: pointer"
            >
              <div style="color: #d9001b">
                {{ scope.row.notComplianceCount }}
              </div>
              /
              <div style="color: #70b603">
                {{ scope.row.complianceCount }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="updateTime"
          label="最后扫描时间"
          width="230px"
          sortable
        >
          <template #default="scope">
            <ScanStatusColumn
              :account-job-record-list="accountJobRecordList"
              :complaince-scan-result="scope.row"
              :cloud-account-source-list="cloudAccountSourceList"
              :open-details-job-view="openDetailsJobView"
              :cloud-account-id="activeCloudAccount"
            ></ScanStatusColumn>
          </template>
        </el-table-column>
      </ce-table>
    </div>
    <job_details_view
      ref="jobDetailsRef"
      :account-job-record="currentJobRow"
    ></job_details_view>
  </div>
</template>
<script setup lang="ts">
import ruleApi from "@/api/rule";
import ScanStatusColumn from "./ScanStatusColumn.vue";
import job_details_view from "@/views/scan/complonents/compliance_rule/JobDetailsView.vue";
import type { KeyValue } from "@commons/api/base/type";
import type { ComplianceScanResultResponse } from "@/api/compliance_scan_result/type";
import complianceScanResultApi from "@/api/compliance_scan_result";
import { onMounted, ref, watch, onBeforeUnmount, computed } from "vue";
import complianceScanApi from "@/api/compliance_scan";
import type { SupportCloudAccountResourceResponse } from "@/api/compliance_scan/type";
import { platformIcon } from "@commons/utils/platform";
import platform_icon from "@commons/components/platform-icon/index.vue";
import {
  PaginationConfig,
  TableConfig,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useRouter } from "vue-router";
import cloudAccountApi from "@commons/api/cloud_account";
import type {
  AccountJobRecord,
  CloudAccount,
} from "@commons/api/cloud_account/type";
import bus from "@commons/bus";
import { useRoute } from "vue-router";
const route = useRoute();
let jobInterval: number;
// 路由对象
const router = useRouter();
const supportResourceTypes = computed(() => {
  if (activeCloudAccount.value === "all" || !activeCloudAccount.value) {
    return resourceTypes.value;
  } else {
    return supportCloudAccountResourceList.value
      .filter((support) => support.cloudAccount.id === activeCloudAccount.value)
      .flatMap((item) => {
        return item.resourceTypes;
      });
  }
});
onMounted(() => {
  complianceScanApi.listSupportCloudAccountResource().then((ok) => {
    supportCloudAccountResourceList.value = ok.data;
  });
  // 查询所有资源类型
  ruleApi.listResourceType().then((ok) => {
    resourceTypes.value = ok.data;
  });

  if (route.query.resourceType) {
    activeResourceType.value = route.query.resourceType as string;
  }
  if (route.query.cloudAccountId) {
    activeCloudAccount.value = route.query.cloudAccountId as string;
  }
  // 查询列表数据
  if (table.value) {
    table.value?.search(table?.value.getTableSearch());
  }

  // 查询云账号数据
  cloudAccountApi.listAll().then((ok) => {
    cloudAccountList.value = [
      { key: "全部云账号", value: "all" },
      ...ok.data.map((cloudAccount) => ({
        key: cloudAccount.name,
        value: cloudAccount.id,
      })),
    ];
    cloudAccountSourceList.value = ok.data;
  });
  // 监控合规规则组变化
  bus.on(
    "update:compliance_rule_group_id",
    (compliance_rule_group_id: string) => {
      activeComplianceRuleGroupId.value = compliance_rule_group_id;
    }
  );
  complianceScanApi.listJobRecord().then((ok) => {
    accountJobRecordList.value = ok.data;
  });
  jobInterval = setInterval(() => {
    complianceScanApi.listJobRecord().then((ok) => {
      accountJobRecordList.value = ok.data;
      currentJobRow.value = accountJobRecordList.value.find(
        (item) =>
          item.resourceType === currentJobRow.value?.resourceType &&
          item.resourceId === currentJobRow.value?.resourceId
      );
    });
  }, 6000);
});

onBeforeUnmount(() => {
  if (jobInterval) {
    clearInterval(jobInterval);
  }
});
const supportCloudAccountResourceList = ref<
  Array<SupportCloudAccountResourceResponse>
>([]);
const accountJobRecordList = ref<Array<AccountJobRecord>>([]);
// 列表数据
const tableData = ref<Array<ComplianceScanResultResponse>>([]);
// 选中的合规规则组id
const activeComplianceRuleGroupId = ref<string>("");
// 选中的云账号
const activeCloudAccount = ref<string>("all");
// 云账号列表
const cloudAccountList = ref<Array<KeyValue<string, string>>>([]);
// 云账号原始数据列表
const cloudAccountSourceList = ref<Array<CloudAccount>>([]);
// 选中的资源类型
const activeResourceType = ref<string>();
// 资源类型列表数据
const resourceTypes = ref<Array<KeyValue<string, string>>>([]);
// 当前这一行任务数据
const currentJobRow = ref<AccountJobRecord>();
// 任务详情组件
const jobDetailsRef = ref<InstanceType<typeof job_details_view>>();
/**
 * 路由到详情页面
 * @param row 当前行数据
 */
const details = (row: ComplianceScanResultResponse) => {
  router.push({
    name: "details",
    params: {
      compliance_rule_id: row.id,
      cloud_account_id: activeCloudAccount.value,
    },
  });
};

/**
 * 资源类型点击事件
 * @param resourceType 资源类型
 */
const selectResourceType = (resourceType: string | undefined) => {
  activeResourceType.value = resourceType;
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

/**
 *监控资源类型的变化查询列表
 */
watch(activeResourceType, () => {
  table.value.search();
});

/**
 * 监控云账号的变化查询列表数据
 */
watch(activeCloudAccount, () => {
  table.value.search();
});

/**
 * 监控合规规则组的变化查询列表数据
 */
watch(activeComplianceRuleGroupId, () => {
  table.value.search();
});
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
  const params = TableSearch.toSearchParams(condition);
  complianceScanResultApi
    .page(
      tableConfig.value.paginationConfig.currentPage,
      tableConfig.value.paginationConfig.pageSize,
      {
        ...params,
        resourceType: activeResourceType.value,
        cloudAccountId:
          activeCloudAccount.value === "all"
            ? undefined
            : activeCloudAccount.value,
        complianceRuleGroupId: activeComplianceRuleGroupId.value,
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
.active {
  color: var(--el-color-primary);
  background-color: var(--el-menu-hover-bg-color);
}
.compliance_rule_wapper {
  display: flex;
  margin-top: 10px;
  height: 700px;
  width: 100%;

  .left_wapper {
    border: 1px solid var(--el-menu-border-color);
    width: 200px;
    height: 100%;
    .item {
      padding-left: 15px;
      display: flex;
      align-items: center;
      height: 35px;
      border-bottom: 1px solid var(--el-menu-border-color);
      color: #555555;
      cursor: pointer;
    }
  }
  .right_wapper {
    width: calc(100% - 220px);
    margin-left: 20px;
    height: 100%;
  }
}
</style>
