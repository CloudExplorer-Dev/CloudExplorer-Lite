<template>
  <el-card class="box-card" shadow="never" style="width: 100%; height: auto">
    <template #header>
      <div class="card-header">
        <span
          >{{ billingMode === "ON_DEMAND" ? "按需按量" : "包年包月" }}
        </span>
        <span></span>
        <div>
          <el-popover
            placement="top-start"
            popper-class="price_preview"
            width="auto"
            popper-style="--el-popover-padding:0px"
            :visible="pricePreview"
          >
            <PricePreview
              @close="close"
              :billing-mode="billingMode"
              :field-list="fieldList"
              :field-meta="fieldMeta"
            ></PricePreview>
            <template #reference>
              <span @click="view">费用预览</span>
            </template>
          </el-popover>
        </div>
      </div>
    </template>
    <el-form label-position="top" label-width="100px">
      <el-form-item
        v-for="item in fieldList"
        :key="item.field"
        :label="fieldMeta[item.field].fieldLabel"
      >
        <el-input
          :disabled="disabled"
          v-number="{
            max: 100000000,
            min: 0,
            fixed: 3,
            type: 'float',
          }"
          v-model="item.price"
          class="input-with-select"
        >
          <template #prepend>
            <span>1 {{ fieldMeta[item.field].unitLabel }}</span>
          </template>
          <template #append>
            <span>{{
              fieldMeta[item.field].priceLabel +
              "/" +
              (billingMode === "ON_DEMAND" ? "小时" : "月")
            }}</span>
          </template>
        </el-input>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { BillingField, BillingFieldMeta } from "@/api/billing_policy/type";
import type { SimpleMap } from "@commons/api/base/type";
import PricePreview from "@/views/billing_policy/components/PricePreview.vue";
import bus from "@commons/bus/index";
defineProps<{
  billingMode: "ON_DEMAND" | "MONTHLY";
  /**
   * 字段列表
   */
  fieldList: Array<BillingField>;
  /**
   * 字段元数据
   */
  fieldMeta: SimpleMap<BillingFieldMeta>;

  disabled: boolean;
}>();
const pricePreview = ref<boolean>(false);
const view = () => {
  pricePreview.value = true;
};
const close = () => {
  pricePreview.value = false;
};
onMounted(() => {
  bus.on("closePricePreview", () => {
    close();
  });
});
</script>
<style lang="scss" scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  span {
    &:last-child {
      color: #3370ff;
      font-weight: 400;
      font-size: 14px;
      line-height: 22px;
      cursor: pointer;
    }
  }
}
</style>
