<template v-loading="_loading">
  <div style="width: 100%">
    <el-tabs v-model="activeTab" type="card">
      <el-tab-pane
        v-for="(item, index) in _data"
        :key="index"
        :label="'主机' + (index + 1)"
        :name="index"
      >
        <template v-if="formItem.optionList">
          <CeForm
            :read-only="confirm"
            label-position="top"
            require-asterisk-position="right"
            ref="ceFormRef"
            v-model="_data[index]"
            :other-params="other"
            :formViewData="formItem.optionList"
            v-bind="attr"
          ></CeForm>
        </template>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import _ from "lodash";
import type { FormView } from "@commons/components/ce-form/type";
import CeForm from "@commons/components/ce-form/index.vue";

const props = defineProps<{
  modelValue?: Array<any>;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  otherParams: any;
  formItem: FormView;
  confirm?: boolean;
}>();

const emit = defineEmits(["update:modelValue", "change"]);

// 校验实例对象
const ceFormRef = ref<Array<InstanceType<typeof CeForm>>>([]);
const _data = computed({
  get() {
    if (props.modelValue) {
      return props.modelValue;
    }
    return [{}];
  },
  set(value) {
    emit("update:modelValue", value);
  },
});
const attr = computed(() => {
  if (props.formItem.attrs) {
    return { ...JSON.parse(props.formItem.attrs) };
  }
  return {};
});
const activeTab = ref(0);
const c = computed(() => {
  if (props.allData && props.allData.count) {
    return props.allData.count;
  }
  return 1;
});
/**
 * 监听数量变化，获取值
 */
watch(c, () => {
  if (c.value) {
    setServers(c.value);
  }
});

function setServers(count: number | undefined) {
  if (count !== undefined) {
    if (_data.value) {
      if (_data.value.length > count) {
        _data.value = _.slice(_data.value, 0, count - _data.value.length);
      } else if (_data.value.length < count) {
        _data.value = [..._data.value, {}];
      }
    }
  }
}

/**
 * 校验方法
 */
function validate() {
  return Promise.all(ceFormRef.value.map((item) => item.validate()));
}
const other = computed(() => {
  return { ...(props.allData ? props.allData : {}), ...props.otherParams };
});

onMounted(() => {
  setServers(c.value);
});

defineExpose({
  validate,
  field: props.field,
});
</script>
<style lang="scss" scoped>
.add-button {
  margin: 10px;
}
.network-confirm-view {
  .card-container {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    align-items: stretch;
    justify-content: flex-start;
    margin-bottom: 18px;

    .card {
      width: 300px;
      margin-right: 20px;
    }
  }
}
</style>
