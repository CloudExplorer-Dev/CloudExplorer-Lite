<script setup lang="ts">
const props = defineProps<{
  label?: string;
}>();
</script>

<template>
  <span class="label">
    {{ label }}
    <slot />
  </span>
</template>
<style scoped lang="scss">
.label {
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 22px;
  color: #8f959e;
}
</style>
