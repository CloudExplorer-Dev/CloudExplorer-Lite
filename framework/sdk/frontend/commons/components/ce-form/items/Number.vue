<template>
  <el-input-number v-bind="$attrs" :value-on-clear="null" :style="style" />
</template>
<script setup lang="ts">
defineProps<{
  formItem: FormView;
  style?: any;
}>();

import type { FormView } from "@commons/components/ce-form/type";
</script>
<style lang="scss"></style>
