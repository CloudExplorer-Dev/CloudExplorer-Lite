<script setup lang="ts">
const props = withDefaults(
  defineProps<{
    contentBorder: boolean;
  }>(),
  {
    contentBorder: false,
  }
);
</script>
<template>
  <div class="base-container">
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
    <div
      class="content"
      v-if="$slots.content"
      :style="{ border: contentBorder ? '' : 'none' }"
    >
      <slot name="content"></slot>
    </div>
    <div class="form" v-if="$slots.form">
      <slot name="form"></slot>
    </div>
    <div class="formFooter" v-if="$slots.formFooter">
      <div class="footer-inner">
        <slot name="formFooter"></slot>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.base-container {
  margin: 0 0 20px 0;
  overflow-y: hidden;
  overflow-x: hidden;
  height: calc(100% - 20px);
  .header {
    display: flex;
    margin-bottom: 16px;
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
    :deep(.btn) {
      color: var(--el-color-primary);
      cursor: pointer;
    }
  }
  .content {
    padding: 0px;
    border: 1px solid var(--el-border-color);
  }
  .form {
    width: 800px;
    margin-left: auto;
    margin-right: auto;
  }
  .formFooter {
    position: absolute;
    display: flex;
    width: 100%;
    height: 80px;
    right: 0px;
    bottom: 0px;
    box-shadow: 0 -2px 2px rgb(0 0 0 / 4%);
    justify-content: center;
    align-items: center;
    z-index: 10;
    border-radius: 0 0 var(--ce-main-content-border-radius, 10px)
      var(--ce-main-content-border-radius, 10px);
    background-color: var(--ce-main-content-bg-color, #fff);
    :deep(.footer-inner) {
      width: 800px;
      text-align: right;
      button {
        min-width: 80px;
      }
    }
  }
}
</style>
