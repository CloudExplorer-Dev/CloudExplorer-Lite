<template>
  <div class="content">
    <el-select
      :modelValue="modelValue.field"
      class="avg spacing"
      placeholder="请选择规则字段"
      size="default"
      @change="handler('field', $event)"
    >
      <el-option
        v-for="item in fields"
        :key="item.field"
        :label="item.label"
        :value="item.field"
      />
    </el-select>
    <el-select
      :modelValue="modelValue.compare"
      @change="handler('compare', $event)"
      class="avg spacing"
      placeholder="请选择比较器"
      size="default"
    >
      <el-option
        v-for="item in compares"
        :key="item.value"
        :label="item.key"
        :value="item.value"
      />
    </el-select>
    <el-select
      v-if="
        activeField &&
        (activeField.fieldType === 'Enum' ||
          activeField.fieldType === 'ArrayEnum') &&
        !['EXIST', 'NOT_EXIST'].includes(modelValue.compare)
      "
      :modelValue="modelValue.value"
      @change="handler('value', $event)"
      class="avg spacing"
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
    <el-input-number
      class="avg spacing"
      v-else-if="
        activeField && ['ArrayNumber', 'Number'].includes(activeField.fieldType)
      "
      :modelValue="modelValue.value"
      @input="handler('value', $event)"
      :precision="2"
    />
    <el-input
      class="avg spacing"
      v-else-if="!['EXIST', 'NOT_EXIST'].includes(modelValue.compare)"
      :modelValue="modelValue.value"
      @input="handler('value', $event)"
      placeholder="请输入"
    />
    <div style="flex: auto"></div>
    <div @click="deleteRule" style="cursor: pointer">删除</div>
  </div>
</template>
<script setup lang="ts">
import { computed } from "vue";
import type { InstanceSearchField, Rule } from "@/api/rule/type";
const props = defineProps<{
  fields: Array<InstanceSearchField>;
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
  return props.fields.find((f) => f.field === props.modelValue.field);
});

// 字段绑定
const emit = defineEmits(["update:modelValue"]);

const deleteRule = () => {
  emit("update:modelValue", "delete");
};
/**
 * 输入处理器
 * @param field 字段
 * @param value 值
 */
const handler = (field: string, value: any) => {
  if (field === "field") {
    const f = props.fields.find((f) => f.field === value);
    if (f && ["ArrayNumber", "Number"].includes(f.fieldType)) {
      emit("update:modelValue", {
        ...props.modelValue,
        value: 0,
        [field]: value,
      });
      return;
    }
  }
  emit("update:modelValue", { ...props.modelValue, [field]: value });
};
</script>
<style lang="scss" scoped>
.content {
  width: 100%;
  display: flex;
  flex-wrap: nowrap;
}
.avg {
  width: 30%;
}
.spacing {
  margin: 0 5px;
}
</style>
