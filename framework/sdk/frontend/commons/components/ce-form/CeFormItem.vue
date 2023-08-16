<template>
  <el-form-item
    v-loading="loading"
    :style="elFormItemStyle"
    :label="label"
    :prop="formItem.field"
    :key="formItem.field"
    :rules="rules"
    v-if="formItem.type === 'form-item'"
  >
    <template #label v-if="formItem.label">
      <CeFormItemLabel :formItem="formItem"></CeFormItemLabel>
    </template>
    <component
      :disabled="readOnly"
      v-model="itemValue"
      :is="formItem.inputType"
      :formItem="formItem"
      :other-params="otherParams"
      :style="componentStyle"
      :field="formItem.field"
      v-bind="{ ...JSON.parse(formItem.attrs) }"
    ></component>
    <span v-if="formItem.unit" style="padding-left: 15px">{{
      formItem.unit
    }}</span>
  </el-form-item>
  <component
    v-else
    ref="componentFormRef"
    :readOnly="readOnly"
    :disabled="readOnly"
    v-model="itemValue"
    :is="formItem.inputType"
    :formItem="formItem"
    :other-params="otherParams"
    :style="componentStyle"
    :field="formItem.field"
    v-bind="{ ...JSON.parse(formItem.attrs) }"
  ></component>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import CeFormItemLabel from "@commons/components/ce-form/CeFormItemLabel.vue";
import { computed, type Ref, ref, watch } from "vue";
const emit = defineEmits(["change"]);
const props = defineProps<{
  // 双向绑定的值
  modelValue: any;
  // 表单Item
  formItem: FormView;
  // 是否只读
  readOnly: boolean;
  // 调用接口所需要的其他参数
  otherParams: any;
  // 获取Options
  listOptions: (formItem: FormView, loading: Ref<boolean>) => Promise<any>;
  // 初始化默认数据
  initDefaultData: (formItem: FormView) => void;
  // 默认每个宽度
  defaultItemWidth: string;
}>();

const itemValue = computed({
  get: () => {
    return props.modelValue;
  },
  set: (value: any) => {
    emit("change", value);
  },
});
const loading = ref<boolean>(false);

/**
 * 组件样式
 */
const componentStyle = computed(() => {
  return props.formItem.propsInfo.style
    ? props.formItem.propsInfo.style
    : { width: props.defaultItemWidth };
});
/**
 * 表单样式
 */
const elFormItemStyle = computed(() => {
  return props.formItem.propsInfo.elFormItemStyle
    ? props.formItem.propsInfo.elFormItemStyle
    : { width: "100%" };
});
/**
 * 表单错误Msg
 */
const errMsg = computed(() => {
  return props.formItem.propsInfo.errMsg
    ? props.formItem.propsInfo.errMsg
    : props.formItem.label + "不能为空";
});
/**
 *表单Label
 */
const label = computed(() => {
  return props.formItem.propsInfo.showLabel !== undefined &&
    props.formItem.propsInfo.showLabel === false
    ? ""
    : props.formItem.label;
});

const rules = computed(() => {
  return props.formItem.propsInfo.rules
    ? props.formItem.propsInfo.rules
    : {
        message: errMsg.value,
        trigger: "blur",
        required: props.formItem.required,
      };
});

watch(
  () => props.formItem,
  () => {
    props.listOptions(props.formItem, loading).then(() => {
      if (props.formItem.valueField && props.formItem.optionList) {
        if (
          props.formItem.optionList.some((item: any) =>
            item[props.formItem.valueField as string] ===
            props.formItem.defaultJsonValue
              ? JSON.parse(props.formItem.defaultValue)
              : props.formItem.defaultValue
          )
        ) {
          props.initDefaultData(props.formItem);
        }
      } else {
        props.initDefaultData(props.formItem);
      }
    });
  },
  {
    immediate: true,
  }
);

const componentFormRef = ref<any>();

const validate = () => {
  if (componentFormRef.value) {
    return componentFormRef.value.validate();
  }
  return Promise.resolve();
};
defineExpose({ validate });
</script>
<style lang="scss" scoped></style>
