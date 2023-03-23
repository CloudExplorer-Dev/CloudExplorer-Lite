<template>
  <el-card shadow="never" class="info-card">
    <el-row>
      <el-col :span="10">
        <div class="title">云主机分布</div>
      </el-col>
      <el-col :span="14" style="text-align: right">
        <el-radio-group
          class="custom-radio-group"
          v-model="paramDepartmentType"
          @change="getSpreadByDepartmentData()"
        >
          <el-radio-button label="org">组织</el-radio-button>
          <el-radio-button label="workspace">工作空间</el-radio-button>
        </el-radio-group>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="24">
        <div class="echarts">
          <div class="echarts-content">
            <div class="back-btn" @click="handleBackClick" v-if="showBack">
              返回
            </div>
            <v-chart
              @click="onClick"
              class="chart"
              :option="options"
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
import CloudServerViewApi from "@/api/server_analysis/index";
import _ from "lodash";
import type { ResourceAnalysisRequest } from "@/api/server_analysis/type";
import type { ECBasicOption } from "echarts/types/src/util/types";
const props = defineProps<{
  cloudAccountId?: string | undefined;
}>();
const paramDepartmentType = ref<string>("org");
const params = ref<ResourceAnalysisRequest>();
const loading = ref<boolean>(false);
const apiData = ref<any>();
const showBack = ref<boolean>(false);
const parentItem = ref<any>({});
const getSpreadByDepartmentData = () => {
  showBack.value = false;
  _.set(params, "analysisWorkspace", paramDepartmentType.value === "workspace");
  _.set(
    params,
    "accountIds",
    props.cloudAccountId === "all" ? [] : [props.cloudAccountId]
  );
  CloudServerViewApi.getAnalysisOrgWorkspaceVmCount(params, loading).then(
    (res) => (apiData.value = res.data)
  );
};

const options = computed<ECBasicOption>(() => {
  const obj = apiData.value;
  const options = _.cloneDeep(defaultSpeedOptions);
  if (obj) {
    const tree = apiData.value.tree;
    if (tree.length === 0) {
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
    _.set(
      options,
      "xAxis.data",
      tree.map((item: any) => item.name)
    );
    _.set(options, "series[0].itemStyle", barSeriesItemStyle);
    _.set(options, "series[0].label", barSeriesLabel);
    const seriesData = ref<any>([]);
    _.forEach(tree, (v) => {
      seriesData.value.push({ value: v.value, groupName: v.groupName });
    });
    _.set(options, "series[0].data", seriesData.value);
    _.set(options, "series[0].name", "云主机");
    _.set(options, "legend.data", ["云主机"]);
    const deptNumber = tree.map((item: any) => item.name);
    let showEcharts = false;
    let nameNum = 0;
    if (deptNumber.length > 0) {
      nameNum = Math.floor(100 / (deptNumber.length / 9));
      showEcharts = deptNumber.length > 9;
    }
    _.set(options, "dataZoom.[0].end", nameNum);
    _.set(options, "dataZoom.[1].end", nameNum);
    _.set(options, "dataZoom.[0].show", showEcharts);
  }
  return options;
});

watch(
  props,
  () => {
    getSpreadByDepartmentData();
  },
  { immediate: true }
);

const handleBackClick = () => {
  const item = apiData.value.all.find(
    (v: any) => v.name === parentItem.value.name
  );
  if (!item) return;
  if (!item.pid) {
    showBack.value = false;
    apiData.value.tree = [item];
  } else {
    //父节点的父节点
    const ppItem = apiData.value.all.find((v: any) => v.id === item.pid);
    const children = ppItem.children;
    if (children && children.length > 0) {
      showBack.value = true;
      parentItem.value = ppItem;
      apiData.value.tree = children;
    }
  }
};
const onClick = (params: any) => {
  const item = apiData.value.all.find((v: any) => v.name === params.name);
  if (!item) return;
  const children = item.children;
  if (children && children.length > 0) {
    showBack.value = true;
    parentItem.value = item;
    apiData.value.tree = children;
  }
};

const defaultSpeedOptions = {
  dataZoom: [
    {
      type: "slider",
      realtime: true,
      start: 0,
      end: 100, // 数据窗口范围的结束百分比。范围是：0 ~ 100。
      height: 15, //组件高度
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
      startValue: 0, // 从头开始。
      endValue: 9, // 最多六个
      minValueSpan: 9, // 放大到最少几个
      maxValueSpan: 9, //  缩小到最多几个
    },
    {
      type: "inside", // 支持内部鼠标滚动平移
      start: 0,
      end: 100,
      zoomOnMouseWheel: false, // 关闭滚轮缩放
      moveOnMouseWheel: false, // 开启滚轮平移
      moveOnMouseMove: false, // 鼠标移动能触发数据窗口平移
    },
  ],
  color: ["#4E83FD"],
  legend: {
    show: true,
    type: "scroll",
    icon: "circle",
    y: "bottom",
    padding: [0, 0, 10, 0],
  },
  grid: {
    left: "3%",
    right: "4%",
    top: "17px",
    bottom: "15%",
    containLabel: true,
  },
  tooltip: {
    show: true,
  },
  xAxis: {
    type: "category",
    data: [],
  },
  yAxis: {
    type: "value",
  },
  series: [
    {
      barWidth: 16,
      data: [],
      type: "bar",
    },
  ],
};
const colors = ["#4E83FD"];
const barSeriesItemStyle = {
  color: function (params: any) {
    if (params.data && params.data?.groupName === "org") {
      return colors[0];
    } else if (params.data && params.data?.groupName === "available") {
      return colors[0];
    }
    return colors[0];
  },
};
const barSeriesLabel = {
  show: true, //开启显示
  position: "top", //在上方显示
  textStyle: {
    //数值样式
    color: "black",
    fontSize: 12,
  },
};
</script>
<style scoped lang="scss">
.info-card {
  height: 448px;
  background: #ffffff;
  border-radius: 4px;
  flex: none;
  order: 0;
  flex-grow: 0;
}
.chart {
  min-height: 368px;
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
  //position: absolute;
  z-index: 10;
  cursor: pointer;
}
</style>
