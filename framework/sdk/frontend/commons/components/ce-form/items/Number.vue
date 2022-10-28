<template>
  <el-input-number v-bind="$attrs" />
</template>
<script setup lang="ts">
const props = defineProps<{ formItem: FormView }>();

import type { FormView } from "@commons/components/ce-form/type";
</script>
<style lang="scss"></style>
