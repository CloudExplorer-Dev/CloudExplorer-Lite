<template>
  <el-select
    class="m-2"
    filterable
    clearable
    v-bind="$attrs"
    v-model="_modelValue"
  >
    <el-option
      v-for="(item, index) in optionList"
      :key="index"
      :label="baseTextField(formItem, item)"
      :value="item[formItem.valueField ? formItem.valueField : 'value']"
    >
      <div v-html="formatTextField(formItem, item)"></div>
    </el-option>
  </el-select>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import { computed } from "vue";
import _ from "lodash";

const props = defineProps<{ modelValue?: string; formItem: FormView }>();
const optionList = computed(() => {
  if (props.formItem.optionList) {
    return props.formItem.optionList;
  }
  return [];
});
const emit = defineEmits(["update:modelValue", "change"]);
const _modelValue = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
    emit("change", props.formItem);
  },
});

function baseTextField(formItem: FormView, item: any) {
  //置空
  if (props.modelValue) {
    const oldItem = _.find(formItem.optionList, [
      props.formItem.valueField,
      props.modelValue,
    ]);
    if (!oldItem) {
      emit("update:modelValue", undefined);
      emit("change", formItem);
    }
  }
  if (formItem.formatTextField) {
    if (formItem.baseTextField && formItem.baseTextField !== "") {
      return item[formItem.baseTextField];
    }
  }
  if (formItem.textField && formItem.textField !== "") {
    return item[formItem.textField];
  }
  return item["label"];
}

function formatTextField(formItem: FormView, item: any) {
  if (
    formItem.formatTextField &&
    formItem.textField &&
    formItem.textField !== ""
  ) {
    let temp = _.replace(formItem.textField, /{/g, "{item['");
    temp = _.replace(temp, /}/g, "']}");
    return eval("`" + temp + "`");
  } else {
    return baseTextField(formItem, item);
  }
}
</script>
<style lang="scss"></style>
