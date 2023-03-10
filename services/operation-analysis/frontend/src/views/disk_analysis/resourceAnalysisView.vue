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
    <div class="spread-main" style="min-width: 1000px">
      <div class="spread-main-up">
        <el-row :gutter="10">
          <el-col :span="8">
            <div class="myChart" style="height: 370px">
              <div class="echart-title">按云账号</div>
              <div style="position: relative">
                <ChartsSpeed
                  :height="300"
                  :options="spreadByAccountOption"
                  :ref="(el) => childRef(el, 'byAccount-chart')"
                />
              </div>
            </div>
          </el-col>
          <el-col :span="16">
            <div class="myChart" style="height: 370px">
              <div class="echart-title">
                <div class="echart-title-left">磁盘趋势</div>
                <div class="echart-title-right">
                  <el-select
                    v-model="paramDiskIncreaseTrendMonth"
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
              <div style="position: relative">
                <ChartsSpeed
                  :height="300"
                  :options="spreadByStatusOption"
                  :ref="(el) => childRef(el, 'byStatus-chart')"
                />
              </div>
            </div>
          </el-col>
          <el-col :span="16">
            <div class="myChart" style="height: 370px">
              <div class="echart-title">组织</div>
              <div style="position: relative">
                <ChartsSpeed
                  :height="300"
                  :options="spreadByDepartmentOrgOption"
                  :tree-bar="true"
                  :tree-bar-data="spreadByDepartmentOptionOrgData"
                  :tree-bar-all-data="spreadByDepartmentOptionOrgAllData"
                  :ref="(el) => childRef(el, 'byDepartmentType-org-chart')"
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
              <div style="position: relative">
                <ChartsSpeed
                  :height="300"
                  :options="spreadByTypeOption"
                  :ref="(el) => childRef(el, 'byType-chart')"
                />
              </div>
            </div>
          </el-col>
          <el-col :span="16">
            <div class="myChart" style="height: 370px">
              <div class="echart-title">工作空间</div>
              <div style="position: relative">
                <ChartsSpeed
                  :height="300"
                  :options="spreadByDepartmentWorkspaceOption"
                  :tree-bar="true"
                  :tree-bar-data="spreadByDepartmentOptionWorkspaceData"
                  :tree-bar-all-data="spreadByDepartmentOptionWorkspaceAllData"
                  :ref="
                    (el) => childRef(el, 'byDepartmentType-workspace-chart')
                  "
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
import ChartsSpeed from "@commons/components/echart/ChartsSpeed.vue";
import ResourceSpreadViewApi from "@/api/disk_analysis/index";
import { ResourceAnalysisRequest } from "@/api/disk_analysis/type";
import _ from "lodash";
import {
  defaultLineOption,
  emptyOptions,
  defaultPieDoughnutOptions,
  trendSeriesColor,
  getRandomColor,
  defaultTrendOptions,
  defaultBarOptions,
} from "@commons/components/echart/index";
import * as echarts from "echarts";
//分布情况
const spreadByAccountOption = ref<any>({});
const spreadByStatusOption = ref<any>({});
const spreadByTypeOption = ref<any>({});
const spreadByResourceUsedTopOption = ref<any>({});
const spreadByDepartmentOrgOption = ref<any>({});
const spreadByDepartmentWorkspaceOption = ref<any>({});
const spreadByDepartmentOptionOrgData = ref<any>([]);
const spreadByDepartmentOptionOrgAllData = ref<any>([]);
const spreadByDepartmentOptionWorkspaceData = ref<any>([]);
const spreadByDepartmentOptionWorkspaceAllData = ref<any>([]);
//趋势
const increaseOption = ref<any>({});
//参数
const params = ref<any>({});
const paramAccountId = ref<string>("all_list");
const paramsStatisticalBlock = ref<string>("block");
const paramDiskIncreaseTrendMonth = ref<any>("7");
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
  //paramsStatisticalBlock.value = "block" as string;
  //_.set(params, "statisticalBlock", true);
  spreadByAccountOption.value = emptyOptions;
  spreadByStatusOption.value = emptyOptions;
  spreadByTypeOption.value = emptyOptions;
  increaseOption.value = emptyOptions;
  spreadByDepartmentOrgOption.value = emptyOptions;
  spreadByDepartmentWorkspaceOption.value = emptyOptions;
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
  getSpreadByDepartmentOrgData("byDepartmentType-org");
};
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
      const options = _.cloneDeep(defaultPieDoughnutOptions);
      _.set(options, "series[0].data", spreadData[spreadType]);
      _.set(options, "series[0].name", "磁盘分布");
      _.set(
        options,
        "series[0].label.formatter",
        `{title|总数}\r\n{value|${_.sumBy(spreadData[spreadType], "value")}}`
      );
      if (spreadType === "byAccount") {
        spreadByAccountOption.value = options;
      }
      if (spreadType === "byStatus") {
        spreadByStatusOption.value = options;
      }
      if (spreadType === "byType") {
        spreadByTypeOption.value = options;
      }
      childRefMap.get(spreadType + "-chart").hideEchartsLoading();
    }
  });
};
//趋势
const getIncreaseTrend = (chartName: string) => {
  childRefMap.get(chartName + "-chart").echartsClear();
  childRefMap.get(chartName + "-chart").echartsLoading();
  _.set(
    params,
    "accountIds",
    paramAccountId.value === "all_list" ? [] : paramAccountId.value
  );
  _.set(params, "statisticalBlock", paramsStatisticalBlock.value === "block");
  _.set(params, "dayNumber", paramDiskIncreaseTrendMonth.value);
  let legend: any[] = [],
    series: any = {},
    xAxis: any[] = [];
  const seriesData: any[] = [];
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
//组织分布
const getSpreadByDepartmentOrgData = (chartName: string) => {
  childRefMap.get(chartName + "-chart").echartsClear();
  childRefMap.get(chartName + "-chart").echartsLoading();
  _.set(
    params,
    "accountIds",
    paramAccountId.value === "all_list" ? [] : paramAccountId.value
  );
  _.set(params, "statisticalBlock", paramsStatisticalBlock.value === "block");
  _.set(params, "analysisWorkspace", false);
  ResourceSpreadViewApi.getAnalysisOrgDiskCount(params).then((res) => {
    getSpreadByDepartmentWorkspaceData("byDepartmentType-workspace");
    const options = _.cloneDeep(defaultBarOptions);
    const chartData = res.data.tree;
    spreadByDepartmentOptionOrgAllData.value = res.data.all;
    spreadByDepartmentOptionOrgData.value = chartData;
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
    spreadByDepartmentOrgOption.value = options;
    childRefMap.get(chartName + "-chart").hideEchartsLoading();
  });
};
//工作空间分布
const getSpreadByDepartmentWorkspaceData = (chartName: string) => {
  childRefMap.get(chartName + "-chart").echartsClear();
  childRefMap.get(chartName + "-chart").echartsLoading();
  _.set(params, "statisticalBlock", paramsStatisticalBlock.value === "block");
  _.set(params, "analysisWorkspace", true);
  ResourceSpreadViewApi.getAnalysisWorkspaceDiskCount(params).then((res) => {
    const options = _.cloneDeep(defaultBarOptions);
    const chartData = res.data.tree;
    spreadByDepartmentOptionWorkspaceAllData.value = res.data.all;
    spreadByDepartmentOptionWorkspaceData.value = chartData;
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
    spreadByDepartmentWorkspaceOption.value = options;
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
  width: 100% !important;
  min-width: 950px;
}
.myChart {
  height: 300px;
  min-width: 350px;
  border: 1px solid #e5e5e5;
}
.spread-main {
  margin-top: 10px;
  width: 99% !important;
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
