<template>
  信息确认:
  <br />
  {{ allData }}
  <br />
  表单:
  <br />
  {{ allFormViewData }}
</template>

<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";

const props = defineProps<{
  allData?: any;
  allFormViewData?: Array<FormView>;
}>();

//todo 展示
</script>

<style lang="scss" scoped></style>
