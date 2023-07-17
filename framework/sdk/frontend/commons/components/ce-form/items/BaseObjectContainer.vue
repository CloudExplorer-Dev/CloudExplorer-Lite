<template>
  <CeForm
    :read-only="confirm"
    :style="style"
    label-position="top"
    require-asterisk-position="right"
    ref="ceFormRef"
    v-model="data"
    :other-params="other"
    :formViewData="formItem.optionList"
    v-bind="attrs"
  ></CeForm>
</template>
<script setup lang="ts">
import { computed } from "vue";
const emit = defineEmits(["update:modelValue", "change"]);
import type { FormView } from "@commons/components/ce-form/type";
const props = defineProps<{
  modelValue?: any;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  otherParams: any;
  formItem: FormView;
  confirm?: boolean;
}>();
const data = computed({
  get: () => {
    if (props.modelValue) {
      return props.modelValue;
    }
    return {};
  },
  set: ($event) => {
    emit("update:modelValue", $event);
  },
});
const attrs = computed(() => {
  if (props.formItem.attrs) {
    return JSON.parse(props.formItem.attrs);
  }
  return { "label-width": 0, "label-position": "top" };
});
const style = computed(() => {
  if (props.formItem.propsInfo && props.formItem.propsInfo.style) {
    return props.formItem.propsInfo.style;
  }
  return { width: "100%" };
});
const other = computed(() => {
  return { ...(props.allData ? props.allData : {}), ...props.otherParams };
});
</script>
<style lang="scss" scoped>
:deep(.el-form-item__content) {
  margin-bottom: 14px;
}
</style>
