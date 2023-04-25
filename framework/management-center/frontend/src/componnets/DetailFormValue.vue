<script setup lang="ts">
const props = defineProps<{
  value?: string | number;
}>();
</script>

<template>
  <div class="value">
    {{ value }}
    <slot />
  </div>
</template>
<style scoped lang="scss">
.value {
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 22px;
  color: #1f2329;
  padding-bottom: 16px;
}
</style>
