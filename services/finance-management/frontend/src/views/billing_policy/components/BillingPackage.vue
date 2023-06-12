<template>
  <div>
    <el-button type="primary" @click="openAddPackage">添加套餐</el-button>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="name" label="套餐名称" />
      <el-table-column
        :prop="field.prop"
        :label="field.label"
        v-for="(field, index) in tableDynamicFieldList"
        :key="index"
      />
      <el-table-column prop="onDemand" label="按需按量 (元/小时)" />
      <el-table-column prop="monthly" label="包年包月 (元/月)" />
      <fu-table-operations
        fixed="right"
        v-bind="tableOperations"
        width="120px"
        fix
      />
    </el-table>
    <OperatePackageBilling
      :add-package-billing="addPackageBilling"
      :edit-package-billing="editPackageBilling"
      :on-demand-billing-policy="onDemandBillingPolicy"
      :monthly-billing-policy="monthlyBillingPolicy"
      :field-meta="fieldMeta"
      ref="operatePackageBillingRef"
    ></OperatePackageBilling>
  </div>
</template>
<script setup lang="ts">
import type {
  PackagePriceBillingPolicy,
  BillingFieldMeta,
  BillingField,
} from "@/api/billing_policy/type";
import type { SimpleMap } from "@commons/api/base/type";
import { TableOperations } from "@commons/components/ce-table/type";
import { computed, ref } from "vue";
import OperatePackageBilling from "@/views/billing_policy/components/OperatePackageBilling.vue";

const props = defineProps<{
  /**
   * 添加套餐计费
   */
  addPackageBilling: (
    resourceType: string,
    packagePriceBillingPolicy: PackagePriceBillingPolicy
  ) => void;
  /**
   * 修改套餐计费
   */
  editPackageBilling: (
    resourceType: string,
    id: string,
    packagePriceBillingPolicy: PackagePriceBillingPolicy
  ) => void;
  /**
   * 删除套餐策略
   */
  deletePackageBilling: (resourceType: string, id: string) => void;
  /**
   * 按量
   */
  onDemandBillingPolicy: Array<BillingField>;
  /**
   * 包年包月
   */
  monthlyBillingPolicy: Array<BillingField>;
  /**
   * 套餐计费
   */
  packagePriceBilling: Array<PackagePriceBillingPolicy>;
  /**
   * 字段元数据
   */
  fieldMeta: SimpleMap<BillingFieldMeta>;
  /**
   * 资源类型
   */
  resourceType: string;
}>();
/**
 * 操作套餐计费组件
 */
const operatePackageBillingRef =
  ref<InstanceType<typeof OperatePackageBilling>>();

/**
 *打开新增套餐计费抽屉
 */
const openAddPackage = () => {
  operatePackageBillingRef.value?.open();
};

/**
 * 添加套餐计费策略
 * @param packagePriceBillingPolicy
 */
const addPackageBilling = (
  packagePriceBillingPolicy: PackagePriceBillingPolicy
) => {
  if (checkPackageBilling(packagePriceBillingPolicy)) {
    props.addPackageBilling(props.resourceType, packagePriceBillingPolicy);
    return true;
  }
  return false;
};
/**
 * 校验套餐计费
 * @param packagePriceBillingPolicy 套餐计费
 * @param id 主键id
 */
const checkPackageBilling = (
  packagePriceBillingPolicy: PackagePriceBillingPolicy,
  id?: string
) => {
  if (id) {
    return !tableData.value
      .filter((item) => item.id !== id)
      .some((item) => {
        return packagePriceBillingPolicy.billPolicyFields.every(
          (p) =>
            item.billPolicyFields.find((f) => f.field === p.field)?.number ===
            p.number
        );
      });
  }
  return !tableData.value.some((item) => {
    return packagePriceBillingPolicy.billPolicyFields.every(
      (p) =>
        item.billPolicyFields.find((f) => f.field === p.field)?.number ===
        p.number
    );
  });
};

/**
 * 修改套餐计费策略
 * @param id 计费策略id
 * @param packagePriceBillingPolicy 套餐计费策略
 */
const editPackageBilling = (
  id: string,
  packagePriceBillingPolicy: PackagePriceBillingPolicy
) => {
  if (checkPackageBilling(packagePriceBillingPolicy, id)) {
    props.editPackageBilling(props.resourceType, id, packagePriceBillingPolicy);
    return true;
  } else {
    return false;
  }
};

/**
 *表单动态字段
 */
const tableDynamicFieldList = computed(() => {
  return Object.keys(props.fieldMeta).map((key) => {
    return {
      prop: key,
      label:
        props.fieldMeta[key].fieldLabel +
        "(" +
        props.fieldMeta[key].unitLabel +
        ")",
    };
  });
});

/**
 * 表单数据
 */
const tableData = computed(() => {
  return (props.packagePriceBilling || []).map((p) => mapToTableItem(p));
});

/**
 * 转换
 */
const mapToTableItem = (packagePriceBilling: PackagePriceBillingPolicy) => {
  return {
    ...packagePriceBilling.billPolicyFields
      .map((item) => ({ [item.field]: item.number }))
      .reduce((pre, next) => ({
        ...pre,
        ...next,
      })),
    id: packagePriceBilling.id,
    billPolicyFields: packagePriceBilling.billPolicyFields,
    name: packagePriceBilling.name,
    onDemand: packagePriceBilling.onDemand,
    monthly: packagePriceBilling.monthly,
  };
};

/**
 * 打开套餐计费修改抽屉
 * @param row 需要修改的这一行数据
 */
const openUpdateBilling = (row: PackagePriceBillingPolicy) => {
  operatePackageBillingRef.value?.open(row);
};
/**
 * 删除计费策略
 * @param row 需要删除的这一行
 */
const deleteBilling = (row: PackagePriceBillingPolicy) => {
  props.deletePackageBilling(props.resourceType, row.id);
};
const tableOperations = new TableOperations(
  [
    TableOperations.buildButtons().newInstance(
      "编辑",
      "primary",
      openUpdateBilling,
      "EditPen",
      undefined
    ),
    TableOperations.buildButtons().newInstance(
      "删除",
      "primary",
      deleteBilling,
      "Delete",
      undefined
    ),
  ],
  "label"
);
</script>
<style lang="scss"></style>
