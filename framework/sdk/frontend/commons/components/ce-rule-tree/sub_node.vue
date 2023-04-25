<template>
  <childNode
    ref="cNode"
    :readerOperate="readerOperate"
    :reader="reader"
    :pid="pid"
    v-for="t in trees"
    @updateTreeValue="updateTreeValue(t, $event)"
    :tree="t"
    :key="t.id"
    :style="{
      '--vertical-top': getHeight([t]),
      '--vertical-bottom': getVerticalBottom([t]),
      '--tree-node-leaf-height': leftHeight + 'px',
    }"
  >
    <sub_node
      :leftHeight="leftHeight"
      :readerOperate="readerOperate"
      :reader="reader"
      :pid="t.id"
      v-if="t.children && t.children.length > 0"
      :trees="t.children"
      ref="sNode"
    >
    </sub_node>
  </childNode>
</template>
<script setup lang="ts">
import { ref } from "vue";
import childNode from "@commons/components/ce-rule-tree/child_node.vue";
import type { TreeItem, Tree } from "@commons/components/ce-rule-tree/type";
import type { VNode } from "vue";

const props = defineProps<{
  trees: Array<Tree>;
  pid?: string;
  /**
   * 子节点数据渲染
   */
  reader: (tree: TreeItem) => VNode;
  /**
   * 子节点操作渲染器
   */
  readerOperate: (prop: {
    addCondition: () => void;
    addRelation: () => void;
    tree: Tree;
  }) => VNode;
  leftHeight: number;
}>();
const updateTreeValue = (t: Tree, event: any) => {
  t.value = event;
};
const getHeightRecursion = (ts: Array<Tree>, level: Array<number>) => {
  ts.forEach((item) => {
    level[0]++;
    level[0] += item.items ? item.items.length : 0;
    if (item.children) {
      getHeightRecursion(item.children, level);
    }
  });
  return level[0];
};

const getHeight = (ts: Array<Tree>) => {
  if (ts.length > 0 && ts[0].children && ts[0].children.length > 0) {
    if (ts[0].items && ts[0].items.length > 0) {
      return props.leftHeight / 2 + "px";
    }
    return (
      getHeightRecursion([ts[0].children[0]], [0]) * (props.leftHeight / 2) +
      "px"
    );
  }
  return props.leftHeight / 2 + "px";
};

const getVerticalBottom = (ts: Array<Tree>) => {
  if (ts.length > 0 && ts[0].children && ts[0].children.length > 0) {
    const level = getHeightRecursion(
      [ts[0].children[ts[0].children.length - 1]],
      [0]
    );
    return level * (props.leftHeight / 2) + props.leftHeight / 2 + "px";
  }
  if (ts[0].items && ts[0].items?.length > 1) {
    return props.leftHeight + "px";
  }
  if (ts[0].items && ts[0].items.length === 1) {
    return props.leftHeight / 2 + "px";
  }
  return "0px";
};
const cNode = ref<Array<InstanceType<typeof childNode>>>([]);
const sNode = ref<any>();

const validate = () => {
  if (cNode.value && sNode.value) {
    return Promise.all([
      ...cNode.value.map((i) => i.validate()),
      ...sNode.value.map((i: any) => i.validate()),
    ]);
  }
  if (sNode.value) {
    return Promise.all(sNode.value.map((i: any) => i.validate()));
  }
  if (cNode.value) {
    return Promise.all(cNode.value.map((i) => i.validate()));
  }
  return Promise.reject();
};
defineExpose({ validate });
</script>
<style lang="scss"></style>
