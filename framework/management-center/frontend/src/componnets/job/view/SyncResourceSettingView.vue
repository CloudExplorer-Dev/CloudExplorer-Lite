<template>
  <el-descriptions direction="vertical" :column="3">
    <el-descriptions-item :span="3" width="33.33%">
      <template #label>
        <DetailFormLabel label="同步范围" />
      </template>
      <el-space wrap>
        <div class="region-label" v-for="(r, i) in checkedRegions" :key="i">
          {{ r }}
        </div>
      </el-space>
    </el-descriptions-item>
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
import type {
  CloudAccount,
  Region,
  JobDetails,
} from "@/api/cloud_account/type";
import DetailFormValue from "@/componnets/DetailFormValue.vue";
import DetailFormLabel from "@/componnets/DetailFormLabel.vue";
import CronView from "./CronView.vue";
import IntervalView from "./IntervalView.vue";
import { computed } from "vue";
import _ from "lodash";

const props = defineProps<{
  jobDetails: Array<JobDetails>;
  cloudAccount: CloudAccount;
  regions: Array<Region>;
}>();

const checkedRegionIds = computed(() => {
  return props.jobDetails[0].params["REGIONS"]
    ? (props.jobDetails[0].params["REGIONS"].map(
        (r: Region) => r.regionId
      ) as Array<string>)
    : [];
});

const checkedRegions = computed(() => {
  return _.map(
    _.filter(props.regions, (r) =>
      _.includes(checkedRegionIds.value, r.regionId)
    ),
    (r) => r.name
  );
});
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
