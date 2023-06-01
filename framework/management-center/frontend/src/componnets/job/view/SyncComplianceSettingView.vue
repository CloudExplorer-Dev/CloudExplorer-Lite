<template>
  <el-descriptions direction="vertical" :column="3">
    <template v-for="(item, index) in jobDetails" :key="index">
      <el-descriptions-item width="33.33%">
        <template #label>
          <DetailFormLabel :label="item.description + '频率'" />
        </template>
        <DetailFormValue>
          <IntervalView
            v-if="item.jobType === 'INTERVAL'"
            :unit="item.unit"
            :interval="item.interval"
          />
          <CronView v-else :cron="item.cronExpression" />
        </DetailFormValue>
      </el-descriptions-item>
    </template>
  </el-descriptions>
</template>
<script setup lang="ts">
import type { JobDetails } from "@/api/cloud_account/type";
import type { CloudAccount } from "@/api/cloud_account/type";
import DetailFormValue from "@/componnets/DetailFormValue.vue";
import DetailFormLabel from "@/componnets/DetailFormLabel.vue";
import CronView from "./CronView.vue";
import IntervalView from "./IntervalView.vue";

const props = defineProps<{
  jobDetails: Array<JobDetails>;
  cloudAccount: CloudAccount;
}>();
</script>
<style lang="scss"></style>
