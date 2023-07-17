<template>
  <el-input v-bind="$attrs">
    <template #prefix>
      <div>{{ $attrs.label }}</div>
    </template>
  </el-input>
</template>
<script setup lang="ts">
const props = defineProps<{ formItem: FormView }>();

import type { FormView } from "@commons/components/ce-form/type";
</script>
<style lang="scss"></style>
