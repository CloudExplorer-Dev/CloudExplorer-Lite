<template>
  <base-container>
    <template #header>
      <span>
        {{ jobTypeDescription + "频率" }}
      </span>
    </template>
    <template #content>
      <div
        class="corn_item_wapper"
        v-for="details in jobDetails"
        :key="details.jobName"
      >
        <div>{{ details.description }}</div>

        <el-switch
          v-model="details.active"
          :disabled="readOnly || details.activeReadOnly"
        />

        <div style="display: flex; align-items: center" v-if="details.active">
          <div style="width: 550px">
            <cron-interval-view
              ref="cronIntervalViewRef"
              :readOnly="readOnly || details.cronReadOnly"
              v-if="details.jobType === 'INTERVAL'"
              v-model:unit="details.unit"
              v-model:job-type="details.jobType"
              v-model:interval="details.interval"
            />
            <cron-in-view
              :readOnly="readOnly || details.cronReadOnly"
              v-else
              v-model:job-type="details.jobType"
              ref="cronInViewRef"
              v-model="details.cronExpression"
            />
          </div>
        </div>
      </div>
    </template>
  </base-container>
</template>
<script setup lang="ts">
import { ref, watch } from "vue";
import { useI18n } from "vue-i18n";
import type { JobDetails } from "@/api/cloud_account/type";
import CronInView from "@/componnets/job/job_setting/CronInView.vue";
import CronIntervalView from "@/componnets/job/job_setting/CronIntervalView.vue";
import _ from "lodash";

const { t } = useI18n();
// 校验实例对象
const cronInViewRef = ref<
  Array<InstanceType<typeof CronInView>> | InstanceType<typeof CronInView>
>();
const cronIntervalViewRef = ref<
  | Array<InstanceType<typeof CronIntervalView>>
  | InstanceType<typeof CronIntervalView>
>();

const props = withDefaults(
  defineProps<{
    /**
     * 是否可读
     */
    readOnly: boolean;
    /**
     *定时任务信息
     */
    jobDetails: Array<JobDetails>;
    jobTypeDescription?: string;
  }>(),
  { readOnly: false, jobTypeDescription: "同步" }
);
watch(
  () => props.jobDetails,
  () => {
    console.log(props.jobDetails);
  },
  {
    deep: true,
  }
);
// 校验
const validate = () => {
  const array: Array<any> = [];
  if (cronInViewRef.value) {
    if (Array.isArray(cronInViewRef.value)) {
      _.forEach(
        cronInViewRef.value as Array<InstanceType<typeof CronInView>>,
        (c) => array.push(c.validate())
      );
    } else {
      array.push(
        (cronInViewRef.value as InstanceType<typeof CronInView>).validate()
      );
    }
  }
  if (cronIntervalViewRef.value) {
    if (Array.isArray(cronIntervalViewRef.value)) {
      _.forEach(
        cronIntervalViewRef.value as Array<
          InstanceType<typeof CronIntervalView>
        >,
        (c) => array.push(c.validate())
      );
    } else {
      array.push(
        (
          cronIntervalViewRef.value as InstanceType<typeof CronIntervalView>
        ).validate()
      );
    }
  }
  if (array.length > 0) {
    return Promise.all(array);
  }
  return true;
};
defineExpose({ validate });
</script>
<style lang="scss" scoped>
.corn_item_wapper {
  margin-bottom: 12px;
}
</style>
