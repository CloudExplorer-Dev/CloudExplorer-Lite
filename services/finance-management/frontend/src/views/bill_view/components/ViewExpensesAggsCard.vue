<template>
  <div class="info-card view-expenses-aggs-card" v-loading="loading">
    <span class="title">{{ title }}</span>
    <div class="money">
      {{ CurrencyFormat.format(expenses.current) }}
    </div>
    <div class="compare">
      {{ compareTitle }}
      <span :class="expenses.current > expenses.up ? 'up' : 'down'"
        >{{ scale }}
      </span>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from "vue";
import { computed } from "vue";
import CurrencyFormat from "@commons/utils/currencyFormat";
const loading = ref<boolean>(false);
const expenses = ref<{ current: number; up: number }>({ current: 0, up: 0 });
const props = defineProps<{
  // 获取数据
  getAggsCount: () => Promise<{ current: number; up: number }>;
  // 标题
  title: string;
  /**
   * 比较标题
   */
  compareTitle: string;
}>();
const scale = computed(() => {
  const s =
    expenses.value.up == 0
      ? CurrencyFormat.format(expenses.value.current)
      : (
          ((expenses.value.current - expenses.value.up) / expenses.value.up) *
          100
        ).toFixed(2) + "%";
  return expenses.value.current > expenses.value.up ? "+" + s : s;
});
onMounted(() => {
  loading.value = true;
  props
    .getAggsCount()
    .then((count) => {
      expenses.value = count;
      loading.value = false;
    })
    .catch(() => {
      loading.value = false;
    });
});
</script>
<style lang="scss" scoped>
.view-expenses-aggs-card {
  height: 94px;
}
.info-card {
  background: #ffffff;
  border-radius: 4px;
  padding: 24px;
  overflow: hidden;
  .title {
    margin: 4px 0 0 4px;
    height: 22px;
    font-size: 14px;
    font-weight: 500;
    color: #1f2329;
  }
  .money {
    margin-top: 6px;
    font-size: 28px;
    line-height: 36px;
    color: #1f2329;
  }
  .compare {
    margin-top: 7px;
    height: 22px;
    color: #1f2329;
    line-height: 22px;
  }
}
.up {
  color: rgba(245, 74, 69, 1);
  font-weight: 400;
  font-size: 16px;
  line-height: 24px;
}
.down {
  color: rgba(52, 199, 36, 1);
  font-weight: 400;
  font-size: 16px;
  line-height: 24px;
}
</style>
