<template>
  <div class="spread-layout">
    <div class="spread-header">
      <el-select v-model="paramAccountId" @change="getAccounts()" class="m-2">
        <el-option label="全部云账号" value="all_list" />
        <el-option
          v-for="item in accounts"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
      <el-select v-model="paramClusterId" @change="getClusters()" class="m-2">
        <el-option label="全部集群" value="all_list" />
        <el-option
          v-for="item in clusters"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
      <el-select v-model="paramHostId" @change="getHosts()" class="m-2">
        <el-option label="全部宿主机" value="all_list" />
        <el-option
          v-for="item in hosts"
          :key="item.id"
          :label="item.hostName"
          :value="item.id"
        />
      </el-select>
      <el-select
        v-model="paramDatastoreId"
        @change="getDatastores()"
        class="m-2"
      >
        <el-option label="全部存储器" value="all_list" />
        <el-option
          v-for="item in datastroes"
          :key="item.id"
          :label="item.datastoreName"
          :value="item.id"
        />
      </el-select>
    </div>
    <div class="spread-main">
      <div class="spread-main-up">
        <el-row :gutter="10">
          <el-col :span="24">
            <div class="myChart" style="height: 300px">
              <div class="echart-title">
                <div class="echart-title-left">基础资源分配情况</div>
              </div>
              <div class="echart-content">
                <el-row>
                  <el-col :span="8">
                    <div class="echart-content-left">
                      <el-row>
                        <el-col :span="12">
                          <ChartsSpeed
                            :height="200"
                            :options="allocatedComputerCpuOption"
                            :ref="(el) => childRef(el, 'cpu')"
                          />
                        </el-col>
                        <el-col :span="12">
                          <div class="echart-right">
                            <el-descriptions :column="1" size="small" border>
                              <el-descriptions-item
                                label-align="right"
                                label="总量"
                                >{{
                                  allocatedInfo.cpu?.total
                                }}核</el-descriptions-item
                              >
                              <el-descriptions-item
                                label-align="right"
                                label="已分配"
                                >{{
                                  allocatedInfo.cpu?.allocated
                                }}核</el-descriptions-item
                              >
                              <el-descriptions-item
                                label-align="right"
                                label="未分配"
                                >{{
                                  allocatedInfo.cpu?.free
                                }}核</el-descriptions-item
                              >
                            </el-descriptions>
                          </div>
                        </el-col>
                      </el-row>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="echart-content-right">
                      <el-row>
                        <el-col :span="12">
                          <ChartsSpeed
                            :height="200"
                            :options="allocatedComputerMemoryOption"
                            :ref="(el) => childRef(el, 'memory')"
                          />
                        </el-col>
                        <el-col :span="8">
                          <div class="echart-right">
                            <el-descriptions :column="1" size="small" border>
                              <el-descriptions-item
                                label-align="right"
                                label="总量"
                                >{{
                                  allocatedInfo.memory?.total
                                }}GB</el-descriptions-item
                              >
                              <el-descriptions-item
                                label-align="right"
                                label="已分配"
                                >{{
                                  allocatedInfo.memory?.allocated
                                }}GB</el-descriptions-item
                              >
                              <el-descriptions-item
                                label-align="right"
                                label="未分配"
                                >{{
                                  allocatedInfo.memory?.free
                                }}GB</el-descriptions-item
                              >
                            </el-descriptions>
                          </div>
                        </el-col>
                      </el-row>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="echart-content-right">
                      <el-row>
                        <el-col :span="12">
                          <ChartsSpeed
                            :height="200"
                            :options="allocatedDatastoreOption"
                            :ref="(el) => childRef(el, 'datastore')"
                          />
                        </el-col>
                        <el-col :span="8">
                          <div class="echart-right">
                            <el-descriptions :column="1" size="small" border>
                              <el-descriptions-item
                                label-align="right"
                                label="总量"
                                >{{
                                  allocatedInfo.datastore?.total
                                }}GB</el-descriptions-item
                              >
                              <el-descriptions-item
                                label-align="right"
                                label="已分配"
                                >{{
                                  allocatedInfo.datastore?.allocated
                                }}GB</el-descriptions-item
                              >
                              <el-descriptions-item
                                label-align="right"
                                label="未分配"
                                >{{
                                  allocatedInfo.datastore?.free
                                }}GB</el-descriptions-item
                              >
                            </el-descriptions>
                          </div>
                        </el-col>
                      </el-row>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="spread-main-content">
        <el-row :gutter="10">
          <el-col :span="8">
            <div class="myChart">
              <div class="echart-title">宿主机分布</div>
              <div style="position: relative">
                <ChartsSpeed
                  :height="300"
                  :options="spreadHostOption"
                  :ref="(el) => childRef(el, 'spread-host')"
                />
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="myChart">
              <div class="echart-title">存储器分布</div>
              <div style="position: relative">
                <ChartsSpeed
                  :height="300"
                  :options="spreadDatastoreOption"
                  :ref="(el) => childRef(el, 'spread-datastore')"
                />
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="myChart">
              <div class="echart-title">
                <div class="echart-title-left">宿主机上云主机分布</div>
                <div class="echart-title-right">
                  <el-select
                    v-model="paramVmStatus"
                    @change="
                      getSpreadComputerInfo('宿主机上云主机分布', 'pie', 'vm')
                    "
                    style="width: 100px"
                    size="small"
                  >
                    <el-option label="全部状态" value="all" />
                    <el-option label="运行中" value="running" />
                    <el-option label="已关机" value="stopped" />
                  </el-select>
                </div>
              </div>
              <div style="position: relative">
                <div>
                  <ChartsSpeed
                    :height="300"
                    :options="spreadVmOption"
                    :ref="(el) => childRef(el, 'spread-vm')"
                  />
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="spread-main-bottom">
        <el-row :gutter="10">
          <el-col :span="24">
            <div class="myChart" style="height: 370px">
              <div class="echart-title">
                <div class="echart-title-left">计算/存储资源使用情况</div>
                <div class="echart-title-right">
                  <el-date-picker
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
                  <el-select
                    v-model="paramTrendType"
                    @change="getResourceTrendData('chart')"
                    style="width: 100px; margin-bottom: 7px"
                    size="small"
                  >
                    <el-option label="CPU使用率" value="CPU_USED_UTILIZATION" />
                    <el-option
                      label="内存使用率"
                      value="MEMORY_USED_UTILIZATION"
                    />
                    <el-option
                      label="存储器使用率"
                      value="DATASTORE_USED_UTILIZATION"
                    />
                  </el-select>
                  <el-select
                    v-if="false"
                    v-model="trendChartType"
                    @change="getResourceTrendData('chart')"
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
                    trendChartType === 'trend' ? trendOption : trendPieOption
                  "
                  :ref="(el) => childRef(el, 'trend-chart')"
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
import ChartsSpeed from "@commons/components/echart/ChartsSpeed.vue";
import ResourceSpreadViewApi from "@/api/resource_spread_view/index";
import { ResourceAnalysisRequest } from "@commons/api/resource_spread_view/type";
import {
  defaultSpeedOptions,
  defaultPieDoughnutOptions,
  defaultTrendOptions,
  emptyOptions,
  trendSeriesColor,
  getRandomColor,
} from "@commons/components/echart";
import * as echarts from "echarts";
//分配情况
const allocatedInfo = ref<any>({});
const allocatedComputerCpuOption = ref<any>({});
const allocatedComputerMemoryOption = ref<any>({});
const allocatedDatastoreOption = ref<any>({});
//分布情况
const spreadInfo = ref<any>({});
const spreadHostOption = ref<any>({});
const spreadDatastoreOption = ref<any>({});
const spreadVmOption = ref<any>({});
//趋势
const trendInfo = ref<any>({});
const trendOption = ref<any>({});
const trendPieOption = ref<any>({});
//参数
const params = ref<ResourceAnalysisRequest | undefined>();
const paramAccountId = ref<string>("all_list");
const paramClusterId = ref<string>();
const paramHostId = ref<string>();
const paramDatastoreId = ref<string>();
const paramVmStatus = ref<string>();
const paramTrendType = ref<string>("CPU_USED_UTILIZATION");
const trendChartType = ref<string>("trend");
//下拉框数据
const accounts = ref<any>();
const clusters = ref<any>();
const hosts = ref<any>();
const datastroes = ref<any>();

