<template>
  <div v-if="modelValue.rules.length > 0">
    <el-select
      style="width: 60px"
      v-bind:modelValue="modelValue.conditionType"
      @update:modelValue="updateConditionType($event)"
      class="m-2"
      size="small"
    >
      <el-option
        v-for="item in [
          { label: '并且', value: 'AND' },
          { label: '或者', value: 'OR' },
        ]"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      />
    </el-select>
  </div>
  <div style="width: 90%">
    <div v-for="(rule, index) in modelValue.rules" :key="rule.field">
      <compliance_rule_item
        :fields="fields"
        v-bind:modelValue="modelValue.rules[index]"
        @update:modelValue="update(index, $event)"
      ></compliance_rule_item>
    </div>
  </div>

  <div style="display: flex; width: 100%; justify-content: space-between">
    <div @click="addRule" style="cursor: pointer">添加规则</div>
    <el-radio-group
      v-bind:modelValue="modelValue.scanRule"
      @update:modelValue="updateScanRule($event)"
      class="ml-4"
    >
      <el-radio label="COMPLIANCE" size="large">视为合规</el-radio>
      <el-radio label="NOT_COMPLIANCE" size="large">视为不合规</el-radio>
    </el-radio-group>
  </div>
</template>
<script setup lang="ts">
import { ref, watch } from "vue";
import compliance_rule_item from "@/views/rule/components/compliance_rules/ComplianceRuleItem.vue";
import complianceRuleApi from "@/api/rule";
import type { InstanceSearchField, Rules, Rule } from "@/api/rule/type";
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
const emit = defineEmits(["update:modelValue"]);

const updateConditionType = (conditionType: "AND" | "OR") => {
  const rules = {
    rules: props.modelValue.rules,
    conditionType: conditionType,
    scanRule: props.modelValue.scanRule
      ? props.modelValue.scanRule
      : "COMPLIANCE",
  };
  emit("update:modelValue", rules);
};

const updateScanRule = (scanRule: "COMPLIANCE" | "NOT_COMPLIANCE") => {
  const rules = {
    rules: props.modelValue.rules,
    conditionType: props.modelValue.conditionType,
    scanRule: scanRule,
  };
  emit("update:modelValue", rules);
};
/**
 * 双向绑定修改数据
 * @param index 需要修改的index
 * @param rule  需要修改的对象
 */
const update = (index: number, rule: Rule | string) => {
  if (typeof rule === "string") {
    deleteRule(index);
  } else {
    const rules = {
      rules: props.modelValue.rules.map((item, i) => {
        if (i === index) {
          return rule;
        }
        return item;
      }),
      conditionType: props.modelValue.conditionType
        ? props.modelValue.conditionType
        : "AND",
      scanRule: props.modelValue.scanRule
        ? props.modelValue.scanRule
        : "COMPLIANCE",
    };
    emit("update:modelValue", rules);
  }
};
/**
 * 删除数据
 * @param index 需要修改的index
 * @param rule  需要修改的对象
 */
const deleteRule = (index: number) => {
  emit("update:modelValue", {
    rules: props.modelValue.rules.filter((item, tindex) => tindex !== index),
    conditionType: props.modelValue.conditionType,
    scanRule: props.modelValue.scanRule,
  });
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
    emit("update:modelValue", {
      conditionType: "AND",
      rules: [{ field: "", compare: "", value: "" }],
      scanRule: "COMPLIANCE",
    });
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
    emit("update:modelValue", {
      conditionType: "AND",
      rules: [{ field: "", compare: "", value: "" }],
      scanRule: "COMPLIANCE",
    });
  }
);
/**
 * 添加规则
 */
const addRule = () => {
  const rules: Rules = {
    rules: [
      ...(props.modelValue.rules ? props.modelValue.rules : []),
      { field: "", compare: "", value: "" },
    ],
    conditionType: props.modelValue.conditionType
      ? props.modelValue.conditionType
      : "AND",
    scanRule: props.modelValue.scanRule
      ? props.modelValue.scanRule
      : "COMPLIANCE",
  };

  emit("update:modelValue", rules);
};
</script>

<style lang="scss"></style>
