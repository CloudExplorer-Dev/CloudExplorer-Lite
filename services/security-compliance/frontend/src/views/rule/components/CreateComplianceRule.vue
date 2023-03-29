<template>
  <!-- 创建 -->
  <el-dialog
    :close-on-press-escape="false"
    :close-on-click-modal="false"
    v-model="createComplianceRuleVisible"
    title="创建规则"
    width="60%"
    :before-close="close"
  >
    <el-form
      label-position="top"
      :inline="true"
      require-asterisk-position="right"
      :model="createComplianceRuleForm"
      :rules="createComplianceRuleFormRules"
      ref="ruleForm"
      label-width="120px"
    >
      <base-container class="base_container" :contentBorder="false">
        <template #header><span>基本信息</span> </template>
        <template #content>
          <div class="base_info">
            <el-form-item prop="name" style="width: 45%" label="规则名称">
              <el-input v-model="createComplianceRuleForm.name" />
            </el-form-item>
            <el-form-item prop="description" style="width: 45%" label="描述">
              <el-input v-model="createComplianceRuleForm.description" />
            </el-form-item>
            <el-form-item prop="ruleGroupId" style="width: 45%" label="规则组">
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
            <el-form-item prop="platform" style="width: 45%" label="云平台">
              <el-select
                style="width: 100%"
                v-model="createComplianceRuleForm.platform"
                @change="changePlatform"
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
            <el-form-item prop="riskLevel" style="width: 45%" label="规则等级">
              <el-radio-group v-model="createComplianceRuleForm.riskLevel">
                <el-radio-button
                  v-for="level in riskLevelOptionList"
                  :key="level.key"
                  :label="level.value"
                  >{{ level.key }}</el-radio-button
                >
              </el-radio-group>
            </el-form-item>
            <el-form-item
              style="width: 45%"
              prop="insuranceStatuteIds"
              v-loading="insuranceStatuteLoading"
              label="关联风险条例"
            >
              <el-select
                style="width: 100%"
                filterable
                v-model="createComplianceRuleForm.insuranceStatuteIds"
                class="m-2"
                :multiple="true"
                :placeholder="'请选择等保条例'"
                :max-collapse-tags="1"
                collapse-tags
                collapse-tags-tooltip
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
          </div>
        </template>
      </base-container>
      <base-container class="base_container" :contentBorder="false">
        <template #header><span>规则详情</span></template>
        <template #content>
          <div class="rule_details">
            <el-form-item
              prop="resourceType"
              style="width: 45%"
              label="资源类型"
            >
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
            <el-form-item
              prop="rules.scanRule"
              style="width: 45%"
              label="规则类型"
            >
              <el-radio-group
                v-model="createComplianceRuleForm.rules.scanRule"
                class="ml-4"
              >
                <el-radio label="COMPLIANCE" size="large">视为合规</el-radio>
                <el-radio label="NOT_COMPLIANCE" size="large"
                  >视为不合规</el-radio
                >
              </el-radio-group>
            </el-form-item>
            <el-form-item
              prop="rules.rules"
              style="width: 100%"
              label="判断条件"
            >
              <compliance_rules
                ref="rulesRef"
                v-model="createComplianceRuleForm.rules"
                :platform="createComplianceRuleForm.platform"
                :resource-type="createComplianceRuleForm.resourceType"
              ></compliance_rules>
            </el-form-item>
          </div>
        </template>
      </base-container>
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
import { ref, onMounted, computed } from "vue";
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
const rulesRef = ref<InstanceType<typeof compliance_rules>>();
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
  rules: {
    conditionType: "AND",
    rules: [{ field: "", compare: "", value: "" }],
    scanRule: "COMPLIANCE",
  },
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
  "rules.rules": [
    {
      required: true,
      min: 1,
      message: "规则条件不能为空",
      type: "array",
    },
  ],
  "rules.scanRule": [
    {
      required: true,
      message: "规则类型不能为空",
      trigger: "change",
      type: "string",
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

const changePlatform = () => {
  createComplianceRuleForm.value.rules = {
    conditionType: "AND",
    rules: [],
    scanRule: createComplianceRuleForm.value.rules.scanRule
      ? createComplianceRuleForm.value.rules.scanRule
      : "COMPLIANCE",
  };
  createComplianceRuleForm.value.resourceType = "";
};
/**
 * 表单提交
 */
const submit = () => {
  rulesRef.value?.validate().then(() => {
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
  createComplianceRuleForm.value = {
    name: "",
    ruleGroupId: "",
    platform: "",
    resourceType: "",
    rules: {
      conditionType: "AND",
      rules: [{ field: "", compare: "", value: "" }],
      scanRule: "COMPLIANCE",
    },
    riskLevel: "LOW",
    insuranceStatuteIds: [],
    description: "",
  };
  // 需要等elementui校验结束后再进清空
  ruleForm.value
    ?.validate(() => {
      ruleForm.value?.clearValidate();
    })
    .catch();
};
// 关闭弹出框
const close = () => {
  createComplianceRuleVisible.value = false;
};
defineExpose({ open, close });
</script>
<style lang="scss" scoped>
.base_container {
  width: 100%;
  height: auto;
  .base_info {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
  }
  .rule_details {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
  }
}
</style>
