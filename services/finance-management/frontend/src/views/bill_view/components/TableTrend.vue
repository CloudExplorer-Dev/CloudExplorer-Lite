<template>
  <div ref="echars" style="width: 200px; height: 50px"></div>
</template>
<script setup lang="ts">
import type { TrendData } from "@/echarts/bill_view/type";
import { onMounted, ref, inject, watch } from "vue";
import { getTrendViewOption } from "@/echarts/bill_view/index";
import { cloneDeep } from "lodash";
const echarts: any = inject("echarts");
const props = defineProps<{
  trend: Array<TrendData>;
}>();
const echars = ref<any>(null);
const myChart = ref<any>();
onMounted(() => {
  init();
});
watch(
  () => props.trend,
  () => {
    init();
  }
);

const init = () => {
  const trend = cloneDeep(props.trend);
  trend.sort((pre, next) => pre.label.localeCompare(next.label));
  const option: any = getTrendViewOption(trend, "line", false, false, false);

  option["grid"] = { left: "-10%", top: 10, bottom: 10 };
  if (echars.value) {
    if (!myChart.value) {
      myChart.value = echarts.init(echars.value);
    }
    myChart.value.setOption(option);
    myChart.value.getZr().on("mousemove", () => {
      myChart.value.getZr().setCursorStyle("pointer");
    });
  }
};
</script>
<style lang="scss" scoped></style>
