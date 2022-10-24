<template>
  <DailyTimeInterval
    :job-details="interval"
    :read-only="readOnly"
    :border="border"
    v-if="interval.length > 0"
  ></DailyTimeInterval>
  <SpecifyHour
    :job-details="specifyHour"
    :read-only="readOnly"
    :border="border"
    v-if="specifyHour.length > 0"
  ></SpecifyHour>
</template>
<script setup lang="ts">
import { computed } from "vue";
import type { JobDetails } from "@/api/cloud_account/type";
import DailyTimeInterval from "@/componnets/job/job_setting/DailyTimeInterval.vue";

import SpecifyHour from "@/componnets/job/job_setting/SpecifyHour.vue";

const props = defineProps<{
  jobDetails: Array<JobDetails>;
  /**
   * 是否可读
   */
  readOnly: boolean;
  /**
   * 是有有boder
   */
  border: boolean;
}>();

/**
 *指定时间同步
 */
const specifyHour = computed(() =>
  props.jobDetails.filter((j) => j.jobType === "SpecifyHour")
);

/**
 * 间隔同步
 */
const interval = computed(() =>
  props.jobDetails.filter((j) => j.jobType === "Interval")
);
</script>
<style lang="scss"></style>
