<template>
  <subNode
    :leftHeight="leftHeight"
    :readerOperate="readerOperate"
    :trees="tree"
    :reader="reader"
    ref="sNode"
  >
  </subNode>
</template>
<script setup lang="ts">
import { ref, onMounted, createVNode } from "vue";
import subNode from "@commons/components/ce-rule-tree/sub_node.vue";
import { nanoid } from "nanoid";
import type { Tree, TreeItem } from "@commons/components/ce-rule-tree/type";
import bus from "@commons/bus";
import type { VNodeTypes } from "vue";
import defaultComponent from "@commons/components/ce-rule-tree/default_component.vue";
import defaultOperate from "@commons/components/ce-rule-tree/default_operate.vue";
const sNode = ref<InstanceType<typeof subNode>>();

/**
 *
 * @param tree 树结构
 */
const defaultMapTree = (tree: Array<Tree>): Array<any> => {
  return tree.map((item) => {
    let children = [];
    if (item.children && item.children.length > 0) {
      children = defaultMapTree(item.children);
    }
    return {
      id: item.id,
      conditionType: item.value,
      conditions: item.items
        ?.filter((i) => i.value)
        .map((i) => ({ ...i.value, id: i.id })),
      children,
    };
  });
};

const props = withDefaults(
  defineProps<{
    // 映射数据
    mapTree?: <T>(tree: Array<Tree>) => Array<T>;
    // 组件对象
    component?: VNodeTypes;
    /**
     * 操作组件渲染对象
     */
    operate?: VNodeTypes;
    /**
     *叶子高度
     */
    leftHeight?: number;
  }>(),
  {
    leftHeight: 48,
    component: defaultComponent,
    operate: defaultOperate,
  }
);

/**
 * 校验所有节点
 */
const validate = () => {
  if (sNode.value) {
    return sNode.value.validate();
  }
  return Promise.resolve();
};

/**
 * 渲染函数
 * @param treeItem 子节点
 */
const reader = (treeItem: TreeItem) => {
  return createVNode(props.component, {
    modelValue: treeItem.value,
    "onUpdate:modelValue": (v: any) => {
      treeItem.value = v;
    },
  });
};
/**
 *
 * @param prop
 */
const readerOperate = (prop: {
  addCondition: () => void;
  addRelation: () => void;
  tree: Tree;
}) => {
  return createVNode(props.operate, {
    addCondition: prop.addCondition,
    addRelation: prop.addRelation,
    tree: prop.tree,
    root: tree.value,
  });
};
/**
 * 树结构
 */
const tree = ref<Array<Tree>>([
  {
    id: nanoid(),
  },
]);

onMounted(() => {
  // 添加关系
  bus.on("addRelation", (pid: string) => {
    validate().then(() => {
      execTreeById(tree.value, pid, (item) => {
        if (item.children) {
          item.children.push({ id: nanoid() });
        } else {
          item.children = [{ id: nanoid() }];
        }
      });
    });
  });
  // 添加条件
  bus.on("addCondition", (pid: string) => {
    validate().then(() => {
      execTreeById(tree.value, pid, (item) => {
        if (item.items) {
          item.items.push({ id: nanoid(), value: {} });
        } else {
          item.items = [{ id: nanoid(), value: {} }];
        }
      });
    });
  });
  // 删除条件
  bus.on(
    "deleteCondition",
    (condition: { pid: string; condtionId: string }) => {
      execTreeById(tree.value, condition.pid, (item) => {
        item.items = item.items?.filter(
          (item) => item.id !== condition.condtionId
        );
      });
    }
  );
  // 删除关系
  bus.on("deleteRelation", (pid: string) => {
    execTreeById(tree.value, pid, (item, parent) => {
      parent.splice(parent.indexOf(item), 1);
    });
  });
});

/**
 * 查询到数据 并且操作数据
 * @param tree 树结构
 * @param pid  树结构需要操作的id
 * @param exec 操作函数
 */
const execTreeById = (
  tree: Array<Tree>,
  pid: string,
  exec: (item: Tree, prentTree: Array<Tree>) => void
) => {
  tree.forEach((item) => {
    if (item.id == pid) {
      exec(item, tree);
    } else {
      if (item.children && item.children.length > 0) {
        execTreeById(item.children, pid, exec);
      }
    }
  });
};
const setTree = (treeData: Array<Tree>) => {
  tree.value = treeData;
};
const getTree = () => {
  return tree.value;
};
defineExpose({ validate, setTree, getTree });
</script>
<style lang="scss"></style>
