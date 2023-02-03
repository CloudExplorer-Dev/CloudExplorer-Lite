<template>
  <layout-container :border="border">
    <template #header
      ><h4>
        {{ t("cloud_account.sync.timing", "同步频率设置") }}
      </h4></template
    >
    <template #content>
      <div
        class="corn_item_wapper"
        v-for="details in jobDetails"
        :key="details.jobName"
      >
        <el-checkbox
          :checked="details.active"
          :label="details.active"
          v-model="details.active"
          :disabled="readOnly"
        >
          <div style="display: flex; align-items: center">
            <span style="width: 120px; white-space: NORMAL"
              >{{ details.description.replace("同步", "") }}:</span
            >
            <div style="width: 550px">
              <cron-in-view
                ref="cronInViewRef"
                v-model="details.cronExpression"
              ></cron-in-view>
            </div>

            <div
              @click.stop.prevent
              style="
                height: 42px;
                text-align: center;
                line-height: 42px;
                margin-left: 20px;
                cursor: default;
              "
            >
              <span>{{ t("cloud_account.sync.once", "同步一次") }}</span>
            </div>
          </div>
        </el-checkbox>
      </div></template
    >
  </layout-container>
</template>
<script setup lang="ts">
import { ref } from "vue";
import { useI18n } from "vue-i18n";
import type { JobDetails } from "@/api/cloud_account/type";
import CronInView from "@/componnets/job/job_setting/CronInView.vue";
const { t } = useI18n();
// 校验实例对象
const cronInViewRef = ref<
  Array<InstanceType<typeof CronInView>> | InstanceType<typeof CronInView>
>();
withDefaults(
  defineProps<{
    /**
     * 是否有边框
     */
    border: boolean;
    /**
     * 是否可读
     */
    readOnly: boolean;
    /**
     *定时任务信息
     */
    jobDetails: Array<JobDetails>;
  }>(),
  { readOnly: false, border: false }
);
// 校验
const validate = () => {
  if (cronInViewRef.value) {
    if (Array.isArray(cronInViewRef.value)) {
      return Promise.all(
        (cronInViewRef.value as Array<InstanceType<typeof CronInView>>).map(
          (c) => c.validate()
        )
      );
    } else {
      return (
        cronInViewRef.value as InstanceType<typeof CronInView>
      ).validate();
    }
  }
  return true;
};
defineExpose({ validate });
</script>
<style lang="scss" scoped>
.corn_item_wapper {
  height: 50px;
  margin: 3px 0;
  border-radius: 3px;
}
</style>
