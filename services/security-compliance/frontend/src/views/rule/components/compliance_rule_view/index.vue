<template>
  <div class="rule-group-content" v-loading="loading">
    <div class="view" v-if="firstLabel">
      {{ firstLabel }}
    </div>
    <el-popover
      v-if="firstLabel"
      placement="top-start"
      :width="200"
      trigger="hover"
    >
      <template #reference>
        <div class="item-size">+{{ rules.rules.length }}</div>
      </template>

      <div class="item" v-for="(rule, index) in rules.rules" :key="index">
        <Item
          :field="getField(rule)"
          :compare="getCompare(rule)"
          :value="getValue(rule)"
        ></Item>
        <el-divider
          v-if="index < rules.rules.length - 1"
          border-style="dashed"
        />
      </div>
    </el-popover>
    <div class="error" v-if="!firstLabel && fields.length > 0">
      规则解析失败,请重新编辑规则
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, watch, computed } from "vue";
import type { KeyValue } from "@commons/api/base/type";
import complianceRuleApi from "@/api/rule";
import Item from "@/views/rule/components/compliance_rule_view/Item.vue";
import type { InstanceSearchField, Rule, Rules } from "@/api/rule/type";

const fields = ref<Array<InstanceSearchField>>([]);

const loading = ref<boolean>(false);

const firstLabel = computed(() => {
  if (props.rules.rules.length > 0) {
    return getField(props.rules.rules[0]);
  }
  return "";
});
/**
 * 获取字段名称
 * @param rule 规则
 */
const getField = (rule: Rule) => {
  const f = fields.value.find((f) => f.field === rule.field);
  return f ? f.label : "";
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
:deep(.el-divider--horizontal) {
  margin: 8px 0;
}
.rule-group-content {
  width: 100%;
  height: 100%;
  display: inline-flex;
  overflow: hidden;
  .view {
    text-align: center;

    display: inline-block;
    text-overflow: ellipsis;
    overflow: hidden;
    height: 24px;
  }

  .item-size {
    width: 29px;
    height: 24px;
    border-radius: 2px;
    display: inline-block;
    background: rgba(31, 35, 41, 0.1);
    text-align: center;
    cursor: pointer;
    margin-left: 8px;
    &:hover {
      background: #ebf1ff;
      color: #3370ff;
    }
  }
  .item {
    &:first-child {
      margin-top: 0;
    }

    display: flex;
    flex-wrap: nowrap;
    width: 100%;
    justify-content: space-between;
    div {
      width: 30%;
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
