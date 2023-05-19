<template>
  <el-input
    type="number"
    inputmode="numeric"
    v-bind="$attrs"
    :step="1"
    controls-position="right"
    autocomplete="off"
    :style="style"
    v-model.number="_value"
    :disabled="disabled"
    :readonly="readonly"
  >
    <template #prefix>
      <slot name="perfix"></slot>
    </template>

    <template #append>
      <div
        style="
          display: flex;
          flex-direction: column;
          flex-wrap: nowrap;
          align-items: center;
          width: 30px;
        "
      >
        <el-button
          icon="ArrowUp"
          class="arrowBtn"
          @click="add"
          :disabled="disabled || readonly"
        />
        <el-button
          icon="ArrowDown"
          class="arrowBtn"
          @click="minus"
          :disabled="disabled || readonly"
        />
      </div>
      <div class="unit-label" v-if="formItem?.unit">{{ formItem?.unit }}</div>
    </template>
  </el-input>
</template>
<script setup lang="ts">
import { computed, toRef } from "vue";

const props = withDefaults(
  defineProps<{
    formItem?: FormView;
    style?: any;
    specialStep?: number | string;
    modelValue?: number | string;
    min?: number | string;
    max?: number | string;
    disabled?: boolean;
    readonly?: boolean;
  }>(),
  {
    specialStep: 1,
  }
);
import type { FormView } from "@commons/components/ce-form/type";

const emit = defineEmits(["update:modelValue"]);

const _value = computed<number | undefined>({
  get() {
    if (props.modelValue === undefined) {
      return undefined;
    } else {
      if (_min.value && Number(props.modelValue) < _min.value) {
        return _min.value;
      }
      if (_max.value && Number(props.modelValue) > _max.value) {
        return _max.value;
      }
      return Number(props.modelValue);
    }
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

const _min = computed(() => {
  return Number(props.min);
});
const _max = computed(() => {
  return Number(props.max);
});
const _specialStep = computed(() => {
  const s = Number(props.specialStep);
  if (s && s > 0) {
    return s;
  } else {
    return 1;
  }
});
function add() {
  const n =
    (_value.value ? _value.value : _min.value ? _min.value : 0) +
    _specialStep.value;
  if (_max.value) {
    if (n > _max.value) {
      _value.value = _max.value;
      return;
    }
  }
  _value.value = n;
}
function minus() {
  const n =
    (_value.value ? _value.value : _min.value ? _min.value : 0) -
    _specialStep.value;
  if (_min.value) {
    if (n < _min.value) {
      _value.value = _min.value;
      return;
    }
  }
  _value.value = n;
}
</script>
<style lang="scss" scoped>
:deep(input::-webkit-outer-spin-button),
:deep(input::-webkit-inner-spin-button) {
  -webkit-appearance: none;
  appearance: none;
  margin: 0;
}
:deep(input) {
  -moz-appearance: textfield;
}
:deep(.el-input__wrapper) {
}
:deep(.el-input-group__append) {
  background-color: #ffffff;
  padding: 0;
}
:deep(.el-input-group__prepend) {
  background-color: #ffffff;
  border-right: 0;
}

.arrowBtn {
  width: 30px;
  height: 15px;
  padding: 0;
  font-size: 12px;
}

.unit-label {
  background-color: #eff0f1;
  min-width: 30px;
  height: 30px;
  border-top-right-radius: 3px;
  border-bottom-right-radius: 3px;
  border: solid 1px var(--el-input-border-color);
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  justify-content: center;
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 22px;
  color: #1f2329;
}
</style>
