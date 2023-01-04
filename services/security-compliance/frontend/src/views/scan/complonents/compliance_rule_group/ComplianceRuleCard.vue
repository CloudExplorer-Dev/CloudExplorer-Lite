<template>
  <div class="card_wapper" v-loading="loading">
    <div class="title">{{ scanComplianceRuleGroup?.ruleGroupName }}</div>
    <div class="complian_group_wapper">
      <div class="left">
        <div class="flex" style="height: 25px">
          <span class="not_ok">不合规</span>/
          <span class="ok">合规</span>
        </div>
        <div class="flex">
          <span class="not_ok">{{
            scanComplianceRuleGroup?.notComplianceRuleCount
          }}</span
          >/
          <span class="ok">{{
            scanComplianceRuleGroup?.complianceRuleCount
          }}</span>
        </div>
      </div>
      <div class="right">
        <div class="text">
          <div>高:{{ scanComplianceRuleGroup?.high }}</div>
          <div>中：{{ scanComplianceRuleGroup?.middle }}</div>
          <div>低:{{ scanComplianceRuleGroup?.low }}</div>
        </div>
        <div class="colors">
          <div
            id="high"
            :style="{
              width:
                sum === 0
                  ? '33%'
                  : scanComplianceRuleGroup
                  ? (scanComplianceRuleGroup?.high / sum) * 100 + '%'
                  : '33%',
            }"
          ></div>
          <div
            id="middle"
            :style="{
              width:
                sum === 0
                  ? '33%'
                  : scanComplianceRuleGroup
                  ? (scanComplianceRuleGroup?.middle / sum) * 100 + '%'
                  : '33%',
            }"
          ></div>
          <div
            id="low"
            :style="{
              width:
                sum === 0
                  ? '33%'
                  : scanComplianceRuleGroup
                  ? (scanComplianceRuleGroup?.low / sum) * 100 + '%'
                  : '33%',
            }"
          ></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import type { ScanComplianceRuleGroupResponse } from "@/api/compliance_scan/type";
import { computed, ref, watch } from "vue";
import complianceScanApi from "@/api/compliance_scan";

/**
 * 中高低风险总和
 */
const sum = computed(() => {
  if (scanComplianceRuleGroup.value) {
    return (
      scanComplianceRuleGroup.value.high +
      scanComplianceRuleGroup.value.middle +
      scanComplianceRuleGroup.value.low
    );
  }
  return 0;
});

const props = defineProps<{
  // 合规规则id
  complianceRuleId: string;
}>();
/**
 * 规则组扫描结果
 */
const scanComplianceRuleGroup = ref<ScanComplianceRuleGroupResponse>();
/**
 * 加载器
 */
const loading = ref<boolean>(false);

watch(
  () => props.complianceRuleId,
  () => {
    if (props.complianceRuleId) {
      complianceScanApi
        .getScanComplianceRuleGroup(props.complianceRuleId, loading)
        .then((ok) => {
          scanComplianceRuleGroup.value = ok.data;
        });
    }
  },

  {
    immediate: true,
  }
);
</script>
<style lang="scss" scoped>
.flex {
  display: flex;
  justify-content: center;
}
.card_wapper {
  padding: 8px;
  box-sizing: border-box;
  height: 100px;
  width: 240px;
  border: 1px solid var(--el-menu-border-color);
  .title {
    height: 35px;
    font-size: 14px;
  }
  .complian_group_wapper {
    display: flex;
    font-size: 12px;
    .left {
      width: 80px;
      height: 70px;
      .ok {
        color: #6aaa03;
      }
      .not_ok {
        color: #d9001b;
      }
    }
    .right {
      width: 160px;
      .text {
        height: 30px;
        display: flex;
        justify-content: space-around;
      }
      .colors {
        padding: 0 5px;
        display: flex;
        height: 5px;
        #middle {
          background-color: rgb(250, 200, 0);
          margin-left: 2px;
          margin-right: 2px;
        }
        #low {
          background-color: rgb(100, 125, 150);
        }
        #high {
          background-color: rgb(200, 1, 0);
        }
      }
    }
  }
}
</style>
