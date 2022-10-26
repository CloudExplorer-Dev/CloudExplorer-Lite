<template>
  VsphereDiskConfigForm.vue

  {{ modelValue }}

  <div style="display: flex; flex-direction: row; flex-wrap: wrap">
    <div
      v-for="(obj, index) in list"
      :key="index"
      style="
        height: 160px;
        width: 200px;
        margin-right: 20px;
        margin-bottom: 20px;
      "
    >
      <el-card style="height: 120px">
        {{ index === 0 ? "系统盘" : "数据盘" }}

        <el-input-number v-bind="$attrs" />

        <el-button @click="remove(index)">-</el-button>
      </el-card>
    </div>
    <div style="height: 160px; width: 200px">
      <el-card
        style="
          height: 120px;
          display: flex;
          align-items: center;
          justify-content: center;
        "
      >
        <el-button style="margin: auto" class="el-button--primary" @click="add"
          >+</el-button
        >
      </el-card>
    </div>
  </div>
</template>
<script setup lang="ts">
const props = defineProps<{
  modelValue: any;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  formItem?: FormView;
}>();

const emit = defineEmits(["update:modelValue", "change"]);

import { computed, onUpdated, useAttrs, ref, watch, onMounted } from "vue";
import _ from "lodash";
import type { FormView } from "@commons/components/ce-form/type";

const list = computed({
  get() {
    return props.modelValue ? props.modelValue : [];
  },
  set(value) {
    console.log(value);
    //_.set(props.data, props.field, value);
    emit("update:modelValue", value);
  },
});
//const list = ref([{}]);

function onChange() {
  console.log(1111111);
}

function add() {
  console.log("add");
  list.value.push({});
  //emit("update:data", list.value);
}
function remove(index: number) {
  console.log(index);
  _.remove(list.value, (n, i) => index === i);
  //emit("update:data", list.value);
}

onMounted(() => {
  if (props.modelValue == undefined) {
    //_.set(props.data, props.field, []);
    emit("update:modelValue", []);
  }
});

/**
 * 监听模版变化，获取值
 */
// watch(
//   () => props.allData.template,
//   (data) => {
//     console.log(data);
//     // _.set(props.data, props.field, []);
//     emit("update:modelValue", []);
//   },
//   { deep: true }
// );

defineExpose({
  onChange,
  field: props.field,
});
</script>
<style lang="scss"></style>
