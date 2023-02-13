<template>
  <el-card
    shadow="always"
    v-loading="loading"
    :body-style="{ padding: '0px', height: '400px' }"
  >
    <div
      v-resize="reSize"
      id="chart"
      ref="chart"
      style="width: 100%; overflow: hidden; height: 100%"
    ></div>
  </el-card>
</template>
<script setup lang="ts">
import { ref, onMounted, inject, markRaw, watch } from "vue";
import viewApi from "@/api/view/index";
import type { ComplianceViewGroupResponse } from "@/api/view/type";
import type { ECharts, ECElementEvent } from "echarts";

const props = defineProps<{
  groupType: "CLOUD_ACCOUNT" | "RESOURCE_TYPE" | "RULE_GROUP" | "RULE";
  cloudAccountId?: string;
  clickSeries?: (event: ECElementEvent) => void;
  getOptions: (req: Array<ComplianceViewGroupResponse>) => any;
}>();
// 分组数据
const groupData = ref<Array<ComplianceViewGroupResponse>>();
// 加载器
const loading = ref<boolean>(false);
// echarts 工具对象
const echarts: any = inject("echarts");
/**
 * echarts 实例
 */
const viewCahrts = ref<ECharts>();

/**
 * echarts 容器
 */
const chart = ref<InstanceType<typeof HTMLElement>>();

const reSize = () => {
  viewCahrts.value?.resize();
};

onMounted(() => {
  refresh();
});

const refresh = () => {
  viewApi
    .group(
      {
        groupType: props.groupType,
        cloudAccountId:
          props.cloudAccountId === "all" ? undefined : props.cloudAccountId,
      },
      loading
    )
    .then((ok) => {
      groupData.value = ok.data;
      const options = props.getOptions(groupData.value);
      viewCahrts.value = markRaw(echarts.init(chart.value));
      viewCahrts.value?.setOption(options);
      viewCahrts.value?.on("click", "series", (event) => {
        if (props.clickSeries) {
          props.clickSeries(event);
        }
      });
    });
};

watch(
  () => props.cloudAccountId,
  () => {
    refresh();
  }
);
</script>
<style lang="scss"></style>
