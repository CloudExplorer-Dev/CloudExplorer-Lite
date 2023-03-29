<template>
  <div class="radio_content">
    <div
      v-for="item in dataList"
      :key="item.value"
      class="item"
      :class="[activeValue == item.value ? 'active' : '']"
      @click="selected(item.value)"
    >
      {{ item.label }}
    </div>
  </div>
</template>
<script lang="ts" setup>
import { watch } from "vue";
const props = defineProps<{
  dataList: Array<{
    // 提示
    label: string;
    // 值
    value: string | number;
  }>;
  // 选中的值
  activeValue: string | number;
}>();
watch(
  props.dataList,
  () => {
    if (
      (props.dataList &&
        props.dataList.length > 0 &&
        !props.dataList.some((item) => item.value === props.activeValue)) ||
      !props.activeValue
    ) {
      emit("update:activeValue", props.dataList[0].value);
    }
  },
  { immediate: true }
);
const selected = (activeValue: string | number) => {
  console.log(activeValue);
  emit("update:activeValue", activeValue);
};
const emit = defineEmits(["update:activeValue"]);
</script>
<style lang="scss" scoped>
.radio_content {
  height: 32px;
  display: inline-flex;
  border: 1px solid #bbbfc4;
  border-radius: 4px;
  font-weight: 500;
  font-size: 14px;
  color: #1f2329;
  padding: 3px 4px;
  box-sizing: border-box;
  .active {
    border-radius: 4px;
    background: rgba(51, 112, 255, 0.1);
    color: #3370ff;
  }
  .item {
    cursor: pointer;
    margin: 0px 2px;
    padding: 2px 8px;
    height: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
    &:last-child {
      margin: 0 4px 0 2px;
    }
    &:first-child {
      margin: 0 2px 0 4px;
    }
  }
}
</style>
