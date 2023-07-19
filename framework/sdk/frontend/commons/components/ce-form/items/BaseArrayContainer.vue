<template>
  <div class="base-array-content">
    <div v-for="(adapter, i) in data" :key="i" class="card-content">
      <template v-if="formItem.optionList">
        <div class="title-content">
          {{ "网卡" + (i + 1) }}

          <el-icon
            style="cursor: pointer"
            v-if="data.length > 1 || i > 0"
            @click="removeCard(data, i)"
          >
            <CloseBold />
          </el-icon>
        </div>
        <div class="value-content">
          <CeForm
            :read-only="readOnly"
            ref="ceFormRef"
            label-position="top"
            require-asterisk-position="right"
            v-model="data[i]"
            :other-params="otherParams"
            :formViewData="formItem.optionList ? formItem.optionList : []"
            v-bind="attr"
          ></CeForm>
        </div>
      </template>
    </div>
    <div
      class="add-card"
      style="
        color: var(--el-disabled-text-color);
        border: 1px dashed var(--el-disabled-text-color);
      "
      v-if="readOnly"
    >
      + {{ addLabel }}
    </div>
    <div class="add-card" v-else @click="addCard()">+ {{ addLabel }}</div>
  </div>
</template>
<script setup lang="ts">
import CeForm from "@commons/components/ce-form/index.vue";
import type { FormView } from "@commons/components/ce-form/type";
import type { SimpleMap } from "@commons/api/base/type";
import { computed, ref } from "vue";
import _ from "lodash";
const props = withDefaults(
  defineProps<{
    // 调用接口所需要的其他参数
    otherParams: any;
    // 是否只读
    readOnly?: boolean;
    // 收集到的数据
    modelValue?: Array<SimpleMap<any>>;
    /**
     * 表单Item
     */
    formItem: FormView;
    // 默认每个宽度
    defaultItemWidth?: string;
  }>(),
  { readOnly: false, defaultItemWidth: "75%" }
);
const emit = defineEmits(["update:modelValue", "change"]);
const ceFormRef = ref<Array<InstanceType<typeof CeForm>>>([]);
const data = computed({
  get: () => {
    if (props.modelValue) {
      return props.modelValue;
    }
    return [];
  },
  set: ($event) => {
    emit("update:modelValue", $event);
  },
});

const addLabel = computed(() => {
  return props.formItem.propsInfo.addLabel
    ? props.formItem.propsInfo.addLabel
    : "添加";
});

const addCard = () => {
  data.value.push({});
};

const attr = computed(() => {
  if (props.formItem.attrs) {
    return { ...JSON.parse(props.formItem.attrs) };
  }
  return {};
});
function removeCard(list: Array<SimpleMap<any>>, index: number) {
  _.remove(list, (o, i) => index === i);
}
const validate = () => {
  return Promise.all(ceFormRef.value.map((item) => item.validate()));
};
defineExpose({ validate });
</script>
<style lang="scss" scoped>
.base-array-content {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  align-items: stretch;
  justify-content: flex-start;
  margin-bottom: 18px;

  .dns-input {
    width: 75%;
  }

  .card-content {
    min-height: 240px;
    width: 334px;
    margin: 10px;
    background: #fff;
    border: 1px solid #ffffff;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.04);
    border-radius: 4px;

    .title-content {
      font-size: 14px;
      font-weight: bold;
      line-height: 22px;
      vertical-align: center;
      height: 22px;
      background: #f5f8ff;
      color: #3370ff;
      padding: 12px;
      display: flex;
      flex-direction: row;
      flex-wrap: nowrap;
      justify-content: space-between;
      align-items: center;
    }
    .value-content {
      margin: 18px;
    }
  }

  .add-card {
    min-height: 240px;
    width: 334px;
    margin: 10px;
    padding: 0;
    background: #ffffff;
    border: 1px dashed #3370ff;
    border-radius: 4px;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
    color: #3370ff;
    cursor: pointer;
  }
}
</style>
