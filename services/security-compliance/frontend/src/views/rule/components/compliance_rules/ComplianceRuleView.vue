<template>
  <div class="content" v-loading="loading">
    <div class="item" v-for="(rule, index) in rules.rules" :key="index">
      <div class="field">{{ getField(rule) }}</div>
      <div class="compare">{{ getCompare(rule) }}</div>
      <div class="value">{{ getValue(rule) }}</div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, watch } from "vue";
import type { KeyValue } from "@commons/api/base/type";
import complianceRuleApi from "@/api/rule";
import type { InstanceSearchField, Rule, Rules } from "@/api/rule/type";

const fields = ref<Array<InstanceSearchField>>([]);
const loading = ref<boolean>(false);
/**
 * 获取字段名称
 * @param rule 规则
 */
const getField = (rule: Rule) => {
  const f = fields.value.find((f) => f.field === rule.field);
  return f?.label;
};

/**
 * 获取比较器中文
 * @param rule 比较器
 */
const getCompare = (rule: Rule) => {
  const f = fields.value.find((f) => f.field === rule.field);
  const c = f?.compares.find(
    (c: KeyValue<string, string>) => c.value === rule.compare
  );
  if (c) {
    return c.key;
  } else {
    return rule.compare;
  }
};
/**
 * 获取value中文
 * @param rule 规则
 */
const getValue = (rule: Rule) => {
  const f = fields.value.find((f) => f.field === rule.field);
  if (f?.fieldType === "Enum" || f?.fieldType === "ArrayEnum") {
    return f.options?.find((o: KeyValue<string, any>) => o.value == rule.value)
      ?.key;
  }
  return rule.value;
};

const props = defineProps<{
  /**
   * 资源类型
   */
  resourceType: string;
  /**
   * 云平台
   */
  platform: string;
  /**
   * 规则信息
   */
  rules: Rules;
}>();

watch(
  props,
  () => {
    if (props.platform && props.resourceType) {
      complianceRuleApi
        .listInstanceSearchField(props.platform, props.resourceType, loading)
        .then((ok) => {
          fields.value = ok.data;
        });
    }
  },
  { immediate: true }
);
</script>
<style lang="scss" scoped>
.content {
  display: flex;
  flex-wrap: wrap;
  width: 100%;
  .item {
    display: flex;
    flex-wrap: nowrap;
    width: 100%;
    justify-content: space-between;
    div {
      width: 100%;
      width: 70px;
      overflow: hidden;
      text-overflow: ellipsis;
      text-align: center;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
}
</style>
