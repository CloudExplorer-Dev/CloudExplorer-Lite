<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <div class="up_wapper">
      <div class="left_wapper" :border="false">
        <div class="top" v-loading="currentMonthExpensesLoading">
          <p><span class="title_font money_title">当月花费</span></p>
          <div class="money_wapper">
            <div>{{ currentMonthExpenses }} 元</div>
          </div>
        </div>
        <div class="bottom" v-loading="currentYearExpensesLoading">
          <p><span class="title_font money_title">今年总花费</span></p>
          <div class="money_wapper">{{ currentYearExpenses }} 元</div>
        </div>
      </div>
      <div class="right_wapper" ref="rightWapper">
        <div class="header">
          <div class="title title_font">
            费用趋势<span class="sub_title_font">（单位：元）</span>
          </div>
        </div>
        <div class="operation_wapper">
          <div class="operation">
            <div
              class="left"
              :class="[activeTreedYear === 'MONTH' ? 'active' : '']"
              @click="historyTrend(6, 'MONTH')"
            >
              近半年
            </div>
            <div class="line"></div>
            <div
              class="right"
              :class="[activeTreedYear === 'YEAR' ? 'active' : '']"
              @click="historyTrend(12, 'YEAR')"
            >
              近一年
            </div>
          </div>
        </div>
        <div
          class="chart_wapper"
          v-loading="historyTrendLoading"
          ref="chartWapper"
        ></div>
        <span
          >{{ _.minBy(hostryTreed, "label")?.label }} ～
          {{ _.maxBy(hostryTreed, "label")?.label }}总费用为{{
            _.floor(_.sumBy(hostryTreed, "value"), 2)
          }}
          元</span
        >
      </div>
    </div>
    <div class="bottom_wapper berder">
      <div class="title title_font">
        2022年10月份账单汇总<span class="sub_title_font">（单位：元）</span>
      </div>
      <div class="operation">
        <div>
          <el-date-picker
            :editable="false"
            v-model="viewMonth"
            type="month"
            placeholder="Pick a day"
            format="YYYY-MM"
            :disabled-date="disabledDate"
            value-format="YYYY-MM"
          >
            <template #default="cell">
              <div class="cell" :class="{ current: cell.isCurrent }">
                <span class="text">{{ cell.text }}</span>
              </div>
            </template>
          </el-date-picker>
          <span>{{ currentMonth }}未出账</span>
        </div>
        <div style="color: #006eff; cursor: pointer; top: 20px">导出</div>
      </div>
      <div class="content">
        <el-tabs
          class="demo-tabs"
          v-model="activeName"
          @tab-change="tabChange"
          style="width: calc(100% - 40px); margin-left: 20px"
        >
          <el-tab-pane
            v-for="rule in BillRules"
            :key="rule.id"
            :label="rule.name"
            :name="rule.id"
          >
          </el-tab-pane>
          <div v-loading="billViewChartLoading">
            <div class="chart" style="position: relative">
              <div
                ref="chart"
                style="width: 100%; height: 300px; min-width: 1000px"
              ></div>
              <div
                v-if="groups.length > 1"
                @click="fallback()"
                style="
                  position: absolute;
                  top: 20px;
                  cursor: pointer;
                  left: 10px;
                  color: var(--el-color-primary);
                "
              >
                返回
              </div>
            </div>
            <div style="height: 500px; width: calc(100% - 30px)">
              <el-auto-resizer>
                <template #default="{ height, width }">
                  <el-table-v2
                    :columns="columns"
                    :sort-by="sortState"
                    :data="viewData"
                    :width="width"
                    :height="height"
                    @column-sort="onSort"
                    fixed
                  />
                </template>
              </el-auto-resizer>
            </div>
          </div>
        </el-tabs>
      </div>
    </div>
  </layout-content>
  <el-dialog v-model="tableChartVisible">
    <template #default>
      <div ref="detailsWapper" style="height: 200px; width: 100%"></div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { inject, ref, onMounted, nextTick, computed, h, watch } from "vue";
