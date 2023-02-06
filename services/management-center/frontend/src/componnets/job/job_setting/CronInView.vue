<template>
  <el-form
    ref="ruleFormRef"
    :model="cronFrom"
    label-width="0px"
    class="wapper"
    @click.stop.prevent
  >
    <el-form-item>
      <el-select
        @click.stop.prevent
        class="item"
        style="width: 100px"
        v-model="selectCronType"
        placeholder="Select"
      >
        <el-option
          v-for="item in usableCronTypes"
          :key="item.value"
          :label="item.key"
          :value="item.value"
        />
      </el-select>
    </el-form-item>

    <template v-if="selectCronType !== undefined">
      <template v-for="c in cronTypes[selectCronType].children" :key="c">
        <el-form-item
          :rules="{
            message: '请选择时间',
            trigger: 'blur',
            required: true,
          }"
          :prop="`composeCrons[${c}]`"
          class="item"
          v-if="c === minuteAndSecond"
        >
          <el-time-picker
            :disabled-hours="disabledHours"
            v-if="c === minuteAndSecond"
            v-model="cronFrom.composeCrons[c]"
            format="mm分ss秒"
            value-format="mm:ss"
            placeholder="请选择时间"
          />
        </el-form-item>
        <el-form-item
          :rules="{
            message: '请选择时间',
            trigger: 'blur',
            required: true,
          }"
          :prop="`composeCrons[${c}]`"
          class="item"
          v-else-if="c === hoursAndMinuteAndSecond || c === hoursAndMinute"
        >
          <el-time-picker
            class="item"
            v-model="cronFrom.composeCrons[c]"
            format="HH时mm分ss秒"
            value-format="HH:mm:ss"
            placeholder="请选择时间"
        /></el-form-item>

        <el-form-item
          :rules="{
            message: '请选择时间',
            trigger: 'change',
            required: true,
            type: 'array',
            min: 1,
          }"
          v-else
          :prop="`crons[${c}]`"
          class="item"
        >
          <el-select
            class="item"
            @click.stop.prevent
            style="width: 150px"
            collapse-tags
            v-model="cronFrom.crons[c]"
            :multiple="true"
            collapse-tags-tooltip
            placeholder="请选择时间"
          >
            <el-option
              v-for="item in cronTypes[c].selects()"
              :key="item.value"
              :label="item.key"
              :value="item.value"
            /> </el-select
        ></el-form-item>
      </template>
    </template>
  </el-form>
</template>
<script setup lang="ts">
import { onMounted, ref, watch, computed } from "vue";
import {
  SimpleCron,
  cronTypes,
  minuteAndSecond,
  hoursAndMinuteAndSecond,
  hoursAndMinute,
  region,
} from "@commons/utils/cron";
import type { FormInstance } from "element-plus";
const props = withDefaults(
  defineProps<{
    modelValue: string;
    // 指定可定是同步类型 2:按小时 3:按天 4:按月 5:按周
    usableCronType: Array<2 | 3 | 4 | 5>;
  }>(),
  {
    usableCronType: () => [2, 3, 4, 5],
  }
);
// 获取默认值
const getDefaultCron = () => {
  if (props.modelValue) {
    return props.modelValue;
  } else {
    emit("update:modelValue", "0 0 * * * ? *");
    return "0 0 * * * ? *";
  }
};
// cron对象 用于转化和解析cron字符串
const simpleCron = ref<SimpleCron>(new SimpleCron(getDefaultCron()));
// 同步类型 每天 每日 每月
const selectCronType = ref<number>(simpleCron.value.cronType);

// 可用的类型
const usableCronTypes = computed(() => {
  return Object.values(cronTypes).filter((a) =>
    props.usableCronType.includes(a.value)
  );
});
// 字段绑定
const emit = defineEmits(["update:modelValue"]);
/**
 * 校验实例对象
 */
