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
        v-for="resourceType in resourceTypes"
        :key="resourceType.value"
        :class="activeResourceType === resourceType.value ? 'active' : ''"
        @click="selectResourceType(resourceType.value)"
      >
        {{ resourceType.key }}
      </div>
    </div>
    <div class="right_wapper">
      <ce-table
        v-loading="loading"
        height="100%"
        ref="table"
        :columns="columns"
        :data="tableData"
        :tableConfig="tableConfig"
        row-key="id"
      >
        <el-table-column prop="name" label="规则名称" width="180" />
        <el-table-column prop="cloudAccountName" label="云账号" width="180" />
        <el-table-column prop="riskLevel" label="风险等级">
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
        <el-table-column prop="scanStatus" label="检测状态">
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
        <el-table-column prop="complianceCount" label="不合规/合规资源">
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
        <el-table-column prop="lastSyncTime" label="最后扫描时间" />
      </ce-table>
    </div>
  </div>
</template>
<script setup lang="ts">
import ruleApi from "@/api/rule";
import type { KeyValue } from "@commons/api/base/type";
import type { ComplianceScanResponse } from "@/api/compliance_scan/type";
import { onMounted, ref, watch } from "vue";
import complianceScanApi from "@/api/compliance_scan";
import {
  PaginationConfig,
  TableConfig,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useRouter } from "vue-router";
const router = useRouter();
const resourceTypes = ref<Array<KeyValue<string, string>>>([]);
onMounted(() => {
  ruleApi.listResourceType().then((ok) => {
    resourceTypes.value = ok.data;
  });
  table.value.search(table?.value.getTableSearch());
});
const tableData = ref<Array<ComplianceScanResponse>>([]);

const activeResourceType = ref<string>();
const details = (row: ComplianceScanResponse) => {
  router.push({
    name: "details",
    params: {
      compliance_rule_id: row.id,
      cloud_account_id: row.cloudAccountId,
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
const table: any = ref(null);
// 列表字段数据
const columns = ref([]);
/**
 * 加载器
 */
const loading = ref<boolean>(false);

watch(activeResourceType, () => {
  table.value.search(table?.value.getTableSearch());
});
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  complianceScanApi
    .pageScanComplianceRule(
      tableConfig.value.paginationConfig.currentPage,
      tableConfig.value.paginationConfig.pageSize,
      { ...params, resourceType: activeResourceType.value },
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
  height: 600px;
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
