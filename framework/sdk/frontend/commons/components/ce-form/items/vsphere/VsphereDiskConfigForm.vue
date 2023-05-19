<template>
  <template v-if="!confirm">
    <div style="display: flex; flex-direction: row; flex-wrap: wrap">
      <div v-for="(obj, index) in data" :key="index" class="row">
        <el-form-item
          :data-var="(_minValue = _.defaultTo(defaultDisks[index]?.size, 1))"
          :prop="`disks[${index}].size`"
          :rules="[
            {
              message: '磁盘大小必填',
              trigger: 'blur',
              required: true,
            },
            {
              message: `磁盘大小最小值为${_minValue}`,
              trigger: ['change', 'blur'],
              type: 'number',
              min: _minValue,
            },
          ]"
        >
          <template #label v-if="index <= 1">
            {{ index === 0 ? "系统盘" : "数据盘 " }}
            <span
              style="font-size: smaller; color: var(--el-text-color-secondary)"
            >
              (GB)
            </span>
          </template>
          <div class="disk-row">
            <LineNumber
              special-step="10"
              v-model="obj.size"
              :min="_minValue"
              :step="1"
              :readonly="obj.readonly"
              required
              style="width: 200px"
            >
              <template #perfix>
                <div>磁盘大小</div>
              </template>
            </LineNumber>

            <CeIcon
              style="cursor: pointer; margin-left: 9px"
              size="16px"
              code="icon_delete-trash_outlined1"
              v-if="!defaultDisks[index]"
              @click="remove(index)"
              color="#6E748E"
            />
          </div>
        </el-form-item>
      </div>

      <el-button link type="primary" @click="add">+ 添加磁盘 </el-button>
    </div>
  </template>
  <template v-else>
    <el-descriptions :column="3" direction="vertical">
      <el-descriptions-item
        :label="i === 0 ? '系统盘' : '数据盘' + i"
        v-for="(disk, i) in modelValue"
        :key="i"
        width="33.33%"
      >
        {{ disk.size }}GB
      </el-descriptions-item>
      <el-descriptions-item
        width="33.33%"
        v-for="x in modelValue.length % 3"
        :key="x"
      />
    </el-descriptions>
  </template>
</template>
<script setup lang="ts">
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

import { computed, watch } from "vue";
import _ from "lodash";
import type { FormView } from "@commons/components/ce-form/type";
import LineNumber from "@commons/components/ce-form/items/LineNumber.vue";
import CeIcon from "@commons/components/ce-icon/index.vue";

/**
 * 模板默认应该有的盘
 */
const templateDisks = computed(() => {
  const templateFormView = _.find(props.allFormViewData, (formViewData) => {
    return formViewData.field === "template";
  });

  const option = _.find(templateFormView?.optionList, (obj) => {
    return obj.imageId === props.allData.template;
  });
  return _.map(
    option?.diskInfos ? JSON.parse(option.diskInfos) : undefined,
    (disk) => {
      return {
        key: disk.key,
        label: disk.deviceInfo?.label,
        capacityInKB: disk.capacityInKB,
        sizeGB: disk.capacityInKB / 1024 / 1024,
      };
    }
  );
});
/**
 * 默认展示的盘
 */
const defaultDisks = computed(() => {
  return _.defaultTo(
    _.map(templateDisks.value, (templateDisk) => {
      return {
        size: templateDisk.sizeGB,
        deleteWithInstance: true,
        readonly: true,
      };
    }),
    []
  );
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
  data.value?.push({ size: 20, deleteWithInstance: true });
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

/**
 * 监听模板变化，获取值
 */
if (!props.confirm) {
  watch(
    () => props.allData.template,
    (_data) => {
      //emit("update:modelValue", defaultDisks.value);
      data.value = defaultDisks.value;
    }
  );
}

defineExpose({
  validate,
  field: props.field,
});
</script>
<style lang="scss" scoped>
:deep(.el-form-item--default) {
  margin-bottom: 0;
}

.row {
  width: 100%;
}

.disk-row {
  width: 100%;
  background: #f7f9fc;
  border-radius: 4px;
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  height: 46px;
  padding-left: 12px;
  padding-right: 12px;
  margin-bottom: 6px;
}
</style>
