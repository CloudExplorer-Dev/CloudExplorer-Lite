<template>
  <el-popover
    @show="open(billSummary.treed, billSummary)"
    placement="top-start"
    :width="600"
    trigger="hover"
  >
    <template #default>
      <div class="content">
        <div class="title">
          {{ props.billSummary.group1
          }}<span class="child_title">的费用趋势</span>
        </div>
        <div ref="tree" class="tree"></div>
      </div>
    </template>
    <template #reference>
      <TableTrend :trend="props.billSummary.treed"></TableTrend
    ></template>
  </el-popover>
</template>
<script setup lang="ts">
import type { BillSummary, TrendData } from "@/echarts/bill_view/type";
import type { ECharts } from "echarts";
import { ref, inject, nextTick } from "vue";
import { getTrendViewOption } from "@/echarts/bill_view/index";
import _ from "lodash";
import TableTrend from "@/views/bill_view/components/TableTrend.vue";

const echarts: any = inject("echarts");
const title = ref<string>();

const treeChart = ref<ECharts>();

const tree = ref<InstanceType<typeof HTMLDivElement>>();

const props = defineProps<{
  billSummary: BillSummary;
}>();
/**
 * 打开趋势图
 * @param treed 打开查看趋势图
 */
const open = (treed: Array<TrendData>, row: BillSummary) => {
  title.value = Object.keys(row)
    .filter((g) => g.startsWith("group"))
    .map((k) => row[k])
    .join(",");
  treed.sort((pre, next) => pre.label.localeCompare(next.label));

  const option: any = getTrendViewOption(treed, "line", true, true, true);
  option["tooltip"] = {
    trigger: "item",
    formatter: (p: any) => {
      return `<div>月份:${p.name}</div><div>金额:${_.round(p.value, 2).toFixed(
        2
      )}</div>`;
    },
  };
  option["grid"] = { top: 20, bottom: 20 };
  if (treeChart.value) {
    treeChart.value.setOption(option);
  } else {
    nextTick(() => {
      treeChart.value = echarts.init(tree.value);
      treeChart.value?.setOption(option);
    });
  }
};
</script>
<style lang="scss" scoped>
.content {
  height: 300px;
  width: 600px;
  .tree {
    height: calc(100% - 20px);
  }
  .title {
    margin-left: 20px;
    height: 20px;
    font-size: 14px;
    font-weight: 500;
    .child_title {
      font-size: 12px;
      font-weight: 500;
      margin-left: 8px;
    }
  }
}
</style>
