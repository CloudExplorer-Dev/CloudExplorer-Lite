<script setup lang="ts">
import { ref, onMounted, watch } from "vue";
import type { FormView } from "@/components/form/type";
import formApi from "@/api/form_resource_api/index";
import type { FormInstance } from "element-plus";
// 表单数据
const formData = ref<any>({});
// 校验实例对象
const ruleFormRef = ref<FormInstance>();

const props = defineProps<{
  // 页面渲染数据
  formViewData: Array<FormView>;
  // 调用接口所需要的其他参数
  otherParams: any;
}>();

// 发生变化
const change = (formItem: FormView) => {
  const relationForms = props.formViewData.filter((item) =>
    item.relationTrigger.includes(formItem.field)
  );
  relationForms.forEach((relationItem) => {
    // 修改当前的值为null
    formData.value[relationItem.field] = undefined;
    if (
      relationItem.clazz &&
      relationItem.method &&
      relationItem.relationTrigger.every((r) => formData.value[r])
    ) {
      formApi
        .getResourceMyMethod(
          relationItem.clazz,
          relationItem.method,
          {
            ...formData.value,
            ...props.otherParams,
          },
          resourceLoading
        )
        .then((ok) => {
          relationItem.optionList = ok.data;
        });
    }
  });
};

/**
 * 表单数据初始化,调用函数
 * @param formItem 表单item
 */
const initFormItem = (formItem: FormView) => {
  const relationForms = props.formViewData.filter((item) =>
    item.relationTrigger.includes(formItem.field)
  );
  relationForms.forEach((relationItem) => {
    if (
      relationItem.clazz &&
      relationItem.method &&
      relationItem.relationTrigger.every((r) => formData.value[r])
    ) {
      formApi
        .getResourceMyMethod(
          relationItem.clazz,
          relationItem.method,
          {
            ...formData.value,
            ...props.otherParams,
          },
          resourceLoading
        )
        .then((ok) => {
          relationItem.optionList = ok.data;
        });
    }
  });
};

/**
 * 初始化表单显示数据,设置默认值
 */
const initDefaultData = () => {
  // 初始化默认值 defaultValue后端只能传入string,所以只能是string
  props.formViewData.forEach((item) => {
    if (item.defaultValue && !formData.value[item.field]) {
      formData.value[item.field] = JSON.parse(item.defaultValue as string);
    }
  });
};

// 提交表单
const submit = (exec: (formData: any) => void) => {
  if (!ruleFormRef.value) return;
  ruleFormRef.value.validate((valid) => {
    if (exec && valid) {
      exec(formData.value);
    }
  });
};

// 监控formViewData 用于初始化数据
watch(
  () => props.formViewData,
  (pre) => {
    if (formData.value && pre) {
      initDefaultData();
      Object.keys(formData.value).forEach((key: string) => {
        const form = pre.find((f) => f.field === key);
        if (form) {
          initFormItem(form);
        }
      });
    }
  }
);
/**
 * 获取数据
 */
const getFormData = () => {
  return formData.value;
};

/**
 * 设置表单数据
 * @param data 表单数据
 */
const setData = (data: any) => {
  const dataKeys = Object.keys(data);
  dataKeys.forEach((key: string) => {
    try {
      formData.value[key] = JSON.parse(data[key]);
    } catch (e) {
      formData.value[key] = data[key];
    }
  });
};
/**
 * 清除表单数据
 */
const clearData = () => {
  formData.value = {};
};

const resourceLoading = ref<boolean>(false);
// 暴露获取当前表单数据函数
defineExpose({
  getFormData,
  submit,
  clearData,
  initDefaultData,
  setData,
});
</script>
<template>
  <el-form
    ref="ruleFormRef"
    label-width="130px"
    label-suffix=":"
    label-position="left"
    :model="formData"
    v-loading="resourceLoading"
  >
    <div v-for="item in formViewData" :key="item.field">
      <el-form-item
        v-if="item.relationShows.every((i:string) => formData[i])"
        :label="item.label"
        :prop="item.field"
        :rules="{
          message: item.label + '不能为空',
          trigger: 'blur',
          required: item.required,
        }"
      >
        <component
          style="width: 75%"
          @change="change(item)"
          v-model="formData[item.field]"
          :is="item.inputType"
          :formItem="item"
        ></component>
      </el-form-item>
    </div>
  </el-form>
</template>

<style lang="scss"></style>
