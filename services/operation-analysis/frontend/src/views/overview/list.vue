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
    </div>
    <div class="spread-main">
      <div class="spread-main-up">
        <el-row :gutter="10">
          <el-col :span="6">
            <div class="myChart" style="height: 330px">
              <div class="echart-title">云主机</div>
              <div style="position: relative">
                <ChartsSpeed
                  :height="250"
                  :options="spreadVmOption"
                  :ref="(el) => childRef(el, 'vm-spread-chart')"
                />
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="myChart" style="height: 330px">
              <div class="echart-title">磁盘</div>
              <div style="position: relative">
                <ChartsSpeed
                  :height="250"
                  :options="spreadDiskOption"
                  :ref="(el) => childRef(el, 'disk-spread-chart')"
                />
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="myChart" style="height: 330px">
              <div class="echart-title">宿主机（私有云）</div>
              <div style="position: relative">
                <ChartsSpeed
                  :height="250"
                  :options="spreadHostOption"
                  :ref="(el) => childRef(el, 'host-spread-chart')"
                />
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="myChart" style="height: 330px">
              <div class="echart-title">存储器（私有云）</div>
              <div style="position: relative">
                <ChartsSpeed
                  :height="250"
                  :options="spreadDatastoreOption"
                  :ref="(el) => childRef(el, 'datastore-spread-chart')"
                />
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="spread-main-content">
        <el-row :gutter="10">
          <el-col :span="12">
            <div class="myChart">
              <div class="echart-title">
                云主机分布
                <div class="echart-title-right">
                  <div class="echart-title-left">
                    <el-radio-group
                      class="custom-radio-group"
                      v-model="paramDepartmentType"
                      @change="getSpreadByDepartmentData('department')"
                    >
                      <el-radio-button label="org">组织</el-radio-button>
                      <el-radio-button label="workspace"
                        >工作空间</el-radio-button
                      >
                    </el-radio-group>
                  </div>
                </div>
              </div>
              <div style="position: relative">
                <ChartsSpeed
                  :height="300"
                  :options="spreadByDepartmentOption"
                  :tree-bar="true"
                  :tree-bar-data="spreadByDepartmentOptionData"
                  :tree-bar-all-data="spreadByDepartmentOptionAllData"
                  :ref="(el) => childRef(el, 'department-chart')"
                />
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="myChart">
              <div class="echart-title">
                <div class="echart-title-left">云主机趋势</div>
                <div class="echart-title-right">
                  <el-radio-group
                    class="custom-radio-group"
                    v-model="paramVmIncreaseTrendMonth"
                    @change="getVmIncreaseTrend()"
                  >
                    <el-radio-button label="7">近7天</el-radio-button>
                    <el-radio-button label="30">近30天</el-radio-button>
                    <el-radio-button label="180">近半年</el-radio-button>
                    <el-radio-button label="360">近一年</el-radio-button>
                  </el-radio-group>
                </div>
              </div>
              <div style="position: relative">
                <ChartsSpeed
                  :height="300"
                  :options="vmIncreaseOption"
                  :ref="(el) => childRef(el, 'vm-increase-chart')"
                />
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div
        class="spread-main-content"
        v-if="adminShowBaseResourceAllocatedInfo()"
      >
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
      <div class="spread-main-bottom">
        <div class="myChart" style="height: 210px">
          <div class="echart-title">
            <div class="echart-title-left">资源优化建议</div>
          </div>
          <div class="echart-content" style="padding: 10px">
            <el-row :gutter="12">
              <el-col :span="6" v-for="o in optimizeSuggests" :key="o.code">
                <router-link
                  :to="{ name: 'server_optimization', query: { code: o.code } }"
                >
                  <el-card :body-style="{ padding: '0px' }" shadow="hover">
                    <div class="boxConter">
                      <div class="CenterTheBox">
                        <span
                          ><span style="font-size: 24px">{{ o.value }}</span
                          >台</span
                        >
                      </div>
                      <div
                        class="BottomTheBox"
                        :style="{ 'background-color': o.color }"
                      >
                        <span>{{ o.name }}</span>
                      </div>
                    </div>
                  </el-card>
                </router-link>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { onMounted, ref } from "vue";
