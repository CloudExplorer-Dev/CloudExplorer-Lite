<template>
  <ce-regex-tooltip
    :description="props.formItem.description"
    :model-value="_modelValue"
    :rules="rules"
  >
    <el-input
      ref="regexInputRef"
      v-bind="$attrs"
      :show-password="props.formItem.encrypted"
      :placeholder="props.placeholder ? props.placeholder : ''"
      v-model="_modelValue"
    />
  </ce-regex-tooltip>
</template>

<script setup lang="ts">
import { computed, ref } from "vue";
import type { FormView } from "@commons/components/ce-form/type";
const props = defineProps<{
  modelValue?: string;
  placeholder?: string;
  formItem: FormView;
  setDefaultValue?: boolean;
}>();
const showTooltip = ref<boolean>(false);
const emit = defineEmits(["update:modelValue", "change"]);
const _modelValue = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});
const defaultRules = ref<Array<any>>([{ regex: /\S/, message: "不能为空" }]);
const rules = computed<Array<any>>(() => {
  if (props.formItem.regexList) {
    return props.formItem.regexList;
  } else {
    return defaultRules;
  }
});
const valid = computed(() => {
  return rules?.value?.every((regex) => {
    return new RegExp(regex.regex).test(_modelValue.value as string);
  });
});
const regexInputRef = ref<any>();

function validate(): Promise<boolean> {
  if (!valid.value) {
    regexInputRef.value.focus();
    showTooltip.value = true;
    return new Promise((resolve, reject) => {
      return reject(props.formItem.label + "输入有误！");
    });
  }
  return regexInputRef.value.pending;
}

defineExpose({
  validate,
});
</script>

<style lang="scss" scoped></style>
