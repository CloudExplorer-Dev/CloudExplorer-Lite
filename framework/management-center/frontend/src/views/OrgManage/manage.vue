<script setup lang="ts">
import { computed, ref } from "vue";
import type { TreeNode } from "@commons/components/ce-tree/type";
import { useUserStore } from "@commons/stores/modules/user";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { roleConst } from "@commons/utils/constants";
import MoreOptionsButton from "@commons/components/ce-table/MoreOptionsButton.vue";
import CeTree from "@commons/components/ce-tree/index.vue";
import CeIcon from "@commons/components/ce-icon/index.vue";
import { TableOperations } from "@commons/components/ce-table/type";
import type Node from "element-plus/es/components/tree/src/model/node";
import UserManageTab from "@/views/OrgManage/manage/UserManageTab.vue";
import OrgManageTab from "@/views/OrgManage/manage/OrgManageTab.vue";
import WorkspaceManageTab from "@/views/OrgManage/manage/WorkspaceManageTab.vue";
import OrgCreateDrawer from "@/views/OrgManage/manage/OrgCreateDrawer.vue";
import OrgEditDrawer from "@/views/OrgManage/manage/OrgEditDrawer.vue";
import WorkspaceEditOrCreateDrawer from "@/views/OrgManage/manage/WorkspaceEditOrCreateDrawer.vue";
import CeDrawer from "@commons/components/ce-drawer/index.vue";
import {
  User,
  type AddUserRoleObjectBySourceId,
  type AddUserRoleRequestBySourceId,
  Role,
} from "@/api/user/type";
import UserApi from "@/api/user";
import type { FormInstance } from "element-plus";
import _ from "lodash";
import RoleApi from "@/api/role";
import { ElMessage, ElMessageBox } from "element-plus";
import organizationApi from "@/api/organization";
import { useI18n } from "vue-i18n";
import WorkspaceApi from "@/api/workspace";

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

const { t } = useI18n();

function editSource(source: TreeNode) {
  //console.log(source);
  if (source.type === "ORGANIZATION") {
    editOrganization(source);
  } else if (source.type === "WORKSPACE") {
    editWorkspace(source);
  }
}

function deleteSource(source: { type: string; id: string; inTab: boolean }) {
  //console.log(source);
  if (source.type === "ORGANIZATION") {
    ElMessageBox.confirm(
      t("commons.message_box.confirm_delete", "确认删除"),
      t("commons.message_box.prompt", "提示"),
      {
        confirmButtonText: t("commons.btn.delete", "删除"),
        cancelButtonText: t("commons.btn.cancel", "取消"),
        type: "warning",
      }
    ).then(() => {
      organizationApi.deleteOrg(source.id, loading).then(() => {
        if (source.inTab) {
          orgManageTabRef.value?.refreshList();
        }
        orgTreeRef.value?.reloadTree();
        if (!source.inTab) {
          orgTreeRef.value?.selectFirst();
        }
        ElMessage.success(t("commons.msg.delete_success", "删除成功"));
      });
    });
  } else if (source.type === "WORKSPACE") {
    ElMessageBox.confirm(
      t("commons.message_box.confirm_delete"),
      t("commons.message_box.prompt"),
      {
        confirmButtonText: t("commons.btn.delete"),
        cancelButtonText: t("commons.btn.cancel"),
        type: "warning",
      }
    ).then(() => {
      //执行删除操作
      WorkspaceApi.deleteWorkspaceById(source.id, loading).then(() => {
        if (source.inTab) {
          workspaceManageTabRef.value?.refreshList();
        }
        orgTreeRef.value?.reloadTree();
        if (!source.inTab) {
          orgTreeRef.value?.selectFirst();
        }

        ElMessage.success(t("commons.msg.delete_success", "删除成功"));
      });
    });
  }
}

