<template>
  <div
    style="
      background-color: var(--ce-main-content-bg-color, #fff);
      border-top-left-radius: var(--ce-main-content-border-radius);
      border-top-right-radius: var(--ce-main-content-border-radius);
    "
  >
    <JobSetting
      ref="settingRef"
      :jobDetails="jobDetails"
      jobTypeDescription="扫描"
      :readOnly="false"
    />
  </div>
</template>
<script setup lang="ts">
import type { JobDetails } from "@/api/cloud_account/type";
import type { CloudAccount } from "@/api/cloud_account/type";

import JobSetting from "@/componnets/job/job_setting/index.vue";
import { ref } from "vue";

const props = defineProps<{
  jobDetails: Array<JobDetails>;
  cloudAccount: CloudAccount;
}>();

const settingRef = ref<InstanceType<typeof JobSetting>>();

const validate = () => {
  return Promise.all([settingRef.value?.validate()]);
};
defineExpose({ validate });
</script>
<style lang="scss"></style>
