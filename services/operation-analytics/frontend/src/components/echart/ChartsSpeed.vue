<template>
  <div :id="`echarts-${uuid}`" :style="getChartStyle"></div>
</template>

<script name="MyCharts" lang="ts" setup>
import {
  shallowRef,
  computed,
  watch,
  onMounted,
  onBeforeUnmount,
  nextTick,
} from "vue";
import { debounce } from "lodash";
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
});

const emit = defineEmits(["update"]);

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
  },
  {
    deep: true,
  }
);

const guid = () => {
  return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
};

const getUuid = (): string => {
  const uuid =
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
    guid();
  return uuid;
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

const setOptions = () => {
  let options = {};
  if (!isEmptyObj(props.options)) {
    options = props.options;
  } else {
    options = emptyOptions;
  }
  nextTick(() => {
    eChartsRef.value.clear();
    resizeHandler();
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
});

onBeforeUnmount(() => {
  window.removeEventListener("resize", resizeHandlerOrigin);
  eChartsRef.value.dispose();
});
</script>
<style lang="scss" scoped></style>
