<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import type { Condition } from "../ce-table/type";
import type { Conditions } from "../ce-table/type";
import TreeFilter from "@commons/components/table-filter/TreeFilter.vue";
import { tree } from "@commons/api/organization";
import { workspaceTree } from "@commons/api/workspace";

const props = defineProps<{
  treeType: string;
  leafOnly: boolean;
  field: string;
  label: string;
  popoverRef: any;
  tableRef: any;
}>();

const treeData = ref();
const treeRef = ref();

const selectedIds = computed(() => getSelectedIds(props.leafOnly));

const selectedNames = computed(() => treeRef.value?.getCheckedLabels(false));

const getSelectedIds = (leafOnly: boolean) => {
  return treeRef.value?.getCheckedKeys(leafOnly);
};

/**
 * 筛选按钮事件
 */
const handleSelect = () => {
  props.popoverRef.hide();
  const filter: Condition = {
    field: props.field,
    label: props.label,
    value: selectedIds,
    valueLabel: selectedNames.value,
  };
  const conditions: Conditions = {};
  conditions[filter.field] = filter;

  props.tableRef.search(props.tableRef.getTableSearch(conditions).conditions);
};

/**
 * 重置按钮事件
 */
const handleReset = () => {
  treeRef.value?.getTreeRef().setCheckedNodes([], false);
  props.popoverRef?.hide();
  props.tableRef.handleClear(props.field);
};

/**
 * 取消选中
 */
const cancelChecked = () => {
  treeRef.value?.getTreeRef().setCheckedNodes([], false);
};

defineExpose({
  cancelChecked,
  getSelectedIds,
});

onMounted(() => {
  if (props.treeType === "org") {
    tree().then((res) => {
      treeData.value = res.data;
    });
  } else {
    workspaceTree().then((res) => {
      treeData.value = res.data;
    });
  }
});
</script>
<template>
  <TreeFilter
    ref="treeRef"
    :handle-reset="handleReset"
    :handle-select="handleSelect"
    :tree-data="treeData"
  />
</template>
