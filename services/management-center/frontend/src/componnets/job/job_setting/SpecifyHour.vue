<template>
  <layout-container :border="border">
    <template #header
      ><h4>
        {{ t("cloud_account.sync.timing", "资源同步频率") }}
      </h4></template
    >
    <template #content>
      <div
        style="width: 100%"
        v-for="details in jobDetails"
        :key="details.jobName"
      >
        <el-checkbox
          style="width: 30%"
          :checked="details.active"
          :label="details.active"
          v-model="details.active"
          :disabled="readOnly"
        >
          <div style="display: flex; align-items: center">
            <span style="width: 100px">{{ details.description }}:</span>
            <span style="width: 40px">每天</span>
            <div style="width: 100px">
              <el-input-number
                style="width: 80px"
                @click.stop.prevent
                size="small"
                :min="0"
                :max="23"
                v-model="details.hoursOfDay[0]"
                controls-position="right"
                :disabled="!details.active || (details.active && readOnly)"
              >
              </el-input-number>
            </div>
            <span style="width: 20px">:00</span>
            <span style="width: 80px">同步</span>
          </div>
        </el-checkbox>
      </div>
    </template>
  </layout-container>
</template>
<script setup lang="ts">
import { useI18n } from "vue-i18n";
import type { JobDetails } from "@/api/cloud_account/type";
const { t } = useI18n();
const props = defineProps<{
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
}>();
</script>
<style lang="scss"></style>
