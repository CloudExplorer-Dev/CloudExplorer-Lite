<template>
  <!-- 叶子节点 -->
  <div
    class="rule-tree-node"
    :class="
      operate ? 'left-horizontal-up-child-node' : 'left-horizontal-child-node'
    "
  >
    <div style="height: 100%">
      <div style="height: 100%; display: flex" v-if="$slots.content">
        <slot name="content"></slot>
        <slot name="delete"></slot>
      </div>

      <div v-else style="height: 100%; display: flex">
        <el-select></el-select>
        <el-select></el-select>
        <el-select></el-select>
        <el-select></el-select>
        <slot name="delete"></slot>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
withDefaults(
  defineProps<{
    operate?: boolean;
  }>(),
  {
    operate: false,
  }
);
</script>
<style lang="scss" scoped>
.rule-tree-node {
  position: relative;
  height: var(--tree-node-leaf-height, 48px);
  padding: var(--tree-node-leaf-height-padding, 10px) 0;
  box-sizing: border-box;
}
// 直线节点
.left-horizontal-child-node {
  &::before {
    content: "";
    position: absolute;
    border-top: 1px solid #ccc;
    top: 50%;
    left: calc(-1 * var(--tree-node-leaf-width, 40px));
    height: 100%;
    width: var(--tree-node-leaf-width, 40px);
  }
}
.left-horizontal-up-child-node {
  &::before {
    content: "";
    position: absolute;
    border-bottom: 1px dashed #ccc;
    top: calc(-1 * var(--vertical-bottom) + 50%);
    left: calc(-1 * var(--tree-node-leaf-width, 40px));
    height: var(--vertical-bottom);
    width: var(--tree-node-leaf-width, 40px);
    border-left: 1px dashed #ccc;
  }
}
</style>
