<template>
  <el-select class="m-2">
    <el-option
      v-for="(item, index) in optionList"
      :key="index"
      :label="item[formItem.textField ? formItem.textField : 'label']"
      :value="item[formItem.valueField ? formItem.valueField : 'value']"
    />
  </el-select>
</template>
<script setup lang="ts">
import type { FormView } from "@/components/form/type";
import { computed } from "vue";
const props = defineProps<{ formItem: FormView }>();
const optionList = computed(() => {
  if (props.formItem.optionList) {
    return props.formItem.optionList;
  }
  return [];
});
</script>
<style lang="scss"></style>
