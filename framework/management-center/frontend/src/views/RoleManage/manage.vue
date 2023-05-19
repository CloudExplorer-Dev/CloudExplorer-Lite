<script setup lang="ts">
import { ref, onMounted, computed, watch } from "vue";
import _ from "lodash";
import type { SimpleMap } from "@commons/api/base/type";
import { idFullNames } from "@commons/api/organization";
import { workspaces } from "@commons/api/workspace";
import RoleApi from "@/api/role";
import UserApi from "@/api/user";
import CeDrawer from "@commons/components/ce-drawer/index.vue";
import CeIcon from "@commons/components/ce-icon/index.vue";
import { roleConst } from "@commons/utils/constants";
import { workspaceTree } from "@commons/api/workspace";
import { tree as orgTree } from "@commons/api/organization";
import type { OrganizationTree } from "@commons/api/organization/type";
import type { WorkspaceTree, Workspace } from "@commons/api/workspace/type";
import {
  PaginationConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import {
  ElMessage,
  ElMessageBox,
  type FormInstance,
  type MessageBoxData,
} from "element-plus";
import { useI18n } from "vue-i18n";
import { Role } from "@commons/api/role/type";
import { usePermissionStore } from "@commons/stores/modules/permission";
import MoreOptionsButton from "@commons/components/ce-table/MoreOptionsButton.vue";
import RoleInfoTable from "./RoleInfoTable.vue";
import RolePermissionTable from "./RolePermissionTable.vue";
import { CreateRoleRequest, UpdateRoleRequest } from "@/api/role/type";
import RoleTag from "@commons/business/person-setting/RoleTag.vue";
import {
  User,
  type AddUserRoleObject,
  type AddUserRoleRequest,
} from "@/api/user/type";
import { useRouter } from "vue-router";

class MUser extends User {
  roleTable: Array<any>;
  rowSpan: number;

  constructor(user: User, skipParseRoleMap?: boolean) {
    super(
      user.id,
      user.username,
      user.name,
      user.email,
      user.phone,
      user.password,
      user.roleMap
    );
    this.rowSpan = 0;
    this.roleTable = [];
    if (!skipParseRoleMap) {
      this.roleTable = UserApi.getUserRoleSourceList(
        UserApi.getUserRoleList(_.defaultTo(user.roleMap, {}))
      );
    }
  }
}

const { t } = useI18n();

const ceDrawerRef = ref<InstanceType<typeof CeDrawer>>();

const router = useRouter();
const permissionStore = usePermissionStore();
const columns = ref([]);

const table = ref();
const tableData = ref<Array<MUser>>();
const orgIdFullNameMap = ref<SimpleMap<string>>({});
const workspaceIdMap = ref<SimpleMap<Workspace>>({});

const rolePermissionTableRef = ref<InstanceType<typeof RolePermissionTable>>();

const showAddDialog = ref<boolean>(false);
const roleLoading = ref<boolean>(false);
const userLoading = ref<boolean>(false);
const permissionLoading = ref<boolean>(false);
const roles = ref<Array<Role>>([]);
//表单校验
const ruleFormRef = ref<FormInstance>();

const activeTab = ref<"user" | "permission">("user");

const selectedRole = ref<Role>();

const editPermission = ref<boolean>(false);

const originRoles = ref<Array<Role>>([]);

const systemRoles = computed<Array<Role>>(() => {
  return _.filter(roles.value, (r) => r.type === "origin");
});
const inheritRoles = computed<Array<Role>>(() => {
  return _.filter(roles.value, (r) => r.type === "inherit");
});

function select(role: Role) {
  editPermission.value = false;
  selectedRole.value = role;
  //rolePermissionTableRef.value?.init();
}

const id = ref<string>();

const roleFormData = ref<Role>(new Role("", "", "", ""));

const permissionData = ref<Array<string>>([]);

const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  params.roleId = selectedRole.value?.id;
  UserApi.pageUser(
    {
      currentPage: tableConfig.value.paginationConfig.currentPage,
      pageSize: tableConfig.value.paginationConfig.pageSize,
      ...params,
    },
    userLoading
  ).then((res) => {
    const _list = _.map(res.data.records, (u) => new MUser(u));
    const resultList: Array<MUser> = [];

    _.forEach(_list, (mUser) => {
      const _roleTable = _.filter(
        mUser.roleTable,
        (rt) => rt.roleId === params.roleId
      );

      for (let i = 0; i < _roleTable.length; i++) {
        const u = new MUser(mUser, true);
        if (i === 0) {
          u.rowSpan = _roleTable.length;
        } else {
          u.rowSpan = 0;
        }
        u.roleTable = [_roleTable[i]];
        resultList.push(u);
      }
    });

    tableData.value = resultList;
    tableConfig.value.paginationConfig?.setTotal(
      res.data.total,
      tableConfig.value.paginationConfig
    );
    tableConfig.value.paginationConfig?.setCurrentPage(
      res.data.current,
      tableConfig.value.paginationConfig
    );
  });
};

