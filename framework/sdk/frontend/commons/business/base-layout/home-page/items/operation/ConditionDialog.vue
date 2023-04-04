<script setup lang="ts">
import { withDefaults, ref } from "vue";
import DeratingForm from "./UsedRateSearchForm.vue";
import type { ListOptimizationRequest } from "@commons/api/resource_optimization/type";
import StatusSearchForm from "@commons/business/base-layout/home-page/items/operation/StatusSearchForm.vue";
const props = withDefaults(
  defineProps<{
    optimizationSearchReq?: ListOptimizationRequest;
  }>(),
  {}
);

const dialogVisible = ref(false);
defineExpose({
  dialogVisible,
});

const handleCancel = () => {
  dialogVisible.value = false;
};
const emit = defineEmits(["changeParam"]);

const handleSave = () => {
  dialogVisible.value = false;

  window.localStorage.setItem(
    props.optimizationSearchReq?.code,
    JSON.stringify(props.optimizationSearchReq)
  );

  //更新数据库
  emit("changeParam", props.optimizationSearchReq);
};
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    :title="props.optimizationSearchReq?.name"
    width="25%"
    destroy-on-close
    :close-on-click-modal="false"
    class="custom-dialog"
  >
    <div class="dialog-body">
      <DeratingForm
        :req="props.optimizationSearchReq"
        :code="props.optimizationSearchReq?.code"
        v-show="props.optimizationSearchReq?.code === 'derating'"
      ></DeratingForm>
      <DeratingForm
        :req="props.optimizationSearchReq"
        :code="props.optimizationSearchReq?.code"
        v-show="props.optimizationSearchReq?.code === 'upgrade'"
      ></DeratingForm>
      <StatusSearchForm
        :req="props.optimizationSearchReq"
        :code="props.optimizationSearchReq?.code"
        v-show="props.optimizationSearchReq?.code === 'payment'"
      ></StatusSearchForm>
      <StatusSearchForm
        :req="props.optimizationSearchReq"
        :code="props.optimizationSearchReq?.code"
        v-show="props.optimizationSearchReq?.code === 'recovery'"
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
