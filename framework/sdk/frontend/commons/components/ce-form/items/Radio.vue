<template>
  <el-radio-group v-bind="$attrs">
    <el-radio-button
      v-for="(item, index) in optionList"
      :key="index"
      :label="item[formItem.valueField ? formItem.valueField : 'value']"
    >
      <!--            {{ baseTextField(formItem, item) }}-->
      <div v-html="formatTextField(formItem, item)"></div>
    </el-radio-button>
  </el-radio-group>
</template>
<script setup lang="ts">
import { computed } from "vue";

const props = defineProps<{ formItem: FormView }>();

import type { FormView } from "@commons/components/ce-form/type";
import _ from "lodash";

const optionList = computed(() => {
  if (props.formItem.optionList) {
    return props.formItem.optionList;
  }
  return [];
});

function baseTextField(formItem: FormView, item: any) {
  return item[formItem.textField ? formItem.textField : "label"];
}

function formatTextField(formItem: FormView, item: any) {
  if (formItem.formatTextField && formItem.textField) {
    let temp = _.replace(formItem.textField, /{/g, "{item['");
    temp = _.replace(temp, /}/g, "']}");
    return eval("`" + temp + "`");
  } else {
    return baseTextField(formItem, item);
  }
}
</script>
<style lang="scss"></style>
