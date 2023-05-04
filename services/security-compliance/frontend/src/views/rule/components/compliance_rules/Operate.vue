<template>
  <div class="content">
    <el-button
      style="--el-button-border-color: rgba(51, 112, 255, 1)"
      @click="addCondition"
      plain
      ><el-icon><Plus /></el-icon><span style="margin-left: 5px">添加条件</span>
    </el-button>
  </div>
</template>
<script setup lang="ts">
import type { Tree } from "@commons/components/ce-rule-tree/type";
defineProps<{
  // 添加条件
  addCondition: () => void;
  // 添加关系
  addRelation: () => void;
  // 当前节点树
  tree: Tree;
  // 顶级树
  root: Array<Tree>;
}>();
</script>
<style lang="scss" scoped>
.content {
  .is-disabled {
    span {
      color: var(--el-button-disabled-text-color);
    }
    .el-icon {
      color: var(--el-button-disabled-text-color);
    }
  }
  span {
    color: rgba(51, 112, 255, 1);
  }
  .el-icon {
    color: rgba(51, 112, 255, 1);
  }
}
:deep(.el-button) {
  padding: 5px 12px;
}
</style>
