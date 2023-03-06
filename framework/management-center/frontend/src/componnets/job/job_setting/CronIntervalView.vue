<template>
  <el-form
    :style="{
      color: readOnly
        ? 'var(--el-disabled-text-color)'
        : 'var(--el-checkbox-checked-text-color)',
    }"
    ref="ruleFormRef"
    :model="cronFrom"
    label-width="0px"
    class="wapper"
    @click.stop.prevent
  >
    <div style="height: 100%; display: flex; align-items: center">
      间隔:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </div>
    <el-form-item
      :rules="{
        message: '请输入时间间隔',
        trigger: 'bulr',
        required: true,
        type: 'number',
      }"
    >
      <el-input-number :disabled="readOnly" v-model="cronFrom.intervalF" />
    </el-form-item>
    <el-form-item
      :rules="{
        message: '请选择时间',
        trigger: 'change',
        required: true,
        type: 'array',
        min: 1,
      }"
    >
      <el-select
        :disabled="readOnly"
        @click.stop.prevent
        class="item"
        style="width: 100px"
        v-model="cronFrom.unitF"
        placeholder="Select"
      >
        <el-option
          v-for="item in usableUnitList"
          :key="item.value"
          :label="item.key"
          :value="item.value"
        />
      </el-select>
    </el-form-item>
    <div style="flex: auto"></div>
    <el-icon
      :style="{
        cursor: readOnly ? 'not-allowed' : 'pointer',
      }"
      style="height: 100%"
      @click="switchJobType"
      ><Switch
    /></el-icon>
  </el-form>
</template>
<script setup lang="ts">
import { onMounted, ref, watch, computed } from "vue";
import type { FormInstance } from "element-plus";
import type { KeyValue } from "@commons/api/base/type";
const props = withDefaults(
  defineProps<{
    /**
     *间隔
     */
    interval?: number;
    /**
     * 单位
     */
    unit?: "MINUTE" | "HOUR" | "DAY" | "WEEK" | "MONTH";
    /**
     * 定时任务类型
     */
    jobType: string;
    /**
     * 支持的单位
     */
    usableUnits?: Array<"MINUTE" | "HOUR" | "DAY" | "WEEK" | "MONTH">;

    /**
     * 是否可读
     */
    readOnly: boolean;
  }>(),
  {
    usableUnits: () => ["MINUTE", "HOUR", "DAY", "WEEK", "MONTH"],
  }
);

const unitList: Array<KeyValue<string, string>> = [
  { key: "分钟", value: "MINUTE" },
  { key: "小时", value: "HOUR" },
  { key: "日", value: "DAY" },
  { key: "周", value: "WEEK" },
  { key: "月", value: "MONTH" },
];
const cronFrom = ref<{
  unitF: "MINUTE" | "HOUR" | "DAY" | "WEEK" | "MONTH";
  intervalF: number;
}>({ unitF: "MINUTE", intervalF: 30 });

const usableUnitList = computed(() => {
  return unitList.filter((a) =>
    props.usableUnits.some((unit) => unit === a.value)
  );
});

// 获取默认值
const init = () => {
  if (props.interval) {
    cronFrom.value.intervalF = props.interval;
    cronFrom.value.unitF = props.unit ? props.unit : "MINUTE";
  } else {
    emit("update:unit", "MINUTE");
    emit("update:interval", 30);
  }
};

const switchJobType = () => {
  if (!props.readOnly) {
    emit("update:jobType", "CRON");
  }
};

watch(
  cronFrom,
  () => {
    if (cronFrom.value.unitF) {
      emit("update:unit", cronFrom.value.unitF);
    }
    if (cronFrom.value.intervalF) {
      emit("update:interval", cronFrom.value.intervalF);
    }
  },
  { deep: true }
);
// 字段绑定
const emit = defineEmits(["update:interval", "update:jobType", "update:unit"]);
/**
 * 校验实例对象
 */
const ruleFormRef = ref<FormInstance>();

// 监听同步类型变化,重置值
watch(
  () => props.unit,
  () => {
    cronFrom.value.intervalF = 1;
    emit("update:interval", 1);
  }
);

// 校验
const validate = () => {
  return ruleFormRef.value?.validate();
};
defineExpose({ validate });
onMounted(() => {
  init();
});
</script>
<style lang="scss" scoped>
.wapper {
  width: 100%;
  height: 42px;
  background: #edf2f5;
  padding: 5px 10px;
  display: flex;
  box-sizing: border-box;
  cursor: default;
  border-radius: 5px;
  .item {
    margin-left: 10px;
  }
}
</style>