// 所有图表集合
const childRefMap: Map<string, any> = new Map();
const childRef = (el: any, chartName: any) => {
  childRefMap.set(chartName, el);
};
//初始化参数及数据
const initParam = () => {
  paramClusterId.value = "all_list" as string;
  paramHostId.value = "all_list" as string;
  paramDatastoreId.value = "all_list" as string;
  paramVmStatus.value = "all" as string;
  _.set(params, "clusterIds", []);
  _.set(params, "hostIds", []);
  _.set(params, "datastoreIds", []);
  _.set(params, "vmStatus", "all");
  allocatedComputerCpuOption.value = emptyOptions;
  allocatedComputerMemoryOption.value = emptyOptions;
  allocatedDatastoreOption.value = emptyOptions;
  spreadHostOption.value = emptyOptions;
  spreadVmOption.value = emptyOptions;
  spreadDatastoreOption.value = emptyOptions;
};

// 查询所有云账号
const getAccounts = () => {
  initParam();
  _.set(
    params,
    "accountIds",
    paramAccountId.value === "all_list" ? [] : paramAccountId.value
  );
  ResourceSpreadViewApi.listAccounts().then((res) => {
    if (res.data.length) {
      accounts.value = res.data;
      getClusters();
    }
  });
};
//改变集群时初始化集群下资源
const getClusters = () => {
  _.set(params, "hostIds", []);
  _.set(params, "datastoreIds", []);
  _.set(params, "vmStatus", "all");
  paramHostId.value = "all_list" as string;
  paramDatastoreId.value = "all_list" as string;
  paramVmStatus.value = "all" as string;
  _.set(
    params,
    "clusterIds",
    paramClusterId.value === "all_list" ? [] : paramClusterId.value
  );
  ResourceSpreadViewApi.listClusters(params).then((res) => {
    clusters.value = res.data;
    getHosts();
    getDatastores();
  });
};
const getHosts = () => {
  _.set(
    params,
    "hostIds",
    paramHostId.value === "all_list" ? [] : paramHostId.value
  );
  ResourceSpreadViewApi.listHost(params).then((res) => {
    hosts.value = res.data;
    getAllocatedComputerInfo("CPU分配率", "speed", "cpu");
    getAllocatedComputerInfo("内存分配率", "speed", "memory");
    getSpreadComputerInfo("宿主机分布", "pie", "host");
    getSpreadComputerInfo("宿主机上云主机分布", "pie", "vm");
    getResourceTrendData("chart");
  });
};
const getDatastores = () => {
  _.set(
    params,
    "datastoreIds",
    paramDatastoreId.value === "all_list" ? [] : paramDatastoreId.value
  );
  ResourceSpreadViewApi.listDatastores(params).then((res) => {
    datastroes.value = res.data;
    getAllocatedComputerInfo("存储器分配率", "speed", "datastore");
    getSpreadComputerInfo("存储器分布", "pie", "datastore");
    getResourceTrendData("chart");
  });
};

