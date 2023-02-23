<script setup lang="ts">
import { onMounted, ref, watch } from "vue";
import * as echarts from "echarts";
import _ from "lodash";

const props = defineProps({
  data: {
    type: Object,
    default: () => {
      return {};
    },
  },
  metricName: {
    type: String,
  },
  yUnit: {
    type: String,
  },
  title: {
    type: String,
  },
  legend: {
    type: Array,
  },
  xData: {
    type: Array,
    default: () => {
      return {};
    },
  },
  deviceList: {
    type: Object,
    default: () => {
      return {};
    },
  },
  currentDevice: {
    type: String,
    default: () => {
      return "";
    },
  },
  deviceData: {
    type: Object,
    default: () => {
      return [];
    },
  },
  series: {
    type: Object,
    default: () => {
      return [];
    },
  },
  selectTop: {
    type: String,
    default: () => {
      return "10px";
    },
  },
});

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

// chart实例
let myChart: any;

const initEcharts = (): void => {
  /**
   * 多个图表会被后面的覆盖，这里使用uui区分
   */
  const chartDom = document.getElementById(`echarts-${uuid}`)!;
  myChart = echarts.init(chartDom);
};

const echartsLoading = () => {
  myChart.showLoading({
    text: "loading",
    //color: '#c23531',
    textColor: "#000",
    maskColor: "rgba(255, 255, 255, 0.8)",
    zlevel: 0,
    showSpinner: false,
  });
};
const hideEchartsLoading = () => {
  myChart.hideLoading();
};
defineExpose({
  echartsLoading,
  hideEchartsLoading,
});

onMounted(() => {
  initEcharts();
  setEchartsData(undefined);
  window.addEventListener("resize", () => {
    myChart && myChart.resize();
  });
});

const setEchartsData = (seriesData: any) => {
  const currentSeriesData = _.cloneDeep(props.series);
  if (seriesData) {
    currentSeriesData[0].data = seriesData;
  }
  myChart.hideLoading();
  myChart.setOption({
    xAxis: {
      type: "category",
      data: props.xData,
      axisLabel: {
        formatter: function (timestamp: any) {
          return timestampToTime(timestamp);
        },
        rotate: 0,
        interval: "auto",
        textStyle: {
          fontSize: 12,
        },
      },
    },
    yAxis: {
      type: "value",
      axisLabel: {
        formatter: function (val: any) {
          return yUnitConversion(val);
        },
        textStyle: {
          fontSize: 12,
        },
      },
    },
    graphic: {
      type: "text",
      left: "center",
      top: "middle",
      silent: true,
      invisible: props.series[0].data.length != 0, //是否可见，这里的意思是当没有数据时可见
      style: {
        fill: "black",
        font: '12px "bold" ',
        text: "暂无数据",
      },
    },
    legend: {
      data: props.legend,
      y: "bottom",
      textStyle: {
        fontSize: 12,
      },
    },
    title: {
      text: props.title,
      textStyle: {
        fontSize: 12,
      },
    },
    tooltip: {
      show: true,
      trigger: "axis",
      formatter: function (params: any) {
        const unit = ref<string>();
        let tooltipText = timestampToTime(params[0].name);
        params.forEach(function (v: any) {
          if (props.yUnit === "Byte/s" || props.yUnit === "bit/s") {
            unit.value = changeByte(v.value);
          } else {
            unit.value = v.value + props.yUnit;
          }
          tooltipText += "<br/>";
          tooltipText += v.marker + " " + v.seriesName + " " + unit.value;
        });
        return tooltipText;
      },
      axisPointer: {
        lineStyle: {
          type: "solid",
        },
      },
      textStyle: {
        fontSize: 12,
      },
    },
    series: currentSeriesData,
  });
};

const yUnitConversion = (val: any) => {
  if (props.yUnit === "Byte/s" || props.yUnit === "bit/s") {
    return changeByte(val);
  } else {
    return val + " " + props.yUnit;
  }
};

/**
 * 时间戳转时间格式
 * @param timestamp
 */
const timestampToTime = (timestamp: any) => {
  const date = new Date(timestamp * 1);
  const Y = date.getFullYear() + "-";
  const M =
    (date.getMonth() + 1 < 10
      ? "0" + (date.getMonth() + 1)
      : date.getMonth() + 1) + "-";
  const D = (date.getDate() < 10 ? "0" + date.getDate() : date.getDate()) + " ";
  const h =
    (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":";
  const m =
    (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes()) +
    ":";
  const s =
    date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
  return Y + M + D + h + m + s;
};

const changeByte = (byte: number) => {
  let size: string;
  if (byte < 1024) {
    size = `${byte.toFixed(2)}B`;
  } else if (byte < 1024 * 1024) {
    size = `${(byte / 1024).toFixed(2)}KB`;
  } else if (byte < 1024 * 1024 * 1024) {
    size = `${(byte / (1024 * 1024)).toFixed(2)}MB`;
  } else {
    size = `${(byte / (1024 * 1024 * 1024)).toFixed(2)}GB`;
  }
  // 转成字符串
  const sizeStr = `${size}`;
  // 获取小数点处的索引
  const index = sizeStr.indexOf(".");
  // 获取小数点后两位的值
  const dou = sizeStr.substr(index + 1, 2);
  if (dou == "00") {
    // 后两位是否为00，如果是则删除00
    return sizeStr.substring(0, index) + sizeStr.substr(index + 3, 2);
  }
  return size;
};

//监听data变化
watch(
  () => props.xData,
  () => {
    setEchartsData(undefined);
  }
);
const currentDevice = ref("");
const changeDeviceList = () => {
  if (currentDevice.value != "") {
    //props.xData = props.deviceData[currentDevice.value][0].timestamps;
    //props.series[0].data = props.deviceData[currentDevice.value][0].values;
    setEchartsData(props.deviceData[currentDevice.value][0].values);
  }
};
const isDiskUsed = () => {
  if (currentDevice.value === "") {
    currentDevice.value = props.currentDevice;
    changeDeviceList();
  }
  return props.metricName === "DISK_USED_UTILIZATION";
};
const getLabel = (item: string) => {
  if (item === "ALL") {
    return item;
  }
  return item.substring(1, item.length - 1);
};
</script>
<template>
  <div style="position: relative">
    <div
      style="position: absolute; right: 20px; top: 10px; z-index: 10"
      :style="{ top: selectTop }"
    >
      <el-select
        v-model="currentDevice"
        @change="changeDeviceList()"
        class="m-2"
        v-if="isDiskUsed()"
      >
        <el-option
          v-for="item in props.deviceList"
          :key="item"
          :label="getLabel(item)"
          :value="item"
        />
      </el-select>
    </div>
    <div
      :id="`echarts-${uuid}`"
      style="width: 99%; height: 15rem; padding-top: 10px"
    ></div>
  </div>
</template>
