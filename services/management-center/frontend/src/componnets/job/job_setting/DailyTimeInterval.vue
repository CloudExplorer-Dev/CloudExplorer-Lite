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
            <span style="width: 40px">{{
              t("cloud_account.sync.interval", "每隔")
            }}</span>
            <div style="width: 100px">
              <el-input-number
                style="width: 80px"
                @click.stop.prevent
                size="small"
                v-model="details.timeInterval"
                controls-position="right"
                :disabled="!details.active || (details.active && readOnly)"
              />
            </div>
            <div style="width: 80px">
              <el-select
                @click.stop.prevent
                style="width: 60px"
                v-model="details.unit"
                class="m-2"
                placeholder="Select"
                size="small"
                :disabled="!details.active || (details.active && readOnly)"
              >
                <el-option
                  v-for="item in [
                    {
                      value: 'MILLISECOND',
                      label: t(
                        'cloud_account.sync.interval_time_unit.millisecond',
                        '毫秒'
                      ),
                    },
                    {
                      value: 'SECOND',
                      label: t(
                        'cloud_account.sync.interval_time_unit.second',
                        '秒'
                      ),
                    },
                    {
                      value: 'MINUTE',
                      label: t(
                        'cloud_account.sync.interval_time_unit.minute',
                        '分钟'
                      ),
                    },
                    {
                      value: 'HOUR',
                      label: t(
                        'cloud_account.sync.interval_time_unit.hour',
                        '小时'
                      ),
                    },
                  ]"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </div>

            <span style="width: 80px">{{
              t("cloud_account.sync.once", "同步一次")
            }}</span>
          </div>
        </el-checkbox>
      </div></template
    >
  </layout-container>
</template>
<script setup lang="ts">
import { useI18n } from "vue-i18n";
import type { JobDetails } from "@/api/cloud_account/type";
const { t } = useI18n();

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
}>();
</script>
<style lang="scss"></style>
