<template>
  <layout-auto-height-content
    :style="{ backgroundColor: '#f2f2f2', height: 'auto' }"
    style="
      --ce-main-content-padding-top: 0;
      --ce-main-content-padding-left: 0;
      --ce-main-content-padding-right: 0;
      --ce-main-content-padding-bottom: 0;
    "
  >
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>

    <el-row :gutter="16" v-resize="reSize" class="top-content">
      <el-col :span="6">
        <el-row class="left-div">
          <el-col :spam="24">
            <ViewExpensesAggsCard
              :get-aggs-count="getMonthExpenses"
              compare-title="较上月"
              title="本月花费"
            />
          </el-col>
        </el-row>
        <el-row class="left-div">
          <el-col :spam="24">
            <ViewExpensesAggsCard
              :get-aggs-count="getYearExpenses"
              compare-title="较上周期"
              title="本年总花费"
            />
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="18">
        <BillTrend />
      </el-col>
    </el-row>

    <el-card class="bottom_content" shadow="never">
      <div class="operation">
        <div class="title">
          账单汇总-
          <el-date-picker
            style="width: 120px; margin-left: 4px; height: 24px"
            ref="datePicker"
            :editable="false"
            v-model="viewMonth"
            type="month"
            :clearable="false"
            placeholder="请选择月份"
            format="YYYY年MM月"
            :disabled-date="disabledDate"
            value-format="YYYY-MM"
          >
          </el-date-picker>
          <span
            class="tag"
            v-if="
              viewMonth === currentMonth ||
              (new Date().getDate() < 10 &&
                parseInt(viewMonth.substring(5, 7)) ===
                  parseInt(currentMonth.substring(5, 7)) - 1)
            "
          >
            未出账
          </span>
        </div>
      </div>
      <div class="content">
        <ViewTabs
          :max-num="6"
          ref="viewTabs"
          :local-key="'test'"
          :topping="true"
          v-model:active="activeBillRule"
          :tabs="billRules"
        ></ViewTabs>
        <div>
          <div class="chart_content" v-loading="bullRuleViewDataLoading">
            <ViewPieChart
              @update:view-data="(d) => (viewData = d)"
              @update:groups="(g) => (groups = g)"
              :month="viewMonth"
              ref="viewPieChart"
              :get-bill-view-data="getBillViewData"
            ></ViewPieChart>
          </div>
          <div class="table_content" v-loading="bullRuleViewDataLoading">
            <div class="title">费用明细</div>
            <div class="table">
              <ViewTable
                :bill-rule="billRules.find((b) => b.id === activeBillRule)"
                :view-data="viewData"
                :groups="groups"
              ></ViewTable>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </layout-auto-height-content>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from "vue";
import type { BillSummary } from "@/echarts/bill_view/type";
import billViewAPi from "@/api/bill_view/index";
import billRuleApi from "@/api/bill_rule/index";
import type { BillRule } from "@/api/bill_rule/type";
import ViewTable from "@/views/bill_view/components/ViewTable.vue";
import BillTrend from "@commons/business/base-layout/home-page/items/bill/BillTrend.vue";
import ViewTabs from "@/views/bill_view/components/ViewTabs.vue";
import ViewExpensesAggsCard from "@/views/bill_view/components/ViewExpensesAggsCard.vue";
import ViewPieChart from "@/views/bill_view/components/ViewPieChart.vue";
// tabs组建
const viewTabs = ref<InstanceType<typeof ViewTabs> | null>(null);
// 当前选中账单规则
const activeBillRule = ref("");
// 账单规则饼图
const viewPieChart = ref<InstanceType<typeof ViewPieChart>>();
// 监听账单当前账单规则 重写绘制图表
watch(activeBillRule, () => {
  viewPieChart.value?.refresh();
});
// 当当前页面发生变化重新绘制图表
const reSize = () => {
  viewPieChart.value?.reSize();
};
// 账单规则接口获取加载器
const bullRuleViewDataLoading = ref<boolean>(false);

/**
 *当前月份
 */
