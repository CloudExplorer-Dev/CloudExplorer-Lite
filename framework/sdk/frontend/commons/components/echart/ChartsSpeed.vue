<template>
  <div
    class="button"
    @click="handleBackClick"
    style="margin-left: 5%; position: absolute; z-index: 10"
    v-if="showBack"
  >
    返回
  </div>
  <div :id="`echarts-${uuid}`" :style="getChartStyle"></div>
</template>

<script lang="ts" setup>
import {
  computed,
  nextTick,
  onBeforeUnmount,
  onMounted,
  ref,
  shallowRef,
  watch,
} from "vue";
import _, { debounce } from "lodash";
import * as echarts from "echarts";

const props = defineProps({
  options: {
    type: Object,
    default: () => {
      return {};
    },
  },
  height: {
    type: Number,
    default: 300,
  },
  treeBar: {
    type: Boolean,
    default: () => {
      return false;
    },
  },
  treeBarData: {
    type: Object,
    default: () => {
      return [];
    },
  },
  treeBarAllData: {
    type: Object,
    default: () => {
      return [];
    },
  },
});

const getChartStyle = computed(() => {
  return {
    width: "100%",
    height: `${props.height}px`,
  };
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

const guid = () => {
  return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
};

const getUuid = (): string => {
  return (
    guid() +
    guid() +
    "-" +
    guid() +
    "-" +
    guid() +
    "-" +
    guid() +
    "-" +
    guid() +
    guid() +
    guid()
  );
};

const uuid = getUuid();

const resizeHandler = () => {
  eChartsRef.value.resize();
};

const resizeHandlerOrigin = debounce(resizeHandler, 500);

const eChartsRef = shallowRef<any>();
const initChart = () => {
  /**
   * 多个图表会被后面的覆盖，这里使用uui区分
   */
  const chartDom = document.getElementById(`echarts-${uuid}`)!;
  eChartsRef.value = echarts.init(chartDom);
};
const colors = ["#00b2ff", "#31b1c2", "#67c23a"];
const barSeriesItemStyle = {
  normal: {
    color: function (params: any) {
      if (params.data && params.data?.groupName === "org") {
        return colors[0];
      } else if (params.data && params.data?.groupName === "available") {
        return colors[2];
      }
      return colors[1];
    },
    label: {
      show: true, //开启显示
      position: "top", //在上方显示
      textStyle: {
        //数值样式
        color: "black",
        fontSize: 12,
      },
    },
  },
};
const showBack = ref<boolean>(false);
const parentItem = ref<any>({});
const initTreeBar = () => {
  if (props.treeBar && props.treeBarData.length > 0) {
    showBack.value = false;
    eChartsRef.value.on("click", (params: any) => {
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
        eChartsRef.value.setOption({
          xAxis: { data: children.map((item: any) => item.name) },
          series: [{ data: seriesData.value, itemStyle: barSeriesItemStyle }],
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
    eChartsRef.value.setOption({
      xAxis: { data: props.treeBarData.map((item: any) => item.name) },
      series: [{ data: seriesData.value, itemStyle: barSeriesItemStyle }],
    });
  } else {
    const children = item.children;
    if (children.length > 0) {
      const seriesData = ref<any>([]);
      _.forEach(children, (v) => {
        seriesData.value.push({ value: v.value, groupName: v.groupName });
      });
      parentItem.value = item;
      eChartsRef.value.setOption({
        xAxis: { data: children.map((v: any) => v.name) },
        series: [{ data: seriesData.value, itemStyle: barSeriesItemStyle }],
      });
    }
  }
};
window.onresize = () => {
  console.log("窗口发生变化时会打印该条");
};

const setOptions = () => {
  let options = {};
  if (!isEmptyObj(props.options)) {
    options = props.options;
  } else {
    options = emptyOptions;
  }
  nextTick(() => {
    eChartsRef.value.clear();
    eChartsRef.value.setOption(options);
    window.addEventListener("resize", resizeHandlerOrigin);
  });
};
const echartsClear = () => {
  eChartsRef.value.clear();
  resizeHandler();
};

const echartsLoading = () => {
  eChartsRef.value.showLoading({
    text: "加载中",
    //color: '#c23531',
    textColor: "#000",
    maskColor: "rgba(255, 255, 255, 0.8)",
    zlevel: 0,
  });
};
const hideEchartsLoading = () => {
  eChartsRef.value.hideLoading();
};
defineExpose({
  echartsLoading,
  hideEchartsLoading,
  echartsClear,
  handleBackClick,
  barSeriesItemStyle,
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
