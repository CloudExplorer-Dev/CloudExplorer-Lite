<template>
  <!--  data: {{ _data }}
  <br />-->
  <el-form
    ref="ruleFormRef"
    label-width="130px"
    require-asterisk-position="right"
    label-position="top"
    :model="_data"
    v-loading="_loading"
    :inline="props.inline"
    @submit.prevent
    :scroll-to-error="true"
    :size="props.size"
  >
    <div
      v-for="item in formViewData"
      :key="item.field"
      :style="props.inline ? 'width:50%' : 'width:100%'"
    >
      <template v-if="item.label">
        <el-form-item
          v-if="checkShow(item)"
          :label="item.label"
          :prop="item.field"
          :rules="rules(item)"
        >
          <component
            ref="formItemRef"
            :is="item.inputType"
            v-model.trim="_data[item.field]"
            :style="getComponentStyle(item)"
            :all-data="allData"
            :all-form-view-data="allFormViewData"
            :field="item.field"
            :form-item="item"
            :set-default-value="true"
            :otherParams="otherParams"
            v-bind="{ ...JSON.parse(item.attrs) }"
            @change="change(item)"
          ></component>
          <span
            v-if="item.unit && props.groupId != '0'"
            style="padding-left: 15px"
            >{{ item.unit }}</span
          >
        </el-form-item>
      </template>
      <template v-else>
        <component
          ref="formItemRef"
          :style="getComponentStyle(item)"
          :is="item.inputType"
          v-model.trim="_data[item.field]"
          :all-data="allData"
          :all-form-view-data="allFormViewData"
          :field="item.field"
          :form-item="item"
          :set-default-value="true"
          :otherParams="otherParams"
          v-bind="{ ...JSON.parse(item.attrs) }"
          @change="change(item)"
        ></component>
      </template>
    </div>
  </el-form>
</template>

<script setup lang="ts">
const props = withDefaults(
  defineProps<{
    // 页面渲染
    formViewData: Array<FormView>;
    allFormViewData: Array<FormView>;
    otherParams: any;
    // 数据
    modelValue: any;
    allData: any;
    groupId: string;
    inline: boolean;
    size?: string;
  }>(),
  {
    modelValue: {},
    inline: false,
    size: "default",
  }
);
const emit = defineEmits([
  "update:modelValue",
  "update:formViewData",
  "update:allFormViewData",
  "optionListRefresh",
]);

import _ from "lodash";
import { computed, onBeforeMount, onMounted, reactive, ref } from "vue";
import type { FormView } from "@commons/components/ce-form/type";
import formApi from "@commons/api/form_resource_api";
import type { FormInstance } from "element-plus";

const _loading = ref<boolean>(false);

const formItemRef = ref<InstanceType<any> | null>(null);

/**
 * 组件样式
 * @param formItem
 */
function getComponentStyle(formItem: FormView): any {
  return formItem.propsInfo?.style ? formItem.propsInfo?.style : {};
}

/**
 * 子组件可以修改data
 */
const _data = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
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

function checkShow(currentItem: any): any {
  let isShow = currentItem.label;
  if (currentItem.relationShows && currentItem.relationShowValues) {
    isShow = currentItem.relationShows.every((i: string) =>
      currentItem.relationShowValues.includes(
        _.get(props.allData, i) === true ? "true" : _.get(props.allData, i)
      )
    );
  }
  if (isShow) {
    isShow = currentItem.footerLocation === 0;
  }
  return isShow;
}
function rules(currentItem: any) {
  const rules = [
    {
      message: currentItem.label + "不能为空",
      trigger: "change",
      required: currentItem.required,
    },
  ];
  if (currentItem.propsInfo.rules) {
    rules.push(currentItem.propsInfo.rules);
  }
  if (currentItem.regexp) {
    //华为特殊处理,密码规则不通操作系统不一样
    if (
      currentItem.field === "pwd" &&
      props.otherParams.platform === "fit2cloud_huawei_platform"
    ) {
      let regexp = "";
      if (props.allData.os?.toLowerCase().indexOf("windows") > -1) {
        regexp =
          "^((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*Adm)(?!.*dmi)(?!.*min)(?!.*ini)(?!.*nis)(?!.*ist)(?!.*str)(?!.*tra)(?!.*rat)(?!.*ato)(?!.*tor)(?!.*rotartsinimd[A|a]).{8,25})$";
      } else {
        regexp =
          "^((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*roo)(?!.*oot)(?!.*toor).{8,25})$";
      }
      const regexpObj = {
        message: currentItem.regexpDescription,
        trigger: "blur",
        required: currentItem.required,
      };
      _.set(regexpObj, "pattern", regexp);
      rules.push(regexpObj);
    } else {
      const regexpObj = {
        message: currentItem.regexpDescription,
        trigger: "blur",
        required: currentItem.required,
      };
      _.set(regexpObj, "pattern", currentItem.regexp);
      rules.push(regexpObj);
    }
  }
  return rules;
}

/**
 * 设置optionList
 * @param formItem
 * @param data
 * @param allData
 */
function initOptionList(
  formItem: FormView | undefined,
  data: any,
  allData?: any
): void {
  if (formItem && formItem.clazz && formItem.method) {
    const _temp = _.assignWith(
      {},
      props.otherParams,
      _.defaultTo(allData, props.allData),
      (objValue, srcValue) => {
        return _.isUndefined(objValue) ? srcValue : objValue;
      }
    );
    _.assign(_temp, data);
    if (
      //关联对象有值
      _.every(formItem?.relationTrigger, (trigger) => {
        return _.get(_temp, trigger) !== undefined;
      })
    ) {
      if (formItem.group?.toFixed() === props.groupId) {
        formApi
          .getResourceMethod(
            formItem.serviceMethod,
            formItem.clazz,
            formItem.method,
            _temp,
            _loading
          )
          .then((ok) => {
            formItem.optionList = ok.data;
          });
      } else {
        //交给其他的组内去调用，可以解决loading不生效的问题
        emit("optionListRefresh", formItem.field);
      }
    }
  }
}

/**
 * 根据field字段刷新optionList
 * @param field
 * @param allData
 */
function optionListRefresh(field: string, allData?: any): void {
  initOptionList(
    _.find(props.formViewData, (form) => form.field === field),
    { ..._data.value },
    allData
  );
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
  //console.log(formItem.field);
  _.forEach(props.allFormViewData, (item) => {
    if (_.includes(item.relationTrigger, formItem.field)) {
      //console.log(formItem.field, "in", item.field);
      //设置空值
      _.set(_data.value, item.field, undefined);
      if (item.method === "calculateConfigPrice") {
        emit("optionListRefresh", item.field);
      } else {
        //设置列表
        initOptionList(item, _data.value);
      }
    }
  });
};

// 校验实例对象
const ruleFormRef = ref<FormInstance>();

// validate
function validate(): Array<Promise<boolean>> {
  const list: Array<Promise<boolean>> = [];

  //默认表单校验
  if (ruleFormRef.value) {
    list.push(ruleFormRef.value.validate());
  }
  //执行自定义的校验方法（需要组件实现validate方法并defineExpose暴露）
  _.forEach(formItemRef.value, (formRef) => {
    if (formRef?.validate) {
      list.push(formRef.validate());
    }
  });
  return list;
}

onBeforeMount(() => {
  //console.log(_data.value);
  //console.log(props.formViewData);
  initForms();
});

onMounted(() => {
  //console.log("init!!!");
  //initForms();
});

// 暴露获取当前表单数据函数
defineExpose({
  validate,
  optionListRefresh,
  groupId: props.groupId,
});
</script>

<style lang="scss" scoped></style>
