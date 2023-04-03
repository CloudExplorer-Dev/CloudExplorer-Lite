<script setup lang="ts">
import { reactive, ref } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import DeratingForm from "./UsedRateSearchForm.vue";
import type { ListOptimizationRequest } from "@commons/api/resource_optimization/type";
import StatusSearchForm from "@commons/business/base-layout/home-page/items/operation/StatusSearchForm.vue";
const props = defineProps<{
  optimizationSearchReq?: ListOptimizationRequest;
}>();

const dialogVisible = ref(false);
defineExpose({
  dialogVisible,
});

const handleCancel = () => {
  dialogVisible.value = false;
};

const handleSave = () => {
  dialogVisible.value = false;
};
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    :title="props.optimizationSearchReq?.name + '查询策略'"
    width="25%"
    destroy-on-close
    :close-on-click-modal="false"
    class="custom-dialog"
  >
    <div class="dialog-body">
      <DeratingForm
        :code="props.optimizationSearchReq?.code"
        v-show="props.optimizationSearchReq?.code === 'derating'"
      ></DeratingForm>
      <DeratingForm
        :code="props.optimizationSearchReq?.code"
        v-show="props.optimizationSearchReq?.code === 'upgrade'"
      ></DeratingForm>
      <StatusSearchForm
        :code="props.optimizationSearchReq?.code"
        v-show="props.optimizationSearchReq?.code === 'payment'"
      ></StatusSearchForm>
      <StatusSearchForm
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
