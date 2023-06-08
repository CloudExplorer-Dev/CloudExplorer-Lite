<template>
  <base-container>
    <template #content>
      <el-date-picker
        v-model="timestampData"
        type="datetimerange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :default-time="[new Date(), new Date()]"
        @change="changeTimestamp"
        @input="changeTimestamp"
      >
      </el-date-picker>
      <div class="card-content">
        <el-row :gutter="24" style="margin-right: -8px">
          <el-col
            :xl="12"
            :md="12"
            :sm="48"
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
                @vnode-mounted="loadingEchartsDone"
              />
            </div>
          </el-col>
        </el-row>
      </div>
    </template>
  </base-container>
</template>
<script setup lang="ts">
const props = defineProps<{
  id: string;
}>();
import { ref, onMounted } from "vue";
import {
  PerfMetricConst,
  PerfMetricEntityTypeConst,
} from "@commons/utils/constants";
import { useRouter } from "vue-router";
import VmCloudServerApi from "@/api/vm_cloud_server";
import Charts from "@commons/components/echart/Charts.vue";
import _ from "lodash";
const useRoute = useRouter();
//初始化图表数据，固定指标图表
const echartsData = ref<Array<any>>([]);
const request = ref<any>({});
// 云主机CPU使用率、内存使用率、磁盘BPS、磁盘IOPS、磁盘使用率、内网带宽、外网带宽
const echarts: Map<string, any> = new Map();
// 所有图表集合
const childRefMap: Map<string, any> = new Map();
const childRef = (el: any, metricName: any) => {
  childRefMap.set(metricName, el);
};
// 云主机详情
const infoVmCloudServer = ref<any>({});
// 等待所有图表初始化完成，再进行数据查询
const loadingNumber = ref<number>(0);
const loadingEchartsDone = () => {
  loadingNumber.value++;
  if (loadingNumber.value === echarts.size) {
    getData();
  }
};
/**
 * 查询数据，统一查询所有指标
 */
const getData = () => {
  Object.keys(PerfMetricConst).forEach(function (metricName) {
    if (vSphereFilter(metricName)) {
      return;
    }
    if (huaweiFilter(metricName)) {
      return;
    }
    if (childRefMap.get(metricName)) {
      childRefMap.get(metricName).echartsLoading();
    }
    request.value.metricName = PerfMetricConst[metricName].metricName;
    VmCloudServerApi.listPerfMetricMonitor(request.value).then((res) => {
      const xData = ref<any>([]);
      const yData = ref<any>([]);
      const deviceList = ref<any>([]);
      let yUnit = undefined;
      if (_.keys(res.data).length > 0 && _.has(res.data, "other")) {
        yData.value = res.data["other"][0].values;
        xData.value = res.data["other"][0].timestamps;
        yUnit = res.data["other"][0].unit;
      }
      if (
        metricName === PerfMetricConst.DISK_READ_BPS.metricName ||
        metricName === PerfMetricConst.DISK_WRITE_BPS.metricName
      ) {
        const isInitMapMetricName =
          metricName === PerfMetricConst.DISK_READ_BPS.metricName;
        const metricNameParam = isInitMapMetricName
          ? PerfMetricConst.DISK_READ_BPS.metricName
          : PerfMetricConst.DISK_WRITE_BPS.metricName;
        setXData(
          PerfMetricConst.DISK_READ_BPS.metricName,
          metricNameParam,
          xData.value,
          yData.value,
          yUnit
        );
      } else if (
        metricName === PerfMetricConst.DISK_READ_IOPS.metricName ||
        metricName === PerfMetricConst.DISK_WRITE_IOPS.metricName
      ) {
        const isInitMapMetricName =
          metricName === PerfMetricConst.DISK_READ_IOPS.metricName;
        const metricNameParam = isInitMapMetricName
          ? PerfMetricConst.DISK_READ_IOPS.metricName
          : PerfMetricConst.DISK_WRITE_IOPS.metricName;
        setXData(
          PerfMetricConst.DISK_READ_IOPS.metricName,
          metricNameParam,
          xData.value,
          yData.value,
          yUnit
        );
      } else if (
        metricName === PerfMetricConst.INTERNET_IN_RATE.metricName ||
        metricName === PerfMetricConst.INTERNET_OUT_RATE.metricName
      ) {
        const isInitMapMetricName =
          metricName === PerfMetricConst.INTERNET_IN_RATE.metricName;
        const metricNameParam = isInitMapMetricName
          ? PerfMetricConst.INTERNET_IN_RATE.metricName
          : PerfMetricConst.INTERNET_OUT_RATE.metricName;
        setXData(
          PerfMetricConst.INTERNET_IN_RATE.metricName,
          metricNameParam,
          xData.value,
          yData.value,
          yUnit
        );
      } else if (
        metricName === PerfMetricConst.INTRANET_IN_RATE.metricName ||
        metricName === PerfMetricConst.INTRANET_OUT_RATE.metricName
      ) {
        const isInitMapMetricName =
          metricName === PerfMetricConst.INTRANET_IN_RATE.metricName;
        const metricNameParam = isInitMapMetricName
          ? PerfMetricConst.INTRANET_IN_RATE.metricName
          : PerfMetricConst.INTRANET_OUT_RATE.metricName;
        setXData(
          PerfMetricConst.INTRANET_IN_RATE.metricName,
          metricNameParam,
          xData.value,
          yData.value,
          yUnit
        );
      } else if (
        metricName === PerfMetricConst.DISK_USED_UTILIZATION.metricName
      ) {
        if (_.keys(res.data).length > 0) {
          deviceList.value = _.keys(res.data);
          yData.value = res.data[_.keys(res.data)[0]][0].values;
          xData.value = res.data[_.keys(res.data)[0]][0].timestamps;
          const d = echartsData.value.filter((i) => {
            return metricName == i.metricName;
          });
          if (d[0]) {
            if (deviceList.value.length > 0) {
              d[0].deviceList = deviceList.value;
              d[0].currentDevice = deviceList.value[0];
            }
            d[0].deviceData = res.data;
            d[0].xData = xData.value;
            d[0].series[0].connectNulls = true;
            d[0].series[0].data = yData.value;
          }
        }
      } else {
        const d = echartsData.value.filter((i) => {
          return metricName == i.metricName;
        });
        if (d[0]) {
          d[0].xData = xData.value;
          d[0].series[0].connectNulls = true;
          d[0].series[0].data = yData.value;
        }
      }
      if (childRefMap.get(metricName)) {
        childRefMap.get(metricName).hideEchartsLoading();
      }
    });
  });
};
/**
 * 设置图表x轴数据（时间）
 * @param mapMetricName
 * @param metricName
 * @param res
 * @param yData
 * @param yUnit
 */
