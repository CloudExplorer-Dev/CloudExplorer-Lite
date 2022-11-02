<template>
  <div v-loading="_loading">
    <el-form-item
      label="计算资源类型"
      prop="location"
      :rules="{
        message: '计算资源类型' + '不能为空',
        trigger: 'blur',
        required: true,
      }"
    >
      <el-radio-group
        v-model="_data.location"
        @change="locationChange(_data.location)"
      >
        <el-radio-button
          v-for="(item, index) in computeTypes"
          :key="index"
          :label="item['value']"
        >
          {{ item["name"] }}
        </el-radio-button>
      </el-radio-group>
    </el-form-item>

    <el-form-item
      v-if="_data.location === 'host' || _data.location === 'pool'"
      :label="label"
      prop="mor"
      :rules="{
        message: label + '不能为空',
        trigger: 'blur',
        required: true,
      }"
    >
      {{ list }}
    </el-form-item>
  </div>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import formApi from "@commons/api/form_resource_api";
import { computed, onMounted, ref } from "vue";
import _ from "lodash";

interface ComputeConfig {
  location: string;
  mor?: string;
}

const props = defineProps<{
  modelValue?: ComputeConfig;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  otherParams: any;
  formItem?: FormView;
}>();

const emit = defineEmits(["update:modelValue", "change"]);

const _data = computed({
  get() {
    return props.modelValue ? props.modelValue : { location: "host" };
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

const label = computed(() => {
  if (_data.value.location === "host") {
    return "主机";
  }
  if (_data.value.location === "pool") {
    return "资源池";
  }
  return null;
});

const _loading = ref<boolean>(false);

const computeTypes = ref([]);

const list = ref([]);

function locationChange(value?: string) {
  _data.value.mor = undefined;
  getList();
}

function getTempRequest() {
  return _.assignWith(
    {},
    { computeConfig: _data.value },
    props.otherParams,
    props.allData,
    (objValue, srcValue) => {
      return _.isUndefined(objValue) ? srcValue : objValue;
    }
  );
}

function getComputeTypes() {
  const _temp = getTempRequest();
  formApi
    .getResourceMethod(
      false,
      "com.fit2cloud.provider.impl.vsphere.VsphereCloudProvider",
      "getLocations",
      _temp,
      _loading
    )
    .then((ok) => {
      computeTypes.value = ok.data;
    });
}

function getList() {
  const _temp = getTempRequest();
  if (_data.value.location === "host") {
    formApi
      .getResourceMethod(
        false,
        "com.fit2cloud.provider.impl.vsphere.VsphereCloudProvider",
        "getHosts",
        _temp,
        _loading
      )
      .then((ok) => {
        list.value = ok.data;
      });
  } else if (_data.value.location === "pool") {
    formApi
      .getResourceMethod(
        false,
        "com.fit2cloud.provider.impl.vsphere.VsphereCloudProvider",
        "geResourcePools",
        _temp,
        _loading
      )
      .then((ok) => {
        list.value = ok.data;
      });
  } else {
    list.value = [];
  }
}

onMounted(() => {
  console.log(props.modelValue);
  if (props.modelValue == undefined) {
    //emit("update:modelValue", { location: "host" });
    _data.value = { location: "host" };
  }
  getComputeTypes();
  getList();
});
</script>
<style lang="scss"></style>