import _ from "lodash";
import ChartsSpeed from "@commons/components/echart/ChartsSpeed.vue";
import ResourceSpreadViewApi from "@/api/resource_spread_view/index";
import CloudServerViewApi from "@/api/server_analysis/index";
import CloudDiskViewApi from "@/api/disk_analysis/index";
import {
  defaultPieDoughnutOptions,
  defaultSpeedOptions,
  defaultBarOptions,
  emptyOptions,
  defaultTrendOptions,
  trendSeriesColor,
  getRandomColor,
} from "@commons/components/echart/index";
import {
  type OptimizeSuggest,
  paramOptimizationRequestMap,
  baseOptimizeSuggests,
} from "@commons/api/resource_optimization/type";
import * as echarts from "echarts";
import ResourceOptimizationViewApi from "@/api/resource_optimization";
import { useUserStore } from "@commons/stores/modules/user";

const userStore = useUserStore();
const adminShowBaseResourceAllocatedInfo = () => {
  return _.includes(["ADMIN"], userStore.currentRole);
};

const optimizeSuggests = ref<Array<OptimizeSuggest>>(
  _.clone(baseOptimizeSuggests)
);

//分配情况
const allocatedInfo = ref<any>({});
const allocatedComputerCpuOption = ref<any>({});
const allocatedComputerMemoryOption = ref<any>({});
const allocatedDatastoreOption = ref<any>({});
//分布情况
const spreadVmOption = ref<any>({});
const spreadDiskOption = ref<any>({});
const spreadHostOption = ref<any>({});
const spreadDatastoreOption = ref<any>({});
//部门下云主机分布
const paramDepartmentType = ref<string>("org");
const spreadByDepartmentOption = ref<any>({});
const spreadByDepartmentOptionData = ref<any>([]);
const spreadByDepartmentOptionAllData = ref<any>([]);
//云主机趋势
const vmIncreaseOption = ref<any>({});
//参数
const params = ref<any>({});
const paramVmIncreaseTrendMonth = ref<any>("7");
const paramAccountId = ref<string>("all_list");
//优化建议
const optimizeParam = ref<any>();
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
// 自定义圆环饼图
const defaultPieDoughnutOptionsCustom = ref();
const doughnutLegend = {
  type: "scroll",
  bottom: 10,
  left: "center",
  textStyle: {
    width: "10px",
    fontFamily:
      "'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif",
  },
  tooltip: {
    show: true,
  },
  formatter: function (name: any) {
    const len = name.length;
    if (len > 10) {
      name = name.slice(0, 10) + "...";
    }
    return name;
  },
};

