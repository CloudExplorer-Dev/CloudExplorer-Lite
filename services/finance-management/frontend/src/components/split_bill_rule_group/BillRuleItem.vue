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
      style="width: 90px; margin-left: 5px"
    >
      <el-select
        filterable
        v-model="form.field"
        class="m-2"
        placeholder="请选择"
        size="small"
        style="width: 80px"
      >
        <el-option
          v-for="item in dimensionSettingKeys"
          :key="item.value"
          :label="item.key"
          :value="item.value"
        />
      </el-select>
    </el-form-item>
    <div>等于:&nbsp;&nbsp;</div>
    <template v-if="form.field === 'tags'">
      <el-form-item
        prop="tagField"
        :rules="{
          message: '标签字段不能为空',
          trigger: 'change',
          required: true,
        }"
      >
        <el-select
          filterable
          v-model="form.tagField"
          class="m-2"
          placeholder="请选择"
          size="small"
          style="width: 120px"
        >
          <el-option
            v-for="item in dimensionSettingTagKeys"
            :key="item.value"
            :label="item.key"
            :value="item.value"
          /> </el-select
      ></el-form-item>
    </template>
    <el-form-item
      prop="value"
      :rules="{
        message: '字段值不能为空',
        trigger: 'change',
        required: true,
      }"
    >
      <el-select
        filterable
        v-model="form.value"
        multiple
        collapse-tags
        collapse-tags-tooltip
        placeholder="请选择"
        size="small"
        style="width: 150px"
      >
        <el-option
          v-for="item in dimensionSettingValues"
          :key="item.value"
          :label="item.key"
          :value="item.value"
        /> </el-select
    ></el-form-item>

    <div
      style="margin-left: 12px; cursor: pointer; line-height: 32px"
      @click="deleteItem(item)"
    >
      <ce-icon code="icon_delete-trash_outlined" size="16px"></ce-icon>
    </div>
  </el-form>
</template>
<script setup lang="ts">
import type { SimpleMap } from "@commons/api/base/type";
import dimensionSettingApi from "@/api/dimension_setting/index";
import type { BillAuthorizeRuleCondition } from "@/api/dimension_setting/type";
import { ref, watch, onMounted } from "vue";
import type { FormInstance } from "element-plus";

/**
 * 获取数据加载器
 */
const loading = ref<boolean>(false);
/**
 * 授权字段
 */
const dimensionSettingKeys = ref<Array<SimpleMap<string>>>([]);
/**
 * 授权字段值
 */
const dimensionSettingValues = ref<Array<SimpleMap<string>>>([]);
/**
 * 授权标签字段
 */
const dimensionSettingTagKeys = ref<Array<SimpleMap<string>>>([]);
/**
 * 校验实例对象
 */
const ruleFormRef = ref<FormInstance>();

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
  tagField: string;
  /**
   * 授权值
   */
  value: Array<string>;
}>({ field: "", tagField: "", value: [] });

const props = defineProps<{
  /**
   * 条件对象
   */
  item: BillAuthorizeRuleCondition;
  /**
   * 删除规则条件函数
   */
  deleteRule: (item: BillAuthorizeRuleCondition) => void;
}>();

/**
 * 删除条件
 * @param item 需要删除的条件
 */
const deleteItem = (item: BillAuthorizeRuleCondition) => {
  props.deleteRule(item);
};

watch(
  () => form.value.tagField,
  () => {
    if (form.value.tagField) {
      dimensionSettingApi
        .listAuthorizeValues(form.value.tagField, loading)
        .then((ok) => {
          dimensionSettingValues.value = ok.data;
          if (
            !ok.data.some((item) => {
              return form.value.value.includes(item.value);
            })
          ) {
            form.value.value = [];
          }
        });
    }
  }
);

watch(
  () => form.value.field,
  () => {
    if (form.value.field) {
      dimensionSettingApi
        .listAuthorizeValues(form.value.field, loading)
        .then((ok) => {
          if (form.value.field === "tags") {
            dimensionSettingTagKeys.value = ok.data;
            if (!ok.data.map((i) => i.value).includes(form.value.tagField)) {
              form.value.tagField = "";
              form.value.value = [];
            }
          } else {
            dimensionSettingValues.value = ok.data;

            if (
              !ok.data.some((item) => {
                return form.value.value.includes(item.value);
              })
            ) {
              form.value.value = [];
              form.value.tagField = "";
            }
          }
        });
    }
  }
);

onMounted(() => {
  dimensionSettingApi.listAuthorizeKeys(loading).then((ok) => {
    dimensionSettingKeys.value = ok.data;
  });
  if (props.item.field) {
    if (props.item.field.startsWith("tags.")) {
      form.value.field = "tags";
      form.value.tagField = props.item.field;
    } else {
      form.value.field = props.item.field;
    }
  }
  if (props.item.value) {
    form.value.value = props.item.value;
  }
});

/**
 * 校验函数
 */
const validate = () => {
  if (!ruleFormRef.value) return;
  return ruleFormRef.value.validate();
};

/**
 * 设置数据
 */
const setData = (item: BillAuthorizeRuleCondition) => {
  if (form.value.field !== "tags") {
    item.field = form.value.field;
  } else {
    item.field = form.value.tagField;
  }
  item.value = form.value.value;
};

defineExpose({ validate, setData });
</script>
<style lang="scss" scoped>
:deep(.el-form-item) {
  margin-bottom: 0;
}
.item_rule {
  display: flex;
  align-items: center;
  height: 32px;
  width: 100%;
  margin-top: 8px;
  background: #edf2f5;
  border-radius: 4px;
}
:deep(.el-form-item__content) {
  .el-select__tags {
    flex-wrap: nowrap;
  }
}
</style>
