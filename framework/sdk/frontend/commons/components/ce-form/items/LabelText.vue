<template>
  <span>{{ labelText }}</span>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import type { FormInstance } from "element-plus";
import { computed, watch, onMounted, ref } from "vue";
import _ from "lodash";
import { CloseBold } from "@element-plus/icons-vue";
import formApi from "@commons/api/form_resource_api";
const props = defineProps<{ formItem: FormView }>();
const labelText = computed(() => {
  if (props.formItem) {
    return props.formItem.optionList;
  }
  return "0.00";
});
</script>
<style lang="scss"></style>
