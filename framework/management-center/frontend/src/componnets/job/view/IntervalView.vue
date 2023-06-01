<script setup lang="ts">
import { computed } from "vue";
import _ from "lodash";
import type { KeyValue } from "@commons/api/base/type";

const props = defineProps<{
  interval?: number;
  unit?: "MINUTE" | "HOUR" | "DAY" | "WEEK" | "MONTH";
}>();

const unitList: Array<KeyValue<string, string>> = [
  { key: "分钟", value: "MINUTE" },
  { key: "小时", value: "HOUR" },
  { key: "日", value: "DAY" },
  { key: "周", value: "WEEK" },
  { key: "月", value: "MONTH" },
];

const _unit = computed(() => {
  return _.find(unitList, (u) => u.value === props.unit)?.key;
});
</script>

<template>间隔 {{ interval }} {{ _unit }}</template>

<style scoped lang="scss"></style>
