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
      <el-select
        v-model="paramsStatisticalBlock"
        @change="getDate()"
        class="m-2"
      >
        <el-option label="数量(块)" value="block" />
        <el-option label="容量(GB)" value="capacity" />
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
                <div class="echart-title-left">云磁盘增长趋势</div>
                <div class="echart-title-right">
                  <el-radio-group
                    v-model="paramDiskIncreaseTrendMonth"
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
              <div class="echart-title">挂载状态</div>
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
                <div class="echart-title-left">磁盘使用率排名</div>
                <div class="echart-title-right">
                  <el-select
                    v-model="paramDiskUsedRateTop"
                    @change="changeUsedRateTop()"
                    style="width: 100px; margin-bottom: 7px"
                    size="small"
                  >
                    <el-option label="较高TOP10" value="height" />
                    <el-option label="较低TOP10" value="low" />
                  </el-select>
                </div>
              </div>
              <div style="position: relative">
                <ChartsSpeed
                  :height="300"
                  :options="spreadByResourceUsedTopOption"
                  :ref="(el) => childRef(el, 'byResourceUsedTop-chart')"
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
              <div class="echart-title">磁盘类型</div>
              <div class="echart-content">
                <div class="echart-content-left">
                  <el-row>
                    <el-col :span="24">
                      <ChartsSpeed
                        :height="300"
                        :options="spreadByTypeOption"
                        :ref="(el) => childRef(el, 'byType-chart')"
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
                    @change=""
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
import ChartsSpeed from "../../components/echart/ChartsSpeed.vue";
import ResourceSpreadViewApi from "@/api/disk_analysis/index";
import { ResourceAnalysisRequest } from "@/api/disk_analysis/type";
import _ from "lodash";
import {
  defaultPieOptions,
  defaultLineOption,
  emptyOptions,
  defaultPie2Options,
  trendSeriesColor,
  getRandomColor,
  defaultTrendOptions,
} from "@/components/echart/index";
import * as echarts from "echarts";
//分布情况
const spreadByAccountOption = ref<any>({});
const spreadByStatusOption = ref<any>({});
const spreadByTypeOption = ref<any>({});
const spreadByResourceUsedTopOption = ref<any>({});
const spreadByDepartmentOption = ref<any>({});
//增长趋势
const increaseOption = ref<any>({});
//参数
const params = ref<ResourceAnalysisRequest | undefined>();
const paramAccountId = ref<string>("all_list");
const paramsStatisticalBlock = ref<string>("block");
const paramDiskIncreaseTrendMonth = ref<number>(6);
const paramDiskUsedRateTop = ref<string>("height");
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
  paramsStatisticalBlock.value = "block" as string;
  _.set(params, "statisticalBlock", true);
  spreadByAccountOption.value = emptyOptions;
  spreadByStatusOption.value = emptyOptions;
  spreadByTypeOption.value = emptyOptions;
  increaseOption.value = emptyOptions;
  spreadByDepartmentOption.value = emptyOptions;
  spreadByResourceUsedTopOption.value = emptyOptions;
};
const getALlAccount = () => {
  initParam();
  ResourceSpreadViewApi.listAccounts().then((res) => {
    accounts.value = res.data;
    getDate();
  });
};
const getDate = () => {
  getSpreadData("byAccount");
  getSpreadData("byStatus");
  getSpreadData("byType");
  getIncreaseTrend("byIncrease");
};
const changeUsedRateTop = () => {};
const getSpreadData = (spreadType: string) => {
  childRefMap.get(spreadType + "-chart").echartsClear();
  childRefMap.get(spreadType + "-chart").echartsLoading();
  _.set(
    params,
    "accountIds",
    paramAccountId.value === "all_list" ? [] : paramAccountId.value
  );
  _.set(params, "statisticalBlock", paramsStatisticalBlock.value === "block");
  ResourceSpreadViewApi.getSpreadData(params).then((res) => {
    if (res.data) {
      const spreadData = res.data;
      if (spreadType === "byAccount") {
        const options = _.cloneDeep(defaultPie2Options);
        _.set(options, "series[0].data", spreadData[spreadType]);
        _.set(
          options,
          "title.text",
          "总数：" + _.sumBy(spreadData[spreadType], "value")
        );
        spreadByAccountOption.value = options;
      }
      if (spreadType === "byStatus") {
        const options = _.cloneDeep(defaultPieOptions);
        _.set(options, "series[0].data", spreadData[spreadType]);
        spreadByStatusOption.value = options;
      }
      if (spreadType === "byType") {
        const options = _.cloneDeep(defaultPie2Options);
        _.set(options, "series[0].data", spreadData[spreadType]);
        _.unset(options, "series[0].itemStyle");
        _.set(
          options,
          "title.text",
          "总数：" + _.sumBy(spreadData[spreadType], "value")
        );
        spreadByTypeOption.value = options;
      }
      childRefMap.get(spreadType + "-chart").hideEchartsLoading();
    }
  });
};
//增长趋势
const getIncreaseTrend = (chartName: string) => {
  childRefMap.get(chartName + "-chart").echartsClear();
  childRefMap.get(chartName + "-chart").echartsLoading();
  _.set(
    params,
    "accountIds",
    paramAccountId.value === "all_list" ? [] : paramAccountId.value
  );
  _.set(params, "statisticalBlock", paramsStatisticalBlock.value === "block");
  _.set(params, "monthNumber", paramDiskIncreaseTrendMonth.value);
  let legend: any[] = [],
    series: any = {},
    xAxis: any[] = [],
    seriesData: any[] = [];
  ResourceSpreadViewApi.getIncreaseTrend(params).then((res) => {
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
    _.set(options, "legend.data", legend);
    _.set(options, "xAxis[0].data", xAxis);
    _.set(options, "series", seriesData);
    increaseOption.value = options;
    childRefMap.get(chartName + "-chart").hideEchartsLoading();
  });
};

onMounted(() => {
  getALlAccount();
});
/**
 * 默认时间当前时间过去30天
 */
const timestampData = ref<[Date, Date]>([
  new Date(new Date().getTime() - 3600 * 1000 * 24 * 30),
  new Date(),
]);
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
