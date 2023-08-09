<template>
  <div class="currency-item">
    <span class="message">1人民币=</span>
    <div class="value">
      <el-input
        v-number="{
          max: 10240,
          min: 0,
          fixed: 10,
          type: 'float',
          isNull: true,
        }"
        v-model="exchangeRate"
      ></el-input>
    </div>

    <span class="unit">{{ modelValue.unit }}</span>
  </div>
</template>
<script setup lang="ts">
import { computed } from "vue";
import type { Currency } from "@commons/api/bil_view/type";
const props = defineProps<{
  modelValue: Currency;
}>();
const emit = defineEmits(["update:modelValue"]);

const exchangeRate = computed({
  get: () => {
    return props.modelValue.exchangeRate;
  },
  set: ($event) => {
    emit("update:modelValue", { ...props.modelValue, exchangeRate: $event });
  },
});
</script>
<style lang="scss" scoped>
.currency-item {
  border-radius: 4px;
  margin-top: 8px;
  padding-left: 8px;
  background-color: #f7f9fc;
  height: 48px;
  width: 100%;
  display: flex;
  align-items: center;
  .message {
    color: rgb(31, 35, 41);
    width: 60px;
    margin-right: 4px;
  }
  .value {
    width: 120px;
  }
  .unit {
    color: rgb(31, 35, 41);
    width: 60px;
    margin-left: 4px;
  }
}
</style>
