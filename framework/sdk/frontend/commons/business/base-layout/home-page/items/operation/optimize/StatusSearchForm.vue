<script setup lang="ts">
import { computed, ref } from "vue";
import _ from "lodash";
import type { OptimizeStrategyBaseReq } from "@commons/api/optimize/type";
const props = withDefaults(
  defineProps<{
    code: string;
    optimizeStrategy: any;
  }>(),
  {
    code: "",
  }
);
interface StatusStrategyObj {
  continuedDays: any;
  continuedRunning: any;
  [props: symbol]: any;
}
interface OptimizeStrategyReq {
  cycle: StatusStrategyObj;
  volume: StatusStrategyObj;
  recovery: StatusStrategyObj;
  [props: symbol]: any;
}

const formReq = computed<OptimizeStrategyReq>(() => {
  const result: OptimizeStrategyReq = {
    cycle: { continuedDays: undefined, continuedRunning: undefined },
    volume: { continuedDays: undefined, continuedRunning: undefined },
    recovery: { continuedDays: undefined, continuedRunning: undefined },
  };
  if (props.optimizeStrategy.cycle) {
    _.assign(result.cycle, props.optimizeStrategy.cycle);
  }
  if (props.optimizeStrategy.volume) {
    _.assign(result.volume, props.optimizeStrategy.volume);
  }
  if (props.optimizeStrategy.recovery) {
    _.assign(result.recovery, props.optimizeStrategy.recovery);
  }
  return result;
});
const getNewFormData = () => {
  if (!formReq.value.cycle.continuedDays) {
    formReq.value.cycle.continuedDays = 1;
  }
  if (!formReq.value.volume.continuedDays) {
    formReq.value.volume.continuedDays = 1;
  }
  if (!formReq.value.recovery.continuedDays) {
    formReq.value.recovery.continuedDays = 1;
  }
  const req = ref<OptimizeStrategyBaseReq>({
    optimizeSuggestCode: props.code,
    statusRequest: formReq.value,
  });
  return req.value;
};
defineExpose({
  getNewFormData,
});
</script>
<template>
  <el-form class="form-box">
    <div v-show="props.code === 'payment'">
      <div class="box-title">包年包月资源</div>
      <div class="main-center">
        <el-form-item>
          <div class="mo-input--number">
            <span style="padding: 0 8px 0 8px">持续关机</span>
            <span style="padding: 0 8px 0 0"> > </span>
            <el-input-number
              v-model="formReq.cycle.continuedDays"
              style="width: 276px"
              :min="1"
              :max="100"
              :step="1"
              controls-position="right"
              autocomplete="off"
              :value-on-clear="1"
              :step-strictly="true"
            />
            <span class="unit-label">天</span>
          </div>
          <span style="padding: 0 8px 0 8px">建议转按需按量</span>
        </el-form-item>
      </div>
      <div class="box-title" style="padding-top: 24px">按需按量资源</div>
      <div class="main-center">
        <el-form-item>
          <div class="mo-input--number">
            <span style="padding: 0 8px 0 8px">持续开机</span>
            <span style="padding: 0 8px 0 0"> > </span>
            <el-input-number
              v-model="formReq.volume.continuedDays"
              style="width: 276px"
              :min="1"
              :max="100"
              :step="1"
              controls-position="right"
              autocomplete="off"
              :value-on-clear="1"
              :step-strictly="true"
            />
            <span class="unit-label">天</span>
          </div>
          <span style="padding: 0 8px 0 8px">建议转包年包月</span>
        </el-form-item>
      </div>
    </div>
    <div v-show="props.code === 'recovery'">
      <div class="box-title">闲置资源</div>
      <div class="main-center">
        <el-form-item>
          <div class="mo-input--number">
            <span style="padding: 0 8px 0 8px">持续关机</span>
            <span style="padding: 0 8px 0 0"> > </span>
            <el-input-number
              v-model="formReq.recovery.continuedDays"
              style="width: 276px"
              :min="1"
              :max="100"
              :step="1"
              controls-position="right"
              autocomplete="off"
              :value-on-clear="1"
              :step-strictly="true"
            />
            <span class="unit-label">天</span>
          </div>
          <span style="padding: 0 8px 0 8px">建议回收</span>
        </el-form-item>
      </div>
    </div>
  </el-form>
</template>

<style lang="scss" scoped>
.main-center {
  padding: 16px 16px 0 16px;
  border-radius: 4px 4px 0 0;
  border: 1px solid rgba(223, 224, 227, 1);
  background-color: rgba(245, 246, 247, 1);
}
.main-footer {
  padding: 16px;
  border-radius: 0 0 4px 4px;
  border: 1px solid rgba(223, 224, 227, 1);
  border-top: none;
}

/* 自定义数字输入框append  */
.mo-input--number {
  display: flex;
  :deep(.el-input__wrapper) {
    border-radius: 4px 0 0px 4px;
  }
  :deep(.el-input-number__decrease) {
    background-color: #ffffff;
  }
  :deep(.el-input-number__increase) {
    background-color: #ffffff;
  }
  .unit-label {
    background-color: rgba(239, 240, 241, 1);
    padding: 0 8px 0 8px;
    border: 1px solid #dcdfe5;
    border-left: 0px;
    border-radius: 0 4px 4px 0;
    height: 30px;
  }
}
.box-title {
  padding-bottom: 5px;
  width: 96px;
  height: 24px;
  left: 24px;
  top: calc(50% - 24px / 2 - 100px);
  font-family: "PingFang SC";
  font-style: normal;
  font-weight: 500;
  font-size: 16px;
  line-height: 24px;
  color: #1f2329;
}
</style>
