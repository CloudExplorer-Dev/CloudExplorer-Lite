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
      style="width: 40%; margin-left: 20px"
    >
      <el-select
        style="width: 100%"
        v-model="form.field"
        class="m-2"
        placeholder="选择统计字段"
      >
        <el-option
          v-for="item in billKeys"
          :key="item.value"
          :label="item.key"
          :value="item.value"
        />
      </el-select>
    </el-form-item>
    <template v-if="form.field === 'tags' || form.field === 'orgTree'">
      <el-form-item
        prop="childField"
        :rules="{
          message: '标签字段不能为空',
          trigger: 'change',
          required: true,
        }"
        style="width: 45%"
      >
        <el-select
          style="width: 100%"
          v-model="form.childField"
          class="m-2"
          placeholder="请选择"
        >
          <el-option
            v-for="item in billChildKeys"
            :key="item.value"
            :label="item.key"
            :value="item.value"
          /> </el-select
      ></el-form-item>
    </template>

    <div class="delete_icon" @click="deleteItem(item.id)">
      <ce-icon
        code="icon_delete-trash_outlined"
        size="16px"
        color="rgba(100, 106, 115, 1)"
      ></ce-icon>
    </div>
  </el-form>
</template>
<script setup lang="ts">
import { ref, watch, onMounted, computed } from "vue";
import type { SimpleMap } from "@commons/api/base/type";
import type { BillGroupRule } from "@/api/bill_rule/type";
import billRuleApi from "@/api/bill_rule";
import type { FormInstance } from "element-plus";

/**
 * 校验实例对象
 */
const ruleFormRef = ref<FormInstance>();

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

const billKeys = computed(() => {
  if (props.item.name) {
    return props.billRuleGroupKeys.filter((b) => {
      return (
        !props.selectedGroupFields.some((s) => s.field === b.value) ||
        props.item.field === b.value
      );
    });
  } else {
    return props.billRuleGroupKeys.filter((b) => {
      return !props.selectedGroupFields.some((s) => s.field === b.value);
    });
  }
});
const billChildKeys = computed(() => {
  return billChildKeyList.value.filter((b) => {
    return (
      !props.selectedGroupFields.some((s) => s.field === b.value) ||
      b.value === props.item.field
    );
  });
});
/**
 * 加载器
 */
const loading = ref<boolean>(false);

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
  const findKey = props.billRuleGroupKeys.find((g) => g.value === field);
  if (findKey) {
    return findKey.key;
  }
  return "";
};
const billChildKeyList = ref<Array<SimpleMap<string>>>([]);

watch(
  () => form.value.field,
  () => {
    if (form.value.field) {
      if (form.value.field === "tags" || form.value.field === "orgTree") {
        billRuleApi.getGroupChildKeys(form.value.field, loading).then((ok) => {
          billChildKeyList.value = ok.data;
        });
      } else {
        if (props.billRuleGroupKeys.length > 0) {
          props.updateRule(
            props.item.id,
            form.value.field,
            getLabelByField(form.value.field)
          );
        }
      }
    }
  },
  {
    immediate: true,
  }
);

watch(
  () => form.value.childField,
  () => {
    if (
      form.value.childField &&
      form.value.field &&
      props.billRuleGroupKeys.length > 0
    ) {
      props.updateRule(
        props.item.id,
        form.value.childField,
        `${getLabelByField(form.value.field)}[${form.value.childField.replace(
          form.value.field + ".",
          ""
        )}]`
      );
    }
  }
);

onMounted(() => {
  if (props.item.field.startsWith("tags.")) {
    form.value.field = "tags";
    form.value.childField = props.item.field;
  } else if (props.item.field.startsWith("orgTree.")) {
    form.value.field = "orgTree";
    form.value.childField = props.item.field;
  } else {
    form.value.field = props.item.field;
  }
});
/**
 * 校验函数
 */
const validate = () => {
  if (!ruleFormRef.value) return;
  return ruleFormRef.value.validate();
};

defineExpose({ validate });
</script>
<style lang="scss" scoped>
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
.delete_icon {
  margin-left: 10px;
  color: var(--el-color-primary);
  margin-right: 20px;
  cursor: pointer;
  white-space: nowrap;
}
</style>
