<script setup lang="ts">
import CeIcon from "../../ce-icon/index.vue";
import type { Module } from "@commons/api/module/type";

const props = withDefaults(
  defineProps<{
    /**
     * 数据
     */
    startMenuItem: Module;
    /**
     * 鼠标移入回掉函数
     */
    hoverHandler?: () => void;
    /**
     * 鼠标移出回掉函数
     */
    leaveHandler?: () => void;
    /**
     * 是否展示当前
     */
    hover: boolean;
    /**
     * 是否是服务目录
     */
    rootItem?: boolean;
  }>(),
  {
    hover: false,
  }
);
</script>
<template>
  <div
    class="item"
    :class="hover ? 'hover_item' : ''"
    :style="{
      height: props.rootItem ? '50px' : 'var(--ce-star-menu-item-height)',
    }"
    @mouseenter="props.hoverHandler ? props.hoverHandler() : null"
    @mouseleave="props.leaveHandler ? props.leaveHandler() : null"
  >
    <div class="icon">
      <CeIcon
        size="var(--ce-star-menu-icon-width, 18px)"
        color="#1F2329"
        :code="props.startMenuItem.icon"
      />
    </div>
    <div class="text">{{ props.startMenuItem.name }}</div>
    <div class="handle">
      <slot></slot>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.item {
  height: var(--ce-star-menu-item-height);
  display: flex;
  width: 100%;
  justify-content: space-around;
  align-items: center;
  overflow: hidden;
  transition: 0.3s;
  color: #1f2329;

  &:hover {
    background-color: var(--ce-star-menu-hover-bg-color);
    cursor: pointer;
    > .handle {
      width: 20%;
    }
  }
  .icon {
    font-size: 25px;
    height: var(--ce-star-menu-item-icon-size);
  }
  .text {
    overflow: hidden;
    display: none;
    white-space: nowrap;
    line-height: var(--ce-star-menu-item-height);
    width: 50%;
  }
  .handle {
    display: none;
    flex-wrap: nowrap;
    justify-content: space-around;
    line-height: var(--ce-star-menu-item-height);
    width: var(--ce-star-menu-item-height);
  }
}
.hover_item {
  .icon {
    width: 30%;
    height: var(--ce-star-menu-item-icon-size);
    .el-icon {
      width: 100%;
    }
  }
  .text {
    text-overflow: ellipsis;
    width: 50%;
    display: block;
  }
  .handle {
    width: 20%;
    display: flex;
    .el-icon {
      width: 100%;
    }
  }
}
</style>