import {
  initBillView,
  getBillViewOptions,
  getTrendViewOption,
} from "@/echarts/bill_view/index";
import type { ECharts } from "echarts";
import type { BillSummary, TrendData } from "@/echarts/bill_view/type";
import billViewAPi from "@/api/bill_view/index";
import billRuleApi from "@/api/bill_rule/index";
import type { BillView } from "@/api/bill_view/type";
import type { BillRule } from "@/api/bill_rule/type";
import type { SimpleMap } from "@commons/api/base/type";
import TableTrend from "@/views/bill_view/TableTrend.vue";
import { TableV2FixedDir } from "element-plus";
import _ from "lodash";

const echarts: any = inject("echarts");

const chartWapper = ref<any>(null);

const detailsWapper = ref<any>(null);

const activeName = ref("");

const activeGroup = computed(() => {
  const find = BillRules.value.find((b) => b.id === activeName.value);
  if (find) {
    return find.groups;
  } else {
    return [];
  }
});

const tableChartVisible = ref<boolean>(false);

const tableChart = ref<ECharts>();
const billViewData = ref<SimpleMap<Array<BillView>>>();

const activeTreedYear = ref<string>("MONTH");

/**
 * 重置数据
 * @param billViewData
 */
const resetViewData = (billViewData: SimpleMap<Array<BillView>>) => {
  const treedMap: any = Object.keys(billViewData)
    .flatMap((key) => {
      return billViewData[key].map((item) => {
        return {
          [key + item.billGroupDetails.map((i) => i.value).join(",")]:
            item.value,
        };
      });
    })
    .reduce((pre, next) => {
      return { ...pre, ...next };
    }, {});
  if (Object.keys(billViewData).includes(viewMonth.value)) {
    // 树形数据
    const data = billViewData[viewMonth.value].map((view) => {
      return view.billGroupDetails
        .map((b) => {
          return { [b.key]: b.value };
        })
        .reduce(
          (pre, next) => {
            return { ...pre, ...next };
          },
          {
            treed: Object.keys(billViewData).map((month) => {
              const v =
                treedMap[
                  month + view.billGroupDetails.map((i) => i.value).join(",")
                ];
              return { label: month, value: v ? v : 0 };
            }),
            value: view.value,
          }
        );
    });
    viewData.value = data as unknown as Array<BillSummary>;
    viewData.value.sort((pre, next) => next.value - pre.value);
  } else {
    viewData.value = [];
  }
  groups.value = ["root"];
  if (char.value) {
    char.value?.setOption(getBillViewOptions(viewData.value, groups));
  } else {
    init();
  }
};

/**
 * 打开趋势图
 * @param treed 打开查看趋势图
 */
const open = (treed: Array<TrendData>) => {
  treed.sort((pre, next) => pre.label.localeCompare(next.label));
  tableChartVisible.value = true;
  const option: any = getTrendViewOption(treed, "line", true, true, true);
  option["grid"] = { top: 20, bottom: 20 };
  if (tableChart.value) {
    tableChart.value.setOption(option);
  } else {
    nextTick(() => {
      tableChart.value = echarts.init(detailsWapper.value);
      tableChart.value?.setOption(option);
    });
  }
};

/**
 *当前月份
 */
const currentMonth =
  new Date().getFullYear().toString() +
  "-" +
  ((new Date().getMonth() + 1).toString().length === 0
    ? "0" + (new Date().getMonth() + 1).toString()
    : (new Date().getMonth() + 1).toString());
/**
 * 查看账单选择的月份
 */
const viewMonth = ref<string>(currentMonth);

const disabledDate = (time: Date) => {
  return time.getTime() > Date.now();
};

const chart = ref();
/**
 *历史趋势数据
 */
const hostryTreed = ref<Array<TrendData>>([]);
/**
 * 当前月份花费
 */
const currentMonthExpenses = ref<number>();
/**
 * 当前月份花费处理器
 */
