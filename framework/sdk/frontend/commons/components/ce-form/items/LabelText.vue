<template>
  <template v-if="!confirm">
    <span>{{ labelText }}</span>
  </template>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import { computed } from "vue";
const props = defineProps<{ formItem: FormView }>();
const labelText = computed(() => {
  if (props.formItem) {
    return props.formItem.optionList;
  }
  return "";
});
</script>
<style lang="scss"></style>
