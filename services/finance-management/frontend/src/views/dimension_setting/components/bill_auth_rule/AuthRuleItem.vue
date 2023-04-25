<template>
  <el-form
    ref="ruleFormRef"
    :model="modelValue"
    class="item_rule"
    v-loading="loading"
  >
    <el-form-item
      prop="field"
      :rules="{
        message: '字段值不能为空',
        trigger: 'change',
        required: true,
      }"
      style="width: 200px"
    >
      <el-cascader
        filterable
        v-model="field"
        :props="dimensionFieldOption"
        placeholder="请选择字段"
      />
    </el-form-item>
    <el-form-item style="margin-left: 8px">
      <el-select
        :disabled="true"
        v-model="compare"
        placeholder="请选择"
        style="width: 117px"
      >
        <el-option
          v-for="item in compareOptions"
          :key="item.value"
          :label="item.key"
          :value="item.value"
        />
      </el-select>
    </el-form-item>

    <el-form-item
      style="margin-left: 8px"
      prop="value"
      :rules="{
        message: '字段值不能为空',
        trigger: 'change',
        required: true,
      }"
    >
      <el-select
        v-model="value"
        filterable
        multiple
        collapse-tags
        collapse-tags-tooltip
        placeholder="请选择"
        style="width: 140px"
      >
        <el-option
          v-for="item in dimensionSettingValues"
          :key="item.value"
          :label="item.key"
          :value="item.value"
        />
      </el-select>
    </el-form-item>
  </el-form>
</template>
<script setup lang="ts">
import type { KeyValue, SimpleMap } from "@commons/api/base/type";
import dimensionSettingApi from "@/api/dimension_setting/index";
import type { BillAuthorizeRuleCondition } from "@/api/dimension_setting/type";
import { ref, computed } from "vue";
import type { FormInstance, CascaderProps } from "element-plus";

/**
 * 获取数据加载器
 */
const loading = ref<boolean>(false);

/**
 * 授权字段值
 */
const dimensionSettingValues = ref<Array<SimpleMap<string>>>();

const dimensionFieldOption: CascaderProps = {
  lazy: true,
  lazyLoad(node, resolve) {
    const { level } = node;
    if (level === 0) {
      dimensionSettingApi.listAuthorizeKeys(loading).then((ok) => {
        const children = ok.data.map((item) => ({
          label: item.key,
          value: item.value,
          leaf: item.value.startsWith("tags") ? false : true,
        }));
        resolve(children);
      });
    }
    if (level == 1 && node.data?.value === "tags") {
      dimensionSettingApi.listAuthorizeValues(node.data?.value).then((ok) => {
        const children = ok.data.map((item) => ({
          label: item.key,
          value: item.value,
          leaf: true,
        }));
        resolve(children);
      });
    }
  },
};
const compareOptions = ref<Array<KeyValue<string, string>>>([
  {
    key: "等于",
    value: "EQ",
  },
]);

/**
 * 校验实例对象
 */
const ruleFormRef = ref<FormInstance>();
/**
 * 第一次进来 字段值未加载的情况 需要加载数据
 */
const updateDimensionSettingValues = () => {
  if (!dimensionSettingValues.value) {
    dimensionSettingApi
      .listAuthorizeValues(props.modelValue.field)
      .then((ok) => {
        dimensionSettingValues.value = ok.data;
      });
  }
};
const field = computed({
  get: () => {
    if (props.modelValue.field) {
      updateDimensionSettingValues();
      if (props.modelValue.field.startsWith("tags.")) {
        return ["tags", props.modelValue.field];
      } else {
        return [props.modelValue.field];
      }
    }
    return [];
  },
  set: (event) => {
    if (event.length > 0) {
      console.log(event[event.length - 1]);
      emit("update:modelValue", {
        ...props.modelValue,
        field: event[event.length - 1],
        value: [],
      });
      // 清除校验
      ruleFormRef.value?.resetFields();
      dimensionSettingApi
        .listAuthorizeValues(event[event.length - 1], loading)
        .then((ok) => {
          dimensionSettingValues.value = ok.data;
        });
    } else {
      emit("update:modelValue", {
        ...props.modelValue,
        field: "",
        value: [],
      });
    }
  },
});

const value = computed({
  get: () => {
    if (props.modelValue.value) {
      return props.modelValue.value;
    }
    return [];
  },
  set: (event) => {
    emit("update:modelValue", { ...props.modelValue, value: event });
  },
});

const compare = computed({
  get: () => {
    if (props.modelValue.compare) {
      return props.modelValue.compare;
    }
    emit("update:modelValue", { ...props.modelValue, compare: "EQ" });
    return "";
  },
  set: (event) => {
    emit("update:modelValue", { ...props.modelValue, compare: event });
  },
});

const props = defineProps<{
  /**
   * 条件对象
   */
  modelValue: BillAuthorizeRuleCondition;
}>();

/**
 * 校验函数
 */
const validate = () => {
  if (!ruleFormRef.value) return;
  return ruleFormRef?.value.validate();
};

const emit = defineEmits(["update:modelValue"]);

defineExpose({ validate });
</script>
<style lang="scss" scoped>
:deep(.el-form-item) {
  margin-bottom: 0;
}
.item_rule {
  display: flex;
  align-items: center;
}
:deep(.el-form-item__content) {
  .el-select__tags {
    flex-wrap: nowrap;
  }
}
</style>