const currentMonthExpensesLoading = ref<boolean>(false);
/**
 * 当前年花费
 */
const currentYearExpenses = ref<number>();
/**
 * 当前年花费加载器
 */
const currentYearExpensesLoading = ref<boolean>(false);
/**
 *历史趋势图
 */
const historyTrendChart = ref<any>(null);
const historyTrendLoading = ref<boolean>(false);

const billViewChartLoading = ref<boolean>(false);
const historyTrend = (historyNum: number, active: string) => {
  activeTreedYear.value = active;
  // 获取历史趋势
  billViewAPi
    .getHistoryTrend("MONTH", historyNum, historyTrendLoading)
    .then((ok) => {
      hostryTreed.value = ok.data;
      if (!historyTrendChart.value) {
        historyTrendChart.value = echarts.init(chartWapper.value);
      }
      hostryTreed.value.sort((pre, next) =>
        pre.label.localeCompare(next.label)
      );
      const option = getTrendViewOption(
        hostryTreed.value,
        "bar",
        true,
        true,
        false
      );
      historyTrendChart.value.setOption(option);
    });
};
const BillRules = ref<Array<BillRule>>([]);
onMounted(() => {
  billViewAPi
    .getExpenses("MONTH", currentMonth, currentMonthExpensesLoading)
    .then((ok) => {
      currentMonthExpenses.value = Math.floor(ok.data * 100) / 100;
    });
  billViewAPi
    .getExpenses(
      "YEAR",
      new Date().getFullYear().toString(),
      currentYearExpensesLoading
    )
    .then((ok) => {
      currentYearExpenses.value = Math.floor(ok.data * 100) / 100;
    });
  historyTrend(12, "YEAR");
  billRuleApi.listBillRules().then((ok) => {
    BillRules.value = ok.data;
    if (ok.data) {
      activeName.value = ok.data[0].id;
    }
  });
  window.onresize = function () {
    char.value?.resize();
  };
});
const char = ref<ECharts>();

const viewData = ref<Array<BillSummary>>([]);
const groups = ref<Array<string>>(["root"]);

/**
 * 返回
 */
const fallback = () => {
  groups.value = [];
  char.value?.setOption(getBillViewOptions(viewData.value, groups));
};
/**
 * 初始化饼图
 */
const init = () => {
  char.value = initBillView(echarts, chart.value, viewData.value, groups);
  // 监听图标点击事件
  char.value?.on("click", "series", (param: any) => {
    if (
      viewData.value.every((i) =>
        Object.keys(i).includes("group" + (groups.value.length + 1))
      )
    ) {
      groups.value.push(param.name);

      char.value?.setOption(getBillViewOptions(viewData.value, groups));
    }
  });
  // 监听legend选中事件
  char.value?.on("legendselectchanged", (param: any) => {
    char.value?.setOption(
      getBillViewOptions(viewData.value, groups, param.selected)
    );
  });
};

/**
 *监听月份改变
 */
watch(viewMonth, () => {
  billViewAPi
    .getBillView(activeName.value, viewMonth.value, billViewChartLoading)
    .then((ok) => {
      billViewData.value = ok.data;
      resetViewData(ok.data);
    });
});

/**
 * tab 切换触发函数
 * @param ruleId 规则id
 */
const tabChange = (ruleId: string) => {
  billViewAPi
    .getBillView(ruleId, viewMonth.value, billViewChartLoading)
    .then((ok) => {
      billViewData.value = ok.data;
      resetViewData(ok.data);
    });
};

// --------------- table start ---
/**
 * table动态字段
 */
