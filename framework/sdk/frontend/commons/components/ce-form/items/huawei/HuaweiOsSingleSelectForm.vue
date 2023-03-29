<template>
  <template v-if="!confirm">
    <el-form
      ref="ruleFormRef"
      label-suffix=":"
      label-position="left"
      :model="_data"
      size="small"
      v-loading="_loading"
    >
      <el-form-item style="width: 100%">
        <el-select
          class="m-2"
          filterable
          v-model="selectOsVersion"
          @change="selectChange"
        >
          <el-option
            v-for="item in optionList"
            :key="item.osVersion"
            :label="item.imageName"
            :value="item.imageId"
          >
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
  </template>
  <template v-else>
    {{ modelValue?.osVersion }}
  </template>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import formApi from "@commons/api/form_resource_api";
import { computed, onMounted, ref, watch } from "vue";
import _ from "lodash";
import type { FormInstance } from "element-plus";

const props = defineProps<{
  modelValue?: OsConfig;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  otherParams: any;
  formItem: FormView;
  confirm?: boolean;
}>();

interface OsConfig {
  os: string;
  osVersion: string;
  imageId: string;
  imageName: string;
  imageMinDiskSize: number;
}

const selectOsVersion = ref<string>();

const optionList = computed(() => {
  if (props.formItem.optionList) {
    return props.formItem.optionList;
  }
  return [];
});

const emit = defineEmits(["update:modelValue", "change"]);

const _data = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

const selectChange = () => {
  _data.value = _.find(
    props.formItem?.optionList,
    (o: OsConfig) => o.imageId === selectOsVersion.value
  );
};

// 校验实例对象
const ruleFormRef = ref<FormInstance>();

const _loading = ref<boolean>(false);

function getTempRequest() {
  return _.assignWith(
    {},
    {},
    props.otherParams,
    props.allData,
    (objValue, srcValue) => {
      return _.isUndefined(objValue) ? srcValue : objValue;
    }
  );
}

function getOsConfigList() {
  const _temp = getTempRequest();
  const clazz = "com.fit2cloud.provider.impl.huawei.HuaweiCloudProvider";
  const method = "listOsVersion";
  formApi
    .getResourceMethod(false, clazz, method, _temp, _loading)
    .then((ok) => {
      if (_data.value) {
        if (!_.find(ok.data, ["imageName", _data.value?.imageName])) {
          selectOsVersion.value = "";
          _data.value = undefined;
          _.set(props.formItem, "optionList", []);
        } else {
          selectOsVersion.value = _data.value?.imageId;
        }
      }
      _.set(props.formItem, "optionList", ok.data);
    });
}

onMounted(() => {
  getOsConfigList();
});
watch(
  () => props.allData.availabilityZone,
  (n, o) => {
    getOsConfigList();
  }
);
watch(
  () => props.allData.instanceSpecConfig,
  (n, o) => {
    selectOsVersion.value = "";
    _data.value = undefined;
    _.set(props.formItem, "optionList", []);
    getOsConfigList();
  }
);
</script>
<style lang="scss" scoped>
.usage-bar-top-text {
  display: flex;
  flex-direction: row-reverse;
  justify-content: space-between;
  font-size: smaller;
  font-weight: bold;
}

.usage-bar-bottom-text {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  font-size: smaller;
}
</style>
