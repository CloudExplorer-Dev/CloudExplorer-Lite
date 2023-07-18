<template>
  <component :is="column.component" v-bind="attrs"></component>
</template>
<script setup lang="ts">
import { computed, ref } from "vue";
import _ from "lodash";
import DecimalFormat from "@commons/utils/decimalFormat";
const props = defineProps<{
  /**
   *表单渲染Item column
   */
  column: any;
  /**
   * 这一行数据
   */
  row: any;
}>();

const tempDecimalFormat = ref<any>();

function evalF(text: string, row: any, DecimalFormat: any) {
  tempDecimalFormat.value = DecimalFormat;
  return eval(text);
}

const attrs = computed(() => {
  if (props.column.attrs) {
    const temp = _.cloneDeep(props.column.attrs);

    return Object.keys(temp)
      .map((key) => ({
        [key]: computedAttrsValue(temp[key], props.row),
      }))
      .reduce((pre, next) => ({ ...pre, ...next }), {});
  }
  return {};
});

const computedAttrsValue = (value: any, row: any) => {
  if (typeof value === "string") {
    if (/row\[.*\]/.test(value)) {
      return evalF(value, row, DecimalFormat);
    }
  }
  return value;
};
</script>
<style lang="scss" scoped></style>
