<template>
  <!-- 创建 -->
  <el-dialog
    v-model="createComplianceRuleVisible"
    title="创建合规规则"
    width="60%"
    :before-close="close"
  >
    <el-form
      :model="updateComplianceRuleForm"
      :rules="updateComplianceRuleFormRules"
      ref="ruleForm"
      label-width="120px"
    >
      <el-form-item prop="name" label="规则名称">
        <el-input v-model="updateComplianceRuleForm.name" />
      </el-form-item>
      <el-form-item prop="riskLevel" label="规则等级">
        <el-radio-group
          v-model="updateComplianceRuleForm.riskLevel"
          size="large"
        >
          <el-radio-button
            v-for="level in riskLevelOptionList"
            :key="level.key"
            :label="level.value"
            >{{ level.key }}</el-radio-button
          >
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="ruleGroupId" label="规则组">
        <el-select
          style="width: 100%"
          v-model="updateComplianceRuleForm.ruleGroupId"
          class="m-2"
          :placeholder="'请选择规则组'"
        >
          <el-option
            v-for="item in complianceRuleGroupList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="platform" label="云平台">
        <el-select
          style="width: 100%"
          v-model="updateComplianceRuleForm.platform"
          class="m-2"
          :placeholder="'请选择云平台'"
        >
          <el-option
            v-for="item in Object.keys(platformIcon).map((key) => ({
              key: platformIcon[key].name,
              value: key,
              icon: platformIcon[key].icon,
            }))"
            :key="item.value"
            :label="item.key"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item prop="resourceType" label="资源类型">
        <el-select
          style="width: 100%"
          v-model="updateComplianceRuleForm.resourceType"
          class="m-2"
          :placeholder="'请选择资源类型'"
        >
          <el-option
            v-for="item in resourceTypeOptionList"
            :key="item.value"
            :label="item.key"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="resourceType" label="规则">
        <compliance_rules
          v-model="updateComplianceRuleForm.rules"
          :platform="updateComplianceRuleForm.platform"
          :resource-type="updateComplianceRuleForm.resourceType"
        ></compliance_rules>
      </el-form-item>
      <el-form-item
        prop="insuranceStatuteIds"
        v-loading="insuranceStatuteLoading"
        label="等保条例"
      >
        <el-select
          style="width: 100%"
          v-model="updateComplianceRuleForm.insuranceStatuteIds"
          class="m-2"
          :multiple="true"
          :placeholder="'请选择等保条例'"
        >
          <el-option
            v-for="item in complianceInsuranceStatuteList"
            :key="item.id"
            :label="item.id + ':' + item.baseClause"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="description" label="描述">
        <el-input v-model="updateComplianceRuleForm.description" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="createComplianceRuleVisible = false">取消</el-button>
        <el-button type="primary" @click="submit"> 提交 </el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import { nextTick, ref, onMounted } from "vue";
import compliance_rules from "@/views/rule/components/compliance_rules/index.vue";
import type { FormRules, FormInstance } from "element-plus";
import type {
  ComplianceRule,
  UpdateComplianceRuleRequest,
} from "@/api/rule/type";
import complianceRuleApi from "@/api/rule";
import type { ComplianceRuleGroup } from "@/api/rule_group/type";
import { platformIcon } from "@commons/utils/platform";
import type { KeyValue } from "@commons/api/base/type";
import { ElMessage } from "element-plus";
import _ from "lodash";
import complianceInsuranceStatuteApi from "@/api/compliance_insurance_statute";
import type { ComplianceInsuranceStatute } from "@/api/compliance_insurance_statute/type";
const props = defineProps<{
  /**
   * 资源类型
   */
  resourceTypeOptionList: Array<KeyValue<string, string>>;
  /**
   * 合规规则分组
   */
  complianceRuleGroupList: Array<ComplianceRuleGroup>;
  /**
   * 合规等级
   */
  riskLevelOptionList: Array<KeyValue<string, string>>;
  /**
   * 刷新页面
   */
  refresh: () => void;
}>();
/**
 * 等保条例列表
 */
const complianceInsuranceStatuteList = ref<Array<ComplianceInsuranceStatute>>(
  []
);
const insuranceStatuteLoading = ref<boolean>(false);
/**
 * 表单对象
 */
const ruleForm = ref<FormInstance>();

// 创建规则弹出框
const createComplianceRuleVisible = ref<boolean>(false);

/**
 * 创建规则表单
 */
const updateComplianceRuleForm = ref<UpdateComplianceRuleRequest>({
  id: "",
  name: "",
  ruleGroupId: "",
  platform: "",
  resourceType: "",
  rules: [],
  riskLevel: "LOW",
  insuranceStatuteIds: [],
  description: "",
  enable: false,
});

/**
 * 表单校验对象
 */
const updateComplianceRuleFormRules = ref<FormRules>({
  name: [
    {
      required: true,
      message: "规则名称不能为空",
      trigger: "blur",
      type: "string",
    },
  ],
  ruleGroupId: [
    {
      required: true,
      message: "规则组不能为空",
      trigger: "change",
      type: "string",
    },
  ],
  platform: [
    {
      required: true,
      message: "供应商不能为空",
      trigger: "change",
      type: "string",
    },
  ],
  resourceType: [
    {
      required: true,
      message: "资源类型不能为空",
      trigger: "change",
      type: "string",
    },
  ],
  rules: [
    {
      required: true,
      min: 1,
      message: "规则条件不能为空",
      type: "array",
    },
  ],
  insuranceStatuteIds: [
    {
      required: false,
      min: 0,
      message: "等保条例不能为空",
      type: "array",
    },
  ],
  riskLevel: [
    {
      required: true,
      message: "风险级别不能为空",
      type: "string",
      trigger: "change",
    },
  ],
  description: [
    {
      required: true,
      message: "描述不能为空",
      type: "string",
      trigger: "blur",
    },
  ],
});

/**
 * 表单提交
 */
const submit = () => {
  ruleForm.value?.validate((v) => {
    if (v) {
      complianceRuleApi
        .updateComplianceRule(updateComplianceRuleForm.value)
        .then(() => {
          ElMessage.success("修改成功");
          props.refresh();
          close();
        });
    }
  });
};

onMounted(() => {
  complianceInsuranceStatuteApi
    .list(undefined, insuranceStatuteLoading)
    .then((ok) => {
      complianceInsuranceStatuteList.value = ok.data;
    });
});
/**
 * 打开弹出框
 */
const open = () => {
  createComplianceRuleVisible.value = true;
};
// 关闭弹出框
const close = () => {
  createComplianceRuleVisible.value = false;
};
/**
 * 回显数据
 * @param complianceRule 合规规则对象
 */
const echoData = (complianceRule: ComplianceRule) => {
  updateComplianceRuleForm.value.rules = [];
  updateComplianceRuleForm.value = {
    ..._.cloneDeep(complianceRule),
    insuranceStatuteIds: [],
  };
  complianceInsuranceStatuteApi
    .list({ complianceRuleId: complianceRule.id }, insuranceStatuteLoading)
    .then((ok) => {
      updateComplianceRuleForm.value.insuranceStatuteIds = ok.data.map(
        (d) => d.id
      );
    });
  nextTick(() => {
    updateComplianceRuleForm.value.rules = complianceRule.rules;
  });
};
defineExpose({ open, close, echoData });
</script>
<style lang="scss"></style>
