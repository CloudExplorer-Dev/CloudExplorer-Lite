<template>
  <el-select class="m-2" filterable clearable>
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

const props = defineProps<{ formItem: FormView }>();
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
