<script setup lang="ts">
const props = defineProps<{
  title?: string;
}>();
</script>

<template>
  <div class="title">
    {{ title }}
    <slot />
  </div>
</template>
<style scoped lang="scss">
.title {
  border-left: #3370ff solid 3px;
  padding-left: 8px;
  font-style: normal;
  font-weight: 500;
  font-size: 14px;
}
</style>
