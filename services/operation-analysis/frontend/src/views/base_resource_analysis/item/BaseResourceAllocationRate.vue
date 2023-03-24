<template>
  <div class="info-card">
    <el-row :gutter="10">
      <el-col :span="24">
        <div class="title">基础资源分配率</div>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="8" :key="item.code" v-for="item in data">
        <div class="echarts-content">
          <v-chart
            class="chart"
            :option="item.options"
            v-loading="loading"
            autoresize
          />
        </div>
        <div class="echarts-footer">
          {{ item.footerName }}
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script setup lang="ts">
import VChart from "vue-echarts";
import { computed, ref, watch } from "vue";
import ResourceSpreadViewApi from "@/api/resource_spread_view";
import _ from "lodash";
import type { ResourceAnalysisRequest } from "@commons/api/resource_spread_view/type";
interface EchartsObj {
  code: string;
  footerName: string;
  options: any;
}
const props = defineProps<{
  cloudAccountId?: string | undefined;
  clusterId?: string | undefined;
  datastoreId?: string | undefined;
  hostId?: string | undefined;
}>();
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
    ? _.set(params, "hostIds", props.hostId === "all" ? [] : [props.hostId])
    : "";
};
//获取分配情况
const getAllocatedComputerInfo = () => {
  setParams();
  ResourceSpreadViewApi.getAllocatedInfo(params, loading).then(
    (res) => (apiData.value = res.data)
  );
};
const data = computed<Array<EchartsObj>>(() => {
  const result: Array<EchartsObj> = [];
  const obj = apiData.value;
  let options = _.cloneDeep(defaultSpeedOptions);
  if (obj) {
    _.set(options, "series[0].data[0].value", obj["cpu"]?.allocatedRate);
    result.push({ code: "cpu", footerName: "CPU分配率", options: options });
    options = _.cloneDeep(defaultSpeedOptions);
    _.set(options, "series[0].data[0].value", obj["memory"]?.allocatedRate);
    result.push({ code: "memory", footerName: "内存分配率", options: options });
    options = _.cloneDeep(defaultSpeedOptions);
    _.set(options, "series[0].data[0].value", obj["datastore"]?.allocatedRate);
    result.push({
      code: "datastore",
      footerName: "存储分配率",
      options: options,
    });
  }
  return result;
});
watch(
  props,
  () => {
    getAllocatedComputerInfo();
  },
  { immediate: true }
);

const defaultSpeedOptions = {
  series: [
    {
      type: "gauge",
      progress: {
        show: false,
        width: 1,
      },
      axisLine: {
        lineStyle: {
          width: 10,
          color: [
            [0.6, "rgba(79,131,253,1)"],
            [0.8, "rgba(250,211,85,1)"],
            [1, "rgba(247,105,101,1)"],
          ],
        },
      },
      axisTick: {
        show: false,
      },
      splitLine: {
        distance: 1,
        length: 5,
        lineStyle: {
          width: 1,
          color: "#999",
        },
      },
      axisLabel: {
        show: false,
        distance: 15,
        color: "#999",
        fontSize: 5,
      },
      anchor: {
        show: true,
        showAbove: true,
        size: 10,
        itemStyle: {
          borderWidth: 4,
          borderColor: "rgba(77,83,105)",
        },
      },
      pointer: {
        itemStyle: {
          color: "rgba(77,83,105)",
        },
        color: "rgba(77,83,105)",
        //默认透明
        shadowBlur: 50,
        width: 3,
      },
      title: {
        offsetCenter: [0, "100%"],
        fontSize: 15,
        show: true,
      },
      radius: "100%",
      detail: {
        formatter: function (value: string) {
          if (value) {
            return value + "%";
          } else {
            return "0" + "%";
          }
        },
        valueAnimation: true,
        fontSize: 20,
        offsetCenter: [0, "85%"],
      },
      data: [{ value: 0 }],
    },
  ],
};
</script>
<style scoped lang="scss">
.info-card {
  background: #ffffff;
  border-radius: 4px;
  padding: 24px;
  overflow: hidden;
}
.chart {
  min-height: 120px;
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
</style>
