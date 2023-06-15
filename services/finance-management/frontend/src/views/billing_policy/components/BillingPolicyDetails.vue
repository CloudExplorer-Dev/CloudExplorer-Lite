<template>
  <div>
    <el-tabs @tab-click="bus.emit('closePricePreview')" type="card">
      <el-tab-pane
        v-for="(policy, index) in modelValue"
        :label="policy.resourceName"
        :key="index"
      >
        <base-container style="--ce-base-container-height: auto">
          <template #header>
            <span>按单价计费规则</span>
          </template>
          <template #content>
            <el-row :gutter="16">
              <el-col :span="12">
                <BillingRuleCard
                  :disabled="disabled"
                  billing-mode="ON_DEMAND"
                  :field-list="policy.unitPriceOnDemandBillingPolicy"
                  :field-meta="policy.billingFieldMeta"
                ></BillingRuleCard
              ></el-col>
              <el-col :span="12" style="padding-right: 15px"
                ><BillingRuleCard
                  :disabled="disabled"
                  billing-mode="MONTHLY"
                  :field-list="policy.unitPriceMonthlyBillingPolicy"
                  :field-meta="policy.billingFieldMeta"
                ></BillingRuleCard
              ></el-col>
            </el-row>
          </template>
        </base-container>
        <base-container style="--ce-base-container-height: auto">
          <template #header>
            <span>按套餐计费</span>
          </template>
          <template #content>
            <BillingPackage
              :disabled="disabled"
              :resource-type="policy.resourceType"
              :add-package-billing="addPackageBilling"
              :edit-package-billing="editPackageBilling"
              :delete-package-billing="deletePackageBilling"
              :monthly-billing-policy="policy.unitPriceMonthlyBillingPolicy"
              :on-demand-billing-policy="policy.unitPriceOnDemandBillingPolicy"
              :field-meta="policy.billingFieldMeta"
              :package-price-billing="policy.packagePriceBillingPolicy"
            ></BillingPackage>
          </template>
        </base-container>
        <base-container
          v-if="policy.globalConfigMetaForms"
          style="--ce-base-container-height: auto"
        >
          <template #header>
            <span>计费设置</span>
          </template>
          <template #content>
            <CeForm
              :disabled="disabled"
              label-suffix=""
              require-asterisk-position="right"
              label-position="top"
              :form-view-data="policy.globalConfigMetaForms"
              v-model="policy.globalConfigMeta"
              :otherParams="{}"
            ></CeForm>
          </template>
        </base-container>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script setup lang="ts">
import type {
  BillingPolicyDetails,
  PackagePriceBillingPolicy,
} from "@/api/billing_policy/type";
import BillingRuleCard from "@/views/billing_policy/components/BillingRuleCard.vue";
import BillingPackage from "@/views/billing_policy/components/BillingPackage.vue";
import CeForm from "@commons/components/ce-form/index.vue";
import bus from "@commons/bus";
const props = withDefaults(
  defineProps<{
    modelValue: Array<BillingPolicyDetails>;
    disabled?: boolean;
  }>(),
  {
    disabled: true,
  }
);
const emit = defineEmits(["update:modeValue"]);

// 添加套餐计费
const addPackageBilling = (
  resourceType: string,
  packagePriceBillingPolicy: PackagePriceBillingPolicy
) => {
  const newValue = props.modelValue.map((item) => {
    if (item.resourceType === resourceType) {
      item.packagePriceBillingPolicy = [
        ...item.packagePriceBillingPolicy,
        packagePriceBillingPolicy,
      ];
    }
    return item;
  });
  emit("update:modeValue", newValue);
};

/**
 * 修改套餐计费
 */
const editPackageBilling = (
  resourceType: string,
  id: string,
  packagePriceBillingPolicy: PackagePriceBillingPolicy
) => {
  const newValue = props.modelValue.map((item) => {
    if (item.resourceType === resourceType) {
      item.packagePriceBillingPolicy = item.packagePriceBillingPolicy.map(
        (policy) => {
          if (policy.id === id) {
            return packagePriceBillingPolicy;
          }
          return policy;
        }
      );
    }
    return item;
  });
  emit("update:modeValue", newValue);
};

/**
 * 删除套餐计费
 */
const deletePackageBilling = (resourceType: string, id: string) => {
  const newValue = props.modelValue.map((item) => {
    if (item.resourceType === resourceType) {
      item.packagePriceBillingPolicy = item.packagePriceBillingPolicy.filter(
        (p) => p.id !== id
      );
    }
    return item;
  });
  emit("update:modeValue", newValue);
};
</script>
<style lang="scss" scoped>
:deep(.el-tabs--card > .el-tabs__header .el-tabs__item.is-active) {
  background-color: #fff;
  border-top: 2px solid #3370ff;
}
:deep(.el-tabs__nav-scroll) {
  background: #f7f9fc;
}

:deep(.el-tabs__header.is-top) {
  border: none;
}
:deep(.el-tabs--card > .el-tabs__header .el-tabs__item) {
  border: none;
}
:deep(.el-tabs--card > .el-tabs__header .el-tabs__nav) {
  border: none;
}
</style>
