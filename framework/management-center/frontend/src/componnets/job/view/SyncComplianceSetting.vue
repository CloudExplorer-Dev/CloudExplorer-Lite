<template>
  <JobSetting
    ref="settingRef"
    :jobDetails="jobDetails"
    jobTypeDescription="扫描"
    :readOnly="false"
  />
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
