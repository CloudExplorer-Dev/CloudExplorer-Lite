<template>
  <el-radio-group v-bind="$attrs">
    <template v-if="radioType === 'radio'">
      <el-radio
        v-for="(item, index) in optionList"
        :key="index"
        :label="item[formItem.valueField ? formItem.valueField : 'value']"
      >
        <div v-html="formatTextField(formItem, item)"></div>
      </el-radio>
    </template>
    <template v-else>
      <el-radio-button
        v-for="(item, index) in optionList"
        :key="index"
        :label="item[formItem.valueField ? formItem.valueField : 'value']"
      >
        <div v-html="formatTextField(formItem, item)"></div>
      </el-radio-button>
    </template>
  </el-radio-group>
</template>
<script setup lang="ts">
import { computed } from "vue";
import type { FormView } from "@commons/components/ce-form/type";
import _ from "lodash";
const props = defineProps<{ formItem: FormView }>();

const radioType = computed(() => {
  return props.formItem.propsInfo.radioType
    ? props.formItem.propsInfo.radioType
    : "button";
});

const optionList = computed(() => {
  if (props.formItem.optionList) {
    return props.formItem.optionList;
  }
  return [];
});

function baseTextField(formItem: FormView, item: any) {
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
