<template>
  <div
    class="button"
    @click="handleBackClick"
    style="margin-left: 5%; position: absolute; z-index: 10"
    v-if="showBack"
  >
    返回
  </div>
  <div
    v-resize="resize"
    ref="chartWrapper"
    :style="getChartStyle"
    style="width: 100%; display: flex"
  ></div>
</template>

<script lang="ts" setup>
import { computed, nextTick, onMounted, ref, watch } from "vue";
import _ from "lodash";
import * as echarts from "echarts";
import type { ECharts } from "echarts";

const props = withDefaults(
  defineProps<{
    options?: any;
    height?: number | string;
    treeBar?: boolean;
    treeBarData?: Array<any>;
    treeBarAllData?: Array<any>;
  }>(),
  {
    options: {},
    height: "300px",
    treeBar: false,
    treeBarData: () => [],
    treeBarAllData: () => [],
  }
);

const chartWrapper = ref<any>(null);

const getChartStyle = computed(() => {
  if (typeof props.height === "number") {
    return {
      height: `${props.height}px`,
    };
  } else {
    return {
      height: props.height,
    };
  }
});

watch(
  () => props.options,
  () => {
    setOptions();
    initTreeBar();
  },
  {
    deep: true,
  }
);

let eChartsRef: ECharts | undefined = undefined;

const resize = () => {
  eChartsRef?.resize();
};

const initChart = () => {
  if (!eChartsRef) {
    eChartsRef = echarts.init(chartWrapper.value);
  }
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
const showBack = ref<boolean>(false);
const parentItem = ref<any>({});
const initTreeBar = () => {
  if (props.treeBar && props.treeBarData.length > 0) {
    showBack.value = false;
    eChartsRef?.on("click", (params: any) => {
      const item = props.treeBarAllData.find(
        (v: any) => v.name === params.name
      );
      if (!item) return;
      const children = item.children;
      if (children && children.length > 0) {
        showBack.value = true;
        parentItem.value = item;
        const seriesData = ref<any>([]);
        _.forEach(children, (v) => {
          seriesData.value.push({ value: v.value, groupName: v.groupName });
        });
        eChartsRef?.setOption({
          xAxis: { data: children.map((item: any) => item.name) },
          series: [
            {
              barWidth:16,
              data: seriesData.value,
              label: barSeriesLabel,
              itemStyle: barSeriesItemStyle,
            },
          ],
        });
      }
    });
  }
};

const handleBackClick = () => {
  const item = props.treeBarAllData.find(
    (v: any) => v.id === parentItem.value.pid
  );
  if (!item) {
    showBack.value = false;
    const seriesData = ref<any>([]);
    _.forEach(props.treeBarData, (v) => {
      seriesData.value.push({ value: v.value, groupName: v.groupName });
    });
    eChartsRef?.setOption({
      xAxis: { data: props.treeBarData.map((item: any) => item.name) },
      series: [
        {
          barWidth:16,
          data: seriesData.value,
          label: barSeriesLabel,
          itemStyle: barSeriesItemStyle,
        },
      ],
    });
  } else {
    const children = item.children;
    if (children.length > 0) {
      const seriesData = ref<any>([]);
      _.forEach(children, (v) => {
        seriesData.value.push({ value: v.value, groupName: v.groupName });
      });
      parentItem.value = item;
      eChartsRef?.setOption({
        xAxis: { data: children.map((v: any) => v.name) },
        series: [
          {
            barWidth:16,
            data: seriesData.value,
            label: barSeriesLabel,
            itemStyle: barSeriesItemStyle,
          },
        ],
      });
    }
  }
};

const setOptions = () => {
  let options = {};
  if (!isEmptyObj(props.options)) {
    options = props.options;
  } else {
    options = emptyOptions;
  }
  nextTick(() => {
    eChartsRef?.clear();
    eChartsRef?.setOption(options);
  });
};
const echartsClear = () => {
  eChartsRef?.clear();
};

const echartsLoading = () => {
  eChartsRef?.showLoading({
    text: "加载中",
    //color: '#c23531',
    textColor: "#000",
    maskColor: "rgba(255, 255, 255, 0.8)",
    zlevel: 0,
  });
};
const hideEchartsLoading = () => {
  eChartsRef?.hideLoading();
};
defineExpose({
  echartsLoading,
  hideEchartsLoading,
  echartsClear,
  handleBackClick,
  barSeriesItemStyle,
  barSeriesLabel,
});

const isEmptyObj = (obj: any) => {
  return typeof obj === "object" && JSON.stringify(obj) === "{}";
};

const emptyOptions = {
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

onMounted(() => {
  initChart();
  initTreeBar();
});
</script>
<style lang="scss" scoped>
.button {
  cursor: pointer;
}
</style>
