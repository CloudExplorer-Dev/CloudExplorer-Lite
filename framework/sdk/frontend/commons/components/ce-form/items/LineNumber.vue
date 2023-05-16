<template>
  <div class="mo-input--number">
    <el-input-number
      v-bind="$attrs"
      :step="1"
      controls-position="right"
      autocomplete="off"
      :style="style"
    />
    <span class="unit-label" v-if="formItem.unit">
      {{ formItem.unit }}
    </span>
  </div>
</template>
<script setup lang="ts">
defineProps<{
  formItem: FormView;
  style?: any;
}>();

import type { FormView } from "@commons/components/ce-form/type";
</script>
<style lang="scss" scoped>
/* 自定义数字输入框append  */
.mo-input--number {
  display: flex;
  height: 24px;
  :deep(.el-input__wrapper) {
    border-radius: 4px 0px 0px 4px;
  }
  :deep(.el-input-number__decrease) {
    background-color: #ffffff;
    //--el-input-number-controls-height: 15px !important;
  }
  :deep(.el-input-number__increase) {
    background-color: #ffffff;
    // --el-input-number-controls-height: 16px !important;
    margin-bottom: -2px;
  }
  .label {
    width: 56px;
    height: 22px;
    font-family: "PingFang SC";
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    line-height: 22px;
    color: #1f2329;
    padding-right: 12px;
  }
  .unit-label {
    background-color: rgba(239, 240, 241, 1);
    padding: 0 8px 0 8px;
    border: 1px solid #dcdfe5;
    border-left: 0px;
    border-radius: 0 4px 4px 0;
    height: 22px;
    font-family: "PingFang SC";
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    line-height: 22px;
  }
}
</style>