const ruleFormRef = ref<FormInstance>();
const cronFrom = ref<{
  crons: { [propName: number]: Array<number> };
  composeCrons: { [propName: number]: string };
}>({
  crons: {
    0: [],
    1: [],
    2: [],
    3: [],
    4: [],
    5: [],
    6: [],
  },
  // 组合定时任务按钮存放参数
  composeCrons: {
    100: "00:00",
    101: "00:00:00",
    102: "00:00:00",
  },
});

const disabledHours = () => {
  return region(0, 23);
};
/**
 * 回显数据
 */
const echoData = () => {
  const children = cronTypes[selectCronType.value].children;
  children.forEach((key) => {
    if (simpleCron.value) {
      if (key === hoursAndMinuteAndSecond || key === hoursAndMinute) {
        cronFrom.value.composeCrons[key] =
          (simpleCron.value?.hours.length > 0
            ? simpleCron.value?.hours[0].toString().padStart(2, "0")
            : "00") +
          ":" +
          (simpleCron.value?.minute.length > 0
            ? simpleCron.value?.minute[0].toString().padStart(2, "0")
            : "00") +
          ":" +
          (simpleCron.value.second.length > 0
            ? simpleCron.value?.second[0].toString().padStart(2, "0")
            : "00");
      }
      if (key === minuteAndSecond) {
        cronFrom.value.composeCrons[minuteAndSecond] =
          (simpleCron.value?.minute.length > 0
            ? simpleCron.value?.minute[0].toString().padStart(2, "0")
            : "00") +
          ":" +
          (simpleCron.value.second.length > 0
            ? simpleCron.value?.second[0].toString().padStart(2, "0")
            : "00");
      }
      if (Object.values(cronTypes).some((i) => i.value === key)) {
        simpleCron.value.crons[key].forEach((v) => {
          cronFrom.value.crons[key].push(v);
        });
      }
    }
  });
};

/**
 * 将组合数据解析处理
 */
const reverseComposeCrons = () => {
  const children = cronTypes[selectCronType.value].children;
  const key = Object.keys(cronFrom.value.composeCrons).find((key) =>
    children.some((c) => c.toString() === key)
  );

  if (key === "100") {
    if (cronFrom.value.composeCrons[key]) {
      const ms = cronFrom.value.composeCrons[key]
        .split(":")
        .map((s) => parseInt(s));
      cronFrom.value.crons[1] = [ms[0]];
      cronFrom.value.crons[0] = [ms[1]];
    }
  } else if (key !== undefined) {
    if (cronFrom.value.composeCrons[parseInt(key)]) {
      const ms = cronFrom.value.composeCrons[parseInt(key)]
        .split(":")
        .map((s) => parseInt(s));
      cronFrom.value.crons[2] = [ms[0]];
      cronFrom.value.crons[1] = [ms[1]];
      cronFrom.value.crons[0] = [ms[2]];
    }
  }
};
// 监听同步类型变化,重置值
watch(selectCronType, () => {
  cronFrom.value.crons = {
    0: [],
    1: [],
    2: [],
    3: [],
    4: [],
    5: [],
    6: [],
  };
  reverseComposeCrons();
});
// 监听组件变化
watch(
  () => cronFrom.value.composeCrons,
  () => {
    reverseComposeCrons();
  },
  {
    deep: true,
  }
);
// 监听普通变化
watch(
  () => cronFrom.value.crons,
  () => {
    const cron = SimpleCron.parseInCron(
      Array.from(new Set(cronFrom.value.crons[0])),
      Array.from(new Set(cronFrom.value.crons[1])),
      Array.from(new Set(cronFrom.value.crons[2])),
      Array.from(new Set(cronFrom.value.crons[3])),
      Array.from(new Set(cronFrom.value.crons[4])),
      Array.from(new Set(cronFrom.value.crons[5])),
      Array.from(new Set(cronFrom.value.crons[6]))
    );
    emit("update:modelValue", cron);
  },
  { deep: true }
);
// 校验
const validate = () => {
  return ruleFormRef.value?.validate();
};
defineExpose({ validate });
onMounted(() => {
  echoData();
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
