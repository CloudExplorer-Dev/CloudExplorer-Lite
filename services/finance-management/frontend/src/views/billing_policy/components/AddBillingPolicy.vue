<template>
  <el-dialog
    v-model="dialogVisible"
    title="创建计费策略"
    width="600px"
    :before-close="close"
  >
    <el-form
      require-asterisk-position="right"
      label-position="top"
      label-width="100px"
      :model="form"
    >
      <el-form-item
        style="width: 100%"
        label="计费策略名称"
        :rules="{
          required: true,
          message: '名称不能为空',
          trigger: 'blur',
        }"
      >
        <el-input style="width: 100%" v-model="form.name" />
      </el-form-item>
      <el-form-item label="选择云账号">
        <CloudAccountCheckbox
          :cloud-account-list="cloudAccountList"
          v-model="form.linkCloudAccountIds"
        ></CloudAccountCheckbox>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="close">取消</el-button>
        <el-button type="primary" @click="create"> 创建 </el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import { ref, onMounted } from "vue";
import CloudAccountCheckbox from "@/views/billing_policy/components/CloudAccountCheckbox.vue";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import type { CreateBillingPolicyRequest } from "@/api/billing_policy/type";
import billingPolicyApi from "@/api/billing_policy/index";
import { ElMessage } from "element-plus";
const cloudAccountList = ref<Array<CloudAccount>>([]);
const dialogVisible = ref<boolean>(false);
onMounted(() => {
  billingPolicyApi.listCloudAccount(undefined).then((ok) => {
    cloudAccountList.value = ok.data.filter((c) => !c.publicCloud);
  });
});
const form = ref<CreateBillingPolicyRequest>({
  name: "",
  linkCloudAccountIds: [],
});
const open = () => {
  form.value = {
    name: "",
    linkCloudAccountIds: [],
  };
  dialogVisible.value = true;
};
const close = () => {
  dialogVisible.value = false;
};
const emit = defineEmits(["refrece"]);
const create = () => {
  billingPolicyApi
    .create(form.value)
    .then((ok) => {
      emit("refrece", ok.data.id);
      ElMessage.success("创建成功");
      close();
    })
    .catch((e) => {
      ElMessage.success("创建失败", e);
    });
};
defineExpose({ open, close });
</script>
<style lang="scss"></style>
