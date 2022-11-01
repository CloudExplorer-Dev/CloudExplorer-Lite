<template v-bind="$attrs">
  <div style="display: flex; flex-direction: row; flex-wrap: wrap">
    <div v-for="(obj, index) in data" :key="index" class="vs-disk-config-card">
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
        </span>

        <el-input-number
          v-model="obj.size"
          :min="_.defaultTo(defaultDisks[index]?.size, 1)"
          :step="1"
          required
        />

        <el-button
          v-if="!defaultDisks[index]"
          class="remove-button"
          @click="remove(index)"
          :icon="CloseBold"
          type="info"
          text
        ></el-button>
      </el-card>

      <div style="width: 100%; height: 30px; text-align: center">
        <el-checkbox v-model="obj.deleteWithInstance">随实例删除</el-checkbox>
      </div>
    </div>
    <div class="vs-disk-config-card">
      <el-card class="card add-card">
        <el-button style="margin: auto" class="el-button--primary" @click="add"
          >添加磁盘</el-button
        >
      </el-card>
    </div>
  </div>
</template>
<script setup lang="ts">
const props = defineProps<{
  modelValue: any;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  formItem?: FormView;
}>();

const emit = defineEmits(["update:modelValue", "change"]);

import { computed, watch, onMounted } from "vue";
import _ from "lodash";
import type { FormView } from "@commons/components/ce-form/type";
import { CloseBold } from "@element-plus/icons-vue";

/**
 * 模版默认应该有的盘
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
      return { size: templateDisk.sizeGB, deleteWithInstance: true };
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
  data.value.push({ size: 1, deleteWithInstance: true });
}
function remove(index: number) {
  _.remove(data.value, (n, i) => index === i);
}

/**
 * 校验方法
 */
function validate(): Promise<boolean> {
  return new Promise((resolve, reject) => {
    if (data.value.length === 0) {
      return reject(false);
    }
    return _.every(data.value, (disk) => disk.size > 0)
      ? resolve(true)
      : reject(false);
  });
}

/**
 * 初始化数据
 */
onMounted(() => {
  if (props.modelValue == undefined) {
    emit("update:modelValue", defaultDisks.value);
  }
});

/**
 * 监听模版变化，获取值
 */
watch(
  () => props.allData.template,
  (data) => {
    emit("update:modelValue", defaultDisks.value);
  }
);

defineExpose({
  validate,
  field: props.field,
});
</script>
<style lang="scss">
.vs-disk-config-card {
  height: 130px;
  width: 200px;
  margin-right: 20px;
  margin-bottom: 20px;

  .card {
    height: 100px;
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
