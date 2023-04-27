<template>
  <div
    class="tree_content"
    v-loading="loading"
    style="--el-font-size-base: 14px"
  >
    <div class="search">
      <el-input
        v-model="filterText"
        placeholder="搜索关键词"
        :prefix-icon="Search"
        class="input"
      />
    </div>
    <div class="tree">
      <el-tree
        ref="treeRef"
        :data="filterTreeData"
        :props="defaultProps"
        node-key="id"
        :expand-on-click-node="false"
        :default-expanded-keys="defaultExpandedKeys"
        @node-click="handleNodeClick"
        :filter-node-method="filterNode"
        :highlight-current="true"
      >
        <template #default="treeNode">
          <template v-if="$slots.default"
            ><slot v-bind="treeNode"></slot>
          </template>
          <template v-else>
            <div>
              <ce-icon
                style="color: black"
                :code="`${
                  treeNode.data.type === 'ORGANIZATION'
                    ? 'zuzhijiagou1'
                    : 'project_space'
                }`"
                size="3px"
              ></ce-icon>
              <span style="margin-left: 8px">{{ treeNode.node.label }}</span>
            </div></template
          >
        </template>
      </el-tree>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElTree } from "element-plus";
import { ref, onMounted, nextTick, computed } from "vue";
import { Search } from "@element-plus/icons-vue";
import baseOrganizationApi from "@commons/api/organization/index";
import type { Tree, TreeNode } from "@commons/components/ce-tree/type";
import type { OrganizationTree } from "@commons/api/organization/type";
import _ from "lodash";
const emit = defineEmits(["update:modelValue"]);
const props = withDefaults(
  defineProps<{
    /**
     * 选中的数据
     */
    modelValue: TreeNode;
    /**
     * 树形数据
     */
    treeData?: () => Promise<Array<Tree>>;
    /**
     * 重写树
     */
    resetData: (tree: Array<Tree>) => Array<Tree>;
  }>(),
  {
    treeData: () => {
      return baseOrganizationApi
        .tree("ORGANIZATION_AND_WORKSPACE")
        .then((ok) => {
          return toTree(ok.data);
        });
    },
    resetData: (tree: Array<Tree>) => {
      return tree;
    },
  }
);

/**
 * 树对象
 */
const treeRef = ref<InstanceType<typeof ElTree>>();
/**
 *过滤文本
 */
const filterText = ref("");

/**
 * 过滤节点
 * @param value
 * @param data
 */
const filterNode = (value: string, data: OrganizationTree | any) => {
  if (!value) return true;
  return data.name.includes(value);
};

onMounted(() => {
  loading.value = true;
  props
    .treeData()
    .then((tree) => {
      localTreeData.value = props.resetData(tree);
      nextTick(() => {
        treeRef.value?.setCurrentKey(props.modelValue.id);
      });
    })
    .finally(() => {
      loading.value = false;
    });
});

/**
 * 获取展开字段
 * @param newTree 新的Tree
 * @param res     传[]
 */
const getExpandedKeys = (newTree: Array<Tree>, res: Array<string>) => {
  newTree.map((item) => {
    if (item.children && item.children.length > 0) {
      if (item.type === "ORGANIZATION") {
        res.push(item.id);
      }
      getExpandedKeys(item.children, res);
    }
  });
  return res;
};

/**
 * 默认展开key
 */
const defaultExpandedKeys = computed(() => {
  nextTick(() => {
    treeRef.value?.setCurrentKey(props.modelValue.id);
  });
  if (filterText.value) {
    return getExpandedKeys(filterTreeData.value, []);
  }
  return [];
});
/**
 * 选中树节点触发函数
 * @param data 数节点
 */
const handleNodeClick = (data: Tree) => {
  emit("update:modelValue", data);
};

/**
 * 组织树对象
 */
const localTreeData = ref<Array<Tree>>([]);
/**
 * 过滤后的树对象
 */
const filterTreeData = computed(() => {
  if (!filterText.value) {
    return localTreeData.value;
  }
  let newTree = _.cloneDeep(localTreeData.value);
  newTree = newTree
    .map((item) => {
      filterTree(item.children ? item.children : [], item);
      return item;
    })
    .filter((item) => {
      return (
        (item.children && item.children.length > 0) ||
        item.name.indexOf(filterText.value) > -1
      );
    });

  return newTree;
});

/**
 * 组织树配置
 */
const defaultProps = {
  label: "name",
};

/**
 * 过滤树函数
 * @param treeArray 树
 * @param parentObj 父级对象
 */
const filterTree = (treeArray: Array<Tree>, parentObj?: Tree) => {
  if (!filterText.value) {
    return;
  }
  treeArray.forEach((item) => {
    if (item.name.indexOf(filterText.value) === -1) {
      if (item.children) {
        filterTree(item.children, item);
      }
      if (!item.children || item.children.length === 0) {
        if (parentObj && parentObj.children) {
          parentObj.children = parentObj.children.filter(
            (p) => p.id !== item.id
          );
        }
      }
    }
  });
};
/**
 * 组织加载器
 */
const loading = ref<boolean>(false);
</script>
<script lang="ts">
/**
 * 转换为Tree
 * @param organizationWorkspaceTreeData 组织工作空间树对象
 */
export const toTree = (
  organizationWorkspaceTreeData: Array<OrganizationTree>
): Array<Tree> => {
  return organizationWorkspaceTreeData.map((item) => {
    const children: Array<Tree> = item.workspaces
      ? item.workspaces.map((workspace) => ({
          id: workspace.id,
          name: workspace.name,
          type: "WORKSPACE",
        }))
      : [];

    if (item.children && item.children.length > 0) {
      const childrenTree = toTree(item.children);
      childrenTree.forEach((i) => {
        children.push(i);
      });
    }
    return {
      id: item.id,
      name: item.name,
      type: "ORGANIZATION",
      children,
    };
  });
};
</script>
<style lang="scss" scoped>
@mixin tree-item-content() {
  box-sizing: border-box;
  height: 40px;
  border-radius: 4px;
  cursor: pointer;
  line-height: 40px;
}
.tree-item-content {
  @include tree-item-content;
  &:hover {
    background-color: var(--el-tree-node-hover-bg-color);
  }
}

// 选中样式
@mixin active() {
  background-color: var(--el-tree-node-hover-bg-color);
  color: rgba(51, 112, 255, 1); //节点的字体颜色
  font-weight: 500;
}

.tree_content {
  height: 100%;
  width: 100%;
  .search {
    width: 100%;
    height: 32px;
    margin-bottom: 13px;
    display: flex;
    justify-content: center;
    align-items: flex-end;
    align-content: flex-end;
    .input {
      height: 28px;
    }
  }
  .tree {
    height: calc(100% - 32px);
    width: 100%;
    overflow: hidden;
    &:hover {
      overflow: auto;
    }
  }
}
:deep(.el-tree-node__content) {
  @include tree-item-content;
}

:deep(.el-tree--highlight-current) {
  .el-tree-node.is-current {
    > .el-tree-node__content {
      @include active;
    }
  }
}
:deep(.el-alert) {
  .el-alert__icon {
    color: rgba(51, 112, 255, 1);
  }
}
:deep(.el-tabs__content) {
  height: calc(100% - 40px);
}
:deep(.el-tree) {
  --el-tree-node-hover-bg-color: rgba(51, 112, 255, 0.1);
  --el-tree-text-color: rgba(31, 35, 41, 1);
  --el-tree-expand-icon-color: rgba(100, 106, 115, 1);
  color: #1f2329;
  width: 100%;
  height: calc(100% - 90px);

  > .el-tree-node {
    display: inline-block;
    min-width: 100%;
  }
}
</style>
