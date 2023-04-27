<template>
  <div>这是一条模板数据</div>
</template>
<script setup lang="ts">
const validate = () => {
  return Promise.resolve();
};
defineExpose({ validate });
</script>
<style lang="scss" scoped></style>