function rowSpanSetter({
  row,
  column,
  rowIndex,
  columnIndex,
}: {
  row: any;
  column: any;
  rowIndex: number;
  columnIndex: number;
}) {
  if (columnIndex === 0 || columnIndex === 1) {
    return {
      rowspan: row.rowSpan,
      colspan: 1,
    };
  } else {
    return {
      rowspan: 1,
      colspan: 1,
    };
  }
}

const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    search: search,
    quickPlaceholder: "搜索",
    components: [],
    searchOptions: [
      { label: t("user.username"), value: "username" },
      { label: t("user.name"), value: "name" },
      { label: t("user.email"), value: "email" },
    ],
  },
  paginationConfig: new PaginationConfig(),
});

function addUser() {
  openAddUser();
}

function removeUserRole(user: MUser) {
  ElMessageBox.confirm(t("user.delete_role_confirm"), {
    confirmButtonText: t("commons.message_box.confirm"),
    cancelButtonText: t("commons.btn.cancel"),
    type: "warning",
  })
    .then(() => {
      UserApi.removeUserRole(
        user.id,
        user.roleTable[0].roleId,
        user.roleTable[0].source,
        userLoading
      )
        .then((res) => {
          ElMessage.info(t("commons.msg.delete_success"));
          search(new TableSearch());
        })
        .catch((err) => {
          console.error(err);
        });
    })
    .catch(() => {
      ElMessage.info(t("commons.msg.delete_canceled"));
    });
}

function showUserDetail(user: MUser) {
  router.push({ name: "user_detail", params: { id: user.id } });
}

function addRole() {
  id.value = undefined;
  showAddDialog.value = true;
}

function changeToEditPermission() {
  editPermission.value = true;
}

const cancelEditPermission = () => {
  editPermission.value = false;
};

const submitRolePermission = (permissionIds: Array<string>) => {
  console.log(selectedRole.value?.id, permissionIds);
  if (!selectedRole.value?.id) {
    return;
  }
  console.log(permissionIds);
  RoleApi.updateRolePermissions(
    selectedRole.value.id,
    permissionIds,
    permissionLoading
  ).then((ok) => {
    permissionData.value = ok.data;
    cancelEditPermission();
    ElMessage.success(t("commons.msg.save_success"));
  });
};

function cancelAddRole() {
  roleFormData.value = new Role("", "", "", "");
  showAddDialog.value = false;
}

function editRole(row: Role) {
  id.value = row.id;
  roleFormData.value = row;
  showAddDialog.value = true;
}

function deleteRole(row: Role) {
  ElMessageBox.confirm(
    t("commons.message_box.confirm_delete"),
    t("commons.message_box.prompt"),
    {
      confirmButtonText: t("commons.btn.delete"),
      cancelButtonText: t("commons.btn.cancel"),
      type: "warning",
    }
  ).then(
    (value: MessageBoxData) => {
      console.log(value);
      RoleApi.deleteRole(row.id, roleLoading)
        .then((response) => {
          console.log(response);
          ElMessage.success(t("commons.msg.delete_success"));
        })
        .catch((err) => {
          console.log(err);
        })
        .finally(() => {
          listRoles();
        });
    },
    (reason: any) => {
      console.log(reason);
    }
  );
}

const tableOperations = computed(
  () =>
    new TableOperations([
      TableOperations.buildButtons().newInstance(
        "重命名",
        "primary",
        editRole,
        undefined,
        (row: Role) => {
          return row.type === "origin";
        },
        permissionStore.hasPermission("[management-center]ROLE:EDIT")
      ),
      TableOperations.buildButtons().newInstance(
        "删除",
        "danger",
        deleteRole,
        undefined,
        (row: Role) => {
          return row.type === "origin";
        },
        permissionStore.hasPermission("[management-center]ROLE:DELETE"),
        "#F54A45"
      ),
    ])
);

function listRoles(role?: Role) {
  RoleApi.listRoles(undefined, roleLoading).then((res) => {
    roles.value = res.data;
    if (role) {
      selectedRole.value = role;
    } else {
      selectedRole.value = roles.value[0];
    }
    //rolePermissionTableRef.value?.init();
  });
}

