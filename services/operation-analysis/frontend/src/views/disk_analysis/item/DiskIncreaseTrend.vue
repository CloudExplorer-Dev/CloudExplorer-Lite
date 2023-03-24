<template>
  <el-card shadow="never" class="info-card">
    <el-row>
      <el-col :span="10">
        <div class="title">磁盘趋势</div>
      </el-col>
      <el-col :span="14" style="text-align: right">
        <el-radio-group
          class="custom-radio-group"
          v-model="paramDiskIncreaseTrendMonth"
          @change="getIncreaseTrend()"
        >
          <el-radio-button label="7">近7天</el-radio-button>
          <el-radio-button label="30">近30天</el-radio-button>
          <el-radio-button label="180">近半年</el-radio-button>
          <el-radio-button label="360">近一年</el-radio-button>
        </el-radio-group>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="24">
        <div class="echarts">
          <div class="echarts-content">
            <v-chart
              class="chart"
              :option="options"
              v-loading="loading"
              autoresize
            />
          </div>
        </div>
      </el-col>
    </el-row>
  </el-card>
</template>
<script setup lang="ts">
import VChart from "vue-echarts";
import { computed, ref, watch } from "vue";
import _ from "lodash";
import type { ResourceAnalysisRequest } from "@/api/disk_analysis/type";
import type { ECBasicOption } from "echarts/types/src/util/types";
import ResourceSpreadViewApi from "@/api/disk_analysis/index";
import * as echarts from "echarts";
const props = defineProps<{
  cloudAccountId?: string | undefined;
  currentUnit?: string | undefined;
}>();
const params = ref<ResourceAnalysisRequest>();
const paramDiskIncreaseTrendMonth = ref<string>("7");
const loading = ref<boolean>(false);
const apiData = ref<any>();

const getIncreaseTrend = () => {
  _.set(params, "dayNumber", paramDiskIncreaseTrendMonth.value);
  _.set(
    params,
    "accountIds",
    props.cloudAccountId === "all" ? [] : [props.cloudAccountId]
  );
  _.set(params, "statisticalBlock", props.currentUnit === "block");
  ResourceSpreadViewApi.getIncreaseTrend(params, loading).then(
    (res) => (apiData.value = res.data)
  );
};

const options = computed<ECBasicOption>(() => {
  const obj = apiData.value;
  let legend: any[] = [],
    series: any = {},
    xAxis: any[] = [];
  const seriesData: any[] = [];
  const options = _.cloneDeep(defaultSpeedOptions);
  if (obj) {
    obj.forEach(function (item: any) {
      if (_.indexOf(xAxis, item.xaxis) === -1) {
        xAxis.push(item.xaxis);
      }
    });
    xAxis = _.sortBy(xAxis);
    obj.forEach(function (item: any) {
      const name = item.groupName;
      if (_.indexOf(legend, name) === -1) {
        legend.push(name);
        series[name] = [];
        for (let i = 0; i < xAxis.length; i++) {
          const d = _.find(obj, { groupName: name, xaxis: xAxis[i] });
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
          width: 2,
          opacity: 0.7,
        },
        showSymbol: false,
        areaStyle: {
          opacity: 0.1,
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
    _.set(options, "legend.data", legend);
    _.set(options, "xAxis[0].data", xAxis);
    _.set(options, "series", seriesData);
  } else {
    return {
      title: {
        text: "暂无数据",
        x: "center",
        y: "center",
        textStyle: {
          fontSize: 12,
          fontWeight: "normal",
        },
      },
    };
  }
  return options;
});

watch(
  props,
  () => {
    getIncreaseTrend();
  },
  { immediate: true }
);

const defaultSpeedOptions = {
  color: ["#80FFA5", "#00DDFF", "#0080ff", "#FFBF00", "#FF0087", "#37A2FF"],
  title: {},
  tooltip: {
    trigger: "axis",
    axisPointer: {
      lineStyle: {
        type: "solid",
      },
      label: {
        backgroundColor: "#6a7985",
      },
    },
  },
  legend: {
    type: "scroll",
    data: ["Line 1", "Line 2", "Line 3", "Line 4", "Line 5"],
    y: "bottom",
  },
  toolbox: {
    feature: {
      // saveAsImage: {}
    },
  },
  grid: {
    left: "3%",
    right: "4%",
    top: "17px",
    bottom: "15%",
    containLabel: true,
  },
  xAxis: [
    {
      type: "category",
      boundaryGap: false,
      data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
    },
  ],
  yAxis: [
    {
      type: "value",
    },
  ],
  series: [
    {
      name: "Line 1",
      type: "line",
      stack: "Total",
      smooth: true,
      lineStyle: {
        width: 2,
        opacity: 0.7,
      },
      showSymbol: false,
      areaStyle: {
        opacity: 0.1,
      },
      emphasis: {
        focus: "series",
      },
      data: [140, 232, 101, 264, 90, 340, 250],
    },
  ],
};
/**
 * 颜色
 */
const trendSeriesColor = [
  [
    {
      offset: 0,
      color: "rgb(128, 255, 165)",
    },
    {
      offset: 1,
      color: "rgb(1, 191, 236)",
    },
  ],
  [
    {
      offset: 0,
      color: "rgb(0, 221, 255)",
    },
    {
      offset: 1,
      color: "rgb(77, 119, 255)",
    },
  ],
  [
    {
      offset: 0,
      color: "rgb(55, 162, 255)",
    },
    {
      offset: 1,
      color: "rgb(116, 21, 219)",
    },
  ],
  [
    {
      offset: 0,
      color: "rgb(255, 191, 0)",
    },
    {
      offset: 1,
      color: "rgb(224, 62, 76)",
    },
  ],
  [
    {
      offset: 0,
      color: "rgb(255, 0, 135)",
    },
    {
      offset: 1,
      color: "rgb(135, 0, 157)",
    },
  ],
];
/**
 * 随机颜色
 */
const getRandomColor = () => {
  const randomColor = [
    {
      offset: 0,
      color:
        "rgb(" +
        _.random(0, 255) +
        ", " +
        _.random(0, 255) +
        ", " +
        _.random(0, 255) +
        ")",
    },
    {
      offset: 1,
      color:
        "rgb(" +
        _.random(0, 255) +
        ", " +
        _.random(0, 255) +
        ", " +
        _.random(0, 255) +
        ")",
    },
  ];
  return randomColor;
};
</script>
<style scoped lang="scss">
.info-card {
  height: 295px;
  min-width: 400px;
}
.chart {
  min-height: 214px;
  width: 100%;
}
.title {
  font-weight: bold;
  font-size: 16px;
  padding-top: 4px;
  padding-bottom: 16px;
}
.echarts-content {
  margin-top: 7px;
}
.echarts-footer {
  margin-top: 15px;
  margin-bottom: 10px;
  position: initial;
  font-family: "PingFang SC", serif;
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 12px;
  text-align: center;
  color: #1f2329;
}
.back-btn {
  margin-left: 5%;
  position: absolute;
  z-index: 10;
  cursor: pointer;
}
</style>