const setXData = (
  mapMetricName: string,
  metricName: string,
  res: any,
  yData: any,
  yUnit: any
) => {
  const d = echartsData.value.filter((i) => {
    return mapMetricName == i.metricName;
  });
  if (d[0]) {
    if (yUnit) {
      d[0].yUnit = yUnit;
    }
    if (d[0].title.indexOf(d[0].yUnit) === -1) {
      d[0].title = d[0].title + "(" + d[0].yUnit + ")";
    }
    d[0].xData = res;
    d[0].series[0].connectNulls = true;
    d[0].series.forEach(function (s: any) {
      if (s.name === PerfMetricConst[metricName].name) {
        s.data = yData;
      }
    });
  }
};

/**
 * 默认时间当前时间过去一个小时
 */
const timestampData = ref<[Date, Date]>([
  new Date(new Date().getTime() - 24 * 60 * 60 * 1000),
  new Date(),
]);
/**
 * 时间调整
 */
const changeTimestamp = () => {
  request.value.startTime = timestampData.value[0].getTime();
  request.value.endTime = timestampData.value[1].getTime();
  getData();
};

onMounted(() => {
  VmCloudServerApi.getVmCloudServerById(props.id)
    .then((res) => {
      infoVmCloudServer.value = _.cloneDeep(res.data);
      // 查监控数据参数
      request.value.startTime = timestampData.value[0].getTime();
      request.value.endTime = timestampData.value[1].getTime();
      request.value.instanceId = infoVmCloudServer.value.instanceUuid;
      request.value.cloudAccountId = infoVmCloudServer.value.accountId;
      request.value.entityType = PerfMetricEntityTypeConst.VIRTUAL_MACHINE;
      request.value.metricName = "";
      request.value.platform = infoVmCloudServer.value.platform;
      initEchartsData();
    })
    .catch((err) => {
      console.log(err);
    });
});
/**
 * 初始化图表数据
 */
