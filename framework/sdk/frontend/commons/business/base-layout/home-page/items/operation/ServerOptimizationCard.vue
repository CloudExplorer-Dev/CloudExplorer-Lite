<script lang="ts" setup>
import { ref, watch } from "vue";
import type { ListOptimizationRequest } from "@commons/api/resource_optimization/type";
import ResourceOptimizationViewApi from "@commons/api/resource_optimization";

const props = withDefaults(
  defineProps<{
    req: ListOptimizationRequest;
    show: boolean;
  }>(),
  {}
);

const loading = ref(false);

const value = ref(0);

function getOptimizeSuggests() {
  if (!props.show) {
    return;
  }
  ResourceOptimizationViewApi.listServer(props.req, loading).then((res) => {
    value.value = res.data.total;
  });
}

watch(
  () => props,
  (data) => {
    getOptimizeSuggests();
  },
  { immediate: true, deep: true }
);
</script>
<template>
  <div class="div-card" v-loading="loading">
    <div class="text">{{ req.name }}</div>
    <div class="text">
      <span class="main">
        {{ value }}
      </span>
      台
    </div>
  </div>
</template>

<style scoped lang="scss">
.div-card {
  cursor: pointer;
  background: #ffffff;
  border: 1px solid #dee0e3;
  border-radius: 4px;
  padding: 16px;

  .text {
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    line-height: 22px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  .main {
    font-style: normal;
    font-weight: 500;
    font-size: 28px;
    line-height: 36px;
  }
}

.div-card:hover {
  box-shadow: 0 6px 24px rgba(31, 35, 41, 0.08);
}
</style>
