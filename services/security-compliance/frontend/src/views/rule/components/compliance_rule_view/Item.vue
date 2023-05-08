<template>
  <div class="content">
    <div class="field">{{ field }}</div>
    <div class="text">
      <span>{{ compare }}</span
      ><span v-if="value">/{{ value }}</span>
    </div>
  </div>
</template>
<script setup lang="ts">
defineProps<{
  field: string;
  compare: string;
  value: string | number | null;
}>();
</script>
<style lang="scss" scoped>
.content {
  .field {
    color: #1f2329;
    font-size: 12px;
    font-weight: 500;
    line-height: 22px;
    width: 100%;
  }
  .text {
    color: #646a73;
    font-size: 12px;
    font-weight: 400;
    line-height: 22px;
  }
}
</style>
