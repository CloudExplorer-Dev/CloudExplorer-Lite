<template>
  <div class="layout-container" :style="{ border: border ? '' : 'none' }">
    <div
      class="header"
      v-if="$slots.header || $slots.btn || $slots.header_content"
    >
      <div class="title" v-if="$slots.header">
        <slot name="header"></slot>
      </div>
      <div class="auto" v-if="$slots.btn || $slots.header_content">
        <slot name="header_content"></slot>
      </div>
      <div class="btn" v-if="$slots.btn">
        <slot name="btn"></slot>
      </div>
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
  border-radius: 5px;
  .header {
    height: 50px;
    display: flex;
    align-items: center;
    font-size: 15px;
    font-weight: normal;
    .title {
      padding-left: 10px;
      color: var(--el-text-color-regular);
    }
    .auto {
      flex: 1 1 auto;
    }
    .btn {
      padding-right: 20px;
      color: var(--el-color-primary);
      cursor: pointer;
    }
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
