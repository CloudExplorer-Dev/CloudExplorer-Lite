<template>
  <div
    style="width: 100%"
    v-for="(rule, index) in modelValue"
    :key="rule.field"
  >
    <compliance_rule_item
      :fields="fields"
      v-bind:modelValue="modelValue[index]"
      @update:modelValue="update(index, $event)"
    ></compliance_rule_item>
  </div>
  <span @click="addRule">添加规则</span>
</template>
<script setup lang="ts">
import { ref, watch } from "vue";
import compliance_rule_item from "@/views/rule/components/compliance_rules/ComplianceRuleItem.vue";
import complianceRuleApi from "@/api/rule";
import type { InstanceSearchField, Rule } from "@/api/rule/type";
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
   * 绑定的值
   */
  modelValue: Array<Rule>;
}>();
const emit = defineEmits(["update:modelValue"]);

/**
 * 双向绑定修改数据
 * @param index 需要修改的index
 * @param rule  需要修改的对象
 */
const update = (index: number, rule: Rule) => {
  emit(
    "update:modelValue",
    props.modelValue.map((item, i) => {
      if (i === index) {
        return rule;
      }
      return item;
    })
  );
};

const loading = ref<boolean>(false);

const fields = ref<Array<InstanceSearchField>>([]);

watch(
  () => props.resourceType,
  () => {
    if (props.resourceType && props.platform) {
      complianceRuleApi
        .listInstanceSearchField(props.platform, props.resourceType, loading)
        .then((ok) => {
          fields.value = ok.data;
        });
    }
    emit("update:modelValue", []);
  },
  {
    immediate: true,
  }
);

watch(
  () => props.platform,
  () => {
    if (props.resourceType && props.platform) {
      complianceRuleApi
        .listInstanceSearchField(props.platform, props.resourceType, loading)
        .then((ok) => {
          fields.value = ok.data;
        });
    }
    emit("update:modelValue", []);
  }
);
/**
 * 添加规则
 */
const addRule = () => {
  emit("update:modelValue", [
    ...props.modelValue,
    { field: "", compare: "", value: "" },
  ]);
};
</script>

<style lang="scss"></style>