const initEchartsData = () => {
  //初始化echarts数据
  const data = {
    metricName: "",
    yUnit: "",
    title: "",
    legend: [],
    xData: [],
    series: [],
    deviceList: [],
    currentDevice: "",
    deviceData: {},
  };
  echarts.set(
    PerfMetricConst.CPU_USED_UTILIZATION.metricName,
    _.cloneDeep(data)
  );
  echarts.set(
    PerfMetricConst.MEMORY_USED_UTILIZATION.metricName,
    _.cloneDeep(data)
  );
  //只需要一个就可以了，只是为了分组用
  echarts.set(PerfMetricConst.DISK_READ_BPS.metricName, _.cloneDeep(data));
  echarts.set(PerfMetricConst.DISK_READ_IOPS.metricName, _.cloneDeep(data));
  echarts.set(PerfMetricConst.INTRANET_IN_RATE.metricName, _.cloneDeep(data));
  echarts.set(PerfMetricConst.INTERNET_IN_RATE.metricName, _.cloneDeep(data));
  echarts.set(
    PerfMetricConst.DISK_USED_UTILIZATION.metricName,
    _.cloneDeep(data)
  );

  if (isVsphere()) {
    echarts.delete(PerfMetricConst.INTERNET_IN_RATE.metricName);
    echarts.delete(PerfMetricConst.DISK_USED_UTILIZATION.metricName);
  }
  if (isHuawei()) {
    echarts.delete(PerfMetricConst.DISK_USED_UTILIZATION.metricName);
  }

  Object.keys(PerfMetricConst).forEach(function (perfMetric) {
    const metricName = PerfMetricConst[perfMetric].metricName;
    if (vSphereFilter(perfMetric)) {
      return;
    }
    if (huaweiFilter(perfMetric)) {
      return;
    }
    const series = {
      name: PerfMetricConst[perfMetric].name,
      showSymbol: false,
      symbol: "circle",
      data: [],
      type: "line",
      smooth: false,
      connectNulls: true,
    };

    const yUnit = PerfMetricConst[perfMetric].unit;
    //多数据图表初始数据处理
    if (metricName === PerfMetricConst.DISK_WRITE_BPS.metricName) {
      const d = echarts.get(PerfMetricConst.DISK_READ_BPS.metricName);
      d.yUnit = yUnit;
      d.title = "磁盘读写BPS";
      d.legend.push(PerfMetricConst[perfMetric].name);
      d.series.push(series);
    }
    if (metricName === PerfMetricConst.DISK_WRITE_IOPS.metricName) {
      const d = echarts.get(PerfMetricConst.DISK_READ_IOPS.metricName);
      d.yUnit = yUnit;
      d.title = "磁盘每秒读取次数IOPS";
      d.legend.push(PerfMetricConst[perfMetric].name);
      d.series.push(series);
    }
    if (metricName === PerfMetricConst.INTRANET_OUT_RATE.metricName) {
      const d = echarts.get(PerfMetricConst.INTRANET_IN_RATE.metricName);
      d.yUnit = yUnit;
      d.title = "内网流入流出带宽";
      d.legend.push(PerfMetricConst[perfMetric].name);
      d.series.push(series);
    }
    if (metricName === PerfMetricConst.INTERNET_OUT_RATE.metricName) {
      const d = echarts.get(PerfMetricConst.INTERNET_IN_RATE.metricName);
      d.yUnit = yUnit;
      d.title = "公网流入流出带宽";
      d.legend.push(PerfMetricConst[perfMetric].name);
      d.series.push(series);
    }
    // 单数据图表初始数据处理
    if (echarts.get(perfMetric)) {
      const d = echarts.get(perfMetric);
      d.metricName = metricName;
      d.yUnit = yUnit;
      d.title = PerfMetricConst[perfMetric].description;
      d.legend.push(PerfMetricConst[perfMetric].name);
      d.series.push(series);
    }
  });
  // 设置初始化数据
  echarts.forEach((v) => {
    echartsData.value.push(v);
  });
};
const vSphereFilter = (perfMetric: any) => {
  const metricName = PerfMetricConst[perfMetric].metricName;
  if (
    isVsphere() &&
    (metricName === PerfMetricConst.INTERNET_OUT_RATE.metricName ||
      metricName === PerfMetricConst.INTERNET_IN_RATE.metricName ||
      metricName === PerfMetricConst.DISK_USED_UTILIZATION.metricName)
  ) {
    return true;
  }
  return false;
};
const huaweiFilter = (perfMetric: any) => {
  const metricName = PerfMetricConst[perfMetric].metricName;
  if (
    isHuawei() &&
    metricName === PerfMetricConst.DISK_USED_UTILIZATION.metricName
  ) {
    return true;
  }
  return false;
};
const isVsphere = () => {
  return request.value.platform === "fit2cloud_vsphere_platform";
};
const isHuawei = () => {
  return request.value.platform === "fit2cloud_huawei_platform";
};
</script>
<style lang="scss">
.myChart {
  width: 100%;
  border: 1px solid #e5e5e5;
}
</style>
