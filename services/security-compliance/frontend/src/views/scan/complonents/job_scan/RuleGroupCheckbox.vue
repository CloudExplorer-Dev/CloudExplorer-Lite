<template>
  <el-checkbox-group v-model="selected">
    <el-checkbox
      v-for="ruleGroup in ruleGroupList"
      :key="ruleGroup.id"
      :label="ruleGroup.id"
      ><span class="text">{{ ruleGroup.name }}</span></el-checkbox
    >
  </el-checkbox-group>
</template>
<script setup lang="ts">
import { computed } from "vue";
import type { ComplianceRuleGroup } from "@/api/rule_group/type";
const props = defineProps<{
  // 规则组
  ruleGroupList: Array<ComplianceRuleGroup>;
  // 选中的数据
  modelValue: Array<string>;
}>();
const emit = defineEmits(["update:modelValue"]);
const selected = computed({
  get() {
    return props.modelValue;
  },
  set(activeIds: Array<string>) {
    emit("update:modelValue", activeIds);
  },
});
</script>
<style lang="scss" scoped>
:deep(.el-checkbox) {
  margin-right: 28px;
  margin-top: 16px;
  height: 22px;
  line-height: 22px;
}
.text {
  color: #1f2329;
}
</style>
