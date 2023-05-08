<template>
  <div class="rule_tree_content">
    <ce-rule-tree
      :left-height="48"
      :component="compliance_rule_item"
      :operate="operate"
      ref="ruleTreeRef"
      :store="fields"
    ></ce-rule-tree>
  </div>
</template>
<script setup lang="ts">
import { ref, watch } from "vue";
import compliance_rule_item from "@/views/rule/components/compliance_rules/ComplianceRuleItem.vue";
import type { Tree } from "@commons/components/ce-rule-tree/type";
import operate from "@/views/rule/components/compliance_rules/Operate.vue";
import CeRuleTree from "@commons/components/ce-rule-tree/index.vue";
import complianceRuleApi from "@/api/rule";
import type { InstanceSearchField, Rules } from "@/api/rule/type";
import { nanoid } from "nanoid";
const ruleTreeRef = ref<InstanceType<typeof CeRuleTree>>();

const emit = defineEmits(["update:modelValue"]);
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
  modelValue: Rules;
}>();

/**
 *转换为Rule
 * @param tree Rule对象
 */
const toRules = (tree?: Array<Tree>): Rules => {
  return {
    conditionType: tree && tree.length > 0 && tree[0].value,
    scanRule: props.modelValue.scanRule
      ? props.modelValue.scanRule
      : "COMPLIANCE",
    rules:
      tree && tree.length > 0 && tree[0].items
        ? tree[0].items
            ?.filter((v) => v.value)
            .map((item: any) => ({
              field: item.value.field,
              compare: item.value.compare,
              value: item.value.value,
            }))
        : [],
  };
};

/**
 *转换为Tree
 * @param rule 规则
 */
const toTree = (rule: Rules): Array<Tree> => {
  return [
    {
      id: nanoid(),
      value: rule.conditionType,
      items: rule.rules.map((item) => ({ id: nanoid(), value: item })),
    },
  ];
};

const loading = ref<boolean>(false);

const fields = ref<Array<InstanceSearchField>>([]);

const initDefaultTree = () => {
  ruleTreeRef.value?.setTree(
    toTree({
      conditionType: "AND",
      rules: [{ field: "", compare: "", value: "" }],
      scanRule: props.modelValue.scanRule
        ? props.modelValue.scanRule
        : "COMPLIANCE",
    })
  );
};
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
    initDefaultTree();
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
    initDefaultTree();
  }
);
// 校验函数
const validate = () => {
  if (ruleTreeRef.value) {
    return ruleTreeRef.value.validate().then(() => {
      emit("update:modelValue", toRules(ruleTreeRef.value?.getTree()));
    });
  }
  return Promise.resolve();
};
/**
 *回显数据
 */
const echo = (rule: Rules) => {
  ruleTreeRef.value?.setTree(toTree(rule));
};
defineExpose({ validate, echo });
</script>

<style lang="scss" scoped>
.rule_tree_content {
  width: 100%;
  border: 1px solid #dee0e3;
  border-radius: 4px;
  box-sizing: border-box;
  padding: 8px 16px 8px 16px;
}
</style>
