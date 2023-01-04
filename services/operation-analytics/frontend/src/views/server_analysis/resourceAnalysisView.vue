<template>
  <div class="spread-layout">
    <div class="spread-header">
      <el-select v-model="paramAccountId" @change="getALlAccount()" class="m-2">
        <el-option label="全部云账号" value="all_list" />
        <el-option
          v-for="item in accounts"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
      <el-select v-model="paramHostId" @change="getHost()" class="m-2">
        <el-option label="全部宿主机" value="all_list" />
        <el-option
          v-for="item in hosts"
          :key="item.id"
          :label="item.hostName"
          :value="item.id"
        />
      </el-select>
    </div>
    <div class="spread-main">
      <div class="spread-main-up">
        <el-row :gutter="10">
          <el-col :span="8">
            <div class="myChart" style="height: 370px">
              <div class="echart-title">按云账号</div>
              <div class="echart-content">
                <div class="echart-content-left">
                  <el-row>
                    <el-col :span="24">
                      <ChartsSpeed
                        :height="300"
                        :options="spreadByAccountOption"
                        :ref="(el) => childRef(el, 'byAccount-chart')"
                      />
                    </el-col>
                  </el-row>
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="16">
            <div class="myChart" style="height: 370px">
              <div class="echart-title">
                <div class="echart-title-left">云主机增长趋势</div>
                <div class="echart-title-right">
                  <el-radio-group
                    v-model="paramVmIncreaseTrendMonth"
                    @change="getIncreaseTrend('byIncrease')"
                    style="margin-bottom: 20px"
                  >
                    <el-radio-button :label="6">近半年</el-radio-button>
                    <el-radio-button :label="12">近一年</el-radio-button>
                  </el-radio-group>
                </div>
              </div>
              <div style="position: relative">
                <ChartsSpeed
                  :height="300"
                  :options="increaseOption"
                  :ref="(el) => childRef(el, 'byIncrease-chart')"
                />
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="spread-main-content">
        <el-row :gutter="10">
          <el-col :span="8">
            <div class="myChart" style="height: 370px">
              <div class="echart-title">按运行状态</div>
              <div class="echart-content">
                <div class="echart-content-left">
                  <el-row>
                    <el-col :span="24">
                      <ChartsSpeed
                        :height="300"
                        :options="spreadByStatusOption"
                        :ref="(el) => childRef(el, 'byStatus-chart')"
                      />
                    </el-col>
                  </el-row>
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="16">
            <div class="myChart" style="height: 370px">
              <div class="echart-title">
                <div class="echart-title-left">按资源使用情况</div>
                <div class="echart-title-right">
                  <el-date-picker
                    style="width: 180px"
                    v-model="trendTime"
                    type="daterange"
                    unlink-panels
                    range-separator="至"
                    start-placeholder="开始时间"
                    end-placeholder="结束时间"
                    :shortcuts="shortcuts"
                    size="small"
                    @change="changeTimestamp"
                    @input="changeTimestamp"
                  />
                  <el-select
                    v-model="paramResourceUsedTrendType"
                    @change="getResourceTrendData('byResourceUsed')"
                    style="width: 100px; margin-bottom: 7px"
                    size="small"
                  >
                    <el-option label="CPU使用率" value="CPU_USED_UTILIZATION" />
                    <el-option
                      label="内存使用率"
                      value="MEMORY_USED_UTILIZATION"
                    />
                  </el-select>
                  <el-select
                    v-model="resourceUsedChartType"
                    @change="getResourceTrendData('byResourceUsed')"
                    style="width: 100px; margin-bottom: 7px"
                    size="small"
                  >
                    <el-option label="趋势" value="trend" />
                    <el-option label="占比" value="pie" />
                  </el-select>
                </div>
              </div>
              <div style="position: relative">
                <ChartsSpeed
                  :height="300"
                  :options="
                    resourceUsedChartType === 'trend'
                      ? resourceUsedTrendOption
                      : resourceUsedTrendPieOption
                  "
                  :ref="(el) => childRef(el, 'byResourceUsed-chart')"
                />
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="spread-main-bottom">
        <el-row :gutter="10">
          <el-col :span="8">
            <div class="myChart" style="height: 370px">
              <div class="echart-title">按付费方式</div>
              <div class="echart-content">
                <div class="echart-content-left">
                  <el-row>
                    <el-col :span="24">
                      <ChartsSpeed
                        :height="300"
                        :options="spreadByChargeTypeOption"
                        :ref="(el) => childRef(el, 'byChargeType-chart')"
                      />
                    </el-col>
                  </el-row>
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="16">
            <div class="myChart" style="height: 370px">
              <div class="echart-title">
                <div class="echart-title-left">
                  <el-select
                    v-model="paramDepartmentType"
                    @change="getSpreadByDepartmentData('byDepartmentType')"
                    style="width: 100px; margin-bottom: 7px"
                    size="small"
                  >
                    <el-option label="组织" value="org" />
                    <el-option label="工作空间" value="workspace" />
                  </el-select>
                </div>
                <div class="echart-title-right"></div>
              </div>
              <div style="position: relative">
                <ChartsSpeed
                  :height="300"
                  :options="spreadByDepartmentOption"
                  :ref="(el) => childRef(el, 'byDepartmentType-chart')"
                />
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { onMounted, ref } from "vue";
import _ from "lodash";
import ChartsSpeed from "../../components/echart/ChartsSpeed.vue";
import ResourceSpreadViewApi from "@/api/server_analysis/index";
import { ResourceAnalysisRequest } from "@/api/server_analysis/type";
//分布情况
const spreadByAccountOption = ref<any>({});
const spreadByStatusOption = ref<any>({});
const spreadByChargeTypeOption = ref<any>({});
const spreadByDepartmentOption = ref<any>({});
//增长趋势
const increaseOption = ref<any>({});
//资源使用情况趋势
const resourceUsedChartType = ref<string>("trend");
const resourceUsedTrendInfo = ref<any>([]);
const resourceUsedTrendOption = ref<any>({});
const resourceUsedTrendPieOption = ref<any>({});
//参数
const params = ref<ResourceAnalysisRequest | undefined>();
const paramAccountId = ref<string>("all_list");
const paramHostId = ref<string>("all_list");
const paramVmIncreaseTrendMonth = ref<number>(6);
const paramResourceUsedTrendType = ref<string>("CPU_USED_UTILIZATION");
const paramDepartmentType = ref<string>("org");
//下拉框数据
const accounts = ref<any>();
const hosts = ref<any>();
// 所有图表集合
const childRefMap: Map<string, any> = new Map();
const childRef = (el: any, chartName: any) => {
  childRefMap.set(chartName, el);
};
//初始化参数及数据
const initParam = () => {
  paramHostId.value = "all_list" as string;
  _.set(params, "hostIds", []);
  spreadByAccountOption.value = emptyOptions;
  spreadByStatusOption.value = emptyOptions;
  spreadByChargeTypeOption.value = emptyOptions;
  increaseOption.value = emptyOptions;
};
const getALlAccount = () => {
  initParam();
  ResourceSpreadViewApi.listAccounts().then((res) => {
    accounts.value = res.data;
    getHost();
  });
};
const getHost = () => {
  _.set(
    params,
    "accountIds",
    paramAccountId.value === "all_list" ? [] : paramAccountId.value
  );
  _.set(
    params,
    "hostIds",
    paramHostId.value === "all_list" ? [] : paramHostId.value
  );
  ResourceSpreadViewApi.listHost(params).then((res) => {
    hosts.value = res.data;
    getSpreadData("byAccount");
    getSpreadData("byStatus");
    getSpreadData("byChargeType");
    getIncreaseTrend("byIncrease");
    getResourceTrendData("byResourceUsed");
    getSpreadByDepartmentData("byDepartmentType");
  });
};
const getSpreadData = (spreadType: string) => {
  childRefMap.get(spreadType + "-chart").echartsClear();
  childRefMap.get(spreadType + "-chart").echartsLoading();
  _.set(
    params,
    "accountIds",
    paramAccountId.value === "all_list" ? [] : paramAccountId.value
  );
  _.set(
    params,
    "hostIds",
    paramHostId.value === "all_list" ? [] : paramHostId.value
  );
  ResourceSpreadViewApi.getSpreadData(params).then((res) => {
    if (res.data) {
      const spreadData = res.data;
      const options = _.cloneDeep(defaultPieOptions);
      _.set(options, "series[0].data", spreadData[spreadType]);
      if (spreadType === "byAccount") {
        spreadByAccountOption.value = options;
      }
      if (spreadType === "byStatus") {
        spreadByStatusOption.value = options;
      }
      if (spreadType === "byChargeType") {
        spreadByChargeTypeOption.value = options;
      }
      childRefMap.get(spreadType + "-chart").hideEchartsLoading();
    }
  });
};
//增长趋势
const getIncreaseTrend = (chartName: string) => {
  childRefMap.get(chartName + "-chart").echartsClear();
  childRefMap.get(chartName + "-chart").echartsLoading();
  _.set(params, "monthNumber", paramVmIncreaseTrendMonth.value);
  const xAxis: any[] = [],
    yAxis: any[] = [];
  ResourceSpreadViewApi.getIncreaseTrend(params).then((res) => {
    let chartData = res.data;
    chartData = _.sortBy(chartData, function (o) {
      return o.xaxis;
    });
    chartData.forEach(function (item: any) {
      if (_.indexOf(xAxis, item.xaxis) === -1) {
        xAxis.push(item.xaxis);
        yAxis.push(item.yaxis);
      }
    });
    const options = _.cloneDeep(defaultLineOption);
    _.set(options, "series[0].data", yAxis);
    _.set(options, "xAxis.data", xAxis);
    increaseOption.value = options;
    childRefMap.get(chartName + "-chart").hideEchartsLoading();
  });
};
//资源使用趋势
const getResourceTrendData = (chartName: string) => {
  childRefMap.get(chartName + "-chart").echartsClear();
  childRefMap.get(chartName + "-chart").echartsLoading();
  _.set(params, "metricName", paramResourceUsedTrendType.value);
  _.set(params, "startTime", timestampData.value[0].getTime());
  _.set(params, "endTime", timestampData.value[1].getTime());
  ResourceSpreadViewApi.getResourceTrendData(params).then((res) => {
    resourceUsedTrendInfo.value = res.data;
    if ("trend" === resourceUsedChartType.value) {
      resourceUsedTrendOption.value = getTrendOptions(
        _.cloneDeep(defaultTrendOptions)
      );
    }
    if ("pie" === resourceUsedChartType.value) {
      resourceUsedTrendPieOption.value = getTrendPieOptions(
        _.cloneDeep(defaultPieOptions)
      );
    }
    childRefMap.get(chartName + "-chart").hideEchartsLoading();
  });
};
//部门分布
const getSpreadByDepartmentData = (chartName: string) => {
  childRefMap.get(chartName + "-chart").echartsClear();
  childRefMap.get(chartName + "-chart").echartsLoading();
  spreadByDepartmentOption.value = _.cloneDeep(emptyOptions);
  childRefMap.get(chartName + "-chart").hideEchartsLoading();
};
const getTrendPieOptions = (options: any) => {
  let legend: any[] = [],
    series: any = {},
    xAxis: any[] = [],
    seriesData: any[] = [];
  if (!resourceUsedTrendInfo.value && resourceUsedTrendInfo.value.length == 0) {
    return emptyOptions;
  }
  if (resourceUsedTrendInfo.value) {
    const chartData = resourceUsedTrendInfo.value;
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
    Object.keys(series).forEach(function (key) {
      const data = series[key];
      if (data.length > 0) {
        const items = {
          name: key,
          value: data[data.length - 1][1],
        };
        seriesData.push(items);
      }
    });
    _.set(options, "series[0].data", seriesData);
    _.set(options, "series[0].name", "");
    _.set(options, "series[0].radius", "70%");
  }
  return options;
};
const getTrendOptions = (options: any) => {
  let legend: any[] = [],
    series: any = {},
    xAxis: any[] = [],
    seriesData: any[] = [];
  if (!resourceUsedTrendInfo.value && resourceUsedTrendInfo.value.length == 0) {
    return emptyOptions;
  }
  if (resourceUsedTrendInfo.value) {
    const chartData = resourceUsedTrendInfo.value;
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
    for (const name in series) {
      const data = series[name];
      const items = {
        name: name,
        type: "line",
        stack: "Total",
        label: {
          show: true,
          position: "top",
        },
        areaStyle: {},
        emphasis: {
          focus: "series",
        },
        data: data,
      };
      seriesData.push(items);
    }
    _.set(options, "legend.data", legend);
    _.set(options, "xAxis[0].data", xAxis);
    _.set(options, "series", seriesData);
  }
  return options;
};
onMounted(() => {
  getALlAccount();
});
/**
 * 默认时间当前时间过去7天
 */
