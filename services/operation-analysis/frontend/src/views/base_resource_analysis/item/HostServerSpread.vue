<template>
  <el-card shadow="never" class="info-card">
    <el-row :gutter="10">
      <el-col :span="24">
        <div class="title">宿主机上云主机分布</div>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="24">
        <div class="echarts">
          <div class="echarts-content">
            <v-chart
                class="chart"
                :option="option"
                v-loading="loading"
                autoresize
            />
          </div>
        </div>
      </el-col>
    </el-row>
  </el-card>
</template>
<script setup lang="ts">
import VChart from "vue-echarts";
import { computed, ref, watch } from "vue";
import ResourceSpreadViewApi from "@/api/resource_spread_view/index";
import _ from "lodash";
import type { ResourceAnalysisRequest } from "@commons/api/resource_spread_view/type";
import type { ECBasicOption } from "echarts/types/src/util/types";
const props = defineProps<{
  cloudAccountId?: string | undefined;
  clusterId?: string | undefined;
  datastoreId?: string | undefined;
  hostId?: string | undefined;
}>();
const params = ref<ResourceAnalysisRequest>();
const loading = ref<boolean>(false);
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
//获取数宿主机按云账号分布
const apiDataStopped = ref<any>();
const apiDataRunning = ref<any>();
const getSpreadInfo = () => {
  setParams();
  _.set(params, "vmStatus", "stopped");
  ResourceSpreadViewApi.getSpreadInfo(params, loading).then((res) => {
    apiDataStopped.value = res.data;
    _.set(params, "vmStatus", "running");
    ResourceSpreadViewApi.getSpreadInfo(params, loading).then(
        (res) => (apiDataRunning.value = res.data)
    );
  });
};
interface EchartsValue {
  name: Array<string>;
  running: Array<number>;
  stopped: Array<number>;
}
interface ApiDataVO {
  name: string;
  value: number;
}

const data = computed<EchartsValue>(() => {
  const result: EchartsValue = { name: [], running: [], stopped: [] };
  const names: Array<string> = [];
  const running: Array<number> = [];
  const stopped: Array<number> = [];
  const hosts: Array<any> = _.unionBy(
      apiDataRunning.value?.vm,
      apiDataRunning.value?.vm,
      "name"
  );
  _.forEach(hosts, (v: ApiDataVO) => {
    names.push(v.name);
    const r = _.find(apiDataRunning.value?.vm, (o) => o.name === v.name);
    if (r) {
      running.push(r.value);
    } else {
      running.push(0);
    }
    const s = _.find(apiDataStopped.value?.vm, (o) => o.name === v.name);
    if (s) {
      stopped.push(s.value);
    } else {
      stopped.push(0);
    }
  });
  result.name = names;
  result.running = running;
  result.stopped = stopped;
  return result;
});
const option = computed<ECBasicOption>(() => {
  const selected = data.value;
  if (!selected || selected.name?.length === 0) {
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
  const options = {
    dataZoom: [
      {
        type: "slider",
        realtime: true,
        start: 0,
        end: 100, // 数据窗口范围的结束百分比。范围是：0 ~ 100。
        height: 5, //组件高度
        left: 5, //左边的距离
        right: 5, //右边的距离
        bottom: 10, //下边的距离
        show: false, // 是否展示
        fillerColor: "rgba(17, 100, 210, 0.42)", // 滚动条颜色
        borderColor: "rgba(17, 100, 210, 0.12)",
        handleSize: 0, //两边手柄尺寸
        showDetail: false, //拖拽时是否展示滚动条两侧的文字
        zoomLock: true, //是否只平移不缩放
        moveOnMouseMove: false, //鼠标移动能触发数据窗口平移
        brushSelect: false,
      },
      {
        type: "inside", // 支持内部鼠标滚动平移
        disabled:true, // 停止组件内功能
        start: 0,
        end: 100,
        zoomOnMouseWheel: false, // 关闭滚轮缩放
        moveOnMouseWheel: false, // 开启滚轮平移
        moveOnMouseMove: false, // 鼠标移动能触发数据窗口平移
      },
    ],
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "shadow",
      },
    },
    axisLabel: {
      formatter: function (value: any) {
        let valueTxt;
        if (value.length > 12) {
          valueTxt = value.substring(0, 12) + "...";
        } else {
          valueTxt = value;
        }
        return valueTxt;
      },
    },
    legend: {
      show: true,
      type: "scroll",
      icon: "circle",
      top: 0,
      right: 10,
      padding: [0, 0, 5, 10],
    },
    grid: {
      left: "0%",
      right: "0%",
      top: "30px",
      bottom: "10%",
      containLabel: true,
    },
    yAxis: {
      type: "value",
    },
    xAxis: {
      type: "category",
      data: selected.name,
      axisTick: false,
      axisLabel: {
        // showMaxLabel: false,
        // showMinLabel: false,
      },
    },
    color: ["rgba(98, 210, 85,1)", "rgba(223, 224, 227, 1)"],
    series: [
      {
        name: "运行中",
        type: "bar",
        stack: "total",
        label: {
          show: false,
        },
        emphasis: {
          focus: "series",
        },
        data: selected.running,
        barWidth: 16,
      },
      {
        name: "已停止",
        type: "bar",
        stack: "total",
        label: {
          show: false,
        },
        emphasis: {
          focus: "series",
        },
        data: selected.stopped,
        barWidth: 16,
        itemStyle: {
          normal: {
            //这里设置柱形图圆角 [左上角，右上角，右下角，左下角]
            barBorderRadius: [2, 2, 0, 0],
          },
        },
      },
    ],
  };
  const deptNumber = selected.name.map((item: any) => item.name);
  let showEcharts = false;
  let nameNum = 0;
  if (deptNumber.length > 0) {
    nameNum = Math.floor(100 / (deptNumber.length / 4));
    showEcharts = deptNumber.length >= 5;
  }
  _.set(options, "dataZoom.[0].end", nameNum);
  _.set(options, "dataZoom.[1].end", nameNum);
  _.set(options, "dataZoom.[0].show", showEcharts);
  return options;
});
watch(
    props,
    () => {
      getSpreadInfo();
    },
    { immediate: true }
);
</script>
<style scoped lang="scss">
.info-card {
  height: 277px;
  background: #ffffff;
  border-radius: 4px;
  flex: none;
  order: 0;
  flex-grow: 0;
}
.chart {
  min-height: 189px;
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
