<script setup lang="ts">
import { ref, watch } from "vue";
import type { WorkspaceTree } from "../../api/workspace/type";
import type { ElTree } from "element-plus";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const props = defineProps<{
  /**
   * 树形结构数据
   */
  treeData: Array<WorkspaceTree>;
  /**
   * 筛选按钮触发事件
   */
  handleSelect: () => void;
  /**
   * 重置按钮触发事件
   */
  handleReset: () => void;
}>();

const filterText = ref("");
const treeRef = ref<InstanceType<typeof ElTree>>();
watch(filterText, (val) => {
  treeRef.value?.$el?.scrollTo(0, 0);
  treeRef.value!.filter(val);
});
const filterNode = (value: string, data: WorkspaceTree) => {
  if (!value) return true;
  return data.name.includes(value);
};

/**
 * 获取选中的节点key
 */
const getCheckedKeys = (leafOnly: boolean) => {
  return treeRef.value?.getCheckedKeys(leafOnly);
};

/**
 * 获取选中的节点Label
 */
const getCheckedLabels = (leafOnly: boolean) => {
  return treeRef.value
    ?.getCheckedNodes()
    .filter((item: any) => getCheckedKeys(leafOnly)?.includes(item.id))
    .map((item: any) => item.name)
    .join(" | ");
};

/**
 * 获取ElTree的Ref属性
 */
const getTreeRef = () => {
  if (!treeRef.value) return;
  return treeRef.value;
};

defineExpose({
  getTreeRef,
  getCheckedKeys,
  getCheckedLabels,
});
</script>

<template>
  <el-input
    v-model="filterText"
    :placeholder="$t('commons.btn.grope', '搜索')"
    size="small"
  />
  <el-tree
    class="filter-tree"
    ref="treeRef"
    node-key="id"
    :props="{ label: 'name' }"
    :data="treeData"
    :filter-node-method="filterNode"
    multiple
    show-checkbox
    style="width: 100%"
  />
  <div class="line" />
  <el-button
    @click="handleSelect()"
    :disabled="treeRef?.getCheckedKeys().length === 0"
    link
    class="btn"
    :class="{ active: treeRef?.getCheckedKeys().length !== 0 }"
  >
    {{ t("commons.btn.filter", "筛选") }}
  </el-button>
  <el-button @click="handleReset()" link class="btn active">
    {{ t("commons.btn.reset", "重置") }}
  </el-button>
</template>

<style lang="scss" scoped>
.line {
  margin: 6px 0px;
  border-bottom: 1px solid var(--el-border-color);
}
.btn {
  color: var(--el-text-color-regular);
  cursor: pointer;
  font-size: var(--el-font-size-small);
}
.active:hover {
  color: var(--el-color-primary);
}
.filter-tree {
  max-height: 200px;
  overflow: auto;
}
:deep(.el-tree-node > .el-tree-node__children) {
  overflow: visible !important;
}
</style>
