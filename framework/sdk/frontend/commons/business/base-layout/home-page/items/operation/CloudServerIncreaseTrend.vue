<template>
  <div class="info-card" v-if="show">
    <el-row>
      <el-col :span="10">
        <div class="title">云主机趋势</div>
      </el-col>
      <el-col :span="14" style="text-align: right">
        <el-radio-group
          class="custom-radio-group"
          v-model="paramVmIncreaseTrendMonth"
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
  </div>
</template>
<script setup lang="ts">
import VChart from "vue-echarts";
import { computed, onMounted, ref, watch } from "vue";
import _ from "lodash";
import type { ResourceAnalysisRequest } from "@commons/api/server_analysis/type";
import type { ECBasicOption } from "echarts/types/src/util/types";
import CloudServerViewApi from "@commons/api/server_analysis/index";
import * as echarts from "echarts";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useUserStore } from "@commons/stores/modules/user";

const props = withDefaults(
  defineProps<{
    needRoles?: Array<"ADMIN" | "ORGADMIN" | "USER">;
    permission?: any;
    module?: string;
    cloudAccountId?: string;
    clusterId?: string;
    datastoreId?: string;
    hostId?: string;
  }>(),
  {
    needRoles: () => ["ADMIN", "ORGADMIN", "USER"],
    permission: ["[operation-analysis]OVERVIEW:READ"],
    module: "operation-analysis",
  }
);

const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();
const userStore = useUserStore();

const show = computed<boolean>(
  () =>
    _.some(
      moduleStore.runningModules,
      (module) => module.id === props.module
    ) &&
    permissionStore.hasPermission(props.permission) &&
    _.includes(props.needRoles, userStore.currentRole)
);

const params = ref<ResourceAnalysisRequest>({});
const paramVmIncreaseTrendMonth = ref<string>("7");
const loading = ref<boolean>(false);
const apiData = ref<any>();

const getIncreaseTrend = () => {
  if (!show.value) {
    return;
  }
  _.set(params.value, "dayNumber", paramVmIncreaseTrendMonth.value);
  _.set(
    params.value,
    "accountIds",
    props.cloudAccountId === "all" || !props.cloudAccountId
      ? []
      : [props.cloudAccountId]
  );
  props.hostId
    ? _.set(
        params.value,
        "hostIds",
        props.hostId === "all" ? [] : [props.hostId]
      )
    : "";
  CloudServerViewApi.getIncreaseTrend(params.value, loading).then(
    (res) => (apiData.value = res.data)
  );
};

const options = computed<ECBasicOption>(() => {
  const obj = apiData.value;
  if (!obj || obj.length === 0) {
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
onMounted(() => {
  getIncreaseTrend();
});
const color: Array<string> = [
  "rgb(79, 131, 253, 1)",
  "rgb(150, 189, 255, 1)",
  "rgb(250, 211, 85, 1)",
  "rgb(255, 230, 104, 1)",
  "rgb(22, 225, 198, 1)",
  "rgb(76, 253, 224, 1)",
  "rgb(81, 206, 251, 1)",
  "rgb(118, 240, 255, 1)",
  "rgb(148, 90, 246, 1)",
  "rgb(220, 155, 255, 1)",
  "rgb(255, 165, 61, 1)",
  "rgb(255, 199, 94, 1)",
  "rgb(241, 75, 169, 1)",
  "rgb(255, 137, 227, 1)",
  "rgb(247, 105, 101, 1)",
  "rgb(255, 158, 149, 1)",
  "rgb(219, 102, 219, 1)",
  "rgb(254, 157, 254, 1)",
  "rgb(195, 221, 65, 1)",
  "rgb(217, 244, 87, 1)",
  "rgb(97, 106, 229, 1)",
  "rgb(172, 173, 255, 1)",
  "rgb(98, 210, 85, 1)",
  "rgb(134, 245, 120, 1)",
];
const defaultSpeedOptions = {
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
    data: ["Line 1", "Line 2", "Line 3", "Line 4", "Line 5"],
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
    top: "17px",
    bottom: "15%",
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
      splitLine: {
        show: false,
      },
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
</script>
<style scoped lang="scss">
.info-card {
  height: 295px;
  min-width: 400px;
  background: #ffffff;
  border-radius: 4px;
  padding: 24px;
  overflow: hidden;
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
