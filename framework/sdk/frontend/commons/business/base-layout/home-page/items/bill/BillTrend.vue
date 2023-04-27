<script lang="ts" setup>
import { computed, onMounted, type Ref, ref } from "vue";
import CurrencyFormat from "@commons/utils/currencyFormat";
import { useModuleStore } from "@commons/stores/modules/module";
import _ from "lodash";
import { usePermissionStore } from "@commons/stores/modules/permission";
import BillViewAPI from "@commons/api/bil_view/index";

import { useUserStore } from "@commons/stores/modules/user";
import type { ECharts } from "echarts";
import type { Result } from "@commons/request/Result";
import DecimalFormat from "@commons/utils/decimalFormat";

import * as echarts from "echarts";

const props = withDefaults(
  defineProps<{
    needRoles?: Array<"ADMIN" | "ORGADMIN" | "USER">;
    permission?: any;
    module?: string;
    getHistoryTrend?: (
      type: "MONTH" | "YEAR",
      historyNum: number,
      loading?: Ref<boolean>
    ) => Promise<Result<Array<any>>>;
    headPosition?: "left" | "center";
    chartHeight?: number | string;
  }>(),
  {
    needRoles: () => ["ADMIN", "ORGADMIN", "USER"],
    getHistoryTrend: BillViewAPI.getHistoryTrend,
    headPosition: "center",
    permission: "[finance-management]BILL_ViEW:READ",
    module: "finance-management",
    chartHeight: 200,
  }
);

const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();
const userStore = useUserStore();

const historyTrendLoading = ref<boolean>(false);

interface TrendData {
  /**
   *值
   */
  value: number;
  /**
   *提示
   */
  label: string;
}

const getTrendViewOption = (
  data: Array<TrendData>,
  type: "line" | "bar",
  showx: boolean,
  showy: boolean,
  showSymbol: boolean
) => {
  const option = {
    grid: {
      x: 40,
      y: 25,
      x2: 0,
      y2: 24,
    },
    xAxis: {
      type: "category",
      data: data.map((d) => d.label),
      show: showx,
      axisLabel: {
        show: true,
        color: "rgba(143, 149, 158, 1)",
        fontSize: 12,
      },
    },
    yAxis: {
      type: "value",
      show: showy,
      axisLabel: {
        color: "rgba(143, 149, 158, 1)",
        fontSize: 12,
        formatter: function (value: number) {
          value / 1000 > 0;
          if (value / 1000 < 1) {
            return _.round(value, 2);
          } else if (value / 10000 > 1) {
            return _.round(value / 10000, 2) + "w";
          } else if (value / 1000 >= 1) {
            return _.round(value / 1000, 2) + "k";
          }
          return _.round(value / 10000, 2) + "w";
        },
      },
    },
    series: [
      {
        barWidth: 16,
        data: data.map((d) => ({
          value: d.value,
          itemStyle: {
            borderRadius: [2, 2, 0, 0],
            color: "#4E83FD",
          },
        })),
        type: type,
        smooth: true,
        showSymbol: showSymbol,
      },
    ],
  };
  return option;
};

const activeTreedYear = ref<string>("MONTH");
const historyTreed = ref<Array<TrendData>>([]);
let historyTrendChart: ECharts | undefined = undefined;
const chartWrapper = ref<any>(null);

const historyTrend = async (historyNum: number, active: string) => {
  if (!show.value) {
    return;
  }
  activeTreedYear.value = active;
  // 获取历史趋势
  await props
    .getHistoryTrend("MONTH", historyNum, historyTrendLoading)
    .then((ok) => {
      historyTreed.value = ok.data;
      if (!historyTrendChart) {
        historyTrendChart = echarts.init(chartWrapper.value);
      }
      historyTreed.value.sort((pre, next) =>
        pre.label.localeCompare(next.label)
      );
      const option: any = getTrendViewOption(
        historyTreed.value,
        "bar",
        true,
        true,
        false
      );
      option["yAxis"]["splitLine"] = {
        show: false,
      };
      option["series"][0]["itemStyle"] = {
        label: {
          show: true,
          position: "top",
          color: "black",
          fontSize: 12,
          formatter: function (param: any) {
            return DecimalFormat.format(param.value, 2);
          },
        },
      };
      option["tooltip"] = {
        trigger: "item",
        formatter: (p: any) => {
          return `<div>月份:${p.name}</div><div>金额:${CurrencyFormat.format(
            p.value
          )}</div>`;
        },
      };
      historyTrendChart?.setOption(option);
    });
};

