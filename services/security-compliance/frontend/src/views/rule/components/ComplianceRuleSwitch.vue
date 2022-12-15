<template>
  <el-switch
    v-loading="loading"
    :modelValue="modelValue"
    size="large"
    @change="switchEnable($event)"
  ></el-switch>
</template>
<script setup lang="ts">
import complianceRuleApi from "@/api/rule";
import { ref } from "vue";
const props = defineProps<{
  /**
   * 值
   */
  modelValue: boolean;
  /**
   * 合规规则id
   */
  complianceRuleId: string;
}>();
const emit = defineEmits(["update:modelValue"]);
const loading = ref<boolean>(false);
const switchEnable = (val: boolean) => {
  complianceRuleApi
    .switchEnable(props.complianceRuleId, val, loading)
    .then((ok) => {
      emit("update:modelValue", ok.data.enable);
    });
};
</script>
<style lang="scss"></style>