const timestampData = ref<[Date, Date]>([
  new Date(new Date().getTime() - 3600 * 1000 * 24 * 7),
  new Date(),
]);
const trendTime = ref(timestampData);
/**
 * 时间调整
 */
const changeTimestamp = () => {
  console.log(trendTime.value);
  getResourceTrendData("byResourceUsed");
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
// 默认显示的图表配置数据
const emptyOptions = {
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
const defaultPieOptions = {
  tooltip: {
    trigger: "item",
  },
  legend: {
    type: "scroll",
  },
  series: [
    {
      name: "",
      type: "pie",
      radius: "50%",
      center: ["50%", "50%"],
      data: [{ value: 0 }],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: "rgba(0, 0, 0, 0.5)",
        },
      },
    },
  ],
};
const defaultTrendOptions = {
  tooltip: {
    trigger: "axis",
    axisPointer: {
      type: "cross",
      label: {
        backgroundColor: "#6a7985",
      },
    },
  },
  legend: {
    data: ["80%-100%", "60%-80%", "40%-60%", "20%-40%", "0%-20%"],
  },
  grid: {
    left: "3%",
    right: "4%",
    bottom: "3%",
    containLabel: true,
  },
  xAxis: [
    {
      type: "category",
      boundaryGap: false,
      data: [],
    },
  ],
  yAxis: [
    {
      type: "value",
    },
  ],
  series: [],
};
const defaultLineOption = {
  tooltip: {
    trigger: "axis",
    axisPointer: {
      type: "cross",
      label: {
        backgroundColor: "#6a7985",
      },
    },
  },
  xAxis: {
    type: "category",
    boundaryGap: false,
    data: [],
  },
  yAxis: {
    type: "value",
  },
  series: [
    {
      data: [],
      type: "line",
      areaStyle: {},
    },
  ],
};
</script>

