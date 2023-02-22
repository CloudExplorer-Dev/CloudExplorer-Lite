<script lang="ts" setup>
import { computed, onMounted, ref } from "vue";
import _ from "lodash";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useUserStore } from "@commons/stores/modules/user";
import {
  PerfMetricConst,
  PerfMetricEntityTypeConst,
} from "@commons/utils/constants";

import API from "./api";

import Charts from "@commons/components/echart/Charts.vue";

const props = withDefaults(
  defineProps<{
    needRoles: Array<"ADMIN" | "ORGADMIN" | "USER">;
    permission?: any;
    module?: string;
    title: string;
    cardShadow?: "always" | "hover" | "never";
  }>(),
  {
    module: "vm-service",
    permission: "[vm-service]CLOUD_SERVER:READ",
    title: "监控数据",
    cardShadow: "always",
  }
);

const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();
const userStore = useUserStore();

const selectedServerId = ref<string | null>(null);
const selectedServer = computed(() => {
  return _.find(
    servers.value,
    (server) => server.id === selectedServerId.value
  );
});
const servers = ref<any[]>();

//初始化图表数据，固定指标图表
const echartsData = ref<Array<any>>([]);

const loading = ref<boolean>(false);
// 所有图表集合
const childRefMap: Map<string, any> = new Map();
const childRef = (el: any, metricName: any) => {
  childRefMap.set(metricName, el);
};

const { CPU_USED_UTILIZATION, MEMORY_USED_UTILIZATION, DISK_USED_UTILIZATION } =
  PerfMetricConst;

const mPerfMetricConstants = [
  CPU_USED_UTILIZATION,
  MEMORY_USED_UTILIZATION,
  DISK_USED_UTILIZATION,
];

/**
 * 默认时间当前时间过去一个小时
 */
const timestampData = ref<[Date, Date]>([
  new Date(new Date().getTime() - 24 * 60 * 60 * 1000),
  new Date(),
]);

const show = computed<boolean>(
  () =>
    _.some(
      moduleStore.runningModules,
      (module) => module.id === props.module
    ) &&
    permissionStore.hasPermission(props.permission) &&
    _.includes(props.needRoles, userStore.currentRole)
);

function initEcharts() {
  _.forEach(mPerfMetricConstants, (perfMetric) => {
    const series = {
      name: perfMetric.name,
      showSymbol: false,
      symbol: "circle",
      data: [],
      type: "line",
      smooth: false,
    };
    const data = {
      metricName: perfMetric.metricName,
      yUnit: perfMetric.unit,
      title: perfMetric.description,
      legend: [perfMetric.name],
      xData: [],
      series: [series],
      deviceList: [],
      currentDevice: "",
      deviceData: {},
    };
    echartsData.value.push(data);
  });
}

function loadAllData() {
  _.forEach(mPerfMetricConstants, (perfMetric) => {
    loadData(perfMetric.metricName);
  });
}

function loadData(name: string) {
  if (!show.value) {
    return;
  }

  if (selectedServer.value == undefined) {
    return;
  }

  const request: any = {};

  request.startTime = timestampData.value[0].getTime();
  request.endTime = timestampData.value[1].getTime();

  request.instanceId = selectedServer.value.instanceUuid;
  request.cloudAccountId = selectedServer.value.accountId;

  request.entityType = PerfMetricEntityTypeConst.VIRTUAL_MACHINE;

  request.metricName = name;

  if (childRefMap.get(name)) {
    childRefMap.get(name).echartsLoading();
  }

  API.listPerfMetricMonitor(request).then((result) => {
    const xData = ref<any>([]);
    const yData = ref<any>([]);
    const deviceList = ref<any>([]);
    if (_.keys(result.data).length > 0 && _.has(result.data, "other")) {
      yData.value = _.get(result.data, "other")[0].values;
      xData.value = _.get(result.data, "other")[0].timestamps;
    }

    if (name === PerfMetricConst.DISK_USED_UTILIZATION.metricName) {
      if (_.keys(result.data).length > 0) {
        deviceList.value = _.keys(result.data);
        yData.value = result.data[_.keys(result.data)[0]][0].values;
        xData.value = result.data[_.keys(result.data)[0]][0].timestamps;

        _.forEach(echartsData.value, (data) => {
          if (name == data.metricName) {
            if (deviceList.value.length > 0) {
              data.deviceList = deviceList.value;
              data.currentDevice = deviceList.value[0];
            }
            data.deviceData = result.data;
            data.xData = xData.value;
            data.series[0].data = yData.value;
          }
        });
      }
    } else {
      _.forEach(echartsData.value, (data) => {
        if (name == data.metricName) {
          data.xData = xData.value;
          data.series[0].data = yData.value;
        }
      });
    }

    if (childRefMap.get(name)) {
      childRefMap.get(name).hideEchartsLoading();
    }
  });
}

onMounted(() => {
  initEcharts();

  if (!show.value) {
    return;
  }
  API.listVmCloudServer({}, loading).then((result) => {
    servers.value = result.data;
    if (servers.value.length > 0) {
      selectedServerId.value = servers.value[0].id;

      loadAllData();
    }
  });
});
</script>
<template>
  <el-card class="server-metric" v-if="show" :shadow="cardShadow">
    <template #header>
      <div
        style="
          display: flex;
          flex-direction: row;
          flex-wrap: wrap;
          align-items: center;
        "
      >
        <h4 style="margin-right: 10px">{{ title }}</h4>
        <el-select v-model="selectedServerId" @change="loadAllData">
          <el-option
            v-for="item in servers"
            :key="item.id"
            :label="item.instanceName"
            :value="item.id"
          />
        </el-select>
        <el-date-picker
          v-model="timestampData"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="loadAllData"
          @input="loadAllData"
        />
      </div>
    </template>
    <el-row :gutter="10" v-loading="loading">
      <el-col
        :span="8"
        v-for="item in echartsData"
        style="padding-top: 10px"
        :key="item.metricName"
      >
        <div class="myChart">
          <Charts
            :ref="(el) => childRef(el, item.metricName)"
            :data="echartsData"
            :metric-name="item.metricName"
            :y-unit="item.yUnit"
            :title="item.title"
            :legend="item.legend"
            :x-data="item.xData"
            :series="item.series"
            :device-list="item.deviceList"
            :device-data="item.deviceData"
            :current-device="item.currentDevice"
            select-top="30px"
            @vnode-mounted="loadData(item.metricName)"
          />
        </div>
      </el-col>
    </el-row>
  </el-card>
</template>

<style scoped lang="scss">
.server-metric {
  height: 100%;
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
