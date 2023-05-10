<script setup lang="ts">
import { ref, type Ref } from "vue";
import type { FormView } from "@commons/components/ce-form/type";
import formApi from "@commons/api/form_resource_api/index";
import type { FormInstance } from "element-plus";
import type { SimpleMap } from "@commons/api/base/type";
import CeFormItem from "@commons/components/ce-form/CeFormItem.vue";
const emit = defineEmits(["update:modelValue"]);

// 校验实例对象
const ruleFormRef = ref<FormInstance>();

// 资源加载器
const resourceLoading = ref<boolean>(false);

const props = withDefaults(
  defineProps<{
    // 页面渲染数据
    formViewData: Array<FormView>;
    // 调用接口所需要的其他参数
    otherParams: any;
    // 是否只读
    readOnly?: boolean;
    // 收集到的数据
    modelValue: SimpleMap<any>;
  }>(),
  { readOnly: false }
);
// 因为emit是异步的 发送后数据未更新,所有需要一个本地变量桥接
const cacheModelValue = ref<SimpleMap<any>>(props.modelValue);

// 发生变化
const change = (formItem: FormView, value: any) => {
  // 这里需要注意,emit是异步的 调用emit后 modelValue不会及时变为最新,所以使用 changeEndValue记录最新数据
  cacheModelValue.value = {
    ...cacheModelValue.value,
    [formItem.field]: value,
  };
  // 修改值
  emit("update:modelValue", cacheModelValue.value);
  // 修改关联值,并且获取初始化关联OptionList
  relationChange(formItem);
};

/**
 *
 * @param formItem  某一个表单Item的变化,影响到其他的表单的
 */
const relationChange = (formItem: FormView) => {
  // 找到受影响的Item
  const relationForms = props.formViewData.filter((item) =>
    item.relationTrigger.includes(formItem.field)
  );
  relationForms.forEach((relationItem) => {
    cacheModelValue.value = {
      ...cacheModelValue.value,
      [relationItem.field]: undefined,
    };
    // 修改当前的值为null
    emit("update:modelValue", cacheModelValue.value);
    if (
      relationItem.clazz &&
      relationItem.method &&
      relationItem.relationTrigger.every((r) => cacheModelValue.value[r]) &&
      // relationShows是哪个组件有值了,才出现当前按钮,子组件挂载的时候,会初始化数据
      !relationItem.relationShows.includes(formItem.field)
    ) {
      formApi
        .getResourceMyMethod(
          relationItem.clazz,
          relationItem.method,
          {
            ...cacheModelValue.value,
            ...props.otherParams,
          },
          resourceLoading
        )
        .then((ok) => {
          relationItem.optionList = ok.data;
        });
    }
    relationChange(relationItem);
  });
};

/**
 * 获取列表数据
 * @param formItem
 */
const listOptions = (formItem: FormView, loading: Ref<boolean>) => {
  if (
    // 执行类
    formItem.clazz &&
    // 执行函数
    formItem.method &&
    // 获取当前Options时,需要的关联数据已经有值
    formItem.relationTrigger.every((r) => props.modelValue[r])
  ) {
    return formApi
      .getResourceMyMethod(
        formItem.clazz,
        formItem.method,
        {
          ...props.modelValue,
          ...props.otherParams,
        },
        loading
      )
      .then((ok) => {
        formItem.optionList = ok.data;
      });
  }
  return Promise.resolve();
};

/**
 * 初始化默认值函数
 * @param formItem 指定需要初始化的表单Item
 */
const initDefaultData = (formItem: FormView) => {
  if (formItem.defaultValue && !props.modelValue[formItem.field]) {
    cacheModelValue.value = {
      ...cacheModelValue.value,
      [formItem.field]: formItem.defaultJsonValue
        ? JSON.parse(formItem.defaultValue)
        : formItem.defaultValue,
    };
    emit("update:modelValue", cacheModelValue.value);
  }
};

/**
 * 校验函数
 */
const validate = () => {
  if (!ruleFormRef.value) return Promise.resolve();
  return ruleFormRef.value.validate();
};

// 暴露获取当前表单数据函数
defineExpose({
  initDefaultData,
  validate,
  ruleFormRef,
});
</script>
<template>
  <el-form
    ref="ruleFormRef"
    label-width="130px"
    label-suffix=":"
    :model="modelValue"
    v-loading="resourceLoading"
    v-bind="$attrs"
  >
    <div v-for="item in formViewData" :key="item.field" style="width: 100%">
      <CeFormItem
        :key="item.field"
        v-if="item.relationShowValues? item.relationShows.every((i:string) => item.relationShowValues.includes(modelValue[i])):item.relationShows.every((i:string) => formData[i])"
        @change="change(item, $event)"
        v-bind:modelValue="modelValue[item.field]"
        :formItem="item"
        :listOptions="listOptions"
        :readOnly="readOnly"
        :initDefaultData="initDefaultData"
      >
      </CeFormItem>
    </div>
  </el-form>
</template>

<style lang="scss"></style>
