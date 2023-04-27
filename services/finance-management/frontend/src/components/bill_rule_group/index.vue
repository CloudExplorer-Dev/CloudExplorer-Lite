<template>
  <BillRuleItem
    v-for="item in groups"
    ref="billRuleItem"
    :key="item.id"
    :item="item"
    :bill-rule-group-keys="billRuleGroupKeys"
    :delete-rule="deleteRuleGroup"
    :update-rule="updateRuleGroup"
    :selected-group-fields="groups"
  ></BillRuleItem>
  <div @click="addRuleGroup" class="add">
    <ce-icon code="icon_add_outlined" size="12px"></ce-icon>添加统计字段
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { SimpleMap } from "@commons/api/base/type";
import type { BillGroupRule } from "@/api/bill_rule/type";
import BillRuleItem from "@/components/bill_rule_group/Item.vue";
import billRuleApi from "@/api/bill_rule/";
const billRuleGroupKeys = ref<Array<SimpleMap<string>>>([]);
const billRuleItem = ref<Array<InstanceType<typeof BillRuleItem>>>([]);
onMounted(() => {
  billRuleApi.getGroupKeys().then((ok) => {
    billRuleGroupKeys.value = ok.data;
  });
});

defineProps<{
  groups: Array<BillGroupRule>;
}>();

const emit = defineEmits([
  "deleteRuleGroup",
  "addRuleGroup",
  "updateRuleGroup",
]);
/**
 * 删除规则
 * @param id 需要删除的id
 */
const deleteRuleGroup = (id: string) => {
  emit("deleteRuleGroup", id);
};

/**
 * 修改规则
 * @param id     id
 * @param field  字段
 * @param name   名称
 */
const updateRuleGroup = (id: string, field: string, name: string) => {
  emit("updateRuleGroup", id, field, name);
};
/**
 * 添加一个分组条件
 */
const addRuleGroup = () => {
  validate()
    .then(() => {
      emit("addRuleGroup");
    })
    // eslint-disable-next-line @typescript-eslint/no-empty-function
    .catch(() => {});
};

/**
 * 校验函数
 */
const validate = () => {
  return Promise.all(billRuleItem.value?.map((b) => b.validate()));
};
defineExpose({ validate });
</script>
<style lang="scss" scoped>
.add {
  cursor: pointer;
  color: rgba(51, 112, 255, 1);
}
</style>
