<template>
  <el-input
    v-model="searchValue"
    @input="input"
    @keydown="keydown"
    v-bind="$attrs"
    clearable
    :placeholder="quickPlaceholder"
    class="input-with-select"
    style="margin-right: 12px"
  >
    <template #prepend>
      <el-select
        v-model="activeSearchOption"
        placeholder="Select"
        style="width: 117px"
        @keydown="keydown"
      >
        <el-option
          v-for="option in searchOptions"
          :label="option.label"
          :value="option"
          :key="option.value"
        />
      </el-select>
    </template>
  </el-input>
</template>
<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import type { SearchOption, Condition } from "./type";

const emit = defineEmits(["update:modelValue", "change"]);

const props = withDefaults(
  defineProps<{
    /**
     *需要查询的下拉框
     */
    searchOptions: Array<SearchOption>;

    quickPlaceholder: string;
  }>(),
  { quickPlaceholder: "搜索" }
);

const activeSearchOption = ref<SearchOption>();

const searchValue = ref<string>();
onMounted(() => {
  if (props.searchOptions) {
    activeSearchOption.value = props.searchOptions[0];
  }
});

const conditionObj = computed(() => {
  if (activeSearchOption.value && searchValue.value) {
    if (searchValue.value.trim() !== "") {
      const obj: Condition = {
        label: activeSearchOption.value.label,
        value: searchValue.value.trim(),
        field: activeSearchOption.value.value,
        valueLabel: searchValue.value.trim() ? searchValue.value.trim() : "",
      };
      return obj;
    }
  }
  return undefined;
});

function input(e: Event) {
  emit("update:modelValue", conditionObj.value, e);
}

function blur(e: Event) {
  emit("change", conditionObj.value, e);
}

function clear() {
  emit("update:modelValue", conditionObj.value);
  emit("change", conditionObj.value);
}

function keydown(e: Event) {
  const event = e as KeyboardEvent;
  if (event.key === "Enter") {
    emit("change", conditionObj.value, e);
    searchValue.value = undefined;
  }
}
</script>
<style lang="scss "></style>