//获取分配情况
const getAllocatedComputerInfo = (
  chartTitle: string,
  chartType: string,
  chartName: string
) => {
  childRefMap.get(chartName).echartsClear();
  childRefMap.get(chartName).echartsLoading();
  ResourceSpreadViewApi.getAllocatedInfo(params).then((res) => {
    allocatedInfo.value = res.data;
    const options = _.cloneDeep(defaultSpeedOptions);
    _.set(
      options,
      "series[0].data[0].value",
      allocatedInfo.value[chartName]?.allocatedRate
    );
    _.set(options, "series[0].data[0].name", chartTitle);
    if (chartName === "cpu") {
      allocatedComputerCpuOption.value = options;
    }
    if (chartName === "memory") {
      allocatedComputerMemoryOption.value = options;
    }
    if (chartName === "datastore") {
      allocatedDatastoreOption.value = options;
    }
    childRefMap.get(chartName).hideEchartsLoading();
  });
};
//获取分布情况
const getSpreadComputerInfo = (
  chartTitle: string,
  chartType: string,
  chartName: string
) => {
  childRefMap.get("spread-" + chartName).echartsClear();
  childRefMap.get("spread-" + chartName).echartsLoading();
  _.set(params, "vmStatus", paramVmStatus.value);
  ResourceSpreadViewApi.getSpreadInfo(params).then((res) => {
    spreadInfo.value = res.data;
    const options = _.cloneDeep(defaultPieDoughnutOptions);
    _.set(options, "series[0].data", spreadInfo.value[chartName]);
    _.set(options, "series[0].name", chartTitle);
    _.set(
      options,
      "series[0].label.normal.formatter",
      `{title|总数}\r\n{value|${_.sumBy(spreadInfo.value[chartName], "value")}}`
    );
    if (chartName === "host") {
      spreadHostOption.value = options;
    }
    if (chartName === "datastore") {
      spreadDatastoreOption.value = options;
    }
    if (chartName === "vm") {
      spreadVmOption.value = options;
    }
    childRefMap.get("spread-" + chartName).hideEchartsLoading();
  });
};
//趋势
const getResourceTrendData = (chartName: string) => {
  if (trendChartType.value === "pie") {
    showTrendTime.value = false;
  } else {
    showTrendTime.value = true;
  }
  childRefMap.get("trend-" + chartName).echartsClear();
  childRefMap.get("trend-" + chartName).echartsLoading();
  _.set(params, "entityType", "HOST");
  _.set(params, "metricName", paramTrendType.value);
  _.set(params, "startTime", timestampData.value[0].getTime());
  _.set(params, "endTime", timestampData.value[1].getTime());
  if ("DATASTORE_USED_UTILIZATION" === paramTrendType.value) {
    _.set(
      params,
      "resourceIds",
      paramDatastoreId.value === "all_list" ? [] : paramDatastoreId.value
    );
    _.set(params, "entityType", "DATASTORE");
  } else {
    _.set(
      params,
      "resourceIds",
      paramHostId.value === "all_list" ? [] : paramHostId.value
    );
  }
  ResourceSpreadViewApi.getResourceTrendData(params).then((res) => {
    trendInfo.value = res.data;
    if ("trend" === trendChartType.value) {
      trendOption.value = getTrendOptions(_.cloneDeep(defaultTrendOptions));
    }
    if ("pie" === trendChartType.value) {
      trendPieOption.value = getTrendPieOptions(
        _.cloneDeep(defaultPieDoughnutOptions)
      );
    }
    childRefMap.get("trend-" + chartName).hideEchartsLoading();
  });
};