<style scoped>
.spread-layout {
  height: 100% !important;
  width: 100% !important;
}
.myChart {
  height: 300px;
  border: 1px solid #e5e5e5;
}
.spread-main {
  margin-top: 10px;
  height: 99% !important;
  width: 99% !important;
  overflow: auto;
}
.spread-main-content {
  margin-top: 10px;
}
.spread-main-bottom {
  margin-top: 10px;
  margin-bottom: 30px;
}
.echart-title {
  padding: 20px;
  height: 20px;
}
.echart-title-left {
  float: left;
}
.echart-title-right {
  float: right;
  margin-top: -5px;
}

.echart-content {
}

.echart-content-left {
  position: relative;
  padding: 7px;
  margin-left: 25px;
}
.echart-content-right {
  position: relative;
  padding: 7px;
  margin-left: 20px;
}

.echart-right {
  position: absolute;
  top: 15%;
  left: 50%;
  /*width: 150px;*/
  height: 150px;
}
.echart-left {
  position: absolute;
}
.echart-right div {
  padding: 5px;
}
:deep(.el-descriptions__cell) {
  border: 0 !important;
}
:deep(.is-bordered-label) {
  background-color: transparent !important;
}
:deep(.el-descriptions__cell) {
  font-size: 12px !important;
}
</style>