//初始化参数及数据
const initParam = () => {
  //基础资源分配情况
  allocatedComputerCpuOption.value = emptyOptions;
  allocatedComputerMemoryOption.value = emptyOptions;
  allocatedDatastoreOption.value = emptyOptions;
  //资源云账号分布
  spreadVmOption.value = emptyOptions;
  spreadDiskOption.value = emptyOptions;
  spreadHostOption.value = emptyOptions;
  spreadDatastoreOption.value = emptyOptions;
  //云主机趋势
  vmIncreaseOption.value = emptyOptions;
  //自定义圆环饼图样式
  defaultPieDoughnutOptionsCustom.value = _.cloneDeep(
    defaultPieDoughnutOptions
  );
  _.set(defaultPieDoughnutOptionsCustom.value, "legend", doughnutLegend);
  _.set(defaultPieDoughnutOptionsCustom.value, "series[0].radius", [
    "43%",
    "65%",
  ]);
  _.set(defaultPieDoughnutOptionsCustom.value, "series[0].center", [
    "50%",
    "40%",
  ]);
};
//查询所有云账号
const getAccounts = () => {
  initParam();
  _.set(
    params,
    "accountIds",
    paramAccountId.value === "all_list" ? [] : paramAccountId.value
  );
  CloudServerViewApi.listAccounts().then((res) => {
    if (res.data.length) {
      accounts.value = res.data;
      if (adminShowBaseResourceAllocatedInfo()) {
        getBaseResourceAllocatedInfo();
      }
      getVmSpreadInfo();
      getDiskSpreadInfo();
      getComputerSpreadInfo("宿主机分布", "host");
      getComputerSpreadInfo("存储器分布", "datastore");
      getSpreadByDepartmentData("department");
      getVmIncreaseTrend();
      getOptimizeSuggests();
    }
  });
};
//云主机云账号分布
const getVmSpreadInfo = () => {
  const spreadType = "vm-spread";
  childRefMap.get(spreadType + "-chart").echartsClear();
  childRefMap.get(spreadType + "-chart").echartsLoading();
  CloudServerViewApi.getSpreadData(params).then((res) => {
    if (res.data) {
      const spreadData = res.data;
      const options = _.cloneDeep(defaultPieDoughnutOptionsCustom.value);
      _.set(options, "series[0].name", "云主机分布");
      _.set(options, "series[0].data", spreadData["byAccount"]);
      _.set(
        options,
        "series[0].label.formatter",
        `{title|总数}\r\n{value|${_.sumBy(spreadData["byAccount"], "value")}}`
      );
      spreadVmOption.value = options;
      childRefMap.get(spreadType + "-chart").hideEchartsLoading();
    }
  });
};
//磁盘云账号分布
const getDiskSpreadInfo = () => {
  const spreadType = "disk-spread";
  childRefMap.get(spreadType + "-chart").echartsClear();
  childRefMap.get(spreadType + "-chart").echartsLoading();
  _.set(params, "statisticalBlock", true);
  CloudDiskViewApi.getSpreadData(params).then((res) => {
    if (res.data) {
      const spreadData = res.data;
      const options = _.cloneDeep(defaultPieDoughnutOptionsCustom.value);
      _.set(options, "series[0].data", spreadData["byAccount"]);
      _.set(options, "series[0].name", "磁盘分布");
      _.set(
        options,
        "series[0].label.formatter",
        `{title|总数}\r\n{value|${_.sumBy(spreadData["byAccount"], "value")}}`
      );
      spreadDiskOption.value = options;
    }
    childRefMap.get(spreadType + "-chart").hideEchartsLoading();
  });
};
//宿主机、存储器云账号分布
const getComputerSpreadInfo = (chartTitle: string, chartName: string) => {
  childRefMap.get(chartName + "-spread-chart").echartsClear();
  childRefMap.get(chartName + "-spread-chart").echartsLoading();
  _.set(params, "vmStatus", "all");
  ResourceSpreadViewApi.getSpreadInfo(params).then((res) => {
    const options = _.cloneDeep(defaultPieDoughnutOptionsCustom.value);
    _.set(options, "series[0].data", res.data[chartName]);
    _.set(options, "series[0].name", chartTitle);
    _.set(
      options,
      "series[0].label.formatter",
      `{title|总数}\r\n{value|${_.sumBy(res.data[chartName], "value")}}`
    );
    if (chartName === "host") {
      spreadHostOption.value = options;
    }
    if (chartName === "datastore") {
      spreadDatastoreOption.value = options;
    }
    childRefMap.get(chartName + "-spread-chart").hideEchartsLoading();
  });
};
//部门
const getSpreadByDepartmentData = (chartName: string) => {
  childRefMap.get(chartName + "-chart").echartsClear();
  childRefMap.get(chartName + "-chart").echartsLoading();
  _.set(
    params,
    "analysisWorkspace",
    paramDepartmentType.value === "workspace" ? true : false
  );
  CloudServerViewApi.getAnalysisOrgWorkspaceVmCount(params)
    .then((res) => {
      const options = _.cloneDeep(defaultBarOptions);
      const chartData = res.data.tree;
      spreadByDepartmentOptionAllData.value = res.data.all;
      spreadByDepartmentOptionData.value = chartData;
      _.set(
        options,
        "xAxis.data",
        chartData.map((item: any) => item.name)
      );
      _.set(
        options,
        "series[0].itemStyle",
        childRefMap.get(chartName + "-chart").barSeriesItemStyle
      );
      _.set(
        options,
        "series[0].label",
        childRefMap.get(chartName + "-chart").barSeriesLabel
      );
      const seriesData = ref<any>([]);
      _.forEach(chartData, (v) => {
        seriesData.value.push({ value: v.value, groupName: v.groupName });
      });
      _.set(options, "series[0].data", seriesData);
      _.set(options, "series[0].name", "云主机");
      _.set(options, "legend.data", ["云主机"]);
      const deptNumber = chartData.map((item: any) => item.name);
      let showEchart = false;
      let nameNum = 0;
      if (deptNumber.length > 0) {
        nameNum = Math.floor(100 / (deptNumber.length / 9));
        if (deptNumber.length > 9) {
          showEchart = true;
        } else {
          showEchart = false;
        }
      }
      _.set(options, "dataZoom.[0].end", nameNum);
      _.set(options, "dataZoom.[1].end", nameNum);
      _.set(options, "dataZoom.[0].show", showEchart);
      spreadByDepartmentOption.value = options;
      childRefMap.get(chartName + "-chart").hideEchartsLoading();
    })
    .catch((err) => {
      console.log(err);
    });
};

