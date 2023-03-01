<template>
  <div
    :style="{
      color: getColorByStatus(status),
    }"
    style="display: flex; justify-content: space-around"
  >
    <ce-icon
      style="cursor: pointer; font-size: 20px"
      :class="status === 'SYNCING' ? 'is-loading' : ''"
      :code="getStatusIconCode(status)"
    ></ce-icon>
    <div v-if="showText">{{ mapStatus(status) }}</div>
  </div>
</template>
<script setup lang="ts">
withDefaults(
  defineProps<{
    status: string;
    showText: boolean;
  }>(),
  { showText: false }
);
const getStatusIconCode = (status: string) => {
  return status === "FAILED"
    ? "Warning"
    : status === "INIT"
    ? "Sunrise"
    : status === "SUCCESS"
    ? "CircleCheck"
    : status === "SYNCING"
    ? "Loading"
    : "InfoFilled";
};
const getColorByStatus = (status: string) => {
  return status === "FAILED"
    ? "var(--el-color-error)"
    : status === "INIT"
    ? "var(--el-color-warning)"
    : status === "SUCCESS"
    ? "var(--el-color-success)"
    : status === "SYNCING"
    ? "var(--el-color-primary)"
    : "var(--el-color-info)";
};
const mapStatus = (status: string) => {
  return status === "FAILED"
    ? "扫描失败"
    : status === "INIT"
    ? "初始化"
    : status === "SUCCESS"
    ? "扫描成功"
    : status === "SYNCING"
    ? "扫描中"
    : "未知";
};
</script>
<style lang="scss"></style>
