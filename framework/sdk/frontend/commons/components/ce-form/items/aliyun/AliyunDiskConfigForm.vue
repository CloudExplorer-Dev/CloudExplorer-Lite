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
            {{ index === 0 ? "系统盘" : "数据盘 " + index }}
            <span
              style="font-size: smaller; color: var(--el-text-color-secondary)"
            >
              (GB)
            </span>
          </span>

          <div>
            <div style="padding-bottom: 5px">
              <span>磁盘类型：</span>
              <el-select
                style="width: 50%"
                v-model="obj.diskType"
                filterable
                v-if="index === 0"
              >
                <el-option
                  v-for="item in systemDiskTypeOptions"
                  :key="item.diskType"
                  :label="item.diskTypeName"
                  :value="item.diskType"
                />
              </el-select>

              <el-select
                style="width: 50%"
                v-model="obj.diskType"
                filterable
                v-else
              >
                <el-option
                  v-for="item in dataDiskTypeOptions"
                  :key="item.diskType"
                  :label="item.diskTypeName"
                  :value="item.diskType"
                />
              </el-select>
            </div>
            <div style="padding-bottom: 5px">
              <span>磁盘大小：</span>
              <el-input-number
                v-model="obj.size"
                :min="minSize(defaultDisks[index], index)"
                :max="maxSize(defaultDisks[index], index)"
                :step="1"
                required
                style="width: 50%"
                value-on-clear="min"
              />
            </div>
          </div>

          <el-button
            v-if="index > 0"
            class="remove-button"
            @click="remove(index)"
            :icon="CloseBold"
            type="info"
            text
          ></el-button>
        </el-card>

        <div style="width: 100%; height: 30px; text-align: center">
          <el-checkbox v-model="obj.deleteWithInstance" :disabled="obj.readonly"
            >随实例删除</el-checkbox
          >
        </div>
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
        :label="i === 0 ? '系统盘' : '数据盘' + i"
        v-for="(disk, i) in modelValue"
        :key="i"
      >
        {{ disk.size }}GB{{ disk.deleteWithInstance ? " (随实例删除)" : "" }}
      </el-descriptions-item>
    </el-descriptions>
  </template>
</template>
<script setup lang="ts">
import { computed, watch, onMounted, ref } from "vue";
import _ from "lodash";
import type { FormView } from "@commons/components/ce-form/type";
import { CloseBold } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";

interface DiskTypeConfig {
  size: number;
  diskType: string;
  deleteWithInstance: boolean;
  diskTypeName?: string;
  minDiskSize: number;
  maxDiskSize: number;
  readonly?: boolean;
}

interface DiskTypeOption {
  diskType: string;
  diskTypeName: string;
  minDiskSize: number;
  maxDiskSize: number;
}

const props = defineProps<{
  modelValue: any;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  formItem?: FormView;
  otherParams: any;
  confirm?: boolean;
}>();
const emit = defineEmits(["update:modelValue", "change"]);

/**
 * 系统盘类型下拉选
 */
const systemDiskTypeOptions = computed<DiskTypeOption[]>(() => {
  if (props.formItem?.optionList) {
    return props.formItem.optionList.systemDiskTypes;
  }
  return [];
});

/**
 * 数据盘类型下拉选
 */
const dataDiskTypeOptions = computed<DiskTypeOption[]>(() => {
  if (props.formItem?.optionList) {
    return props.formItem.optionList.dataDiskTypes;
  }
  return [];
});

const minSize = computed(() => (disk: DiskTypeConfig, index: number) => {
  let min = 20;
  if (disk && index != null) {
    // 系统盘
    if (index === 0) {
      systemDiskTypeOptions.value.forEach((diskTypeOption: DiskTypeOption) => {
        if (diskTypeOption.diskType === disk.diskType) {
          min = diskTypeOption.minDiskSize;
        }
        if (props.allData.os?.toLowerCase().indexOf("windows") > -1) {
          min = 50;
        }
        if (disk.size < min) {
          disk.size = min;
        }
      });
    } else {
      dataDiskTypeOptions.value.forEach((diskTypeOption: DiskTypeOption) => {
        if (diskTypeOption.diskType === disk.diskType) {
          min = diskTypeOption.minDiskSize;
        }
      });
    }
  }
  return min;
});

const maxSize = computed(() => (disk: DiskTypeConfig, index: number) => {
  const maxSize = ref(1024);
  if (disk && index != null) {
    // 系统盘
    if (index === 0) {
      systemDiskTypeOptions.value.forEach((diskTypeOption: DiskTypeOption) => {
        if (diskTypeOption.diskType === disk.diskType) {
          maxSize.value = diskTypeOption.maxDiskSize;
        }
      });
    } else {
      dataDiskTypeOptions.value.forEach((diskTypeOption: DiskTypeOption) => {
        if (diskTypeOption.diskType === disk.diskType) {
          maxSize.value = diskTypeOption.maxDiskSize;
        }
      });
    }
  }
  return maxSize.value;
});

/**
 * 默认展示的盘:系统盘
 */
const defaultDisks = computed(() => [
  {
    diskType: "cloud_essd",
    size: 40,
    deleteWithInstance: true,
    readonly: true,
  },
]);

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

/**
 * 监听数据盘可选值变化
 */
watch(
  () => dataDiskTypeOptions.value,
  () => {
    if (data.value?.length > 1) {
      for (let i = 1; i < data.value.length; i++) {
        if (
          dataDiskTypeOptions.value.every(
            (option) => option.diskType !== data.value[i].diskType
          )
        ) {
          data.value[i].diskType = dataDiskTypeOptions.value[0].diskType;
          data.value[i].size = dataDiskTypeOptions.value[0].minDiskSize;
        }
      }
    }
  },
  { deep: true }
);

/**
 * 监听系统盘可选值变化
 */
watch(
  () => systemDiskTypeOptions.value,
  () => {
    if (
      data.value?.length > 0 &&
      systemDiskTypeOptions.value.filter(
        (option: DiskTypeOption) => option.diskType === data.value[0].diskType
      ).length === 0
    ) {
      data.value[0].diskType = systemDiskTypeOptions.value[0].diskType;
      data.value[0].size = systemDiskTypeOptions.value[0].minDiskSize;
    }
  },
  { deep: true }
);

function add() {
  if (dataDiskTypeOptions.value.length > 0) {
    const dataDiskItem = dataDiskTypeOptions.value[0];
    data.value?.push({
      size: dataDiskItem.minDiskSize,
      diskType: dataDiskItem.diskType,
      deleteWithInstance: true,
      readonly: true,
    });
  } else {
    ElMessage.warning("可选择的数据盘类型为空");
  }
}

function remove(index: number) {
  _.remove(data.value, (n, i) => index === i);
}

/**
 * 校验方法
 */
function validate(): Promise<boolean> {
  return new Promise((resolve, reject) => {
    if (props.confirm || data.value.length === 0) {
      return reject(false);
    }
    return _.every(data.value, (disk) => disk.size > 0)
      ? resolve(true)
      : reject(false);
  });
}

defineExpose({
  validate,
  field: props.field,
});

const loading = ref<boolean>(false);
onMounted(() => {
  if (data.value.length == 0) {
    data.value = defaultDisks.value;
  }
});
</script>
<style lang="scss" scoped>
.vs-disk-config-card {
  height: 150px;
  width: 300px;
  margin-right: 20px;
  margin-bottom: 30px;
  .card {
    height: 150px;
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
