<template>
  <div>
    <el-dialog
      v-model="dialogVisible"
      title="重命名计费策略名称"
      width="600px"
      :before-close="close"
    >
      <span style="color: #1f2329"> </span>
      <el-form
        require-asterisk-position="right"
        label-position="top"
        label-width="100px"
        :model="form"
        ref="from"
      >
        <el-form-item
          style="width: 100%"
          label="计费策略名称"
          :rules="{
            required: true,
            message: '名称不能为空',
            trigger: 'blur',
          }"
          prop="name"
        >
          <el-input style="width: 100%" v-model="form.name" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="reName"> 确定 </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import billingPolicyApi from "@/api/billing_policy/index";
import type { BillingPolicy } from "@/api/billing_policy/type";
import type { FormInstance } from "element-plus";
const from = ref<FormInstance>();
const billingPolicyId = ref<string>();
const form = ref<{
  name: string;
}>({
  name: "",
});

const emit = defineEmits(["refrece"]);
const reName = () => {
  from.value?.validate().then(() => {
    if (billingPolicyId.value) {
      billingPolicyApi
        .reName(billingPolicyId.value, form.value.name)
        .then((ok) => {
          if (ok.code !== 500) {
            emit("refrece", billingPolicyId.value);
            close();
          }
        });
    }
  });
};
const dialogVisible = ref<boolean>(false);
const open = (policy: BillingPolicy) => {
  billingPolicyId.value = policy.id;
  form.value.name = policy.name;
  dialogVisible.value = true;
};
const close = () => {
  dialogVisible.value = false;
};
defineExpose({ open, close });
</script>
<style lang="scss"></style>
