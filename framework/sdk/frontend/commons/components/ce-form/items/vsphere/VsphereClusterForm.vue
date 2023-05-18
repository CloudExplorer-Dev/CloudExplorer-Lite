<template>
  <el-radio-group v-bind="$attrs">
    <el-radio-button
      v-for="(item, index) in optionList"
      :key="index"
      :label="item[formItem.valueField ? formItem.valueField : 'value']"
      style="
        --el-radio-button-checked-bg-color: #ffffff;
        --el-radio-button-checked-text-color: #1f2329;
      "
    >
      <div class="radio-container">
        <div class="main-text">
          <CeIcon
            code="icon_host_outlined"
            size="17px"
            color="#3370FF"
            style="margin-right: 9px"
          />
          {{ item.info }}
        </div>
        <div class="secondary-text">
          {{ item.description }}
        </div>
      </div>
    </el-radio-button>

    <el-radio-button
      v-if="!optionList || optionList.length === 0"
      disabled
      style="
        background-color: #eff0f1;
        --el-radio-button-disabled-checked-fill: #eff0f1;
      "
    >
      <div class="radio-container">
        <div class="main-text" style="color: #8f959e">
          <CeIcon
            code="icon_host_outlined"
            size="17px"
            style="margin-right: 9px"
          />
          暂无可用集群
        </div>
        <div class="secondary-text">-</div>
      </div>
    </el-radio-button>
  </el-radio-group>
</template>
<script setup lang="ts">
import { computed } from "vue";
import type { FormView } from "@commons/components/ce-form/type";
import CeIcon from "@commons/components/ce-icon/index.vue";
const props = defineProps<{ formItem: FormView }>();

const optionList = computed(() => {
  if (props.formItem.optionList) {
    return props.formItem.optionList;
  }
  return [];
});
</script>
<style lang="scss" scoped>
.radio-container {
  min-width: calc(160px);

  .main-text {
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    align-items: center;
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    line-height: 22px;
    color: #1f2329;
  }

  .secondary-text {
    display: flex;
    flex-direction: row;
    margin-top: 3px;
    color: #8f959e;
    font-style: normal;
    font-weight: 400;
    font-size: 12px;
    line-height: 16px;
  }
}
</style>
