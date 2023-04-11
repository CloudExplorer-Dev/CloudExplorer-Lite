<template>
  <el-form
    style="width: 100%; display: flex; flex-wrap: wrap"
    :model="modelValue"
  >
    <div style="width: 60px" v-if="modelValue.rules.length > 0">
      <el-form-item
        style="width: 60px; display: inline-flex; height: 100%"
        prop="conditionType"
      >
        <el-select
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
      </el-form-item>
    </div>
    <div style="width: 90%">
      <div
        style="margin: 14px 0"
        v-for="(rule, index) in modelValue.rules"
        :key="index"
      >
        <compliance_rule_item
          ref="ruleItem"
          :fields="fields"
          v-bind:modelValue="modelValue.rules[index]"
          @update:modelValue="update(index, $event)"
        ></compliance_rule_item>
      </div>
    </div>

    <div style="display: flex; width: 100%; justify-content: space-between">
      <div @click="addRule" class="add">
        <ce-icon code="icon_add_outlined" size="12px"></ce-icon>添加规则
      </div>
    </div>
  </el-form>
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
const ruleItem = ref<Array<InstanceType<typeof compliance_rule_item>>>([]);
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
      scanRule: props.modelValue.scanRule
        ? props.modelValue.scanRule
        : "COMPLIANCE",
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
      scanRule: props.modelValue.scanRule
        ? props.modelValue.scanRule
        : "COMPLIANCE",
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
// 校验函数
const validate = () => {
  return Promise.all(ruleItem.value.map((item) => item.validate())).catch(
    // eslint-disable-next-line @typescript-eslint/no-empty-function
    () => {}
  );
};
defineExpose({ validate });
</script>

<style lang="scss">
.add {
  color: rgba(51, 112, 255, 1);
  cursor: pointer;
}
</style>
