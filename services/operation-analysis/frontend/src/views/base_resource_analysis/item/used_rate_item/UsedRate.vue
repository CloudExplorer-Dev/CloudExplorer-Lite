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
  unit: string;
}>();
const liquidEcharts = ref<HTMLElement>();
let eChartsRef: ECharts | undefined = undefined;
const resize = () => {
  eChartsRef?.resize();
};
const option = computed<ECBasicOption>(() => {
  let value = 0;
  const name = props.typeText + "使用率";
  let total = 0;
  let used = 0;
  let free = 0;
  if (props.apiData) {
    const obj = props.apiData;
    if (obj[props.type]) {
      value = obj[props.type]?.usedRate / 100;
      total = obj[props.type]?.total;
      used = obj[props.type]?.used;
      free = total - used;
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
    tooltip: {
      trigger: "item", // 触发类型, 数据项图形触发，主要在散点图，饼图等无类目轴的图表中使用。
      //无视容器，超出显示
      appendToBody: true,
      textStyle: {
        //color: '#fff' // 文字颜色
      },
      extraCssText: "width:169px;",
      formatter: function (value: any) {
        let tooltip = `<div><p style="font-weight:bold;margin:0 8px 5px;">${name}</p></div>`;
        tooltip += `<div>
          <div style="margin: 0 8px;">
            <span style="display:inline-block;border-radius: 10px;margin-right:5px;width:10px;height:10px;background-color: rgb(223,224,227)"></span>
            <span>总量</span>
            <span style="float:right;color:#000000;">${
              total + " " + props.unit
            }</span>
          </div>
        </div>`;
        tooltip += `<p style="padding: 0 8px 0 0;left: 79.46%;right: 14.2%;top: 23.37%;border: 1px dashed #EFF0F1;"></p>`;
        tooltip += `<div style="padding: 0 0 8px 0;">
          <div style="margin: 0 8px;">
            <span style="display:inline-block;border-radius: 10px;margin-right:5px;width:10px;height:10px;background-color: rgb(52,199,36)"></span>
            <span>未使用</span>
            <span style="float:right;color:#000000;">${
              free + " " + props.unit
            }</span>
          </div>
        </div>`;
        tooltip += `<div>
          <div style="margin: 0 8px;">
            <span style="display:inline-block;border-radius: 10px;margin-right:5px;width:10px;height:10px;background-color: rgb(223,224,227)"></span>
            <span>已使用</span>
            <span style="float:right;color:#000000;">${
              used + " " + props.unit
            }</span>
          </div>
        </div>`;
        return tooltip;
      },
    },
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
                  "if(value<0.6){'rgba(170, 201, 255, 1)'}else if(value<0.8){'rgba(254, 210, 126, 1)'}else{'rgba(254, 178, 170, 1)'}"
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
                  "if(value<0.6){'rgba(97, 145, 254, 1)'}else if(value<0.8){'rgba(254, 174, 75, 1)'}else{'rgba(248, 120, 114, 1)'}"
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
              "if(value<0.6){'rgba(191, 209, 254, 1)'}else if(value<0.8){'rgba(252, 225, 196, 1)'}else{'rgba(250, 207, 208, 1)'}"
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
.card3 {
  left: 79.46%;
  right: 14.2%;
  top: 23.37%;
  border: 1px dashed #eff0f1;
}
</style>
