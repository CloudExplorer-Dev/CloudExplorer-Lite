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
    <div class="main-center">
      <div v-show="props.code === 'payment'">
        <el-form-item>
          <div class="mo-input--number">
            <span style="padding: 0 8px 0 8px">持续关机</span>
            <el-input-number
              v-model="optimizeParam.cycleContinuedDays"
              style="width: 177px"
              min="1"
              max="100"
              step="1"
              controls-position="right"
              autocomplete="off"
            />
            <span style="padding: 0 8px 0 8px"
              >天的包年包月资源，建议转按需按量</span
            >
          </div>
        </el-form-item>
        <el-form-item>
          <div class="mo-input--number">
            <span style="padding: 0 8px 0 8px">持续开机</span>
            <el-input-number
              v-model="optimizeParam.volumeContinuedDays"
              style="width: 177px"
              min="1"
              max="100"
              step="1"
              controls-position="right"
              autocomplete="off"
            />
            <span style="padding: 0 8px 0 8px"
              >天的按需按量资源，建议转包年包月</span
            >
          </div>
        </el-form-item>
      </div>
      <div v-show="props.code === 'recovery'">
        <el-form-item>
          <div class="mo-input--number">
            <span style="padding: 0 8px 0 8px">持续关机</span>
            <el-input-number
              v-model="optimizeParam.continuedDays"
              style="width: 177px"
              min="1"
              max="100"
              step="1"
              controls-position="right"
              autocomplete="off"
            />
            <span style="padding: 0 8px 0 8px">天的闲置资源，建议回收</span>
          </div>
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