//云主机趋势
const getVmIncreaseTrend = () => {
  const chartName = "vm-increase";
  childRefMap.get(chartName + "-chart").echartsClear();
  childRefMap.get(chartName + "-chart").echartsLoading();
  _.set(params, "dayNumber", paramVmIncreaseTrendMonth.value);
  let legend: any[] = [],
    series: any = {},
    xAxis: any[] = [];
  const seriesData: any[] = [];
  CloudServerViewApi.getIncreaseTrend(params).then((res) => {
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
    vmIncreaseOption.value = options;
    childRefMap.get(chartName + "-chart").hideEchartsLoading();
  });
};

//基础资源分配情况
const getBaseResourceAllocatedInfo = () => {
  ResourceSpreadViewApi.listHost(params).then((res) => {
    hosts.value = res.data;
    getAllocatedComputerInfo("CPU分配率", "speed", "cpu");
    getAllocatedComputerInfo("内存分配率", "speed", "memory");
    getAllocatedComputerInfo("存储器分配率", "speed", "datastore");
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
    childRefMap.get(chartName)?.hideEchartsLoading();
  });
};

//优化建议
const getOptimizeSuggests = () => {
  optimizeSuggests.value.forEach((value) => {
    getSearchParams(value);
    _.merge(params, optimizeParam.value);
    ResourceOptimizationViewApi.listServer({
      currentPage: 1,
      pageSize: 10,
      ...params,
    }).then((res) => {
      value.value = res.data.total;
      value.data = res.data.records;
    });
  });
};

const getSearchParams = (o: any) => {
  if (window.localStorage.getItem(o.code)) {
    const str = window.localStorage.getItem(o.code);
    if (str) {
      try {
        optimizeParam.value = JSON.parse(str);
      } catch (e) {
        console.error("get default dialogFormData error", e);
        optimizeParam.value = paramOptimizationRequestMap.get(o.code);
      }
    }
  } else {
    optimizeParam.value = paramOptimizationRequestMap.get(o.code);
  }
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

onMounted(() => {
  getAccounts();
});
</script>

<style scoped lang="scss">
.spread-layout {
  width: 100% !important;
  min-width: 900px;
}
.myChart {
  height: 370px;
  min-width: 200px;
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

.boxConter {
  height: 100px;
  overflow: hidden;
  position: relative;
  //按钮
  button {
    position: absolute;
    display: inline-block;
    color: #f7ba2a;
    width: 100%;
    bottom: 5px;
    right: 0;
    text-align: center;
  }
  //角标主要样式
  .AngleOfTheBox {
    position: absolute;
    padding: 0 5px;
    display: flex;
    align-items: center;
    width: 15%;
    height: 26px;
    color: #fff;
    background-color: #ff9899;
    //文字
    span {
      position: absolute;
      display: inline-block;
      color: #fff;
      width: 100%;
      bottom: 5px;
      left: 0;
      text-align: center;
    }
  }
  .AngleOfTheBox::after {
    content: "";
    position: absolute;
    left: 100%;
    top: 0;
    border-color: transparent transparent transparent #ff9899;
    border-width: 13px 0 13px 13px;
    border-style: solid;
  }
  .BtnOfTheBox {
    cursor: pointer;
    position: absolute;
    padding: 0 5px;
    display: flex;
    align-items: center;
    width: 20px;
    height: 26px;
    right: 0;
  }
  .CenterTheBox {
    border-radius: 1px;
    width: 100%;
    height: 30px;
    position: absolute;
    bottom: 50px;
    text-align: center;
  }
  .BottomTheBox {
    border-radius: 1px;
    width: 100%;
    height: 40px;
    position: absolute;
    bottom: 0px;
    text-align: center;
    line-height: 40px;
  }
}
</style>