const currentMonth =
  new Date().getFullYear().toString() +
  "-" +
  ((new Date().getMonth() + 1).toString().length === 1
    ? "0" + (new Date().getMonth() + 1).toString()
    : (new Date().getMonth() + 1).toString());
/**
 * 查看账单选择的月份
 */
const viewMonth = ref<string>(currentMonth);

const disabledDate = (time: Date) => {
  return time.getTime() > Date.now();
};

/**
 * 当前月份花费处理器
 */
const currentMonthExpensesLoading = ref<boolean>(false);

/**
 * 当前年花费加载器
 */
const currentYearExpensesLoading = ref<boolean>(false);

/**
 * 账单规则列表数据
 */
const billRules = ref<Array<BillRule>>([]);
/**
 * 获取半年费用
 */
const getMonthExpenses = () => {
  return billViewAPi
    .getExpenses("MONTH", currentMonth, currentMonthExpensesLoading)
    .then((ok) => {
      return ok.data;
    });
};
/**
 * 获取当前费用
 */
const getYearExpenses = () => {
  return billViewAPi
    .getExpenses(
      "YEAR",
      new Date().getFullYear().toString(),
      currentYearExpensesLoading
    )
    .then((ok) => {
      return ok.data;
    });
};
/**
 * 挂载数据
 */
onMounted(() => {
  billRuleApi.listBillRules().then((ok) => {
    billRules.value = ok.data;
  });
});
/**
 * 全量账单表格数据
 */
const viewData = ref<Array<BillSummary>>([]);
/**
 * 账单分组
 */

const groups = ref<Array<string>>([]);

const getBillViewData = () => {
  if (activeBillRule.value) {
    return billViewAPi
      .getBillView(
        activeBillRule.value,
        viewMonth.value,
        bullRuleViewDataLoading
      )
      .then((ok) => ok.data);
  }
  return Promise.resolve({});
};

/**
 *监听月份改变
 */
watch(viewMonth, () => {
  viewPieChart.value?.refresh();
});
</script>
<style lang="scss" scoped>
@mixin title() {
  font-size: 16px;
  font-weight: 500;
  height: 24px;
  line-height: 24px;
}

.left-div {
  margin-bottom: 16px;
  height: 142px;
}
.left-div:last-child {
  margin-bottom: 0;
}

.chart_content {
  height: 300px;
  min-width: 760px;
  position: relative;
  border: 1px solid #dee0e3;
  border-radius: 4px;
  .chart {
    &:hover {
      overflow-x: auto;
    }
  }
}

.line {
  height: 100%;
  width: 1px;
  background-color: var(--el-border-color);
}
.active {
  border: 1px solid #006eff;
  background: #fff;
  color: #006eff;
}
.top-content {
  margin-bottom: 16px;
  min-width: 800px;
}
.bottom_content {
  overflow-x: auto;
  overflow-y: hidden;
  min-width: 800px;

  .operation {
    display: flex;
    margin-left: 10px;
    justify-content: space-between;
    .title {
      @include title;
      .tag {
        margin-left: 8px;
        background: rgba(31, 35, 41, 0.1);
        width: 42px;
        height: 22px;
        font-weight: 400;
        font-size: 14px;
        line-height: 22px;
        color: rgba(100, 106, 115, 1);
        padding: 1px 4px;
        border-radius: 2px;
      }
    }
  }
  .content {
    width: 100%;
    box-sizing: border-box;
    padding-left: 10px;
    .table_content {
      min-width: 760px;
      margin-top: 16px;
      width: calc(100% - 2px);
      background: #ffffff;
      border: 1px solid #dee0e3;
      border-radius: 4px;
      .table {
        padding: 24px 24px 0 24px;
        box-sizing: border-box;
        width: 100%;
        .table {
          height: 100%;
        }
      }
      .title {
        margin: 24px 0 0 24px;
        @include title;
      }
    }
  }
}

:deep(.el-tabs__nav) {
  :deep(.el-tabs__ite) {
    padding: 32px;
    color: #6b778c;
    font-size: 32px;
    font-weight: 600;
  }
}
</style>