const submitForm = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid) => {
    if (valid) {
      if (!id.value) {
        RoleApi.addRole(
          CreateRoleRequest.newInstance(roleFormData.value),
          roleLoading
        ).then((response) => {
          cancelAddRole();
          listRoles(response.data);
          editPermission.value = true;
          activeTab.value = "permission";
          ElMessage.success(t("commons.msg.save_success"));
        });
      } else {
        RoleApi.updateRole(
          UpdateRoleRequest.newInstance(roleFormData.value),
          roleLoading
        ).then((response) => {
          cancelAddRole();
          listRoles(response.data);
          ElMessage.success(t("commons.msg.save_success"));
        });
      }
    }
  });
};

const addUserFormRef = ref<FormInstance | undefined>();
const addUserLoading = ref<boolean>(false);

const addUserList = ref<Array<User>>([]);

const addUserData = ref<Array<AddUserRoleObject>>([{}]);

const treeData = ref<Array<OrganizationTree | WorkspaceTree>>();

const sourceType = computed<string>(() => {
  if (selectedRole.value?.parentRoleId === roleConst.orgAdmin) {
    return "组织";
  }
  if (selectedRole.value?.parentRoleId === roleConst.user) {
    return "工作空间";
  }

  return "";
});

function addUserRow() {
  addUserData.value.push({});
}

function removeAddUserRow(index: number) {
  _.remove(addUserData.value, (n, i) => index === i);
}

function openAddUser() {
  ceDrawerRef.value?.open();
  addUserData.value = [{}];
  UserApi.listUser(addUserLoading).then((res) => {
    addUserList.value = res.data;
  });

  if (selectedRole.value?.parentRoleId === roleConst.orgAdmin) {
    orgTree(undefined, addUserLoading).then((res) => {
      treeData.value = res.data;
    });
  }
  if (selectedRole.value?.parentRoleId === roleConst.user) {
    workspaceTree(addUserLoading).then((res) => {
      treeData.value = res.data;
    });
  }
}

function confirmAddUser() {
  if (!addUserFormRef.value) return;
  addUserFormRef.value.validate((valid) => {
    if (valid) {
      const addUserReq: AddUserRoleRequest = {
        roleId: selectedRole.value?.id,
        userSourceMappings: addUserData.value,
      };
      console.log(addUserReq);
      UserApi.userAddRoleV2(addUserReq, addUserLoading).then((res) => {
        search(new TableSearch());
        cancelAddUser();
      });
    }
  });
}

function cancelAddUser() {
  ceDrawerRef.value?.close();
  treeData.value = [];
  addUserList.value = [];
}

watch(
  selectedRole,
  (role) => {
    if (role) {
      search(new TableSearch());
    }
  },
  { immediate: true }
);

