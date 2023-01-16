<script lang="ts" setup>
import { computed, onMounted, type Ref, ref } from "vue";
import { useModuleStore } from "@commons/stores/modules/module";
import _ from "lodash";
import { usePermissionStore } from "@commons/stores/modules/permission";

import { useUserStore } from "@commons/stores/modules/user";
import type { ECharts } from "echarts";
import Result from "@commons/request/Result";

import * as echarts from "echarts";

const props = withDefaults(
  defineProps<{
    needRoles: Array<"ADMIN" | "ORGADMIN" | "USER">;
    permission: any;
    module?: string;
    getHistoryTrend: (
      type: "MONTH" | "YEAR",
      historyNum: number,
      loading?: Ref<boolean>
    ) => Promise<Result<Array<any>>>;
    cardShadow?: "always" | "hover" | "never";
    headPosition?: "left" | "center";
  }>(),
  {
    headPosition: "center",
    module: "finance-management",
    cardShadow: "always",
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
    xAxis: {
      type: "category",
      data: data.map((d) => d.label),
      show: showx,
    },
    yAxis: {
      type: "value",
      show: showy,
    },
    series: [
      {
        data: data.map((d) => d.value),
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
        normal: {
          label: {
            show: true,
            position: "top",
            textStyle: {
              color: "black",
              fontSize: 12,
            },
            formatter: function (param: any) {
              return _.round(param.value, 2).toFixed(2);
            },
          },
        },
      };
      option["tooltip"] = {
        trigger: "item",
        formatter: (p: any) => {
          return `<div>月份:${p.name}</div><div>金额:${_.round(
            p.data,
            2
          ).toFixed(2)}</div>`;
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
  <el-card
    class="bill-trend"
    v-resize="reSize"
    v-if="show"
    :shadow="cardShadow"
  >
    <div
      class="header"
      :class="{
        'header-left': headPosition === 'left',
        'header-center': headPosition === 'center',
      }"
    >
      <div class="title title_font">
        费用趋势<span class="sub_title_font">（单位：元）</span>
      </div>
    </div>
    <div class="operation_wrapper">
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
      class="chart_wrapper"
      v-loading="historyTrendLoading"
      ref="chartWrapper"
    ></div>
    <span style="margin-left: 10px">
      {{ _.minBy(historyTreed, "label")?.label }} ～
      {{ _.maxBy(historyTreed, "label")?.label }}总费用为{{
        _.floor(_.sumBy(historyTreed, "value"), 2)
      }}
      元
    </span>
  </el-card>
</template>

<style scoped lang="scss">
.bill-trend {
  .header {
    display: flex;
    height: 20px;

    .title_font {
      height: 20px;
      font-weight: bold;
      font-size: 16px;
      padding-bottom: 26px;
    }
  }
  .header-center {
    justify-content: center;
  }
  .header-left {
    justify-content: start;
  }

  .operation_wrapper {
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

  .chart_wrapper {
    height: 100%;
    min-height: 200px;
    width: 100%;
    display: flex;
  }

  .active {
    border: 1px solid #006eff;
    background: #fff;
    color: #006eff;
  }
}
</style>
