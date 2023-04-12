<template>
  <el-dialog
    :close-on-press-escape="false"
    :close-on-click-modal="false"
    v-model="updateComplianceRuleGroupVisible"
    title="修改规则组"
    width="60%"
    :before-close="close"
  >
    <el-form
      :model="updateComplianceRuleGroupForm"
      :rules="rules"
      label-position="top"
      require-asterisk-position="right"
      ref="ruleFormRef"
      label-width="120px"
    >
      <el-form-item label="规则组名称" prop="name">
        <el-input v-model="updateComplianceRuleGroupForm.name" />
      </el-form-item>
      <el-form-item label="规则组描述" prop="description">
        <el-input
          type="textarea"
          style="height: 100px"
          v-model="updateComplianceRuleGroupForm.description"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="updateComplianceRuleGroupVisible = false"
          >取消</el-button
        >
        <el-button type="primary" @click="submit"> 提交 </el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import { ref, reactive } from "vue";
import type {
  ComplianceRuleGroup,
  UpdateComplianeRuleGroupRequest,
} from "@/api/rule_group/type";
import complianceRuleGroupApi from "@/api/rule_group";
import type { FormInstance, FormRules } from "element-plus";
import _ from "lodash";
import { ElMessage } from "element-plus";

const props = defineProps<{ refresh: () => void }>();
/**
 * 是否展示
 */
const updateComplianceRuleGroupVisible = ref<boolean>(false);

/**
 * 创建规则组表单对象
 */
const updateComplianceRuleGroupForm = ref<UpdateComplianeRuleGroupRequest>({
  id: "",
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
        .update(updateComplianceRuleGroupForm.value)
        .then(() => {
          ElMessage.success("修改成功成功");
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
  updateComplianceRuleGroupVisible.value = true;
};
/**
 * 关闭规则组表单
 */
const close = () => {
  updateComplianceRuleGroupVisible.value = false;
};
/**
 * 回显设置数据
 * @param complianceRuleGroup
 */
const echoData = (complianceRuleGroup: ComplianceRuleGroup) => {
  updateComplianceRuleGroupForm.value = _.cloneDeep(complianceRuleGroup);
};
defineExpose({ open, close, echoData });
</script>
<style lang="scss"></style>