onMounted(() => {
  listRoles();

  idFullNames().then((res) => {
    orgIdFullNameMap.value = res.data;
  });

  workspaces().then((res) => {
    const _map: SimpleMap<Workspace> = {};
    _.forEach(res.data, (w) => {
      _map[w.id] = w;
    });
    workspaceIdMap.value = _map;
  });

  RoleApi.listOriginRoles().then((res) => {
    originRoles.value = res.data;
  });
});
</script>
<template>
  <el-container style="height: 100%">
    <el-aside width="240px" class="aside" v-loading="roleLoading">
      <div class="role-title">角色管理</div>
      <div class="role-btn title">系统内置</div>

      <div
        class="role-btn role-item"
        :class="{ active: selectedRole?.id === role.id }"
        v-for="role in systemRoles"
        :key="role.id"
        @click="select(role)"
      >
        {{ role.name }}
      </div>

      <div class="divider-container">
        <div class="divider"></div>
      </div>

      <div class="role-btn title">
        自定义角色
        <el-icon
          v-hasPermission="'[management-center]ROLE:CREATE'"
          style="color: #3370ff; font-size: 20px; cursor: pointer"
          @click="addRole"
        >
          <CirclePlusFilled />
        </el-icon>
      </div>

      <div
        class="role-btn role-item"
        v-for="(role, j) in inheritRoles"
        :class="{ active: selectedRole?.id === role.id }"
        :key="j"
        @click="select(role)"
      >
        <div class="role-text">
          {{ role.name }}
          <span
            style="padding-left: 6px; color: #8f959e"
            :data-var="
              (_parent = _.find(
                originRoles,
                (r) => r.id === role.parentRoleId
              )?.name)
            "
          >
            {{ _parent ? `(${_parent})` : "" }}
          </span>
        </div>
        <MoreOptionsButton
          class="more-btn"
          :buttons="tableOperations.buttons"
          :row="role"
        />
      </div>
    </el-aside>
    <el-main style="padding: 0">
      <el-container direction="vertical" style="height: 100%">
        <el-header>
          <el-tabs v-model="activeTab" class="role-tab">
            <el-tab-pane label="成员" name="user"></el-tab-pane>
            <el-tab-pane label="权限配置" name="permission"></el-tab-pane>
          </el-tabs>
        </el-header>
        <el-main v-show="activeTab === 'user'">
          <ce-table
            localKey="userManageTable"
            ref="table"
            :columns="columns"
            :data="tableData"
            :tableConfig="tableConfig"
            height="100%"
            table-layout="auto"
            :span-method="rowSpanSetter"
            v-loading="userLoading"
          >
            <template #toolbar>
              <el-button
                @click="addUser"
                type="primary"
                v-if="
                  permissionStore.hasPermission('[management-center]USER:EDIT')
                "
              >
                添加用户
              </el-button>
            </template>
            <el-table-column prop="username" :label="$t('user.username')" fixed>
              <template #default="scope">
                <div class="custom-column">
                  <a
                    style="cursor: pointer; color: var(--el-color-primary)"
                    @click="showUserDetail(scope.row)"
                  >
                    {{ scope.row.username }}
                  </a>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="name" :label="$t('user.name')">
              <template #default="scope">
                <div class="custom-column">
                  {{ scope.row.name }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="组织">
              <template #default="scope">
                <template v-for="(r, i) in scope.row.roleTable" :key="i">
                  <div
                    class="custom-column"
                    v-if="
                      r.roleId === selectedRole.id &&
                      r.parentRole === roleConst.orgAdmin
                    "
                  >
                    {{
                      r.source
                        ? _.defaultTo(orgIdFullNameMap[r.source], r.source)
                        : "-"
                    }}
                  </div>
                  <div
                    class="custom-column"
                    v-else-if="
                      r.roleId === selectedRole.id &&
                      r.parentRole === roleConst.user
                    "
                  >
                    {{
                      r.source && workspaceIdMap[r.source]?.organizationId
                        ? _.defaultTo(
                            orgIdFullNameMap[
                              workspaceIdMap[r.source]?.organizationId
                            ],
                            workspaceIdMap[r.source]?.organizationId
                          )
                        : "-"
                    }}
                  </div>
                  <div class="custom-column" v-else>-</div>
                </template>
              </template>
            </el-table-column>
            <el-table-column label="工作空间">
              <template #default="scope">
                <template v-for="(r, i) in scope.row.roleTable" :key="i">
                  <div
                    class="custom-column"
                    v-if="
                      r.roleId === selectedRole.id &&
                      r.parentRole === roleConst.user
                    "
                  >
                    {{
                      r.source
                        ? _.defaultTo(workspaceIdMap[r.source]?.name, r.source)
                        : "-"
                    }}
                  </div>
                  <div class="custom-column" v-else>-</div>
                </template>
              </template>
            </el-table-column>
            <el-table-column label="操作" fixed="right">
              <template #default="scope">
                <el-button
                  text
                  type="primary"
                  v-if="
                    permissionStore.hasPermission(
                      '[management-center]USER:EDIT'
                    )
                  "
                  @click="removeUserRole(scope.row)"
                >
                  移除用户
                </el-button>
              </template>
            </el-table-column>
          </ce-table>
        </el-main>
        <el-main
          class="permission-main"
          v-loading="permissionLoading"
          v-show="activeTab === 'permission'"
        >
          <div class="permission-title">
            角色权限
            <RoleTag
              :clickable="false"
              :role="
                _.find(systemRoles, (r) => r.id === selectedRole?.parentRoleId)
              "
            />
          </div>
          <div class="permission-table">
            <RolePermissionTable
              :id="selectedRole?.id"
              :loading="permissionLoading"
              :parent-role-id="selectedRole?.parentRoleId"
              :edit-permission="
                editPermission && selectedRole?.type === 'inherit'
              "
              v-model:permission-data="permissionData"
              ref="rolePermissionTableRef"
            />
          </div>
        </el-main>
        <el-footer
          v-show="activeTab === 'permission'"
          class="permission-footer"
          v-if="
            permissionStore.hasPermission('[management-center]ROLE:EDIT') &&
            selectedRole?.type === 'inherit'
          "
        >
          <el-button
            key="edit"
            type="primary"
            @click="changeToEditPermission"
            v-if="!editPermission && selectedRole?.type === 'inherit'"
            v-hasPermission="'[management-center]ROLE:EDIT'"
          >
            编辑
          </el-button>
          <el-button
            class="cancel-btn"
            v-if="editPermission"
            @click="cancelEditPermission"
          >
            取消
          </el-button>
          <el-button
            class="save-btn"
            type="primary"
            v-if="editPermission"
            @click="submitRolePermission(permissionData)"
          >
            保存
          </el-button>
        </el-footer>
      </el-container>
    </el-main>

    <el-dialog
      v-model="showAddDialog"
      :title="id ? '重命名角色' : '创建自定义角色'"
      width="600px"
      destroy-on-close
      style="min-width: 600px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <RoleInfoTable
        :id="id"
        :loading="roleLoading"
        :edit-info="true"
        v-model:role-data="roleFormData"
        v-model:role-form-data="roleFormData"
        v-model:rule-form-ref="ruleFormRef"
        :create-new="!id"
      />
      <template #footer>
        <el-button @click="cancelAddRole"> 取消</el-button>
        <el-button type="primary" @click="submitForm(ruleFormRef)">
          {{ id ? "保存" : "创建" }}
        </el-button>
      </template>
    </el-dialog>

    <CeDrawer
      ref="ceDrawerRef"
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
                v-if="selectedRole?.parentRoleId !== roleConst.admin"
                :label="sourceType"
                class="form-item"
                :prop="'[' + i + '].sourceIds'"
                :rules="{
                  message: sourceType + '不能为空',
                  type: 'array',
                  trigger: 'change',
                  required: true,
                }"
              >
                <el-tree-select
                  v-model="o.sourceIds"
                  node-key="id"
                  :props="{ label: 'name' }"
                  :data="treeData"
                  :render-after-expand="false"
                  filterable
                  multiple
                  show-checkbox
                  collapse-tags
                  collapse-tags-tooltip
                  :max-collapse-tags="2"
                  :check-strictly="
                    selectedRole?.parentRoleId === roleConst.orgAdmin
                  "
                  style="width: 320px"
                />
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

          <el-button
            v-if="selectedRole.parentRoleId !== roleConst.admin"
            @click="addUserRow"
            link
            type="primary"
          >
            + 添加用户
          </el-button>
        </el-form>
      </el-container>
    </CeDrawer>
  </el-container>
