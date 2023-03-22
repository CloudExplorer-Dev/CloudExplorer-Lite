<script lang="ts" setup>
import { computed, onMounted, ref } from "vue";
import _ from "lodash";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useUserStore } from "@commons/stores/modules/user";
import {
  defaultTrendOptions,
  emptyOptions,
  getRandomColor,
  trendSeriesColor,
} from "@commons/components/echart";
import * as echarts from "echarts";

import API from "./api";
import ChartsSpeed from "@commons/components/echart/ChartsSpeed.vue";

//参数
const params = {};

const props = withDefaults(
  defineProps<{
    needRoles?: Array<"ADMIN" | "ORGADMIN" | "USER">;
    permission?: any;
    module?: string;
  }>(),
  {
    needRoles: () => ["ADMIN", "ORGADMIN", "USER"],
    module: "operation-analysis",
    permission: [
      "[operation-analysis]SERVER_ANALYSIS:READ",
      "[operation-analysis]OVERVIEW:READ",
    ],
  }
);

const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();
const userStore = useUserStore();

const childRefMap: Map<string, any> = new Map();
const childRef = (el: any, chartName: any) => {
  childRefMap.set(chartName, el);
};

const paramVmIncreaseTrendMonth = ref<any>("7");

//趋势
const increaseOption = ref<any>({});

//趋势
const getIncreaseTrend = (chartName: string) => {
  if (!show.value) {
    return;
  }
  childRefMap.get(chartName + "-chart").echartsClear();
  childRefMap.get(chartName + "-chart").echartsLoading();
  _.set(params, "dayNumber", paramVmIncreaseTrendMonth.value);
  let legend: any[] = [],
    series: any = {},
    xAxis: any[] = [];
  const seriesData: any[] = [];
  API.getIncreaseTrend(params).then((res) => {
    const options = _.cloneDeep(defaultTrendOptions);
    const chartData = res.data;
    chartData.forEach(function (item: any) {
      if (_.indexOf(xAxis, item.xaxis) === -1) {
        xAxis.push(item.xaxis);
      }
    });
    xAxis = _.sortBy(xAxis);
    chartData.forEach(function (item: any) {
      const name = item.groupName;
      if (_.indexOf(legend, name) === -1) {
        legend.push(name);
        series[name] = [];
        for (let i = 0; i < xAxis.length; i++) {
          const d = _.find(chartData, { groupName: name, xaxis: xAxis[i] });
          series[name][i] = [xAxis[i], d ? d.yaxis : 0];
        }
      }
    });
    legend = _.sortBy(legend);
    const tmpSeries = [];
    for (let i = 0; i < legend.length; i++) {
      tmpSeries[legend[i]] = series[legend[i]];
    }
    series = tmpSeries;
    let colorIndex = 0;
    for (const name in series) {
      const data = series[name];
      const items = {
        name: name,
        type: "line",
        stack: "Total",
        smooth: true,
        lineStyle: {
          width: 0,
        },
        showSymbol: false,
        areaStyle: {
          opacity: 0.8,
          color: new echarts.graphic.LinearGradient(
            0,
            0,
            0,
            1,
            colorIndex >= 5
              ? getRandomColor()
              : _.nth(trendSeriesColor, colorIndex)
          ),
        },
        emphasis: {
          focus: "series",
        },
        data: data,
      };
      colorIndex++;
      seriesData.push(items);
    }
    _.set(options, "legend.bottom", "3%");
    _.set(options, "grid.bottom", "18%");
    _.set(options, "legend.data", legend);
    _.set(options, "xAxis[0].data", xAxis);
    _.set(options, "series", seriesData);
    increaseOption.value = options;
    childRefMap.get(chartName + "-chart").hideEchartsLoading();
  });
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
  _.set(params, "hostIds", []);
  _.set(params, "accountIds", []);
  _.set(params, "hostIds", []);
  increaseOption.value = emptyOptions;

  getIncreaseTrend("byIncrease");
});
</script>
<template>
  <div class="info-card" v-if="show">
    <div class="echart-title">
      <div class="echart-title-left">云主机趋势</div>
      <div class="echart-title-right">
        <el-select
          v-model="paramVmIncreaseTrendMonth"
          @change="getIncreaseTrend('byIncrease')"
          style="width: 100px; margin-bottom: 7px"
          size="small"
        >
          <el-option label="近7天" value="7" />
          <el-option label="近30天" value="30" />
          <el-option label="近半年" value="180" />
          <el-option label="近一年" value="360" />
        </el-select>
      </div>
    </div>
    <div style="position: relative">
      <ChartsSpeed
        :height="200"
        :options="increaseOption"
        :ref="(el) => childRef(el, 'byIncrease-chart')"
      />
    </div>
  </div>
</template>

<style scoped lang="scss">
.info-card {
  background: #ffffff;
  border-radius: 4px;
  padding: 24px;
  overflow: hidden;

  .echart-title {
    height: 20px;
    font-weight: bold;
    font-size: 16px;
    padding-bottom: 26px;
  }
  .echart-title-left {
    float: left;
  }
  .echart-title-right {
    float: right;
    margin-top: -5px;
  }
}
</style>
