<template>
  <el-form
    ref="ruleFormRef"
    label-width="130px"
    label-suffix=":"
    label-position="left"
    :model="_data"
    v-loading="_loading"
  >
    <div v-for="item in formViewData" :key="item.field">
      <el-form-item
        :label="item.label"
        :prop="item.field"
        :rules="{
          message: item.label + '不能为空',
          trigger: 'blur',
          required: item.required,
        }"
      >
        {{ item }}
        <component
          style="width: 75%"
          @change="change(item)"
          v-model="_data[item.field]"
          :is="item.inputType"
          :formItem="item"
          v-bind="{ ...JSON.parse(item.attrs) }"
        ></component>
      </el-form-item>
    </div>
  </el-form>
</template>

<script setup lang="ts">
const props = defineProps<{
  // 页面渲染
  formViewData: Array<FormView>;
  otherParams: any;
  // 数据
  data: SimpleMap<any>;
}>();
const emit = defineEmits(["update:data", "update:formViewData"]);

import _ from "lodash";
import { computed, onMounted, ref, watch } from "vue";
import type { FormView } from "@commons/components/ce-form/type";
import formApi from "@commons/api/form_resource_api/index";
import type { FormInstance } from "element-plus";
import type { SimpleMap } from "@commons/api/base/type";

const _loading = ref<boolean>(false);
/**
 * 子组件可以修改data
 */
const _data = computed({
  get() {
    return props.data ? props.data : {};
  },
  set(value) {
    emit("update:data", value);
  },
});

/**
 * 获取form的默认值
 * @param formItem
 */
function getDefaultValue(formItem: FormView): any {
  if (formItem.defaultValue) {
    return formItem.defaultJsonValue
      ? JSON.parse(formItem.defaultValue)
      : formItem.defaultValue;
  }
  return undefined;
}

/**
 * 设置optionList
 * @param formItem
 * @param data
 */
function initOptionList(formItem: FormView, data: any): void {
  if (
    formItem.clazz &&
    formItem.method &&
    formItem.relationTrigger.every((r) => _data.value[r])
  ) {
    formApi
      .getResourceMyMethod(
        formItem.clazz,
        formItem.method,
        _.assign({}, data, props.otherParams),
        _loading
      )
      .then((ok) => {
        formItem.optionList = ok.data;
      });
  }
}

/**
 * 初始化表单
 */
function initForms(): void {
  const temp = { ..._data.value };
  _.forEach(props.formViewData, (formItem) => {
    //设置列表
    initOptionList(formItem, temp);
    //设置默认值
    _.set(
      temp,
      formItem.field,
      _.get(_data.value, formItem.field, getDefaultValue(formItem))
    );
  });

  _data.value = temp;
}

/**
 * 找到受当前变化的字段影响的表单
 * @param formItem
 */
const change = (formItem: FormView) => {
  console.log(formItem.field);
  _.forEach(props.formViewData, (item) => {
    if (_.includes(item.relationTrigger, formItem.field)) {
      console.log(item);
      //设置空值
      const temp = { ..._data.value };
      _.set(temp, item.field, undefined);
      console.log(temp);
      _data.value = temp;
      //设置列表
      initOptionList(item, temp);
    }
  });
};

// 校验实例对象
const ruleFormRef = ref<FormInstance>();

// 提交表单
const submit = (exec: (formData: any) => void) => {
  if (!ruleFormRef.value) return;
  ruleFormRef.value.validate((valid) => {
    if (exec && valid) {
    }
  });
};

onMounted(() => {
  console.log("init!!!");
  initForms();
});

// 暴露获取当前表单数据函数
defineExpose({});
</script>

<style lang="scss"></style>
