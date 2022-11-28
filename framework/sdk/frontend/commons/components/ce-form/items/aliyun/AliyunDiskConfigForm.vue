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
              <el-select style="width: 50%" v-model="obj.diskType" filterable>
                <el-option
                    v-for="item in diskTypeOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                />
              </el-select>
            </div>
            <div style="padding-bottom: 5px">
              <span>磁盘大小：</span>
              <el-input-number
                v-model="obj.size"
                :min="_.defaultTo(defaultDisks[index]?.size, 20)"
                :step="1"
                required
                style="width: 50%"
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
        :label="i === 0 ? '系统盘' : '数据盘' + (i + 1)"
        v-for="(disk, i) in modelValue"
        :key="i"
      >
        {{ disk.size }}GB{{ disk.deleteWithInstance ? " (随实例删除)" : "" }}
      </el-descriptions-item>
    </el-descriptions>
  </template>
</template>
<script setup lang="ts">
import {computed, watch, onMounted, ref} from "vue";
import _ from "lodash";
import type { FormView } from "@commons/components/ce-form/type";
import { CloseBold } from "@element-plus/icons-vue";

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
 * 磁盘类型下拉选
 */
const diskTypeOptions = computed(() => {
  if (props.formItem?.optionList) {
    return props.formItem.optionList;
  }
  return [];
});

/**
 * 默认展示的盘:系统盘
 */
const defaultDisks = computed(() => {
  return [
    {
      diskType:'cloud_essd',
      size: 40,
      deleteWithInstance: true,
      readonly: false,
    },
  ];
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
  data.value?.push({ size: 20, deleteWithInstance: true, readonly: false });
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

const loading = ref<boolean>(false);
onMounted(() => {
  data.value = defaultDisks.value;
});

defineExpose({
  validate,
  field: props.field,
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
