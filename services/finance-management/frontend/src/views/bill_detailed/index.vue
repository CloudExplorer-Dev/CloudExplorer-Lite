<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <ce-table
      localKey="billDetailedTable"
      v-loading="loading"
      @clearCondition="clearCondition"
      height="100%"
      ref="table"
      :columns="columns"
      :data="dataList"
      :tableConfig="tableConfig"
      row-key="id"
    >
      <template #toolbar>
        <el-select style="width: 90px" v-model="activeDate">
          <el-option label="账期" value="billingCycle"></el-option>
          <el-option label="日期" value="deductionDate"></el-option
        ></el-select>
        <el-date-picker
          v-if="activeDate === 'billingCycle'"
          :key="'billingCycle'"
          :editable="false"
          v-model="viewMonth"
          type="month"
          placeholder="请选择账期"
          format="YYYY-MM"
          :clearable="false"
          :disabled-date="disabledDate"
          value-format="YYYY-MM"
        >
        </el-date-picker>
        <el-date-picker
          v-else
          :editable="false"
          :key="'deductionDate'"
          v-model="viewDeductionDate"
          type="date"
          placeholder="请选择日期"
          format="YYYY-MM-DD"
          :clearable="false"
          :disabled-date="disabledDate"
          value-format="YYYY-MM-DD"
        >
        </el-date-picker>
      </template>
      <el-table-column type="selection" />
      <el-table-column prop="billingCycle" width="100px" label="账期">
        <template #default="scope">
          <span>{{ scope.row.billingCycle.substring(0, 7) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="deductionDate" width="120px" label="日期" sortable>
        <template #default="scope">
          <span>{{ scope.row.deductionDate.substring(0, 10) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="resourceId"
        label="资源id"
        min-width="120px"
        :show-overflow-tooltip="true"
      >
        <template #default="scope">
          <span class="table_overflow">
            {{ scope.row.resourceId ? scope.row.resourceId : "N/A" }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        min-width="120px"
        prop="resourceName"
        label="资源名称"
        show-overflow-tooltip
      >
        <template #default="scope">
          <span class="table_overflow">
            {{ scope.row.resourceName ? scope.row.resourceName : "N/A" }}</span
          >
        </template>
      </el-table-column>
      <el-table-column
        prop="cloudAccountName"
        label="云账号"
        :filters="
          cloudAccountList.map((item) => ({
            text: item.name,
            value: item.id,
          }))
        "
        show-overflow-tooltip
        :filter-multiple="false"
        column-key="cloudAccountId"
        width="150px"
      >
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <PlatformIcon :platform="scope.row.provider"> </PlatformIcon>
            <div class="table_overflow" style="margin-left: 8px">
              {{ scope.row.cloudAccountName }}
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="productName"
        label="产品名称"
        show-overflow-tooltip
        width="120px"
      >
        <template #default="scope">
          <span class="table_overflow">{{
            scope.row.productName ? scope.row.productName : "N/A"
          }}</span></template
        >
      </el-table-column>
      <el-table-column
        prop="productDetail"
        label="产品明细"
        show-overflow-tooltip
        width="200px"
      >
        <template #default="scope">
          <span class="table_overflow">{{
            scope.row.productDetail ? scope.row.productDetail : "N/A"
          }}</span></template
        >
      </el-table-column>
      <el-table-column
        prop="payAccountId"
        show-overflow-tooltip
        label="付款账号"
        width="200px"
      >
        <template #default="scope">
          <span class="table_overflow">
            {{ scope.row.payAccountId }}
          </span></template
        >
      </el-table-column>
      <el-table-column prop="organizationName" label="组织">
        <template #default="scope">
          <span>{{
            scope.row.organizationName ? scope.row.organizationName : "N/A"
          }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="workspaceName" label="工作空间">
        <template #default="scope">
          <span>{{
            scope.row.workspaceName ? scope.row.workspaceName : "N/A"
          }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="billMode" label="计费模式" show-overflow-tooltip>
        <template #default="scope">
          <span class="table_overflow">{{
            scope.row.billMode === "MONTHLY"
              ? "包年包月"
              : scope.row.billMode === "ON_DEMAND"
              ? "按需"
              : "其他"
          }}</span>
        </template>
      </el-table-column>

      <el-table-column show-overflow-tooltip prop="tags" label="标签">
        <template #default="scope">
          <span class="table_overflow">
            {{
              scope.row.tags
                ? Object.keys(scope.row.tags).length > 0
                  ? Object.keys(scope.row.tags)
                      .map((key) => key + "=" + scope.row.tags[key])
                      .join(",")
                  : "N/A"
                : "N/A"
            }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="projectName" label="企业项目" />
      <el-table-column
        prop="cost.officialAmount"
        label="官网价"
        min-width="100px"
        sortable
      >
        <template #default="scope">
          <span
            :title="scope.row.cost.officialAmount + scope.row.currency.unit"
          >
            {{
              DecimalFormat.format(scope.row.cost.officialAmount, 2) +
              scope.row.currency.unit
            }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        prop="cost.payableAmount"
        label="应付金额"
        min-width="110px"
        sortable
      >
        <template #default="scope">
          <span :title="scope.row.cost.payableAmount + scope.row.currency.unit">
            {{
              DecimalFormat.format(scope.row.cost.payableAmount, 2) +
              scope.row.currency.unit
            }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        prop="cost.cashAmount"
        label="现金支付"
        min-width="110px"
        sortable
      >
        <template #default="scope">
          <span :title="scope.row.cost.cashAmount + scope.row.currency.unit">
            {{
              DecimalFormat.format(scope.row.cost.cashAmount, 2) +
              scope.row.currency.unit
            }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        prop="cost.couponAmount"
        label="代金券支付"
        min-width="120px"
        sortable
      >
        <template #default="scope">
          <span :title="scope.row.cost.couponAmount + scope.row.currency.unit">
            {{
              DecimalFormat.format(scope.row.cost.couponAmount, 2) +
              scope.row.currency.unit
            }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="currency.code" label="币种" min-width="120px">
        <template #default="scope">
          {{ `${scope.row.currency.code}(${scope.row.currency.message})` }}
        </template>
      </el-table-column>
      <template #buttons>
        <CeTableColumnSelect :columns="columns" />
      </template>
    </ce-table>
  </layout-content>
</template>
<script setup lang="ts">
import { ref, watch, onMounted, computed } from "vue";
import {
  PaginationConfig,
  TableConfig,
  TableSearch,
} from "@commons/components/ce-table/type";
import billDetailApi from "@/api/bill_detailed";
import PlatformIcon from "@commons/components/platform-icon/index.vue";
import cloudAccountApi from "@commons/api/cloud_account/index";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import DecimalFormat from "@commons/utils/decimalFormat";
/**
 *当前月份
 */
const currentMonth =
  new Date().getFullYear().toString() +
  "-" +
  ((new Date().getMonth() + 1).toString().length === 1
    ? "0" + (new Date().getMonth() + 1).toString()
    : (new Date().getMonth() + 1).toString());
const viewMonth = ref<string>(currentMonth);
const viewDeductionDate = ref<string>();
/**
 * 日志选择框禁用大于当前月份的时间
 * @param time 时间
 */
const disabledDate = (time: Date) => {
  return time.getTime() > Date.now();
};

const cloudAccountList = ref<Array<CloudAccount>>([]);

const activeDate = ref<"billingCycle" | "deductionDate">("billingCycle");

/**
 * 表格对象
 */
const table = ref<any>();

const clearCondition = () => {
  viewMonth.value = "";
  viewDeductionDate.value = "";
};

const otherSearchQuery = computed(() => {
  const query: any = {};
  if (viewMonth.value) {
    query["month"] = {
      value: viewMonth.value,
      label: "账期",
      valueLabel: viewMonth.value,
      field: "month",
    };
  }
  if (viewDeductionDate.value) {
    query["deductionDate"] = {
      value: viewDeductionDate.value,
      label: "日期",
      valueLabel: viewDeductionDate.value,
      field: "deductionDate",
    };
  }
  return query;
});
onMounted(() => {
  /**
   * 组件挂载查询数据
   */
  search(table.value?.getTableSearch(otherSearchQuery.value));
  cloudAccountApi.listAll().then((ok) => {
    cloudAccountList.value = ok.data;
  });
});

/**
 * 月份发生改变的时候查询数据
 */
watch(otherSearchQuery, () => {
  const tabSearch: TableSearch = table.value?.getTableSearch(
    otherSearchQuery.value
  );
  search(tabSearch);
});
/**
 *加载器
 */
const loading = ref<boolean>(false);
/**
 * 数据列表
 */
const dataList = ref<Array<any>>([]);

// 列表字段数据
const columns = ref([]);
/**
 * 查询函数
 * @param condition 查询条件
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  billDetailApi
    .pageBillDetailed(
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
 * 表单配置
 */
const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    // 查询函数
    search: search,
    quickPlaceholder: "搜索",
    components: [],
    searchOptions: [
      { label: "资源名称", value: "resourceName" },
      { label: "云账号", value: "cloudAccountName" },
      { label: "企业项目", value: "projectName" },
      { label: "产品名称", value: "productName" },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: undefined,
});
</script>
<style lang="scss" scoped>
.table_overflow {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
