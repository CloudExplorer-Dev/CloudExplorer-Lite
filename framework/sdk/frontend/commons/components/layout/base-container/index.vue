<template>
  <div class="layout-container">
    <div
      class="header"
      v-if="$slots.header || $slots.btn || $slots.header_content"
    >
      <div class="title" v-if="$slots.header">
        <slot name="header"> </slot>
      </div>
      <div class="auto" v-if="$slots.btn || $slots.header_content">
        <slot name="header_content"></slot>
      </div>
      <div class="btn" v-if="$slots.btn">
        <slot name="btn"></slot>
      </div>
    </div>
    <div class="content" v-if="$slots.content">
      <slot name="content"></slot>
    </div>
    <div class="footer" v-if="$slots.default">
      <slot></slot>
    </div>
  </div>
</template>
<script setup lang="ts"></script>
<style lang="scss" scoped>
.layout-container {
  margin: 0 0 20px 0;
  overflow-y: hidden;
  overflow-x: hidden;
  .header {
    height: 50px;
    display: flex;
    align-items: center;
    :deep(.title) {
      position: relative;
      span {
        font-style: normal;
        font-weight: 500;
        font-size: 16px;
        line-height: 24px;
        color: #1f2329;
        margin-left: 8px;
      }
      &::before {
        content: "";
        position: absolute;
        left: 0;
        top: 45%;
        transform: translateY(-50%);
        height: 16px;
        width: 2px;
        background: var(--el-color-primary);
      }
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
  .content {
    padding: 0px;
  }
  .footer {
    display: flex;
    justify-content: flex-end;
    padding: 20px;
  }
}
</style>
