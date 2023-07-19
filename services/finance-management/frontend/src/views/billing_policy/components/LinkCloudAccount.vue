<template>
  <div>
    <el-dialog
      v-model="dialogVisible"
      title="关联云账号"
      width="600px"
      :before-close="close"
    >
      <span style="color: #1f2329"> 选择云账号</span>
      <CloudAccountCheckbox
        v-loading="loading"
        :cloud-account-list="cloudAccountList"
        v-model="selectedCloudAccount"
        :row-number="3"
      ></CloudAccountCheckbox>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="link"> 关联 </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import CloudAccountCheckbox from "@/views/billing_policy/components/CloudAccountCheckbox.vue";
import billingPolicyApi from "@/api/billing_policy/index";
import type { CloudAccountResponse } from "@/api/billing_policy/type";

const billingPolicyId = ref<string>("");
const emit = defineEmits(["refrece"]);
const selectedCloudAccount = ref<Array<string>>([]);
const cloudAccountList = ref<Array<CloudAccountResponse>>([]);
const dialogVisible = ref<boolean>(false);
const loading = ref<boolean>(false);
const open = (policyId: string) => {
  billingPolicyId.value = policyId;
  billingPolicyApi
    .listCloudAccount(billingPolicyId.value, loading)
    .then((ok) => {
      cloudAccountList.value = ok.data.filter((c) => !c.publicCloud);
      selectedCloudAccount.value = ok.data
        .filter((c) => c.selected)
        .map((c) => c.id);
    });
  dialogVisible.value = true;
};
const link = () => {
  billingPolicyApi
    .link({
      billingPolicyId: billingPolicyId.value,
      cloudAccountIdList: selectedCloudAccount.value,
    })
    .then(() => {
      emit("refrece", billingPolicyId.value);
      close();
    });
};
const close = () => {
  billingPolicyId.value = "";
  dialogVisible.value = false;
};
defineExpose({ open, close });
</script>
<style lang="scss"></style>
