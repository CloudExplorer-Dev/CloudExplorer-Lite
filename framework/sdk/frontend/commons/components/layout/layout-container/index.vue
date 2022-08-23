<template>
  <div class="layout-container" :style="{ border: border ? '' : 'none' }">
    <div class="header" v-if="$slots.header">
      <slot name="header"></slot>
    </div>
    <div class="line" v-if="$slots.header && $slots.content && border"></div>
    <div class="content" v-if="$slots.content">
      <slot name="content"></slot>
    </div>
    <div class="line" v-if="$slots.content && $slots.default && border"></div>
    <div class="footer" v-if="$slots.default">
      <slot></slot>
    </div>
  </div>
</template>
<script setup lang="ts">
import { defineProps } from "vue";
defineProps({
  border: {
    type: Boolean,
    default: true,
  },
});
</script>
<style lang="scss" scoped>
.layout-container {
  margin: 0 0 20px 0;
  border: 1px solid var(--el-border-color);
  overflow-y: auto;
  overflow-x: hidden;
  border-radius: 10px;
  .header {
    height: 50px;
    padding-left: 10px;
    display: flex;
    align-items: center;
    font-size: 15px;
    color: var(--el-text-color-regular);
    font-weight: normal;
  }
  .line {
    height: 1px;

    background-color: var(--el-border-color);
  }
  .content {
    padding: 20px;
  }
  .footer {
    display: flex;
    justify-content: flex-end;
    padding: 20px;
  }
}
</style>