const moreOperations = computed(
  () =>
    new TableOperations([
      TableOperations.buildButtons().newInstance(
        "编辑",
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

const selectedType = ref<"WORKSPACE" | "ORGANIZATION" | "CE_BASE">();
const selectedSource = ref<string | undefined>();
function onSelectNodeChange(data: any, node: Node) {
  //console.log(data, node);
  selectedType.value = data.type;
  selectedSource.value = data.id;
  if (selectedType.value === "WORKSPACE") {
    //选择工作空间需要把tab切到user页面
    activeTab.value = "user";
  }
}

/** addUser start **/

const addUserFormRef = ref<FormInstance | undefined>();
const addUserDrawerRef = ref<InstanceType<typeof CeDrawer>>();
const userManageTabRef = ref<InstanceType<typeof UserManageTab>>();
const orgManageTabRef = ref<InstanceType<typeof OrgManageTab>>();
const orgCreateDrawerRef = ref<InstanceType<typeof OrgCreateDrawer>>();
const orgEditDrawerRef = ref<InstanceType<typeof OrgEditDrawer>>();
const workspaceDrawerRef =
  ref<InstanceType<typeof WorkspaceEditOrCreateDrawer>>();
const workspaceManageTabRef = ref<InstanceType<typeof WorkspaceManageTab>>();
const addUserList = ref<Array<User>>([]);
const addRoleList = ref<Array<Role>>([]);
const addUserData = ref<Array<AddUserRoleObjectBySourceId>>([{}]);

function addUserRow() {
  addUserData.value.push({});
}

function removeAddUserRow(index: number) {
  _.remove(addUserData.value, (n, i) => index === i);
}

function addUser(source: TreeNode) {
  //console.log(source);
  addUserDrawerRef.value?.open();
  addUserData.value = [{}];
  UserApi.listUser(addUserLoading).then((res) => {
    addUserList.value = res.data;
  });
  let parentRole;
  if (source.type === "WORKSPACE") {
    parentRole = roleConst.user;
  } else if (source.type === "ORGANIZATION") {
    parentRole = roleConst.orgAdmin;
  } else {
    parentRole = roleConst.admin;
  }
  RoleApi.listRoles({ parentRoleId: parentRole }, addUserLoading).then(
    (res) => {
      addRoleList.value = res.data;
    }
  );
}

function cancelAddUser() {
  addUserDrawerRef.value?.close();
  addUserList.value = [];
}

const addUserLoading = ref<boolean>(false);
const loading = ref<boolean>(false);

function confirmAddUser() {
  if (!addUserFormRef.value) return;
  addUserFormRef.value.validate((valid) => {
    if (valid) {
      const addUserReq: AddUserRoleRequestBySourceId = {
        type: selectedType.value,
        sourceId: selectedSource.value,
        userRoleMappings: addUserData.value,
      };
      //console.log(addUserReq);
      UserApi.userAddRoleV3(addUserReq, addUserLoading).then((res) => {
        //orgTreeRef.value?.reloadTree();
        userManageTabRef.value?.refreshList();
        cancelAddUser();
      });
    }
  });
}

/** addUser end **/

function addOrganization(source: { id: string }) {
  //console.log(source);
  orgCreateDrawerRef.value?.open();
  if (source.id !== "CE_BASE") {
    orgCreateDrawerRef.value?.setPid(source.id);
  }
}

function afterSubmitOrganization() {
  orgManageTabRef.value?.refreshList();
  orgTreeRef.value?.reloadTree();
}

function editOrganization(source: { id: string }) {
  console.log(source);
  orgEditDrawerRef.value?.open();
  if (source.id !== "CE_BASE") {
    orgEditDrawerRef.value?.setData(source.id);
  }
}

function jumpToWorkspace(obj: any) {
  orgTreeRef.value?.select(obj.id);
  activeTab.value = "workspace";
}

function jumpToUser(obj: any) {
  orgTreeRef.value?.select(obj.id);
  activeTab.value = "user";
}

function jumpToOrg(obj: any) {
  orgTreeRef.value?.select(obj.id);
}

function addWorkspace(source: { id: string }) {
  workspaceDrawerRef.value?.open();
  if (source.id !== "CE_BASE") {
    workspaceDrawerRef.value?.setOrgId(source.id);
  }
}

function editWorkspace(source: { id: string }) {
  if (source.id) {
    workspaceDrawerRef.value?.open(source.id);
  }
}

function afterSubmitWorkspace() {
  workspaceManageTabRef.value?.refreshList();
  orgTreeRef.value?.reloadTree();
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
              trigger="click"
              :buttons="moreOperations.buttons"
              :row="treeNode.data"
            />
            <MoreOptionsButton
              class="more-btn"
              trigger="click"
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
        <el-main>
          <UserManageTab
            v-if="activeTab === 'user'"
            ref="userManageTabRef"
            :type="selectedType"
            :source-id="selectedSource"
            @add-user="addUser"
            style="height: 100%"
          />
          <OrgManageTab
            v-if="activeTab === 'organization'"
            ref="orgManageTabRef"
            :id="selectedSource"
            @jump-to-workspace="jumpToWorkspace"
            @jump-to-user="jumpToUser"
            @delete-org="deleteSource"
            @create="addOrganization"
            @edit="editOrganization"
            style="height: 100%"
          />
          <WorkspaceManageTab
            v-if="activeTab === 'workspace'"
            ref="workspaceManageTabRef"
            :org-id="selectedSource"
            @jump-to-user="jumpToUser"
            @jump-to-org="jumpToOrg"
            @delete-workspace="deleteSource"
            @create="addWorkspace"
            @edit="editWorkspace"
            style="height: 100%"
          />
        </el-main>
      </el-container>
    </el-main>

    <CeDrawer
      ref="addUserDrawerRef"
      title="添加用户"
      confirm-btn-name="添加"
      @confirm="confirmAddUser"
      @cancel="cancelAddUser"
      :disable-btn="addUserLoading"
    >
      <el-container direction="vertical" v-loading="addUserLoading">
        <el-form
          :model="addUserData"
          ref="addUserFormRef"
          style="width: 100%"
          scroll-to-error
        >
          <template v-for="(o, i) in addUserData" :key="i">
            <div class="add-user-form-item">
              <el-form-item
                label="用户"
                class="form-item"
                :prop="'[' + i + '].userIds'"
                :rules="{
                  message: '用户' + '不能为空',
                  type: 'array',
                  trigger: 'change',
                  required: true,
                }"
              >
                <el-select
                  v-model="o.userIds"
                  multiple
                  filterable
                  collapse-tags
                  collapse-tags-tooltip
                  :max-collapse-tags="2"
                  style="width: 280px"
                >
                  <el-option
                    v-for="u in addUserList"
                    :key="u.id"
                    :label="u.name"
                    :value="u.id"
                  />
                </el-select>
              </el-form-item>

              <el-form-item
                label="角色"
                class="form-item"
                :prop="'[' + i + '].roleIds'"
                :rules="{
                  message: '角色' + '不能为空',
                  type: 'array',
                  trigger: 'change',
                  required: true,
                }"
              >
                <el-select
                  v-model="o.roleIds"
                  multiple
                  filterable
                  collapse-tags
                  collapse-tags-tooltip
                  :max-collapse-tags="2"
                  style="width: 280px"
                >
                  <el-option
                    v-for="u in addRoleList"
                    :key="u.id"
                    :label="u.name"
                    :value="u.id"
                  />
                </el-select>
              </el-form-item>

              <div
                v-if="addUserData.length <= 1"
                style="width: 16px; height: 16px"
              ></div>
              <CeIcon
                style="cursor: pointer"
                size="16px"
                code="icon_delete-trash_outlined1"
                v-if="addUserData.length > 1"
                @click="removeAddUserRow(i)"
              />
            </div>
          </template>

          <el-button @click="addUserRow" text type="primary">
            + 添加用户
          </el-button>
        </el-form>
      </el-container>
    </CeDrawer>

    <OrgCreateDrawer
      ref="orgCreateDrawerRef"
      @submit="afterSubmitOrganization"
    />
    <OrgEditDrawer ref="orgEditDrawerRef" @submit="afterSubmitOrganization" />
    <WorkspaceEditOrCreateDrawer
      ref="workspaceDrawerRef"
      @submit="afterSubmitWorkspace"
    />
  </el-container>
</template>

<style lang="scss" scoped>
.aside {
  height: 100%;
  padding: 24px 16px;
  border-right: rgba(31, 35, 41, 0.15) solid 1px;

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
      opacity: 0;
    }

    &:hover {
      .more-btn {
        opacity: 1;
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
}

.el-header {
  --el-header-padding: 24px 24px 0 24px;
  --el-header-height: 90px;

  .title-name {
    font-style: normal;
    font-weight: 500;
    font-size: 16px;
    line-height: 22px;
    height: 22px;
    color: #1f2329;
    margin-bottom: 4px;
  }
}

.el-main {
  --el-main-padding: 24px;
}

.add-user-form-item {
  width: calc(100% - 24px);
  background: #f7f9fc;
  border-radius: 4px;
  padding: 12px;
  margin-top: 28px;
  margin-bottom: 14px;
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  justify-content: space-between;

  .form-item {
    margin-bottom: 0;
  }

  &:first-child {
    margin-top: 0;
  }
}
</style>
