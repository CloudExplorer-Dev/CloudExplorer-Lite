<template>
  <el-scrollbar>
    <div
      style="
        background-color: var(--ce-main-content-bg-color, #fff);
        border-radius: var(--ce-main-content-border-radius);
      "
    >
      <div style="width: calc(100% - 48px); padding: 24px 24px 0">
        <span class="sync-title">数据同步设置</span>
        <div
          style="
            width: 100%;
            height: 1px;
            background-color: rgba(31, 35, 41, 0.15);
            margin-top: 20px;
          "
        ></div>
      </div>
      <RegionSettingView
        :jobDetails="jobDetails"
        :regions="regions"
        :readOnly="false"
      />
      <JobSetting ref="settingRef" :jobDetails="jobDetails" :readOnly="false" />
    </div>

    <div
      style="
        background-color: var(--ce-main-content-bg-color, #fff);
        border-top-right-radius: var(--ce-main-content-border-radius);
        border-top-left-radius: var(--ce-main-content-border-radius);
        margin-top: 16px;
      "
    >
      <div style="width: calc(100% - 48px); padding: 24px 24px 0">
        <span class="sync-title">监控数据设置</span>
        <div
          style="
            width: 100%;
            height: 1px;
            background-color: rgba(31, 35, 41, 0.15);
            margin-top: 20px;
          "
        ></div>
      </div>
      <JobSetting
        ref="metricSettingRef"
        :jobDetails="metricJobDetails"
        :readOnly="false"
      />
    </div>
  </el-scrollbar>
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

  metricJobDetails: Array<JobDetails>;
  /**
   *云账号区域信息
   */
  regions: Array<Region>;
}>();

const settingRef = ref<InstanceType<typeof JobSetting>>();
const metricSettingRef = ref<InstanceType<typeof JobSetting>>();

const validate = () => {
  return Promise.all([
    settingRef.value?.validate(),
    metricSettingRef.value?.validate(),
  ]);
};
defineExpose({ validate });
</script>
<style lang="scss" scoped>
.sync-title {
  font-style: normal;
  font-weight: 500;
  font-size: 16px;
  line-height: 24px;

  color: #1f2329;
}
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
