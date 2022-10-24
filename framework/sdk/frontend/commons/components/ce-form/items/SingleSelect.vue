<template>
  <el-select class="m-2" filterable>
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
  return item[formItem.textField ? formItem.textField : "label"];
}

function formatTextField(formItem: FormView, item: any) {
  const str = baseTextField(formItem, item);
  if (formItem.formatTextField && formItem.textField) {
    let temp = _.replace(formItem.textField, /{/g, "{item['");
    temp = _.replace(temp, /}/g, "']}");
    return eval("`" + temp + "`");
  } else {
    return str;
  }
}
</script>
<style lang="scss"></style>
