<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import type { TreeNode } from "@commons/components/ce-tree/type";
import { useUserStore } from "@commons/stores/modules/user";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { roleConst } from "@commons/utils/constants";
import MoreOptionsButton from "@commons/components/ce-table/MoreOptionsButton.vue";
import CeTree from "@commons/components/ce-tree/index.vue";
import { TableOperations } from "@commons/components/ce-table/type";
import type Node from "element-plus/es/components/tree/src/model/node";

const defaultAdminTreeNode = {
  id: "CE_BASE",
  name: "云管平台",
  type: "CE_BASE",
};

const activeTreeNode = ref<TreeNode>();

const orgTreeRef = ref<InstanceType<typeof CeTree>>();

const activeTab = ref<"user" | "organization" | "workspace">("user");

const userStore = useUserStore();
const permissionStore = usePermissionStore();

function editSource(source: TreeNode) {
  console.log(source);
}

function deleteSource(source: TreeNode) {
  console.log(source);
}

const moreOperations = computed(
  () =>
    new TableOperations([
      TableOperations.buildButtons().newInstance(
        "重命名",
        "primary",
        editSource,
        undefined,
        undefined,
        (row: TreeNode) => {
          if (row.type === "WORKSPACE") {
            return permissionStore.hasPermission(
              "[management-center]WORKSPACE:EDIT"
            );
          } else if (row.type === "ORGANIZATION") {
            return permissionStore.hasPermission(
              "[management-center]ORGANIZATION:EDIT"
            );
          } else {
            return false;
          }
        }
      ),
      TableOperations.buildButtons().newInstance(
        "删除",
        "danger",
        deleteSource,
        undefined,
        undefined,
        (row: TreeNode) => {
          if (row.type === "WORKSPACE") {
            return permissionStore.hasPermission(
              "[management-center]WORKSPACE:DELETE"
            );
          } else if (row.type === "ORGANIZATION") {
            return permissionStore.hasPermission(
              "[management-center]ORGANIZATION:EDIT"
            );
          } else {
            return false;
          }
        },
        "#F54A45"
      ),
    ])
);

function addUser(source: TreeNode) {
  console.log(source);
}

function addOrganization(source: TreeNode) {
  console.log(source);
}

function addWorkspace(source: TreeNode) {
  console.log(source);
}

const addOperations = computed(
  () =>
    new TableOperations([
      TableOperations.buildButtons().newInstance(
        "添加成员",
        "primary",
        addUser,
        undefined,
        undefined,
        (row: TreeNode) => {
          return permissionStore.hasPermission("[management-center]USER:EDIT");
        }
      ),
      TableOperations.buildButtons().newInstance(
        "创建组织",
        "primary",
        addOrganization,
        undefined,
        undefined,
        (row: TreeNode) => {
          if (row.type === "WORKSPACE") {
            return false;
          } else {
            return permissionStore.hasPermission(
              "[management-center]ORGANIZATION:CREATE"
            );
          }
        }
      ),
      TableOperations.buildButtons().newInstance(
        "创建工作空间",
        "primary",
        addWorkspace,
        undefined,
        undefined,
        (row: TreeNode) => {
          if (row.type === "WORKSPACE") {
            return false;
          } else {
            return permissionStore.hasPermission(
              "[management-center]WORKSPACE:CREATE"
            );
          }
        }
      ),
    ])
);

function onSelectNodeChange(data: any, node: Node) {
  console.log(data, node);
}

onMounted(() => {});
</script>
<template>
  <el-container style="height: 100%">
    <el-aside width="240px" class="aside">
      <div class="org-title">组织架构</div>
      <CeTree
        ref="orgTreeRef"
        class="org-tree"
        @current-change="onSelectNodeChange"
        search-placeholder="请输入名称"
        v-model="activeTreeNode"
        default-expanded-first
        :reset-data="
          (tree) => {
            if (userStore.currentRole === roleConst.admin) {
              return [
                {
                  ...defaultAdminTreeNode,
                  children: tree,
                },
              ];
            } else {
              return tree;
            }
          }
        "
      >
        <template #default="treeNode">
          <div class="menu-item">
            <ce-icon
              type="code"
              :code="`${
                treeNode.data.type === 'ORGANIZATION'
                  ? 'icon_organization_outlined'
                  : treeNode.data.type === 'WORKSPACE'
                  ? 'icon_working_space'
                  : 'icon_moments-categories_outlined'
              }`"
              size="10px"
            />
            <span
              class="node-text"
              :class="
                treeNode.data.type === 'CE_BASE' ? 'base-node' : undefined
              "
            >
              {{ treeNode.node.label }}
            </span>
            <div style="flex: 1"></div>
            <MoreOptionsButton
              class="more-btn"
              :buttons="moreOperations.buttons"
              :row="treeNode.data"
            />
            <MoreOptionsButton
              class="more-btn"
              :buttons="addOperations.buttons"
              :row="treeNode.data"
            >
              <template #icon>
                <el-icon>
                  <Plus />
                </el-icon>
              </template>
            </MoreOptionsButton>
            <div style="width: 8px"></div>
          </div>
        </template>
      </CeTree>
    </el-aside>
    <el-main style="padding: 0">
      <el-container direction="vertical" style="height: 100%">
        <el-header>
          <div class="title-name">{{ activeTreeNode?.name }}</div>
          <el-tabs v-model="activeTab" class="role-tab">
            <el-tab-pane label="用户成员" name="user"></el-tab-pane>
            <el-tab-pane
              label="组织"
              name="organization"
              v-if="
                activeTreeNode?.type === 'CE_BASE' ||
                activeTreeNode?.type === 'ORGANIZATION'
              "
            ></el-tab-pane>
            <el-tab-pane
              label="工作空间"
              name="workspace"
              v-if="
                activeTreeNode?.type === 'CE_BASE' ||
                activeTreeNode?.type === 'ORGANIZATION'
              "
            ></el-tab-pane>
          </el-tabs>
        </el-header>
      </el-container>
    </el-main>
  </el-container>
</template>

<style lang="scss" scoped>
.aside {
  height: 100%;
  padding: 24px 16px;
  border-right: rgba(31, 35, 41, 0.15) solid 1px;
}

.org-title {
  margin-bottom: 16px;
  padding: 0 8px;
  font-style: normal;
  font-weight: 500;
  font-size: 14px;
  line-height: 22px;
  color: #1f2329;
}

.org-tree {
  height: calc(100% - 22px - 16px);
}

.menu-item {
  width: 100%;
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;

  .more-btn {
    display: none;
  }

  &:hover {
    .more-btn {
      display: block;
    }
  }

  .node-text {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    margin-left: 8px;
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    line-height: 22px;
    max-width: 130px;
  }

  .base-node {
    font-weight: 500;
  }
}
</style>
