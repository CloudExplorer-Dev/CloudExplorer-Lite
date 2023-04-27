<template>
  <div class="content">
    <el-button
      style="--el-button-border-color: rgba(51, 112, 255, 1)"
      @click="addCondition"
      plain
      ><el-icon><Plus /></el-icon
      ><span style="margin-left: 5px">添加条件</span> </el-button
    ><el-button
      style="--el-button-border-color: rgba(51, 112, 255, 1)"
      @click="addRelation"
      plain
      :disabled="relationDistabled"
    >
      <el-icon><Plus /></el-icon
      ><span style="margin-left: 5px">添加关系</span></el-button
    >
  </div>
</template>
<script setup lang="ts">
import type { Tree } from "@commons/components/ce-rule-tree/type";
import { computed, ref } from "vue";
const props = defineProps<{
  // 添加条件
  addCondition: () => void;
  // 添加关系
  addRelation: () => void;
  // 当前节点树
  tree: Tree;
  // 顶级树
  root: Array<Tree>;
}>();
const maxLevel = ref<number>(3);

const getLevel = (tree: Tree, prentTree: Array<Tree>): number | undefined => {
  prentTree.push(tree);
  if (tree.id === props.tree.id) {
    return prentTree.length;
  }
  if (tree.children && tree.children.length > 0) {
    for (let index = 0; index < tree.children.length; index++) {
      const element = tree.children[index];
      const l: number | undefined = getLevel(element, [...prentTree]);
      if (l) {
        return l;
      }
    }
  }
  return undefined;
};

const relationDistabled = computed(() => {
  const level = getLevel(props.root[0], []);
  return maxLevel.value <= (level ? level : 0);
});
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
