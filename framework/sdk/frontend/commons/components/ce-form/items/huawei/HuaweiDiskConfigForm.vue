<template>
  <template v-if="!confirm">
    <template v-for="(obj, index) in data" :key="index">
      <div class="disk-title" v-if="index <= 1">
        {{ index === 0 ? "系统盘(GB)" : "数据盘(GB)" }}
      </div>
      <div class="disk-content" :style="{ 'margin-bottom': '10px' }">
        <el-form-item :prop="obj.diskType + ''">
          <el-select style="width: 100%" v-model="obj.diskType" filterable>
            <template v-slot:prefix>
              <span> 磁盘类型 </span>
            </template>
            <el-option
              v-for="item in diskTypeOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item :prop="obj.size + ''">
          <LineNumber
            special-step="10"
            v-model="obj.size"
            :min="index === 0 ? imageDiskMinSize : 10"
            :max="index === 0 ? 1024 : 32768"
            :step="10"
            :readonly="index === 0 ? obj.readonly : false"
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
    <detail-page
      :content="getModelValueDetail(modelValue)"
      :item-width="'33.33%'"
      :item-bottom="'28px'"
    ></detail-page>
  </template>
</template>
<script setup lang="ts">
import { computed, watch, onMounted, ref } from "vue";
import _ from "lodash";
import type { FormView } from "@commons/components/ce-form/type";
import { ElMessage } from "element-plus";

interface DiskTypeOption {
  id: string;
  name: string;
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
 * 盘类型下拉选
 */
const diskTypeOptions = computed<DiskTypeOption[]>(() => {
  if (props.formItem?.optionList) {
    return props.formItem.optionList;
  }
  return [];
});
/**
 * 系统盘最小值
 */
const imageDiskMinSize = computed(() => {
  return defaultDisks.value[0].size;
});
/**
 * 监听系统版本变化，获取镜像最小磁盘
 */
if (!props.confirm) {
  watch(
    () => props.allData.osVersion,
    (_data) => {
      const osVersion = _.find(
        _.find(props.allFormViewData, (formViewData) => {
          return formViewData.field === "osVersion";
        })?.optionList,
        (osConfig) => {
          return osConfig.imageId === _data;
        }
      );
      if (osVersion) {
        defaultDisks.value[0].size = osVersion.imageDiskMinSize;
      }
    }
  );
}

/**
 * 默认展示的盘:系统盘
 */
const defaultDisks = computed(() => [
  {
    diskType: "GPSSD",
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

function add() {
  if (diskTypeOptions.value.length > 0) {
    const dataDiskItem = diskTypeOptions.value[0];
    data.value?.push({
      size: 10,
      diskType: dataDiskItem.id,
      deleteWithInstance: false,
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
  if (data.value?.length == 0) {
    data.value = defaultDisks.value;
  }
});
function getModelValueDetail(modelValue: any) {
  const result: Array<any> = [];
  if (modelValue) {
    for (let i = 0; i < modelValue.length; i++) {
      result.push({
        label: i === 0 ? "系统盘" : "数据盘" + i,
        value:
          modelValue[i].size +
          "GB" +
          (modelValue[i].deleteWithInstance ? " (随实例删除)" : ""),
      });
    }
  }
  return result;
}
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
  margin-bottom: 28px;

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

.detail-box {
  margin-top: 8px;
  :deep(.el-descriptions__header) {
    margin-bottom: 0 !important;
  }
}
</style>
