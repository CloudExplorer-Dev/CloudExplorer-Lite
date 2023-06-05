<template>
  <div>
    <RegionSettingView
      :jobDetails="jobDetails"
      :regions="regions"
      :readOnly="false"
    />
    <JobSetting ref="settingRef" :jobDetails="jobDetails" :readOnly="false" />
  </div>
</template>
<script setup lang="ts">
import RegionSettingView from "@/componnets/job/params/RegionSettingView.vue";
import type { JobDetails, Region } from "@/api/cloud_account/type";
import JobSetting from "@/componnets/job/job_setting/index.vue";
import { ref } from "vue";

const props = defineProps<{
  /**
   *定时任务信息
   */
  jobDetails: Array<JobDetails>;
  /**
   *云账号区域信息
   */
  regions: Array<Region>;
}>();

const settingRef = ref<InstanceType<typeof JobSetting>>();

const validate = () => {
  return Promise.all([settingRef.value?.validate()]);
};
defineExpose({ validate });
</script>
<style lang="scss" scoped>
.region-label {
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 1px 6px;
  background: rgba(31, 35, 41, 0.1);
  border-radius: 2px;

  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 22px;
  color: #646a73;
}
</style>
