<template>
  <div
    style="
      background-color: var(--ce-main-content-bg-color, #fff);
      border-top-left-radius: var(--ce-main-content-border-radius);
      border-top-right-radius: var(--ce-main-content-border-radius);
    "
  >
    <BillSettingView
      :jobDetails="jobDetails"
      :cloudAccount="cloudAccount"
      :readOnly="false"
      ref="billSetting"
    />
    <JobSetting ref="settingRef" :jobDetails="jobDetails" :readOnly="false" />
  </div>
</template>
<script setup lang="ts">
import type { CloudAccount, JobDetails } from "@/api/cloud_account/type";
import BillSettingView from "@/componnets/job/params/BillSettingView.vue";
import JobSetting from "@/componnets/job/job_setting/index.vue";
import { ref } from "vue";

const props = defineProps<{
  /**
   *定时任务信息
   */
  jobDetails: Array<JobDetails>;
  /**
   * 云账号id
   */
  cloudAccount?: CloudAccount;
}>();

const settingRef = ref<InstanceType<typeof JobSetting>>();
const billSetting = ref<InstanceType<typeof BillSettingView>>();

const validate = () => {
  return Promise.all([
    billSetting.value?.validate(),
    settingRef.value?.validate(),
  ]);
};
defineExpose({ validate });
</script>
<style lang="scss"></style>
