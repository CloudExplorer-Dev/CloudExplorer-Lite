<template>
  <BillRuleItem
    v-for="item in billRuleGroupList"
    :key="item.id"
    :item="item"
    :bill-rule-group-keys="billRuleGroupKeys"
    :delete-rule="deleteRule"
    :update-rule="updateRule"
    :selected-group-fields="billRuleGroupList"
  ></BillRuleItem>
  <div @click="addRule">添加字段</div>
</template>
<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { SimpleMap } from "@commons/api/base/type";
import type { BillGroupRule } from "@/api/bill_rule/type";
import BillRuleItem from "@/components/bill_rule_group/Item.vue";
import billRuleApi from "@/api/bill_rule/";
import { nanoid } from "nanoid";
const billRuleGroupList = ref<Array<BillGroupRule>>([]);
const billRuleGroupKeys = ref<Array<SimpleMap<string>>>([]);

onMounted(() => {
  billRuleApi.getGroupKeys().then((ok) => {
    billRuleGroupKeys.value = ok.data;
  });
});
/**
 * 删除规则
 * @param id 需要删除的id
 */
const deleteRule = (id: string) => {
  billRuleGroupList.value = billRuleGroupList.value.filter((g) => g.id !== id);
};

/**
 * 修改规则
 * @param id     id
 * @param field  字段
 * @param name   名称
 */
const updateRule = (id: string, field: string, name: string) => {
  const findRuleGroup = billRuleGroupList.value.find((g) => g.id === id);
  if (findRuleGroup) {
    findRuleGroup.field = field;
    findRuleGroup.name = name;
  }
};
const addRule = () => {
  billRuleGroupList.value.push({ id: nanoid(), field: "", name: "" });
};
</script>
<style lang="scss"></style>
