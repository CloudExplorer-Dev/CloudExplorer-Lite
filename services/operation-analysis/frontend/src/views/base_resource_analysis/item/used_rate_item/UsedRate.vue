<template>
  <div class="echarts">
    <div class="echarts-content">
      <div
        ref="liquidEcharts"
        v-resize="resize"
        style="height: 125px; width: 100%"
      ></div>
      <div class="echarts-footer">{{ props.typeText }}使用率</div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref, watch } from "vue";
import * as echarts from "echarts";
import "echarts-liquidfill";
import type { ECharts } from "echarts";
import type { ECBasicOption } from "echarts/types/src/util/types";
const props = defineProps<{
  type: string;
  typeText: string;
  apiData: any;
  loading: boolean;
}>();
const liquidEcharts = ref<HTMLElement>();
let eChartsRef: ECharts | undefined = undefined;
const resize = () => {
  eChartsRef?.resize();
};
const option = computed<ECBasicOption>(() => {
  let value = 0;
  if (props.apiData) {
    const obj = props.apiData;
    if(obj[props.type]){
      value = obj[props.type]?.usedRate / 100;
    }
  } else {
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
  return {
    series: [
      {
        type: "liquidFill",
        radius: "98%",
        waveAnimation: true,
        data: [
          {
            value: value,
            direction: "left",
            itemStyle: {
              normal: {
                //这里就是根据不同的值显示不同球体的颜色
                color: eval(
                  "if(value<0.8){'rgba(170, 201, 255, 1)'}else if(value<0.9){'rgba(254, 210, 126, 1)'}else{'rgba(254, 178, 170, 1)'}"
                ),
              },
            },
          },
          {
            value: value,
            direction: "left",
            itemStyle: {
              normal: {
                //这里就是根据不同的值显示不同球体的颜色
                color: eval(
                  "if(value<0.8){'rgba(97, 145, 254, 1)'}else if(value<0.9){'rgba(254, 174, 75, 1)'}else{'rgba(248, 120, 114, 1)'}"
                ),
              },
            },
          },
        ],
        outline: {
          // show: true, //是否显示轮廓 布尔值
          borderDistance: 3, // 外部轮廓与图表的距离 数字
          itemStyle: {
            //这里就是根据不同的值显示不同边框的颜色
            borderColor: eval(
              "if(value<0.8){'rgba(191, 209, 254, 1)'}else if(value<0.9){'rgba(252, 225, 196, 1)'}else{'rgba(250, 207, 208, 1)'}"
            ), // 边框的颜色
            borderWidth: 3, // 边框的宽度
            // shadowBlur: 5 , //外部轮廓的阴影范围 一旦设置了内外都有阴影
            // shadowColor: '#000' //外部轮廓的阴影颜色
          },
        },
        itemStyle: {
          opacity: 1, // 波浪的透明度
          shadowBlur: 0, // 波浪的阴影范围
        },
        backgroundStyle: {
          color: "#fff", // 图表的背景颜色
        },
        label: {
          // 数据展示样式
          show: true,
          color: "#000",
          insideColor: "#fff",
          fontSize: 20,
          fontWeight: 400,
          position: ["50%", "80%"],
        },
      },
    ],
    tooltip: {
      show: false, // 鼠标放上显示数据
    },
  };
});
const echartsLoading = () => {
  eChartsRef?.showLoading({
    text: "",
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
});
const setOption = () => {
  eChartsRef?.setOption(option.value);
};
const initLiquidEchartByType = (e: HTMLElement) => {
  const myChart = echarts.init(e!);
  return myChart;
};
const initLiquidEchart = () => {
  eChartsRef = initLiquidEchartByType(liquidEcharts.value!);
};
watch(
  props,
  () => {
    if (props.loading) {
      echartsLoading();
    } else {
      hideEchartsLoading();
    }
    setOption();
  },
  { immediate: true }
);
onMounted(() => {
  initLiquidEchart();
});
</script>
<style scoped lang="scss">
.chart {
  min-height: 120px;
  width: 100%;
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
