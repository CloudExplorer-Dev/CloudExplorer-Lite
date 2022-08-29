<template>
  <el-input
    v-model="searchValue"
    @input="input"
    @blur="blur"
    @keydown="keydown"
    @clear="clear"
    v-bind="$attrs"
    clearable
    :placeholder="quickPlaceholder"
    class="input-with-select"
  >
    <template #prepend>
      <el-select
        v-model="activeSearchOption"
        placeholder="Select"
        style="width: 115px"
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
import { SearchOption, Condition } from "./index";

const emit = defineEmits(["update:modelValue", "change"]);

const props = defineProps({
  /**
   *需要查询的下拉框
   */
  searchOptions: Array<SearchOption>,

  quickPlaceholder: String,
});

const activeSearchOption = ref<SearchOption>();

const searchValue = ref<string>();
onMounted(() => {
  if (props.searchOptions) {
    activeSearchOption.value = props.searchOptions[0];
  }
});

const conditionObj = computed(() => {
  if (activeSearchOption.value && searchValue.value) {
    const obj: Condition = {
      label: activeSearchOption.value.label,
      value: searchValue.value,
      field: activeSearchOption.value.value,
      valueLabel: searchValue.value ? searchValue.value : "",
    };
    return obj;
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
  }
}
</script>
<style lang="scss "></style>
