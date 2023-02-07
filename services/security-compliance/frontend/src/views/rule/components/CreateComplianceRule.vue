<template>
  <!-- 创建 -->
  <el-dialog
    v-model="createComplianceRuleVisible"
    title="创建合规规则"
    width="60%"
    :before-close="close"
  >
    <el-form
      :model="createComplianceRuleForm"
      :rules="createComplianceRuleFormRules"
      ref="ruleForm"
      label-width="120px"
    >
      <el-form-item prop="name" label="规则名称">
        <el-input v-model="createComplianceRuleForm.name" />
      </el-form-item>
      <el-form-item prop="riskLevel" label="规则等级">
        <el-radio-group
          v-model="createComplianceRuleForm.riskLevel"
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
          v-model="createComplianceRuleForm.ruleGroupId"
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
          v-model="createComplianceRuleForm.platform"
          class="m-2"
          :placeholder="'请选择云平台'"
        >
          <el-option
            v-for="item in supportPlatformList"
            :key="item.value"
            :label="item.key"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item prop="resourceType" label="资源类型">
        <el-select
          style="width: 100%"
          v-model="createComplianceRuleForm.resourceType"
          class="m-2"
          :placeholder="'请选择资源类型'"
        >
          <el-option
            v-for="item in supportResourceTypeList"
            :key="item.value"
            :label="item.key"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="resourceType" label="规则">
        <compliance_rules
          v-model="createComplianceRuleForm.rules"
          :platform="createComplianceRuleForm.platform"
          :resource-type="createComplianceRuleForm.resourceType"
        ></compliance_rules>
      </el-form-item>
      <el-form-item
        prop="insuranceStatuteIds"
        v-loading="insuranceStatuteLoading"
        label="等保条例"
      >
        <el-select
          style="width: 100%"
          v-model="createComplianceRuleForm.insuranceStatuteIds"
          class="m-2"
          :multiple="true"
          :placeholder="'请选择等保条例'"
        >
          <el-option
            v-for="item in complianceInsuranceStatuteList"
            :key="item.id"
            :label="
              _.truncate(item.id + '.' + item.baseClause, {
                length: 50,
                separator: ' ',
              })
            "
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="description" label="描述">
        <el-input v-model="createComplianceRuleForm.description" />
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
import { ref, onMounted, computed, watch } from "vue";
import compliance_rules from "@/views/rule/components/compliance_rules/index.vue";
import type { FormRules, FormInstance } from "element-plus";
import type { SaveComplianceRuleRequest } from "@/api/rule/type";
import complianceRuleApi from "@/api/rule";
import complianceScanApi from "@/api/compliance_scan";
import type { SupportPlatformResourceResponse } from "@/api/compliance_scan/type";
import type { ComplianceRuleGroup } from "@/api/rule_group/type";
import { platformIcon } from "@commons/utils/platform";
import type { KeyValue } from "@commons/api/base/type";
import { ElMessage } from "element-plus";
import complianceInsuranceStatuteApi from "@/api/compliance_insurance_statute";
import type { ComplianceInsuranceStatute } from "@/api/compliance_insurance_statute/type";
import _ from "lodash";
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
const createComplianceRuleForm = ref<SaveComplianceRuleRequest>({
  name: "",
  ruleGroupId: "",
  platform: "",
  resourceType: "",
  rules: [],
  riskLevel: "LOW",
  insuranceStatuteIds: [],
  description: "",
});
/**
 * 表单校验对象
 */
const createComplianceRuleFormRules = ref<FormRules>({
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
 * 支持的云平台
 */
const supportPlatformResourceList = ref<Array<SupportPlatformResourceResponse>>(
  []
);
/**
 * 支持的云平台
 */
const supportPlatformList = computed(() => {
  return [
    ...new Set(supportPlatformResourceList.value.map((s) => s.platform)),
  ].map((platform) => ({
    key: platformIcon[platform].name,
    value: platform,
    icon: platformIcon[platform].icon,
  }));
});
/**
 * 支持的资源类型
 */
const supportResourceTypeList = computed(() => {
  if (createComplianceRuleForm.value.platform) {
    return supportPlatformResourceList.value.find(
      (s) => s.platform === createComplianceRuleForm.value.platform
    )?.resourceTypes;
  }
  return [];
});
/**
 * 修改云账号后 清除规则和资源类型
 */
watch(
  () => createComplianceRuleForm.value.platform,
  () => {
    createComplianceRuleForm.value.rules = [];
    createComplianceRuleForm.value.resourceType = "";
  }
);
/**
 * 表单提交
 */
const submit = () => {
  ruleForm.value?.validate((v) => {
    if (v) {
      complianceRuleApi
        .saveComplianceRule(createComplianceRuleForm.value)
        .then(() => {
          ElMessage.success("保存成功");
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
  complianceScanApi.listSupportPlatformResource().then((ok) => {
    supportPlatformResourceList.value = ok.data;
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
defineExpose({ open, close });
</script>
<style lang="scss"></style>
