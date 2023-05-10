<template>
  <div
    class="child-node-warpper child-node-warpper-up"
    :style="{
      '--tree-level-height': pid ? '0px' : '0px',
      '--tree-level': pid ? 1 : 0,
    }"
  >
    <div class="child-node">
      <div class="select-content">
        <select class="select" v-model="condition">
          <option value="AND">并且</option>
          <option value="OR">或者</option>
        </select>
        <div class="icon" v-if="pid" @click="deleteRelation">
          <el-icon><Close /></el-icon>
        </div>
      </div>
    </div>
    <div class="left-node-warpper">
      <template v-if="tree.items && tree.items.length > 0">
        <leftNode v-for="item in tree.items" :key="item.id">
          <template #content>
            <component :is="reader(item)" ref="node"></component
          ></template>
          <template #delete>
            <div @click="deleteCondition(item.id)" class="delete">
              <ce-icon code="icon_delete-trash_outlined" size="16px"></ce-icon>
            </div>
          </template>
        </leftNode>
      </template>

      <slot></slot>
      <leftNode :operate="true"
        ><template #content>
          <component
            :is="readerOperate({ addCondition, addRelation, tree })"
          ></component>
        </template>
      </leftNode>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, computed } from "vue";
import type { VNode } from "vue";
import leftNode from "@commons/components/ce-rule-tree/leaf_node.vue";
import type { Tree, TreeItem } from "@commons/components/ce-rule-tree/type";
import bus from "@commons/bus";
const props = defineProps<{
  /**
   *当前树的父id
   */
  pid?: string;
  /**
   * 当前树
   */
  tree: Tree;
  /**
   * 子节点数据渲染
   */
  reader: (tree: TreeItem) => VNode;

  readerOperate: (prop: {
    addCondition: () => void;
    addRelation: () => void;
    tree: Tree;
  }) => VNode;
}>();

const deleteCondition = (condtionId: string) => {
  bus.emit("deleteCondition", { pid: props.tree.id, condtionId });
};
const addCondition = () => {
  bus.emit("addCondition", props.tree.id);
};
const addRelation = () => {
  bus.emit("addRelation", props.tree.id);
};
const deleteRelation = () => {
  bus.emit("deleteRelation", props.tree.id);
};
const node = ref<Array<any>>([]);
const emit = defineEmits(["updateTreeValue"]);
/**
 * 校验函数
 */
const validate = () => {
  return Promise.all([...node.value.map((i) => i.validate())]);
};

defineExpose({ validate });
const condition = computed({
  get: () => {
    if (props.tree.value) {
      return props.tree.value;
    }
    emit("updateTreeValue", "AND");
    return "AND";
  },
  set: (event) => {
    emit("updateTreeValue", event);
  },
});
</script>
<style lang="scss" scoped>
.select-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #eff0f1;
  height: 16px;
  border-top-left-radius: 2px;
  border-bottom-left-radius: 2px;
  .select {
    font-weight: 500;
    font-size: 12px;
    line-height: 20px;
    color: #1f2329;
    height: 16px;
    width: 48px;
    border: none;
    background-color: #eff0f1;
    border-top-left-radius: 2px;
    border-bottom-left-radius: 2px;
    opacity: 1;
  }
  .icon {
    cursor: pointer;
    display: flex;
    justify-content: center;
    border-left: #fff solid 1px;
    height: 100%;
    background-color: #eff0f1;
    border-top-right-radius: 2px;
    border-bottom-right-radius: 2px;
  }
}

.delete {
  display: flex;
  align-items: center;
  margin-left: 9px;
  cursor: pointer;
}
.child-node-warpper {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
  .child-node {
    width: 20px;
    margin-right: var(--tree-node-leaf-width, 40px);
    z-index: 1000;
  }
}
.child-node-warpper-up {
  &::before {
    content: "";
    position: absolute;
    border-left: 1px solid #ccc;
    position: absolute;
    top: calc(var(--vertical-top, 24px));
    left: 20px;
    width: 140px;
    height: calc(
      100% - var(--vertical-top, 24px) - var(--vertical-bottom) -
        calc(var(--tree-node-leaf-height) / 2)
    );
  }
  &::after {
    content: "";
    position: absolute;
    border-bottom: 1px solid #ccc;
    top: -50%;
    left: calc(
      var(--tree-level) * calc(-1 * var(--tree-node-leaf-width, 40px)) -
        var(--tree-level-height, 40px)
    );
    height: 100%;
    width: calc(
      var(--tree-level) * var(--tree-node-leaf-width, 40px) +
        var(--tree-level-height, 40px)
    );
  }
}

.left-node-warpper {
  width: 100%;
  height: auto;
}

// 向上节点
</style>
