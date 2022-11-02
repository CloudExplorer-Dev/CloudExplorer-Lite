<template>
  <div v-loading="_loading">
    <el-form-item
      :rules="{
        message: '计算资源类型' + '不能为空',
        trigger: 'blur',
        required: true,
      }"
      label="计算资源类型"
      prop="location"
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
      :rules="{
        message: label + '不能为空',
        trigger: 'blur',
        required: true,
      }"
      prop="mor"
    >
      <el-table
        ref="singleTableRef"
        :data="list"
        highlight-current-row
        style="width: 100%"
        @current-change="handleCurrentChange"
      >
        <el-table-column property="name" :label="label" />
        <el-table-column label="CPU使用量">
          <template #default="scope">
            <div class="usage-bar-top-text">
              <span
                >可用:
                {{
                  (scope.row.totalCpu - scope.row.usedCpu).toFixed(2)
                }}GHz</span
              >
            </div>
            <el-progress
              :color="customColors"
              :percentage="
                parseFloat(
                  ((scope.row.usedCpu / scope.row.totalCpu) * 100).toFixed(2)
                )
              "
              :stroke-width="26"
              :text-inside="true"
            />
            <div class="usage-bar-bottom-text">
              <span>已用: {{ scope.row.usedCpu }}GB</span>
              <span>总量: {{ scope.row.totalCpu }}GB</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="内存使用量"
          ><template #default="scope">
            <div class="usage-bar-top-text">
              <span
                >可用:{{
                  (scope.row.totalMemory - scope.row.usedMemory).toFixed(2)
                }}GB</span
              >
            </div>
            <el-progress
              :color="customColors"
              :percentage="
                parseFloat(
                  (
                    (scope.row.usedMemory / scope.row.totalMemory) *
                    100
                  ).toFixed(2)
                )
              "
              :stroke-width="26"
              :text-inside="true"
            />
            <div class="usage-bar-bottom-text">
              <span>已用: {{ scope.row.usedMemory }}GB</span>
              <span>总量: {{ scope.row.totalMemory }}GB</span>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-form-item>
  </div>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import formApi from "@commons/api/form_resource_api";
import { computed, onMounted, ref } from "vue";
import _ from "lodash";
import { ElTable } from "element-plus";

interface ComputeConfig {
  location: string;
  mor?: string;
}

interface HostOrResourcePool {
  mor: string;
  name: string;
  totalCpu: number;
  totalMemory: number;
  usedCpu: number;
  usedMemory: number;
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

/**
 * 列表选中
 */
const currentRow = computed<HostOrResourcePool | undefined>({
  get() {
    return _.find(
      list.value,
      (o: HostOrResourcePool) => o.mor === _data.value.mor
    );
  },
  set(value) {
    _data.value.mor = value?.mor;
  },
});

function handleCurrentChange(val: HostOrResourcePool | undefined) {
  currentRow.value = val;
}

/**
 * 自定义进度条颜色
 */
const customColors = [
  { color: "#37ff00", percentage: 20 },
  { color: "#049638", percentage: 40 },
  { color: "#efa400", percentage: 60 },
  { color: "#ff4400", percentage: 80 },
  { color: "#ff0000", percentage: 100 },
];

const _loading = ref<boolean>(false);

const computeTypes = ref([]);

const list = ref<Array<HostOrResourcePool>>([]);

const singleTableRef = ref<InstanceType<typeof ElTable>>();

/**
 * 主机资源类型变化
 * @param value
 */
function locationChange(value?: string) {
  //清空
  _data.value.mor = undefined;
  list.value = [];
  //重新获取主机/资源池列表
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
  let clazz = "";
  let method = "";
  if (_data.value.location === "host") {
    clazz = "com.fit2cloud.provider.impl.vsphere.VsphereCloudProvider";
    method = "getHosts";
  } else if (_data.value.location === "pool") {
    clazz = "com.fit2cloud.provider.impl.vsphere.VsphereCloudProvider";
    method = "geResourcePools";
  } else {
    list.value = [];
    _data.value.mor = undefined;
    return;
  }
  formApi
    .getResourceMethod(false, clazz, method, _temp, _loading)
    .then((ok) => {
      list.value = ok.data;
      if (currentRow.value === undefined) {
        _data.value.mor = undefined;
      } else {
        //设置界面默认选中
        singleTableRef.value?.setCurrentRow(currentRow.value);
      }
    });
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
<style lang="scss">
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
