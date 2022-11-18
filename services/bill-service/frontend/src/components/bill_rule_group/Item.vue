<template>
  <el-form
    ref="ruleFormRef"
    :model="form"
    class="item_rule"
    v-loading="loading"
  >
    <el-form-item
      prop="field"
      :rules="{
        message: '字段不能为空',
        trigger: 'change',
        required: true,
      }"
      style="width: 100px; margin-left: 20px"
    >
      <el-select v-model="form.field" class="m-2" placeholder="请选择">
        <el-option
          v-for="item in billRuleGroupKeys"
          :key="item.value"
          :label="item.key"
          :value="item.value"
        />
      </el-select>
    </el-form-item>
    <div>等于:</div>
    <template v-if="form.field === 'tags'">
      <el-form-item
        prop="tagField"
        :rules="{
          message: '标签字段不能为空',
          trigger: 'change',
          required: true,
        }"
      >
        <el-select v-model="form.childField" class="m-2" placeholder="请选择">
          <el-option
            v-for="item in billChildKeys"
            :key="item.value"
            :label="item.key"
            :value="item.value"
          /> </el-select
      ></el-form-item>
    </template>
    <div style="flex: auto"></div>
    <div
      style="
        color: var(--el-color-primary);
        margin-right: 20px;
        cursor: pointer;
      "
      @click="deleteItem(item.id)"
    >
      删除条件
    </div>
  </el-form>
</template>
<script setup lang="ts">
import { ref, watch } from "vue";
import type { SimpleMap } from "@commons/api/base/type";
import type { BillGroupRule } from "@/api/bill_rule/type";
import billRuleApi from "@/api/bill_rule";
/**
 * 账单规则标签Key
 */
const billChildKeys = ref<Array<SimpleMap<string>>>([]);

const props = defineProps<{
  /**
   * 账单规则key
   */
  billRuleGroupKeys: Array<SimpleMap<string>>;
  /**
   *
   */
  item: BillGroupRule;
  /**
   *选中的分组
   */
  selectedGroupFields: Array<BillGroupRule>;
  /**
   * 删除规则条件函数
   */
  deleteRule: (id: string) => void;

  /**
   * 更新规则
   */
  updateRule: (id: string, field: string, name: string) => void;
}>();

/**
 * 加载器
 */
const loading = ref<boolean>();

/**
 * 删除规则
 */
const deleteItem = (id: string) => {
  props.deleteRule(id);
};
/**
 * 表单数据收集
 */
const form = ref<{
  /**
   * 授权字段
   */
  field: string;
  /**
   *  标签字段
   */
  childField: string;
}>({ field: "", childField: "" });
/**
 * 根据字段获取label
 * @param field 字段
 */
const getLabelByField = (field: string) => {
  const findKey = props.billRuleGroupKeys.find((g) =>
    Object.values(g).includes(field)
  );
  if (findKey) {
    return Object.keys(findKey)[0];
  }
  return "";
};

watch(
  () => form.value.field,
  () => {
    if (form.value.field) {
      if (form.value.field === "tags" || form.value.field === "orgTree") {
        billRuleApi.getGroupChildKeys(form.value.field, loading).then((ok) => {
          billChildKeys.value = ok.data;
        });
      } else {
        props.updateRule(
          props.item.id,
          form.value.field,
          getLabelByField(form.value.field)
        );
      }
    }
  }
);

watch(
  () => form.value.childField,
  () => {
    if (form.value.childField && form.value.field) {
      props.updateRule(
        props.item.id,
        form.value.childField,
        `${getLabelByField(form.value.field)}[${form.value.childField}]`
      );
    }
  }
);
</script>
<style lang="scss">
:deep(.el-form-item) {
  margin-bottom: 0;
}
.item_rule {
  display: flex;
  align-items: center;
  height: 40px;
  width: 100%;
  margin-top: 5px;
  background: #edf2f5;
  border-radius: 4px;
}
</style>