const reSize = (wh: any) => {
  historyTrendChart?.resize();
};

const show = computed<boolean>(
  () =>
    _.some(
      moduleStore.runningModules,
      (module) => module.id === props.module
    ) &&
    permissionStore.hasPermission(props.permission) &&
    _.includes(props.needRoles, userStore.currentRole)
);

onMounted(() => {
  historyTrend(12, "YEAR");
});
</script>
<template>
  <div class="info-card" v-resize="reSize" v-if="show">
    <div
      class="header"
      :class="{
        'header-left': headPosition === 'left',
        'header-center': headPosition === 'center',
      }"
    >
      <div class="top">
        <div class="title title_font">
          费用趋势<span class="sub_title_font">（单位：元）</span>
        </div>
        <div style="flex: auto"></div>
        <div class="operation_wrapper">
          <div class="operation">
            <div
              class="left"
              :class="[activeTreedYear === 'MONTH' ? 'active' : '']"
              @click="historyTrend(6, 'MONTH')"
            >
              近6月
            </div>
            <div class="line"></div>
            <div
              class="right"
              :class="[activeTreedYear === 'YEAR' ? 'active' : '']"
              @click="historyTrend(12, 'YEAR')"
            >
              近1年
            </div>
          </div>
        </div>
      </div>
      <div class="all_in_cost">
        <span class="describe">
          {{ _.minBy(historyTreed, "label")?.label + " – " }}
          {{ _.maxBy(historyTreed, "label")?.label + "：" }}
        </span>
        <span class="money">
          {{ CurrencyFormat.format(_.sumBy(historyTreed, "value")) }}
        </span>
      </div>
    </div>

    <div
      class="chart_wrapper"
      :style="{ height: chartHeight + 'px' }"
      v-loading="historyTrendLoading"
      ref="chartWrapper"
    ></div>
  </div>
</template>

<style scoped lang="scss">
.info-card {
  background: #ffffff;
  border-radius: 4px;
  padding: 24px;
  overflow: hidden;

  .header {
    .top {
      display: flex;
      height: 32px;
      .operation_wrapper {
        display: flex;
        height: 32px;
        margin-right: 24px;
        justify-content: flex-start;
        .operation {
          cursor: pointer;
          width: 115px;
          height: 100%;
          display: flex;
          border: 1px solid #bbbfc4;
          border-radius: 4px;
          font-weight: 500;
          font-size: 14px;
          color: #1f2329;

          .left {
            margin: 4px 0 4px 4px;
            padding: 2px 0;
            width: 50px;
            height: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
          }

          .right {
            margin: 4px 0 4px 8px;
            padding: 2px 0;
            width: 50px;
            height: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
          }
        }
      }
    }
    .all_in_cost {
      height: 20px;
      line-height: 20px;
      color: #646a73;
      .money {
        font-size: 16px;
        font-weight: 500;
        color: rgba(31, 35, 41, 1);
      }
    }

    .title_font {
      height: 24px;
      font-weight: 500;
      font-size: 16px;
      padding-bottom: 26px;
      color: #1f2329;
    }
  }
  .header-center {
    justify-content: center;
  }
  .header-left {
    justify-content: start;
  }

  .chart_wrapper {
    min-height: 200px;
    width: 100%;
    display: flex;
  }

  .active {
    border-radius: 4px;
    background: rgba(51, 112, 255, 0.1);
    color: #3370ff;
  }
}
</style>
