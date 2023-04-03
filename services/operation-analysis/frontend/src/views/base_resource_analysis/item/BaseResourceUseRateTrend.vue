<template>
  <el-card shadow="never" class="info-card">
    <el-row :gutter="16">
      <el-col :span="8">
        <div class="title">计算资源按使用率分布</div>
      </el-col>
      <el-col :span="16" style="text-align: right">
        <div class="search-box">
          <el-date-picker
            style="width: 200px; height: 32px; margin-right: 16px"
            v-model="trendTime"
            type="daterange"
            v-if="showTrendTime"
            unlink-panels
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            :shortcuts="shortcuts"
            size="small"
            @change="changeTimestamp"
            @input="changeTimestamp"
          />
          <el-radio-group
            style="min-width: 300px"
            class="custom-radio-group"
            v-model="basicResourceType"
            :on-change="getResourceTrendData()"
          >
            <el-radio-button label="CPU_USED_UTILIZATION"
              >CPU使用率</el-radio-button
            >
            <el-radio-button label="MEMORY_USED_UTILIZATION"
              >内存使用率</el-radio-button
            >
            <el-radio-button label="DATASTORE_USED_UTILIZATION"
              >存储器使用率</el-radio-button
            >
          </el-radio-group>
        </div>
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
import type { ResourceAnalysisRequest } from "@commons/api/resource_spread_view/type";
import type { ECBasicOption } from "echarts/types/src/util/types";
import ResourceSpreadViewApi from "@/api/resource_spread_view";
const props = defineProps<{
  cloudAccountId?: string | undefined;
  clusterId?: string | undefined;
  datastoreId?: string | undefined;
  hostId?: string | undefined;
}>();
/**
 * 默认时间当前时间过去7天
 */
const timestampData = ref<[Date, Date]>([
  new Date(new Date().getTime() - 3600 * 1000 * 24 * 7),
  new Date(),
]);
const trendTime = ref(timestampData);
const showTrendTime = ref<boolean>(true);
const basicResourceType = ref<string>("CPU_USED_UTILIZATION");
const params = ref<ResourceAnalysisRequest>();
const loading = ref<boolean>(false);
const apiData = ref<any>();

const setParams = () => {
  props.cloudAccountId
    ? _.set(
        params,
        "accountIds",
        props.cloudAccountId === "all" ? [] : [props.cloudAccountId]
      )
    : "";
  props.clusterId
    ? _.set(
        params,
        "clusterIds",
        props.clusterId === "all" ? [] : [props.clusterId]
      )
    : "";
  props.datastoreId
    ? _.set(
        params,
        "datastoreIds",
        props.datastoreId === "all" ? [] : [props.datastoreId]
      )
    : "";
  props.hostId
    ? _.set(params, "resourceIds", props.hostId === "all" ? [] : [props.hostId])
    : "";
  _.set(params, "entityType", "HOST");
  _.set(params, "metricName", basicResourceType.value);
  _.set(params, "startTime", timestampData.value[0].getTime());
  _.set(params, "endTime", timestampData.value[1].getTime());
  if (basicResourceType.value === "DATASTORE_USED_UTILIZATION") {
    _.set(params, "entityType", "DATASTORE");
    _.set(
      params,
      "resourceIds",
      props.datastoreId === "all" ? [] : [props.datastoreId]
    );
  }
};

const getResourceTrendData = () => {
  setParams();
  ResourceSpreadViewApi.getResourceTrendData(params, loading).then((res) => {
    apiData.value = res.data;
  });
};

const options = computed<ECBasicOption>(() => {
  const trendInfo = apiData.value;
  const options = _.cloneDeep(defaultTrendOptions);
  let legend: any[] = [],
    series: any = {},
    xAxis: any[] = [];
  const seriesData: any[] = [];
  if (!trendInfo) {
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
  const chartData = trendInfo;
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
      data: data,
    };
    colorIndex++;
    seriesData.push(items);
  }
  _.set(options, "legend.data", legend);
  _.set(options, "xAxis[0].data", xAxis);
  _.set(options, "series", seriesData);
  return options;
});

watch(
  props,
  () => {
    getResourceTrendData();
  },
  { immediate: true }
);
const color: Array<string> = [
  "rgb(98, 210, 85, 1)",
  "rgb(22, 225, 198, 1)",
  "rgb(79, 131, 253, 1)",
  "rgb(251, 218, 110, 1)",
  "rgb(250, 139, 135, 1)",
];
const defaultTrendOptions = {
  color: color,
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
    data: [],
    y: "bottom",
  },
  toolbox: {
    feature: {
      // saveAsImage: {}
    },
  },
  grid: {
    left: "0%",
    right: "0%",
    top: "19px",
    bottom: "10%",
    containLabel: true,
  },
  xAxis: [
    {
      type: "category",
      boundaryGap: false,
      data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
      axisLabel: {
        showMaxLabel: false,
        showMinLabel: false,
      },
    },
  ],
  yAxis: [
    {
      type: "value",
      splitLine:{
        show:false
      }
    },
  ],
  series: [],
};

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

/**
 * 时间调整
 */
const changeTimestamp = () => {
  console.log(trendTime.value);
  getResourceTrendData();
};
//快捷时间段
const shortcuts = [
  {
    text: "最近7天",
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
      return [start, end];
    },
  },
  {
    text: "最近30天",
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
      return [start, end];
    },
  },
  {
    text: "最近3个月",
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
      return [start, end];
    },
  },
];
</script>
<style scoped lang="scss">
.info-card {
  height: 350px;
  background: #ffffff;
  border-radius: 4px;
  flex: none;
  order: 0;
  flex-grow: 0;
}
.chart {
  min-height: 262px;
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
.search-box {
  display: inline-flex;
}
</style>