const getTrendPieOptions = (options: any) => {
  let legend: any[] = [],
    series: any = {},
    xAxis: any[] = [],
    seriesData: any[] = [];
  if (!trendInfo.value && trendInfo.value.length === 0) {
    return emptyOptions;
  }
  if (trendInfo.value) {
    const chartData = trendInfo.value;
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
  if (!trendInfo.value && trendInfo.value.length === 0) {
    return emptyOptions;
  }
  if (trendInfo.value) {
    const chartData = trendInfo.value;
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
    _.set(options, "legend.data", legend);
    _.set(options, "xAxis[0].data", xAxis);
    _.set(options, "series", seriesData);
  }
  return options;
};
/**
 * 默认时间当前时间过去7天
 */
const timestampData = ref<[Date, Date]>([
  new Date(new Date().getTime() - 3600 * 1000 * 24 * 7),
  new Date(),
]);
const trendTime = ref(timestampData);
const showTrendTime = ref<boolean>(true);
/**
 * 时间调整
 */
const changeTimestamp = () => {
  console.log(trendTime.value);
  getResourceTrendData("chart");
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

onMounted(() => {
  getAccounts();
});
</script>

<style scoped>
.spread-layout {
  width: 100% !important;
  min-width: 900px;
}
.myChart {
  height: 370px;
  min-width: 300px;
  border: 1px solid #e5e5e5;
}
.spread-main {
  margin-top: 10px;
  width: 100% !important;
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
  left: 55%;
  width: 150px;
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
