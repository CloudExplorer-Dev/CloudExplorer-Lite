<template>
  <el-dialog
    :close-on-press-escape="false"
    :close-on-click-modal="false"
    v-model="createComplianceRuleGroupVisible"
    title="创建规则组"
    width="60%"
    :before-close="close"
  >
    <el-form
      :model="createComplianceRuleGroupForm"
      :rules="rules"
      label-position="top"
      require-asterisk-position="right"
      ref="ruleFormRef"
      label-width="120px"
      @submit.prevent
    >
      <el-form-item label="规则组名称" prop="name">
        <el-input
          v-model="createComplianceRuleGroupForm.name"
          maxlength="64"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="规则组描述" prop="description">
        <el-input
          type="textarea"
          style="height: 100px"
          v-model="createComplianceRuleGroupForm.description"
          maxlength="255"
          show-word-limit
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="createComplianceRuleGroupVisible = false"
          >取消</el-button
        >
        <el-button type="primary" @click="submit"> 创建 </el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import { ref, reactive } from "vue";
import type { ComplianceRuleGroupRequest } from "@/api/rule_group/type";
import complianceRuleGroupApi from "@/api/rule_group";
import type { FormInstance, FormRules } from "element-plus";
import { ElMessage } from "element-plus";

const props = defineProps<{ refresh: () => void }>();
/**
 * 是否展示
 */
const createComplianceRuleGroupVisible = ref<boolean>(false);

/**
 * 创建规则组表单对象
 */
const createComplianceRuleGroupForm = ref<ComplianceRuleGroupRequest>({
  name: "",
  description: "",
});

/**
 * 表单对象
 */
const ruleFormRef = ref<FormInstance>();

/**
 * 校验对象
 */
const rules = reactive<FormRules>({
  name: [
    {
      required: true,
      message: "规则组名称不能为空",
      trigger: "blur",
      type: "string",
    },
  ],
  description: [
    {
      required: true,
      message: "规则组描述不能为空",
      trigger: "blur",
      type: "string",
    },
  ],
});
/**
 * 提交
 */
const submit = () => {
  if (!ruleFormRef.value) return;
  ruleFormRef.value.validate((ok) => {
    if (ok) {
      complianceRuleGroupApi
        .save(createComplianceRuleGroupForm.value)
        .then(() => {
          ElMessage.success("保存成功");
          // 刷新列表
          props.refresh();
          close();
        });
    }
  });
};
/**
 * 打开创建规则组表单
 */
const open = () => {
  ruleFormRef.value?.clearValidate();
  createComplianceRuleGroupForm.value = {
    name: "",
    description: "",
  };
  createComplianceRuleGroupVisible.value = true;
};
/**
 * 关闭规则组表单
 */
const close = () => {
  createComplianceRuleGroupVisible.value = false;
};

defineExpose({ open, close });
</script>
<style lang="scss"></style>
