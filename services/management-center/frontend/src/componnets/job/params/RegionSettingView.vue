<template>
  <layout-container :border="border">
    <template #header
      ><h4>
        {{ t("cloud_account.sync.range", "同步范围") }}
      </h4></template
    >
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
          >全选</el-checkbox
        >
        <el-checkbox-group @change="change" v-model="checkedRegionIds">
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
  </layout-container>
</template>
<script setup lang="ts">
import type { JobDetails, Region } from "@/api/cloud_account/type";
import { computed, ref, watch, onMounted } from "vue";
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
  /**
   * 是有有boder
   */
  border: boolean;
}>();

/**
 * 选中的区域id
 */
const checkedRegionIds = ref<Array<string>>(
  props.jobDetails[0].params["REGIONS"]
    ? (props.jobDetails[0].params["REGIONS"].map(
        (r: Region) => r.regionId
      ) as Array<string>)
    : []
);

/**
 * 全选
 */
const checkAll = ref<boolean>(
  new Set(props.regions.map((r) => r.regionId)).size ===
    checkedRegionIds.value.length
);

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
/**
 * 改变后的值
 * @param selectRegion 改变后的值
 */
const change = (selectRegion: Array<string>) => {
  if (checkedRegionIds.value) {
    checkedRegionIds.value = selectRegion;
  }
  checkAll.value =
    new Set(props.regions.map((r) => r.regionId)).size === selectRegion.length;
};
/**
 * 监听数据,并且修改里面的区域值
 */
watch(checkedRegionIds, () => {
  props.jobDetails.forEach((job) => {
    job.params["REGIONS"] = props.regions.filter((r) =>
      checkedRegionIds.value.includes(r.regionId)
    );
  });
});

watch(
  () => props.jobDetails,
  () => {
    if (props.jobDetails.length > 0) {
      checkedRegionIds.value = props.jobDetails[0].params["REGIONS"].map(
        (r: Region) => r.regionId
      ) as Array<string>;
    }
  },
  {
    immediate: true,
  }
);
</script>
<style lang="scss"></style>
