<template>
  <el-drawer
    v-model="drawer"
    title="汇率设置"
    direction="rtl"
    :before-close="close"
  >
    <div v-loading="loading">
      <CurrencyItem
        :key="key"
        v-for="key in Object.keys(supportCurrencyObj)"
        v-bind:model-value="supportCurrencyObj[key]"
        @update:model-value="change(key, $event)"
      ></CurrencyItem>
    </div>
    <template #footer>
      <el-button @click="close">取消</el-button
      ><el-button type="primary" @click="save">保存</el-button>
    </template>
  </el-drawer>
</template>
<script setup lang="ts">
import type { Currency } from "@commons/api/bil_view/type";
import CurrencyItem from "@/views/bill_view/components/CurrencyItem.vue";
import { ref, computed } from "vue";
import billViewAPi from "@/api/bill_view/index";
const emit = defineEmits(["change"]);
/**
 * 汇率列表
 */
const currencyList = ref<Array<Currency>>([]);
/**
 * 加载器
 */
const loading = ref<boolean>(false);
/**
 * 支持的币种汇率
 */
const supportCurrencyObj = computed(() => {
  return currencyList.value
    .filter((c) => c.code !== "CNY")
    .map((c) => ({ [c.code]: c }))
    .reduce(
      (pre, next) => ({
        ...pre,
        ...next,
      }),
      {}
    );
});
/**
 * 修改汇率
 * @param code 币种
 * @param c 详情
 */
const change = (code: string, c: Currency) => {
  currencyList.value
    .filter((item) => item.code === code)
    .forEach((item) => (item.exchangeRate = c.exchangeRate));
};
/**
 * 抽屉开关
 */
const drawer = ref<boolean>(false);
/**
 * 打开抽屉
 */
const open = () => {
  drawer.value = true;
  billViewAPi.listCurrency(loading).then((ok) => {
    currencyList.value = ok.data;
  });
};
/**
 * 保存汇率
 */
const save = () => {
  billViewAPi.batchUpdateCurrency(currencyList.value, loading).then(() => {
    close();
    emit("change");
  });
};
/**
 * 关闭抽屉
 */
const close = () => {
  drawer.value = false;
};
defineExpose({ open, close });
</script>
<style lang="scss"></style>
