<template>
  <template v-if="!confirm">
    <div style="display: flex; flex-direction: row; flex-wrap: wrap">
      <div
        v-for="(obj, index) in data"
        :key="index"
        class="vs-disk-config-card"
      >
        <el-card
          class="card"
          :body-style="{
            padding: 0,
            'text-align': 'center',
            display: 'flex',
            'flex-direction': 'column',
            'flex-wrap': 'nowrap',
            'align-items': 'center',
            'justify-content': 'space-evenly',
            height: '100%',
            position: 'relative',
          }"
        >
          <span class="title">
            {{ "磁盘 " + (index + 1) }}
            <span
              v-if="obj.boot"
              style="font-size: small; color: var(--el-text-color-secondary)"
            >
              系统盘
            </span>
            <span
              style="font-size: smaller; color: var(--el-text-color-secondary)"
            >
              (GB)
            </span>
          </span>

          <el-input-number
            v-model="obj.size"
            :min="obj.minSize"
            :step="1"
            required
          />

          <el-select
            class="m-2"
            filterable
            v-model="obj.volumeType"
            placeholder="磁盘类型"
            required
            style="width: 126px"
          >
            <el-option
              v-for="(item, index) in formItem?.ext?.volumeType?.optionList"
              :key="index"
              :label="item.name"
              :value="item.name"
            />
          </el-select>

          <el-button
            v-if="!obj.boot"
            class="remove-button"
            @click="remove(index)"
            :icon="CloseBold"
            type="info"
            text
          ></el-button>
        </el-card>

        <!--        <div style="width: 100%; height: 30px; text-align: center">
          <el-checkbox v-model="obj.deleteWithInstance" v-if="obj.boot"
            >随实例删除</el-checkbox
          >
        </div>-->
      </div>
      <div class="vs-disk-config-card">
        <el-card class="card add-card">
          <el-button
            style="margin: auto"
            class="el-button--primary"
            @click="add"
            >添加磁盘</el-button
          >
        </el-card>
      </div>
    </div>
  </template>
  <template v-else>
    <el-descriptions>
      <el-descriptions-item
        :label="'磁盘' + (i + 1)"
        v-for="(disk, i) in modelValue"
        :key="i"
      >
        {{ disk.size }}GB{{ disk.deleteWithInstance ? " (随实例删除)" : "" }}
      </el-descriptions-item>
    </el-descriptions>
  </template>
</template>
<script setup lang="ts">
import formApi from "@commons/api/form_resource_api";

const props = defineProps<{
  modelValue: any;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  formItem: FormView;
  otherParams: any;
  confirm?: boolean;
}>();

const emit = defineEmits(["update:modelValue", "change"]);

import { computed, onMounted, ref, watch } from "vue";
import _ from "lodash";
import type { FormView } from "@commons/components/ce-form/type";
import { CloseBold } from "@element-plus/icons-vue";

interface Disk {
  minSize: number;
  size: number;
  deleteWithInstance: boolean;
  boot: boolean;
  volumeType?: string;
}

const _loading = ref<boolean>(false);

/**
 * 默认系统盘
 */
function getDefaultSystemDisk(): Disk {
  const templateFormView = _.find(props.allFormViewData, (formViewData) => {
    return formViewData.field === "flavorId";
  });

  const option = _.find(templateFormView?.optionList, (obj) => {
    return obj.id === props.allData.flavorId;
  });
  return {
    minSize: option.disk,
    size: option.disk,
    deleteWithInstance: false,
    boot: true,
    volumeType: undefined,
  };
}

/**
 * 双向绑定更新到外面
 */
const data = computed<Array<any>>({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});
/**
 * 触发change事件
 */
watch(
  () => data.value,
  (data) => {
    emit("change");
  },
  { deep: true }
);

function add() {
  data.value?.push({
    minSize: 1,
    size: 1,
    deleteWithInstance: false,
    boot: false,
  });
}
function remove(index: number) {
  _.remove(data.value, (n, i) => index === i);
}

/**
 * 校验方法
 */
function validate(): Promise<boolean> {
  return new Promise((resolve, reject) => {
    if (props.allData.bootFormVolume && data.value.length === 0) {
      return reject(false);
    }
    if (!props.allData.bootFormVolume && data.value.length === 0) {
      return resolve(true);
    }
    return _.every(data.value, (disk) => disk.size > 0 && disk.volumeType)
      ? resolve(true)
      : reject(false);
  });
}

function setDisks() {
  const sysDisk = _.find(data.value, (d) => d.boot);
  const defaultSysDisk = getDefaultSystemDisk();
  const list = _.filter(data.value, (d) => !d.boot);
  if (props.allData.bootFormVolume) {
    if (sysDisk != undefined) {
      defaultSysDisk.deleteWithInstance = sysDisk.deleteWithInstance;
      defaultSysDisk.volumeType = sysDisk.volumeType;
    }
    list.unshift(defaultSysDisk);
  }
  data.value = list;
}

/**
 * 监听模版变化，获取值
 */
if (!props.confirm) {
  watch(
    () => props.allData.bootFormVolume,
    (_data) => {
      setDisks();
    }
  );
  watch(
    () => props.allData.flavorId,
    (_data) => {
      setDisks();
    }
  );
}

function getTempRequest() {
  return _.assignWith(
    {},
    props.otherParams,
    props.allData,
    (objValue, srcValue) => {
      return _.isUndefined(objValue) ? srcValue : objValue;
    }
  );
}

function getVolumeTypes() {
  const _temp = getTempRequest();
  formApi
    .getResourceMethod(
      false,
      "com.fit2cloud.provider.impl.openstack.OpenStackCloudProvider",
      "listVolumeType",
      _temp,
      _loading
    )
    .then((ok) => {
      _.set(props.formItem, "ext.volumeType.optionList", ok.data);
    });
}

onMounted(() => {
  if (!props.confirm) {
    getVolumeTypes();
  }
});

defineExpose({
  validate,
  field: props.field,
});
</script>
<style lang="scss" scoped>
.vs-disk-config-card {
  height: 190px;
  width: 200px;
  margin-right: 20px;
  margin-bottom: 20px;

  .card {
    height: 160px;
    .title {
      font-size: 14px;
      font-weight: bold;
    }
    .remove-button {
      position: absolute;
      top: 0;
      right: 0;
    }
  }
  .add-card {
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>
