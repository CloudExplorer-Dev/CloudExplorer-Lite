<template>
  <base-container>
    <template #header>
      <span>
        {{ t("cloud_account.sync.range", "同步范围") }}
      </span>
    </template>
    <template #content>
      <el-form
        v-if="jobDetails"
        :inline="true"
        status-icon
        label-width="120px"
        label-suffix=":"
        label-position="left"
      >
        <el-checkbox
          style="margin-bottom: 10px"
          v-model="checkAll"
          :disabled="readOnly"
          @change="handleCheckAllChange"
        >
          已选区域({{ checkedRegionIds.length }})
        </el-checkbox>
        <el-checkbox-group v-model="checkedRegionIds">
          <el-checkbox
            :title="region.name"
            v-for="region in regions"
            :key="region.regionId"
            :label="region.regionId"
            size="large"
            :disabled="readOnly"
            ><span
              style="
                display: inline-block;
                width: 120px;
                overflow: hidden;
                text-overflow: ellipsis;
              "
            >
              {{ region.name }}
            </span>
          </el-checkbox>
        </el-checkbox-group>
      </el-form>
    </template>
  </base-container>
</template>
<script setup lang="ts">
import type { JobDetails, Region } from "@/api/cloud_account/type";
import { computed } from "vue";
import { useI18n } from "vue-i18n";
const { t } = useI18n();
const props = defineProps<{
  /**
   *定时任务信息
   */
  jobDetails: Array<JobDetails>;
  /**
   *云账号区域信息
   */
  regions: Array<Region>;
  /**
   * 是否可读
   */
  readOnly: boolean;
}>();

/**
 * 选中的区域id
 */
const checkedRegionIds = computed({
  get: () => {
    return props.jobDetails[0].params["REGIONS"]
      ? (props.jobDetails[0].params["REGIONS"].map(
          (r: Region) => r.regionId
        ) as Array<string>)
      : [];
  },
  set: (selectRegion: Array<string>) => {
    props.jobDetails.forEach((job) => {
      job.params["REGIONS"] = props.regions.filter((r) =>
        selectRegion.includes(r.regionId)
      );
    });
  },
});

const checkAll = computed({
  get: () => {
    return (
      new Set(props.regions.map((r) => r.regionId)).size ===
      checkedRegionIds.value.length
    );
  },
  set: (selected: boolean) => {
    props.jobDetails.forEach((job) => {
      job.params["REGIONS"] = selected
        ? props.regions.filter((r) =>
            checkedRegionIds.value.includes(r.regionId)
          )
        : [];
    });
  },
});

/**
 *是否全选处理函数
 * @param selected 是否全选
 */
const handleCheckAllChange = (selected: boolean) => {
  if (selected) {
    checkedRegionIds.value = [...new Set(props.regions.map((r) => r.regionId))];
  } else {
    checkedRegionIds.value = [];
  }
};
</script>
<style lang="scss"></style>
