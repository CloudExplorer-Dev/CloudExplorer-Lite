<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import _ from "lodash";
import { paramOptimizationRequestMap } from "@commons/api/resource_optimization/type";
const props = withDefaults(
  defineProps<{
    code?: string;
  }>(),
  {
    code: "",
  }
);
const optimizeParam = ref<any>(paramOptimizationRequestMap.get(props.code!));
const getOptimizeParam = () => {
  if (localStorage.getItem(props.code!)) {
    const str = localStorage.getItem(props.code!);
    if (str) {
      try {
        return JSON.parse(str);
      } catch (e) {
        console.error("get default dialogFormData error", e);
        return paramOptimizationRequestMap.get(props.code!);
      }
    }
  } else {
    return paramOptimizationRequestMap.get(props.code!);
  }
};
const comparisonSymbolMap = new Map<string, string>();
comparisonSymbolMap.set("derating", "小于等于");
comparisonSymbolMap.set("upgrade", "大于等于");
const comparisonSymbol = computed<string>(() => {
  return comparisonSymbolMap.get(props.code);
});
onMounted(() => {
  optimizeParam.value = getOptimizeParam();
});
</script>

<template>
  <el-form class="form-box">
    <el-form-item>
      <span>过去</span>
      <div style="width: 168px; padding: 0px 8px 0px 8px">
        <el-input-number
          v-model="optimizeParam.days"
          style="width: 168px !important"
          controls-position="right"
          autocomplete="off"
        />
      </div>
      <span>天</span>
    </el-form-item>
    <div class="main-center">
      <el-form-item>
        <el-select style="width: 236px" v-model="optimizeParam.cpuMaxRate">
          <el-option label="CPU平均使用率" value="false" />
          <el-option label="CPU最大使用率" value="true" />
        </el-select>
        <span style="padding: 0 8px 0 8px">{{ comparisonSymbol }}</span>
        <el-form-item>
          <div class="mo-input--number">
            <el-input-number
              v-model="optimizeParam.cpuRate"
              style="width: 177px"
              min="1"
              max="100"
              step="1"
              controls-position="right"
              autocomplete="off"
            />
            <div class="define-append">%</div>
          </div>
        </el-form-item>
      </el-form-item>
      <el-form-item>
        <el-select style="width: 236px" v-model="optimizeParam.memoryMaxRate">
          <el-option label="内存平均使用率" value="false" />
          <el-option label="内存最大使用率" value="true" />
        </el-select>
        <span style="padding: 0 8px 0 8px">{{ comparisonSymbol }}</span>
        <el-form-item>
          <div class="mo-input--number">
            <el-input-number
              v-model="optimizeParam.memoryRate"
              style="width: 177px"
              min="1"
              max="100"
              step="1"
              controls-position="right"
              autocomplete="off"
            />
            <div class="define-append">%</div>
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
          <el-radio-group size="large" v-model="optimizeParam.conditionOr">
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
  padding: 16px 0 0 16px;
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
  border: 1px solid #dcdfe6;
  width: 100%;
  display: flex;
  border-radius: 4px;
  .el-input-number--mini {
    flex: 1;
  }
}
.define-append {
  display: inline-block;
  padding: 0 9px;
  border-left: none;
  height: 32px;
  line-height: 32px;
  font-size: 12px;
  text-align: center;
}
</style>
