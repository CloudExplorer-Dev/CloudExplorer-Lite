<template>
  <div class="header">费用预览</div>
  <div class="content">
    <el-form label-position="top" label-width="100px">
      <el-form-item v-for="item in fieldList" :key="item.field">
        <template #label>
          <span class="label">{{ fieldMeta[item.field].fieldLabel }}</span>
          <span class="unit"> ({{ fieldMeta[item.field].unitLabel }}) </span>
        </template>
        <el-radio-group v-model="previewSize[item.field]">
          <el-radio
            v-for="(p, index) in previewSizeList(item.field)"
            :key="index"
            :label="p"
            border
            >{{ p }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
  </div>
  <div class="line"></div>
  <div class="footer">
    <span class="type">实例规格: {{ instanceTypeLabel }} </span>
    <div>
      <div class="price">{{ price }}&nbsp;{{ priceUnitLabel }}</div>
      <div class="price">{{ pricePreView }}&nbsp;{{ notCurrentUnitLabel }}</div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, computed } from "vue";
import type { BillingField, BillingFieldMeta } from "@/api/billing_policy/type";
import type { SimpleMap } from "@commons/api/base/type";
import { Decimal } from "decimal.js";
const props = defineProps<{
  billingMode: "ON_DEMAND" | "MONTHLY";
  /**
   * 字段列表
   */
  fieldList: Array<BillingField>;
  /**
   * 字段元数据
   */
  fieldMeta: SimpleMap<BillingFieldMeta>;
}>();
const priceUnitLabel = computed(() => {
  return "元" + "/" + (props.billingMode === "ON_DEMAND" ? "小时" : "月");
});
const notCurrentUnitLabel = computed(() => {
  return "元" + "/" + (props.billingMode !== "ON_DEMAND" ? "小时" : "月");
});
const previewSize = ref<SimpleMap<number>>({});
const instanceTypeLabel = computed(() => {
  return (props.fieldList || [])
    .map((field) => {
      return (
        (previewSize.value[field.field]
          ? previewSize.value[field.field]
          : "-") + props.fieldMeta[field.field].unitLabel
      );
    })
    .join("");
});
/**
 * 单位数据
 */
const price = computed(() => {
  return props.fieldList
    .map((field) => {
      return previewSize.value[field.field]
        ? new Decimal(previewSize.value[field.field]).mul(
            new Decimal(field.price)
          )
        : new Decimal(0);
    })
    .reduce((pre, next) => pre.add(next))
    .toFixed(3);
});
/**
 * 获取当前月份的天数
 */
const currentDays = () => {
  const date = new Date();
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const d = new Date(year, month, 0);
  return d.getDate();
};
const pricePreView = computed(() => {
  if (props.billingMode === "ON_DEMAND") {
    return new Decimal(price.value)
      .mul(new Decimal(currentDays()))
      .mul(new Decimal(24))
      .toFixed(3);
  } else {
    return new Decimal(price.value)
      .div(new Decimal(currentDays()).mul(new Decimal(24)))
      .toFixed(3);
  }
});
/**
 * 可选实例大小
 * @param field 字段
 */
const previewSizeList = (field: string) => {
  if (props.fieldMeta[field].meta.previewList) {
    return props.fieldMeta[field].meta.previewList;
  } else {
    return [2, 4, 8, 16];
  }
};
</script>
<style lang="scss" scoped>
:deep(.el-radio__input) {
  display: none;
}
:deep(.el-radio) {
  margin-right: 12px;
}
.header {
  padding: 16px 16px 0 16px;
  box-sizing: border-box;
}
.content {
  padding: 16px 16px 0 16px;
  box-sizing: border-box;
  .label {
    font-weight: 400;
    font-size: 14px;
    line-height: 22px;
    color: #1f2329;
  }
  .unit {
    font-weight: 400;
    font-size: 14px;
    line-height: 22px;
    color: rgba(143, 149, 158, 1);
  }
}
.footer {
  display: flex;
  justify-content: space-between;
  padding: 16px 16px 16px 16px;
  box-sizing: border-box;
  font-size: 14px;
  line-height: 22px;
  .type {
    font-weight: 400;
    color: #1f2329;
  }
  .price {
    font-weight: 500;
    color: #f54a45;
  }
}
.line {
  height: 1px;
  width: 100%;
  background-color: #dee0e3;
}
</style>
