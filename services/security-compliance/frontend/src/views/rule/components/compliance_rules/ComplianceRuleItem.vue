<template>
  <el-form
    class="content"
    :inline="true"
    :model="modelValue"
    ref="form"
    :rules="rules"
  >
    <el-form-item class="field" prop="field">
      <el-select
        style="width: 100%"
        :modelValue="modelValue.field"
        placeholder="请选择规则字段"
        size="default"
        @change="handler('field', $event)"
      >
        <el-option
          v-for="item in store"
          :key="item.field"
          :label="item.label"
          :value="item.field"
        />
      </el-select>
    </el-form-item>
    <el-form-item prop="compare" class="compare">
      <el-select
        style="width: 100%"
        :modelValue="modelValue.compare"
        @change="handler('compare', $event)"
        placeholder="请选择比较器"
        size="default"
      >
        <el-option
          v-for="item in compares"
          :key="item.value"
          :label="item.key"
          :value="item.value"
        /> </el-select
    ></el-form-item>
    <el-form-item
      class="value"
      key="enum_value"
      prop="value"
      v-if="
        activeField &&
        (activeField.fieldType === 'Enum' ||
          activeField.fieldType === 'ArrayEnum') &&
        !['EXIST', 'NOT_EXIST'].includes(modelValue.compare)
      "
    >
      <el-select
        style="width: 100%"
        :modelValue="modelValue.value"
        @change="handler('value', $event)"
        placeholder="请选择"
        size="default"
      >
        <el-option
          v-for="item in activeField.options"
          :key="item.value"
          :label="item.key"
          :value="item.value"
        />
      </el-select>
    </el-form-item>
    <el-form-item
      class="value"
      v-else-if="
        activeField &&
        ['ArrayNumber', 'Number'].includes(activeField.fieldType) &&
        !['EXIST', 'NOT_EXIST'].includes(modelValue.compare)
      "
      key="number_value"
      prop="value"
    >
      <el-input-number
        style="width: 100%"
        :modelValue="modelValue.value"
        @input="handler('value', $event)"
        :precision="2"
      />
    </el-form-item>
    <el-form-item
      class="value"
      v-else-if="!['EXIST', 'NOT_EXIST'].includes(modelValue.compare)"
      key="string_value"
      prop="value"
    >
      <el-input
        style="width: 100%"
        :modelValue="modelValue.value"
        @input="handler('value', $event)"
        placeholder="请输入"
      />
    </el-form-item>
  </el-form>
</template>
<script setup lang="ts">
import { computed } from "vue";
import type { InstanceSearchField, Rule } from "@/api/rule/type";
import type { FormInstance, FormRules } from "element-plus";
import { ref } from "vue";
const props = defineProps<{
  // 规则下拉框
  store: Array<InstanceSearchField>;
  // 绑定数据
  modelValue: Rule;
}>();

/**
 * 比较器
 */
const compares = computed(() => {
  if (activeField.value) {
    return activeField.value?.compares;
  }
  return [];
});

/**
 * 选中字段
 */
const activeField = computed(() => {
  return props.store.find((f) => f.field === props.modelValue.field);
});
/**
 * 表单校验对象
 */
const rules = ref<FormRules>({
  field: [
    {
      required: true,
      message: "字段不能为空",
      type: "string",
      trigger: "change",
    },
  ],
  compare: [
    {
      required: true,
      message: "比较字段不能为空",
      type: "string",
      trigger: "change",
    },
  ],
  value: [
    {
      required: true,
      message: "值不能为空",
      trigger: "blur",
    },
  ],
});

// 字段绑定
const emit = defineEmits(["update:modelValue"]);
/**
 * 输入处理器
 * @param field 字段
 * @param value 值
 */
const handler = (field: string, value: any) => {
  if (field === "field") {
    const f = props.store.find((f) => f.field === value);
    if (f && ["ArrayNumber", "Number"].includes(f.fieldType)) {
      emit("update:modelValue", {
        ...props.modelValue,
        value: 0,
        compares: "EQ",
        [field]: value,
      });
      return;
    } else {
      emit("update:modelValue", {
        ...props.modelValue,
        value: "",
        compares: "EQ",
        [field]: value,
      });
      return;
    }
  }
  emit("update:modelValue", { ...props.modelValue, [field]: value });
};
const form = ref<FormInstance>();
// 校验函数
const validate = () => {
  return form.value?.validate();
};
defineExpose({ validate });
</script>
<style lang="scss" scoped>
.content {
  display: flex;
  flex-wrap: nowrap;
  margin-left: 0;
  .field {
    width: 200px;
    margin-right: 8px;
  }
  .compare {
    width: 200px;
    margin-right: 8px;
  }
  .value {
    width: 200px;
    margin-right: 0;
  }
}
</style>