const columns = computed(() => {
  const defaulta = [
    {
      key: "value",
      title: "总费用",
      width: 200,
      dataKey: "value",
      fixed: TableV2FixedDir.RIGHT,
      sortable: true,
    },
    {
      key: "value",
      title: "占比",
      width: 100,
      dataKey: "value",
      fixed: TableV2FixedDir.RIGHT,
      cellRenderer: (cellData: any) =>
        Math.floor(
          (cellData.rowData.value /
            viewData.value.map((b) => b.value).reduce((p, n) => p + n)) *
            10000
        ) /
          100 +
        "%",
    },
    {
      key: "treed",
      title: "趋势",
      width: 100,
      dataKey: "treed",
      fixed: TableV2FixedDir.RIGHT,
      cellRenderer: (cellData: any) => {
        return h(TableTrend, {
          trend: cellData.rowData.treed,
          onClick: () => {
            open(cellData.rowData.treed);
          },
        });
      },
    },
  ];
  return [
    ...activeGroup.value.map((g, index) => {
      return {
        key: g.field,
        title: g.name,
        maxWidthth: "500px",
        minWidth: "200px",
        width: 300,
        dataKey: "group" + (index + 1),
      };
    }),
    ...defaulta,
  ];
});
/**
 * table排序
 * @param sortBy 排序对象
 */
const onSort = (sortBy: any) => {
  if (sortBy.order === "asc") {
    viewData.value.sort((pre, next) => pre[sortBy.key] - next[sortBy.key]);
  } else {
    viewData.value.sort((pre, next) => next[sortBy.key] - pre[sortBy.key]);
  }
  sortState.value = sortBy;
};
/**
 * 排序状态
 */
const sortState = ref<any>({
  key: "value",
  order: "desc",
});
// --------------- table end ---
</script>
<style lang="scss" scoped>
.up_wapper {
  display: flex;
  width: 100%;
  height: 300px;
  .left_wapper {
    display: flex;
    flex-wrap: wrap;
    align-content: space-between;
    height: 100%;
    width: 20%;
    min-width: 200px;
    .top {
      border: 1px solid var(--el-border-color);
      height: 45%;
      width: 100%;
      margin-bottom: 30px;
    }
    .bottom {
      border: 1px solid var(--el-border-color);
      height: 45%;
      width: 100%;
    }
  }
  .right_wapper {
    margin-left: 20px;
    border: 1px solid var(--el-border-color);
    width: 80%;
    height: 100%;
    min-width: 600px;
    .header {
      display: flex;
      justify-content: center;
      height: 20px;
    }
    .operation_wapper {
      display: flex;
      justify-content: flex-end;
      height: 30px;
      .operation {
        cursor: pointer;
        width: 200px;
        height: 100%;
        display: flex;
        border: 1px solid var(--el-border-color);
        font-size: 12px;
        line-height: 23px;
        margin-right: 50px;
        .left {
          width: 50%;
          height: 100%;
          display: flex;
          justify-content: center;
          align-items: center;
        }
        .right {
          width: 50%;
          height: 100%;
          display: flex;
          justify-content: center;
          align-items: center;
        }
      }
    }
    .chart_wapper {
      height: 70%;
      width: 100%;
      display: flex;
    }
  }
  margin-bottom: 20px;
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
.bottom_wapper {
  width: 100%;
  .title {
    display: flex;
    justify-content: center;
  }
  .operation {
    display: flex;
    justify-content: space-between;
    padding: 0 30px;
  }
  .content {
    width: 100%;
    padding-left: 10px;
  }
}
.money_wapper {
  display: flex;
  justify-content: center;
  align-content: center;
  color: #333333;
  font-size: 30px;
  height: 60%;
  line-height: 120%;
}
.money_title {
  height: 20%;
  margin-inline-start: 1em;
}
.berder {
  border: 1px solid var(--el-border-color);
}
.title_font {
  margin-top: 20px;
  font-family: "Arial-Black", "Arial Black", sans-serif;
  font-weight: 900;
  font-size: 16px;
}
.sub_title_font {
  font-family: "PingFangSC-Regular", "PingFang SC", sans-serif;
  font-weight: 400;
  font-size: 12px;
  line-height: 200%;
}
.demo-tabs {
  .el-tabs__content {
    padding: 32px;
    color: #6b778c;
    font-size: 32px;
    font-weight: 600;
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
