<template>
  <div class="info-card view-expenses-aggs-card" v-loading="loading">
    <span class="title">{{ title }}</span>
    <div class="money">¥{{ expenses }}</div>
    <div class="compare">较上月 <span class="up"></span></div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from "vue";
const loading = ref<boolean>(false);
const expenses = ref<number>(0);
const props = defineProps<{
  // 获取数据
  getAggsCount: () => Promise<number>;
  // 标题
  title: string;
}>();
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
</style>
