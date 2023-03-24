<template>
  <el-card shadow="never" class="info-card">
    <el-row :gutter="10">
      <el-col :span="24">
        <div class="title">基础资源使用率</div>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="8">
        <div class="echarts">
          <div class="echarts-content">
            <div
              ref="cpuLiquidEcharts"
              v-resize="resize"
              style="height: 125px; width: 100%"
            ></div>
            <div class="echarts-footer">CPU使用率</div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="echarts">
          <div class="echarts-content">
            <div
              ref="memoryLiquidEcharts"
              v-resize="resize"
              style="height: 125px; width: 100%"
            ></div>
            <div class="echarts-footer">内存使用率</div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="echarts">
          <div class="echarts-content">
            <div
              ref="datastoreLiquidEcharts"
              style="height: 125px; width: 100%"
            ></div>
            <div class="echarts-footer">存储使用率</div>
          </div>
        </div>
      </el-col>
    </el-row>
  </el-card>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import * as echarts from "echarts";
import "echarts-liquidfill";
import type { ECharts } from "echarts";
const cpuLiquidEcharts = ref<HTMLElement>();
const memoryLiquidEcharts = ref<HTMLElement>();
const datastoreLiquidEcharts = ref<HTMLElement>();
let eCpuChartsRef: ECharts | undefined = undefined;
let eMemoryChartsRef: ECharts | undefined = undefined;
let eDatastoreChartsRef: ECharts | undefined = undefined;
const resize = () => {
  eCpuChartsRef?.resize();
  eMemoryChartsRef?.resize();
  eDatastoreChartsRef?.resize();
};
const initLiquidEchartByType = (v: number, e: HTMLElement) => {
  const myChart = echarts.init(e!);
  const value = v / 100;
  // 把配置和数据放这里
  myChart.setOption({
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
  });
  return myChart;
};
const initLiquidEchart = () => {
  eCpuChartsRef = initLiquidEchartByType(50, cpuLiquidEcharts.value!);
  eMemoryChartsRef = initLiquidEchartByType(80, memoryLiquidEcharts.value!);
  eDatastoreChartsRef = initLiquidEchartByType(
    90,
    datastoreLiquidEcharts.value!
  );
};
onMounted(() => {
  initLiquidEchart();
});
</script>
<style scoped lang="scss">
.info-card {
  height: 249px;
  min-width: 400px;
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
  font-family: "PingFang SC", serif;
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 12px;
  text-align: center;
  color: #1f2329;
}
</style>
