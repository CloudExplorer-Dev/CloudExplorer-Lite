<script setup lang="ts">
const props = defineProps<{
  value?: string | number;
  paddingBottom?: string | number;
}>();
</script>

<template>
  <span class="value" :style="{ 'padding-bottom': paddingBottom }">
    {{ value }}
    <slot />
  </span>
</template>
<style scoped lang="scss">
.value {
  display: inline-block;
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 22px;
  height: 22px;
  color: #1f2329;
  padding-bottom: 16px;
}
</style>
