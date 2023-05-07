<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import _ from "lodash";
import type { OptimizeStrategyBaseReq } from "@commons/api/optimize/type";
const props = withDefaults(
  defineProps<{
    code?: string;
    optimizeStrategy: any;
  }>(),
  {
    code: "",
  }
);

const comparisonSymbolMap = new Map<string, string>();
comparisonSymbolMap.set("derating", "< =");
comparisonSymbolMap.set("upgrade", "> =");
const comparisonSymbol = computed<string>(() => {
  return comparisonSymbolMap.get(props.code)!;
});
const formData = ref<any>({
  days: 1,
  cpuValue: 1,
  cpuComparisonValue: "",
  comparisonCondition: "",
  memoryValue: 1,
  memoryComparisonValue: "",
});

const getNewFormData = () => {
  const req = ref<OptimizeStrategyBaseReq>({
    optimizeSuggestCode: props.code,
    usedRateRequest: formData,
  });
  return req.value;
};
defineExpose({
  getNewFormData,
});

watch(
  () => props.optimizeStrategy,
  () => {
    if (props.optimizeStrategy) {
      formData.value = _.cloneDeep(props.optimizeStrategy);
    }
  }
);
onMounted(() => {
  if (props.optimizeStrategy) {
    formData.value = _.cloneDeep(props.optimizeStrategy);
  }
});
</script>

<template>
  <el-form class="form-box" v-model="formData">
    <el-form-item>
      <div class="mo-input--number">
        <span>过去</span>
        <div style="width: 168px; padding: 0 0 0 8px">
          <el-input-number
            :min="1"
            v-model="formData.days"
            style="width: 168px !important"
            controls-position="right"
            autocomplete="off"
          >
          </el-input-number>
        </div>
        <span class="unit-label">天</span>
      </div>
    </el-form-item>
    <div class="main-center">
      <el-form-item>
        <el-select style="width: 230px" v-model="formData.cpuComparisonValue">
          <el-option label="CPU平均使用率" value="MAX" />
          <el-option label="CPU最大使用率" value="AVG" />
        </el-select>
        <span style="padding: 0 8px 0 8px">{{ comparisonSymbol }}</span>
        <el-form-item>
          <div class="mo-input--number">
            <el-input-number
              v-model="formData.cpuValue"
              style="width: 220px"
              :min="1"
              :max="100"
              :step="1"
              controls-position="right"
              autocomplete="off"
            />
            <span class="unit-label">%</span>
          </div>
        </el-form-item>
      </el-form-item>
      <el-form-item>
        <el-select
          style="width: 230px"
          v-model="formData.memoryComparisonValue"
        >
          <el-option label="内存平均使用率" value="MAX" />
          <el-option label="内存最大使用率" value="AVG" />
        </el-select>
        <span style="padding: 0 8px 0 8px">{{ comparisonSymbol }}</span>
        <el-form-item>
          <div class="mo-input--number">
            <el-input-number
              v-model="formData.memoryValue"
              style="width: 220px"
              :min="1"
              :max="100"
              :step="1"
              controls-position="right"
              autocomplete="off"
            />
            <span class="unit-label">%</span>
          </div>
        </el-form-item>
      </el-form-item>
    </div>
    <div class="main-footer">
      <div>
        <span>选择逻辑关系</span>
      </div>
      <el-form-item>
        <div>
          <el-radio-group size="large" v-model="formData.comparisonCondition">
            <div style="display: block">
              <div style="height: 22px; display: flex">
                <el-radio label="OR">OR</el-radio>
              </div>
              <div style="height: 22px; display: flex">
                <el-radio label="AND">AND</el-radio>
              </div>
            </div>
          </el-radio-group>
        </div>
      </el-form-item>
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
    border-radius: 4px 0 0 4px;
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
    border-left: 0;
    border-radius: 0 4px 4px 0;
    height: 30px;
  }
}
</style>
