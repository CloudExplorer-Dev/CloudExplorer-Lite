<template>
  <!-- 定时任务参数 -->
  <region-setting-view
    v-if="regionJob.length > 0"
    :jobDetails="regionJob"
    :regions="regions"
    :border="border"
    :readOnly="readOnly"
  ></region-setting-view>
  <bill-setting-view
    v-if="billJob.length > 0"
    :jobDetails="billJob"
    :cloudAccount="cloudAccount"
    :readOnly="readOnly"
    ref="billSetting"
  ></bill-setting-view>
  <!-- 定时任务设置 -->
  <!-- 定时任务设置 -->
  <job-setting
    v-if="billJob.length > 0"
    ref="jobCronSettingRef"
    :jobDetails="billJob"
    :readOnly="readOnly"
  ></job-setting>
  <job-setting
    v-else-if="complianceJob.length > 0"
    ref="jobCronSettingRef"
    :jobDetails="complianceJob"
    jobTypeDescription="扫描"
    :readOnly="readOnly"
  ></job-setting>
  <job-setting
    v-else
    ref="jobCronSettingRef"
    :jobDetails="regionJob"
    :readOnly="readOnly"
  ></job-setting>
</template>
<script setup lang="ts">
import { ref, computed } from "vue";
import type { Region, ModuleJob, CloudAccount } from "@/api/cloud_account/type";
import JobSetting from "@/componnets/job/job_setting/index.vue";
import RegionSettingView from "@/componnets/job/params/RegionSettingView.vue";

import BillSettingView from "@/componnets/job/params/BillSettingView.vue";
const billSetting = ref<InstanceType<typeof BillSettingView> | null>(null);

const jobCronSettingRef = ref<InstanceType<typeof JobSetting>>();
const props = withDefaults(
  defineProps<{
    /**
     * 模块
     */
    module: ModuleJob;
    /**
     * 区域
     */
    regions: Array<Region>;
    /**
     * 是否有border
     */
    border?: boolean;
    /**
     * 是否可读
     */
    readOnly: boolean;
    /**
     * 云账号
     */
    cloudAccount?: CloudAccount;
  }>(),
  { border: true }
);

/**
 * 区域定时任务
 */

const regionJob = computed(() =>
  props.module.jobDetailsList.filter(
    (j) => j.jobGroup === "CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP"
  )
);

/**
 * 账单设置定时任务
 */
const billJob = computed(() =>
  props.module.jobDetailsList.filter(
    (j) => j.jobGroup === "CLOUD_ACCOUNT_BILL_SYNC_GROUP"
  )
);

const complianceJob = computed(() =>
  props.module.jobDetailsList.filter(
    (j) => j.jobGroup === "CLOUD_COMPLIANCE_RESOURCE_SYNC_GROUP"
  )
);

const validate = () => {
  if (billJob.value.length > 0) {
    return Promise.all([
      billSetting.value?.validate(),
      jobCronSettingRef.value?.validate(),
    ]);
  }
  return Promise.all([jobCronSettingRef.value?.validate()]);
};
defineExpose({ validate });
</script>
<style lang="scss"></style>
