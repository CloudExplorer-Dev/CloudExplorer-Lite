<script setup lang="ts">
import CeIcon from "../../ce-icon/index.vue";
import type { StarMenuItem } from "./type";
import { ref } from "vue";
const props = defineProps<{
  /**
   * 数据
   */
  starMenuItem: StarMenuItem;
  /**
   * 鼠标移入回掉函数
   */
  hoverHander?: () => void;
  /**
   * 鼠标移出回掉函数
   */
  leaveHander?: () => void;
  /**
   * 是否是服务目录
   */
  rootItem?: boolean;
}>();
</script>
<template>
  <div
    class="item"
    :style="{
      height: props.rootItem ? '50px' : 'var(--ce-star-menu-item-height)',
    }"
    @mouseenter="props.hoverHander ? props.hoverHander() : null"
    @mouseleave="props.leaveHander ? props.leaveHander() : null"
  >
    <div class="icon">
      <CeIcon size="28px" :code="props.starMenuItem.icon"></CeIcon>
    </div>
    <div class="text">{{ props.starMenuItem.title }}</div>
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
</style>
