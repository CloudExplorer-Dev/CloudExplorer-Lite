<template>
  <div class="status_content">
    <div class="icon"><component :is="iconReader()"></component></div>
    <div class="label">{{ label }}</div>
  </div>
</template>
<script setup lang="ts">
import type { VNode } from "vue";

defineProps<{
  // 图表
  iconReader: () => VNode;
  // 提示
  label: string;
}>();
</script>
<style lang="scss" scoped>
.status_content {
  display: flex;

  .label {
    font-weight: 400;
    font-size: 14px;
    line-height: 22px;
    color: #1f2329;
    height: 22px;
    margin-left: 5px;
  }
}
</style>
