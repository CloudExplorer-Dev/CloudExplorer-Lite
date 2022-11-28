<template>
  <layout-container :border="border">
    <template #header
      ><h4>
        {{ t("cloud_account.sync.timing", "同步频率设置") }}
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
            【
            <div style="width: 100px">
              <el-input
                style="width: 100px"
                type="number"
                @click.stop.prevent
                size="small"
                :min="0"
                :max="23"
                v-model="details.hoursOfDay[0]"
                controls-position="right"
                :disabled="!details.active || (details.active && readOnly)"
                onkeyup="value =
    Math.floor(value) > 23 ? 23 : Math.floor(value) < 0 ? 0 : Math.floor(value)"
              >
                <template #append> <span>:00</span></template>
              </el-input>
            </div>
            】
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
