<template>
  <layout-container :border="border" v-if="regionJob.length > 0">
    <template #content>
      <!-- 定时任务参数 -->
      <RegionSettingView
        :jobDetails="regionJob"
        :regions="regions"
        :border="border"
        :readOnly="readOnly"
      ></RegionSettingView>
      <!-- 定时任务设置 -->
      <!-- 定时任务设置 -->
      <JobSetting
        :jobDetails="regionJob"
        :border="border"
        :readOnly="readOnly"
      ></JobSetting>
    </template>
  </layout-container>
  <layout-container :border="border" v-if="billJob.length > 0">
    <template #content>
      <!-- 定时任务参数 -->
      <BillSettingView
        :jobDetails="billJob"
        :cloudAccount="cloudAccount"
        :border="border"
        :readOnly="readOnly"
        ref="billSetting"
      ></BillSettingView>
      <!-- 定时任务设置 -->
      <JobSetting
        :jobDetails="billJob"
        :border="border"
        :readOnly="readOnly"
      ></JobSetting>
    </template>
  </layout-container>
</template>
<script setup lang="ts">
import { ref, computed } from "vue";
import type {
  Region,
  ModuleJob,
  JobDetails,
  CloudAccount,
} from "@/api/cloud_account/type";

import RegionSettingView from "@/componnets/job/params/RegionSettingView.vue";

import BillSettingView from "@/componnets/job/params/BillSettingView.vue";

import JobSetting from "@/componnets/job/JobSetting.vue";
const billSetting = ref<InstanceType<typeof BillSettingView> | null>(null);

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
    border: boolean;
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

const validate = () => {
  if (billJob.value.length > 0) {
    return billSetting.value?.validate();
  }
  return true;
};
defineExpose({ validate });
</script>
<style lang="scss"></style>
