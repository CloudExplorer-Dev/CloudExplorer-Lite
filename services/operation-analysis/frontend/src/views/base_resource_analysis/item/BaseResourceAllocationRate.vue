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
  obj: any;
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
    result.push({
      code: "cpu",
      footerName: "CPU分配率",
      options: options,
      obj: obj["cpu"],
    });
    options = _.cloneDeep(defaultSpeedOptions);
    _.set(options, "series[0].data[0].value", obj["memory"]?.allocatedRate);
    result.push({
      code: "memory",
      footerName: "内存分配率",
      options: options,
      obj: obj["memory"],
    });
    options = _.cloneDeep(defaultSpeedOptions);
    _.set(options, "series[0].data[0].value", obj["datastore"]?.allocatedRate);
    result.push({
      code: "datastore",
      footerName: "存储分配率",
      options: options,
      obj: obj["datastore"],
    });
  }
  result.forEach((param) => {
    const total = param.obj?.total;
    const allocated = param.obj?.allocated;
    const free = total - allocated;
    const unit = param.code === "cpu" ? " 核" : " GB";
    const tooltip = {
      trigger: "item", // 触发类型, 数据项图形触发，主要在散点图，饼图等无类目轴的图表中使用。
      //无视容器，超出显示
      appendToBody: true,
      textStyle: {
        //color: '#fff' // 文字颜色
      },
      extraCssText: "width:169px;",
      formatter: ``,
    };
    let tooltipFormatter = `<div><p style="font-weight:bold;margin:0 8px 5px;">${param.footerName}</p></div>`;
    tooltipFormatter += `<div>
          <div style="margin: 0 8px;">
            <span style="display:inline-block;border-radius: 10px;margin-right:5px;width:10px;height:10px;background-color: rgb(223,224,227)"></span>
            <span>总量</span>
            <span style="float:right;color:#000000;">${total + unit}</span>
          </div>
        </div>`;
    tooltipFormatter += `<p style="padding: 0 8px 0 0;left: 79.46%;right: 14.2%;top: 23.37%;border: 1px dashed #EFF0F1;"></p>`;
    tooltipFormatter += `<div style="padding: 0 0 8px 0;">
          <div style="margin: 0 8px;">
            <span style="display:inline-block;border-radius: 10px;margin-right:5px;width:10px;height:10px;background-color: rgb(52,199,36)"></span>
            <span>未分配</span>
            <span style="float:right;color:#000000;">${free + unit}</span>
          </div>
        </div>`;
    tooltipFormatter += `<div>
          <div style="margin: 0 8px;">
            <span style="display:inline-block;border-radius: 10px;margin-right:5px;width:10px;height:10px;background-color: rgb(223,224,227)"></span>
            <span>已分配</span>
            <span style="float:right;color:#000000;">${allocated + unit}</span>
          </div>
        </div>`;
    tooltip.formatter = tooltipFormatter;
    _.set(param.options, "tooltip", tooltip);
  });
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
        size: 5,
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
        width: 5,
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
