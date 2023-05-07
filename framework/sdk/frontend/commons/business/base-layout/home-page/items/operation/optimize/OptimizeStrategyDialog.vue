<script setup lang="ts">
import { withDefaults, ref, computed, onMounted, watch } from "vue";
import UsedRateSearchForm from "@commons/business/base-layout/home-page/items/operation/optimize/UsedRateSearchForm.vue";
import StatusSearchForm from "@commons/business/base-layout/home-page/items/operation/optimize/StatusSearchForm.vue";
import type { OptimizeSuggest } from "@commons/api/optimize/type";
import OptimizeViewApi from "@commons/api/optimize";
import _ from "lodash";
import type { OptimizeStrategyBaseReq } from "@commons/api/optimize/type";

const props = withDefaults(
  defineProps<{
    optimizeSuggest: OptimizeSuggest;
  }>(),
  {}
);
//优化建议基本信息
const optimizeSuggestInfo = computed<OptimizeSuggest>(() => {
  const result: OptimizeSuggest = {
    index: 0,
    name: "",
    optimizeSuggestCode: "",
    value: 0,
  };
  if (!props.optimizeSuggest) {
    return result;
  }
  return props.optimizeSuggest!;
});

//策略表单数据
interface OptimizeStrategyFormData {
  [props: symbol]: any;
}
const dialogVisible = ref(false);
defineExpose({
  dialogVisible,
});
//降配升配策略
const usedRateRef = ref<InstanceType<typeof UsedRateSearchForm> | null>();
const showUsedRateForm = computed<boolean>(() => {
  if (
    optimizeSuggestInfo.value.optimizeSuggestCode === "derating" ||
    optimizeSuggestInfo.value.optimizeSuggestCode === "upgrade"
  ) {
    return true;
  }
  return false;
});
//状态策略
const statusRef = ref<InstanceType<typeof StatusSearchForm> | null>();
const showStatusForm = computed<boolean>(() => {
  if (
    optimizeSuggestInfo.value.optimizeSuggestCode === "payment" ||
    optimizeSuggestInfo.value.optimizeSuggestCode === "recovery"
  ) {
    return true;
  }
  return false;
});
//取消
const handleCancel = () => {
  dialogVisible.value = false;
};
//保存
const emit = defineEmits(["changeStrategy"]);
const handleSave = () => {
  dialogVisible.value = false;
  const req = {};
  if (usedRateRef.value) {
    _.assign(req, usedRateRef.value?.getNewFormData());
  }
  if (statusRef.value) {
    _.assign(req, statusRef.value?.getNewFormData());
  }
  OptimizeViewApi.modifyOptimizeStrategy(req as OptimizeStrategyBaseReq).then(
    (res) => {
      emit("changeStrategy", props.optimizeSuggest);
    }
  );
};

//获取优化策略
const apiOptimizeStrategy = ref<any>();
const getOptimizeStrategy = () => {
  if (props.optimizeSuggest) {
    OptimizeViewApi.getOptimizeStrategy(
      props.optimizeSuggest.optimizeSuggestCode
    ).then((res) => {
      apiOptimizeStrategy.value = res.data;
    });
  }
};
watch(
  () => {
    return {
      code: props.optimizeSuggest?.optimizeSuggestCode,
      dialogVisible: dialogVisible.value,
    };
  },
  (n) => {
    if (n.dialogVisible) {
      getOptimizeStrategy();
    }
  }
);
onMounted(() => {
  getOptimizeStrategy();
});
//优化策略转换为表单数据
const formData = computed<OptimizeStrategyFormData>(() => {
  const result: OptimizeStrategyFormData = {};
  if (apiOptimizeStrategy.value) {
    _.assign(result, _.cloneDeep(JSON.parse(apiOptimizeStrategy.value)));
  }
  return result;
});
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    :title="optimizeSuggestInfo.name"
    width="25%"
    destroy-on-close
    :close-on-click-modal="false"
    class="custom-dialog"
  >
    <div class="dialog-body">
      <UsedRateSearchForm
        ref="usedRateRef"
        :code="optimizeSuggestInfo.optimizeSuggestCode"
        :optimize-strategy="formData"
        v-if="showUsedRateForm"
      ></UsedRateSearchForm>
      <StatusSearchForm
        ref="statusRef"
        :code="optimizeSuggestInfo.optimizeSuggestCode"
        :optimize-strategy="formData"
        v-else-if="showStatusForm"
      ></StatusSearchForm>
    </div>
    <template #footer>
      <div style="flex: auto" class="footer-btn">
        <el-button @click="handleCancel()">{{
          $t("commons.btn.cancel")
        }}</el-button>
        <el-button type="primary" @click="handleSave()">{{
          $t("commons.btn.save")
        }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped>
.dialog-body {
  padding: 0 0 20px 0;
}
</style>
