<template>
  <template v-if="!confirm">
    <template v-for="(obj, index) in data" :key="index">
      <div class="disk-title" v-if="index <= 1">
        {{ index === 0 ? "系统盘" : "数据盘" }}(GB)
      </div>
      <div class="disk-content">
        <el-form-item :prop="obj.diskType + ''">
          <el-select
            style="width: 100%"
            v-model="obj.diskType"
            filterable
            v-if="index === 0"
          >
            <template v-slot:prefix>
              <span> 磁盘类型 </span>
            </template>
            <el-option
              v-for="item in systemDiskTypeOptions"
              :key="item.diskType"
              :label="item.diskTypeName"
              :value="item.diskType"
            />
          </el-select>
          <el-select
            style="width: 100%"
            v-model="obj.diskType"
            filterable
            v-else
          >
            <template v-slot:prefix>
              <span> 磁盘类型 </span>
            </template>
            <el-option
              v-for="item in dataDiskTypeOptions"
              :key="item.diskType"
              :label="item.diskTypeName"
              :value="item.diskType"
            />
          </el-select>
        </el-form-item>

        <el-form-item :prop="obj.size + ''">
          <LineNumber
            special-step="10"
            v-model="obj.size"
            :min="minSize(defaultDisks[index], index)"
            :max="maxSize(defaultDisks[index], index)"
            :step="10"
            :readonly="obj.readonly"
            required
            style="width: 200px; margin-left: 8px"
          >
            <template #perfix>
              <div>磁盘大小</div>
            </template>
          </LineNumber>
        </el-form-item>
        <el-form-item :prop="obj.deleteWithInstance + ''">
          <!--          <div style="width: 100%; margin-left: 8px; text-align: center">-->
          <!--            <el-switch-->
          <!--              v-model="obj.deleteWithInstance"-->
          <!--              :disabled="obj.readonly"-->
          <!--              active-text="随实例删除"-->
          <!--            />-->
          <!--          </div>-->
        </el-form-item>
        <el-icon
          v-if="index > 0"
          size="17px"
          color="#646A73"
          class="delete-icon"
          @click="remove(index)"
        >
          <Delete />
        </el-icon>
      </div>
    </template>
    <span class="add-btn" @click="add">+ 添加数据盘</span>
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
const systemDiskTypeOptions = computed(() => {
  if (props.formItem?.optionList) {
    return props.formItem.optionList.systemDiskTypes;
  }
  return [];
});

/**
 * 数据盘类型下拉选
 */
const dataDiskTypeOptions = computed(() => {
  if (props.formItem?.optionList) {
    return props.formItem.optionList.dataDiskTypes;
  }
  return [];
});

/**
 * 默认展示的盘:系统盘
 */
const defaultDisks = computed(() => {
  return [
    {
      diskType: "CLOUD_PREMIUM",
      size: 50,
      deleteWithInstance: true,
      readonly: true,
    },
  ];
});

const minSize = computed(() => (disk: DiskTypeConfig, index: number) => {
  let min = 20;
  if (disk && index != null) {
    // 系统盘
    if (index === 0) {
      systemDiskTypeOptions.value.forEach((diskTypeOption: DiskTypeConfig) => {
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
      dataDiskTypeOptions.value.forEach((diskTypeOption: DiskTypeConfig) => {
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
      systemDiskTypeOptions.value.forEach((diskTypeOption: DiskTypeConfig) => {
        if (diskTypeOption.diskType === disk.diskType) {
          maxSize.value = diskTypeOption.maxDiskSize;
        }
      });
    } else {
      dataDiskTypeOptions.value.forEach((diskTypeOption: DiskTypeConfig) => {
        if (diskTypeOption.diskType === disk.diskType) {
          maxSize.value = diskTypeOption.maxDiskSize;
        }
      });
    }
  }
  return maxSize.value;
});

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
  if (dataDiskTypeOptions.value.length > 0) {
    const dataDiskItem = dataDiskTypeOptions.value[0];
    data.value?.push({
      size: dataDiskItem.minDiskSize,
      diskType: dataDiskItem.diskType,
      deleteWithInstance: true,
      readonly: false,
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

onMounted(() => {
  if (data.value.length == 0) {
    data.value = defaultDisks.value;
  }
});
</script>
<style lang="scss" scoped>
.disk-title {
  font-style: normal;
  font-weight: 500;
  font-size: 14px;
  line-height: 22px;
  color: #1f2329;
  margin-top: 16px;
}
.disk-title:after {
  content: "*";
  color: var(--el-color-danger);
  margin-left: 4px;
}
.disk-title:first-child {
  margin-top: 16px;
}
.margin-top {
  margin-top: 32px;
}
.el-form-item {
  margin-bottom: 28px;
}

.disk-content {
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  padding: 12px;
  background-color: #f7f9fc;
  border-radius: 4px;
  margin-bottom: 10px;

  .el-form-item {
    margin-bottom: 0;
  }

  .delete-icon {
    cursor: pointer;
    margin-left: 8px;
  }
}

.add-btn {
  cursor: pointer;
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 32px;

  color: #3370ff;
}
</style>