</template>

<style lang="scss" scoped>
.aside {
  height: 100%;
  padding-left: 16px;
  padding-right: 16px;
  padding-top: 24px;
  border-right: rgba(31, 35, 41, 0.15) solid 1px;

  .role-title {
    margin-bottom: 4px;
    padding: 0 8px;
    font-style: normal;
    font-weight: 500;
    font-size: 14px;
    line-height: 22px;
    color: #1f2329;
  }

  .divider-container {
    padding: 8px;

    .divider {
      height: 1px;
      width: 100%;
      background-color: #d5d6d8;
    }
  }

  .role-btn {
    padding: 0 8px;
    height: 38px;
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    line-height: 24px;
    color: #1f2329;
    cursor: pointer;
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    align-items: center;
    justify-content: space-between;
    border-radius: 4px;

    .role-text {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }

  .role-btn.title {
    color: #8f959e;
    cursor: default;
    font-weight: 500;
    font-size: 14px;
    line-height: 22px;
  }

  .role-btn.active {
    color: #3370ff;
    background: #ebf1ff;
  }

  .role-btn.role-item {
    .more-btn {
      display: none;
    }

    &:hover {
      background: linear-gradient(
          0deg,
          rgba(51, 112, 255, 0.15),
          rgba(51, 112, 255, 0.15)
        ),
        #ffffff;

      .more-btn {
        display: block;
      }
    }
  }
}

.permission-main {
  padding: 10px 24px 4px 24px;

  .permission-title {
    height: 24px;
    padding-bottom: 20px;

    font-style: normal;
    font-weight: 500;
    font-size: 14px;
    line-height: 24px;

    color: #1f2329;

    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    align-items: center;
  }

  .permission-table {
    position: relative;
    height: calc(100% - 44px);
  }
}

.role-tab {
  margin-top: 10px;
  --el-tabs-header-height: 40px;
}

.custom-column {
  max-width: 250px;
  min-width: 110px;
}

.permission-footer {
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  justify-content: flex-end;
  height: 80px;
  box-shadow: 0px -1px 4px rgba(31, 35, 41, 0.1);

  --el-footer-padding: 24px;
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
