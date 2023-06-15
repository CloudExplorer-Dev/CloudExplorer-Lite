<template>
  <div class="right_content">
    <div class="empty_content">
      <span
        >暂无数据，请
        <span class="add_policy" @click="bus.emit('update:operate', 'CREATE')"
          >添加私有云计费策略</span
        ></span
      >
    </div>
  </div>
</template>
<script setup lang="ts">
import bus from "@commons/bus";
</script>
<style lang="scss" scoped>
.right_content {
  width: calc(100% - 215px);
  box-sizing: border-box;
  padding-left: 24px;
  height: 100%;
  border-left: 1px solid rgba(31, 35, 41, 0.15);
  .empty_content {
    height: 100%;
    width: 100%;
    display: flex;
    justify-content: center;
    span {
      margin-top: 286px;
    }
    .add_policy {
      color: rgba(51, 112, 255, 1);
      cursor: pointer;
    }
  }
}
</style>
