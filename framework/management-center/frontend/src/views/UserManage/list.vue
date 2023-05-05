<script setup lang="ts">
import { ref, onMounted } from "vue";
import {
  deleteUserById,
  pageUser,
  getUserRoleList,
  convertUserRoleSourceList,
} from "@/api/user";
import { useRouter } from "vue-router";
import { User, UserRole } from "@/api/user/type";
import type { SimpleMap } from "@commons/api/base/type";
import {
  PaginationConfig,
  SearchConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import ModifyPwd from "@/views/UserManage/ModifyPwd.vue";
import AddRole from "@/views/UserManage/AddRole.vue";
import MsgConfig from "@/views/UserManage/MsgConfig.vue";
import { useI18n } from "vue-i18n";
import { ElMessageBox, ElMessage } from "element-plus/es";
import { usePermissionStore } from "@commons/stores/modules/permission";
import CeIcon from "@commons/components/ce-icon/index.vue";
import EnableUserSwitch from "./EnableUserSwitch.vue";
import MoreOptionsButton from "@commons/components/ce-table/MoreOptionsButton.vue";
import CreateOrEdit from "@/views/UserManage/CreateOrEdit.vue";
import { sourceIdNames } from "@commons/api/organization";

const { t } = useI18n();
const router = useRouter();
const permissionStore = usePermissionStore();
const columns = ref([]);
const tableData = ref<Array<User>>();
const table = ref();
const selectedUsers = ref<Array<User>>([]); // checkbox选中的用户数据
const selectedUserIds: string[] = [];
const activeUserId = ref<string>();
const batchAddRoleDialogVisible = ref<boolean>(false);
const msgConfigDialogVisible = ref<boolean>(false);
const sourceNames = ref<SimpleMap<string>>({});
const createEditFormRef = ref<InstanceType<typeof CreateOrEdit>>();
const selectedId = ref<string | undefined>();

const loading = ref<boolean>(false);

onMounted(() => {
  search(new TableSearch());
});

// 刷新列表
const refresh = () => {
  table.value.search();
};

const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  const workspaceId = router.currentRoute.value.query.workspaceId;
  if (workspaceId) {
    params["workspaceId"] = workspaceId;
    router.currentRoute.value.query.workspaceId = null;
  }
  pageUser(
    {
      currentPage: tableConfig.value.paginationConfig.currentPage,
      pageSize: tableConfig.value.paginationConfig.pageSize,
      ...params,
    },
    loading
  ).then((res) => {
    tableData.value = res.data.records;
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

// 选中
const handleSelectionChange = (val: User[]) => {
  selectedUsers.value = val;
};

// 用户详情
const showUserDetail = (row: User) => {
  router.push({ name: "user_detail", params: { id: row.id } });
};

const create = () => {
  selectedId.value = undefined;
  createEditFormRef.value?.open();
};

const edit = (row: User) => {
  let needRefresh = false;
  if (selectedId.value === row.id) {
    needRefresh = true;
  }
  selectedId.value = row.id;
  createEditFormRef.value?.open();
  if (needRefresh) {
    createEditFormRef.value?.refreshUser();
  }
};

function editConfirmed() {
  refresh();
}

const deleteUser = (row: User) => {
  ElMessageBox.confirm(t("user.delete_confirm"), {
    confirmButtonText: t("commons.message_box.confirm"),
    cancelButtonText: t("commons.btn.cancel"),
    type: "warning",
  })
    .then(() => {
      deleteUserById(row.id, loading).then(() => {
        refresh();
        ElMessage.success(t("commons.msg.delete_success"));
      });
    })
    .catch(() => {
      ElMessage.info(t("commons.msg.delete_canceled"));
    });
};

const addRole = () => {
  if (!(selectedUsers.value.length > 0)) {
    ElMessage.error(t("user.validate.selected"));
    return;
  }
  selectedUsers.value.forEach((user: User) => {
    selectedUserIds.push(user.id);
  });
  batchAddRoleDialogVisible.value = true;
};

sourceIdNames().then((res) => {
  sourceNames.value = res.data;
});

function getFirstRoleName(list: Array<UserRole>) {
  if (list.length > 0) {
    return list[0].roles[0].name;
  }
  return "";
}

const showMsgConfigDialog = (row: User) => {
  activeUserId.value = row.id;
  msgConfigDialogVisible.value = true;
};

const modifyPwdRef = ref();
const showPwdDialog = (row: User) => {
  activeUserId.value = row.id;
  modifyPwdRef.value.dialogVisible = true;
};

const sourceFilter = (userSource: string) => {
  if (userSource.toLowerCase() === "local") {
    return t("user.local");
  }
  if (userSource.toLowerCase() === "extra") {
    return t("user.extra");
  }
  return userSource;
};

const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    search: search,
    quickPlaceholder: "搜索",
    components: [
      SearchConfig.buildComponent().DateComponent.newInstance(
        "createTime",
        t("commons.create_time")
      ),
    ],
    searchOptions: [
      { label: t("user.username"), value: "username" },
      { label: t("user.name"), value: "name" },
      { label: t("user.role"), value: "roleName" },
      { label: t("user.email"), value: "email" },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([
    TableOperations.buildButtons().newInstance(
      t("commons.btn.edit"),
      "primary",
      edit,
      undefined,
      undefined,
      permissionStore.hasPermission("[management-center]USER:EDIT")
    ),
    TableOperations.buildButtons().newInstance(
      t("commons.personal.edit_pwd"),
      "primary",
      showPwdDialog,
      undefined,
      undefined,
      permissionStore.hasPermission("[management-center]USER:EDIT_PASSWORD")
    ),
    TableOperations.buildButtons().newInstance(
      t("user.notify_setting"),
      "primary",
      showMsgConfigDialog,
      undefined,
      undefined,
      permissionStore.hasPermission(
        "[management-center]USER:NOTIFICATION_SETTING"
      )
    ),
    TableOperations.buildButtons().newInstance(
      t("commons.btn.delete"),
      "danger",
      deleteUser,
      undefined,
      undefined,
      permissionStore.hasPermission("[management-center]USER:DELETE"),
      "#F54A45"
    ),
  ]),
});
</script>

<template>
  <ce-table
    localKey="userManageTable"
    ref="table"
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    @selection-change="handleSelectionChange"
    :show-selected-count="true"
    row-key="id"
    height="100%"
    table-layout="auto"
    v-loading="loading"
  >
    <template #toolbar>
      <el-button
        @click="create"
        type="primary"
        v-hasPermission="'[management-center]USER:CREATE'"
      >
        {{ $t("commons.btn.create") }}{{ $t("user.user") }}
      </el-button>
      <el-button
        @click="addRole"
        v-hasPermission="'[management-center]USER:EDIT'"
      >
        {{ $t("user.add_role") }}
      </el-button>
    </template>
    <el-table-column type="selection" />
    <el-table-column prop="username" :label="$t('user.username')" fixed>
      <template #default="scope">
        <span
          style="cursor: pointer; color: var(--el-color-primary)"
          @click="showUserDetail(scope.row)"
        >
          {{ scope.row.username }}
        </span>
      </template>
    </el-table-column>
    <el-table-column
      prop="enabled"
      :label="$t('user.status')"
      sortable
      min-width="160px"
    >
      <template #default="scope">
        <CeIcon
          :code="scope.row.enabled ? 'success' : 'icon_disable'"
          :color="scope.row.enabled ? '#34C724' : '#D2D3D4'"
          size="1em"
        />

        {{ scope.row.enabled ? "正常" : "禁用" }}
      </template>
    </el-table-column>
    <el-table-column prop="name" :label="$t('user.name')" />
    <el-table-column prop="email" :label="$t('user.email')" min-width="160px" />
    <el-table-column prop="roles" :label="$t('user.role')">
      <template #default="scope">
        <div
          class="role_display"
          :data-var="(scope._list = getUserRoleList(scope.row.roleMap))"
        >
          {{ getFirstRoleName(scope._list) }}
          <el-popover
            v-if="scope.row.roles?.length > 1"
            placement="right"
            :width="400"
            trigger="hover"
            :data-var="(scope._table = convertUserRoleSourceList(scope._list))"
          >
            <template #reference>
              <span class="role_numbers">
                +{{ scope.row.roles.length - 1 }}
              </span>
            </template>
            <table width="100%">
              <tr>
                <th class="inner-table-padding inner-table-th">角色</th>
                <th class="inner-table-padding inner-table-th">
                  组织/工作空间
                </th>
              </tr>
              <tr v-for="_row in scope._table" :key="_row.roleId">
                <td width="40%" class="inner-table-padding inner-table-role">
                  {{ _row.roleName }}
                </td>
                <td width="60%" class="inner-table-padding inner-table-source">
                  <div v-for="s in _row.list" :key="s.source">
                    {{
                      s.source
                        ? sourceNames[s.source]
                          ? sourceNames[s.source]
                          : s.source
                        : "云管理平台"
                    }}
                  </div>
                </td>
              </tr>
            </table>
          </el-popover>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="source"
      :label="$t('user.source')"
      sortable
      min-width="160px"
    >
      <template #default="scope">
        <span>{{ sourceFilter(scope.row.source) }}</span>
      </template>
    </el-table-column>

    <el-table-column prop="phone" :label="$t('commons.personal.phone')">
      <template #default="scope">
        <span>{{ scope.row.phone ? scope.row.phone : "-" }}</span>
      </template>
    </el-table-column>
    <el-table-column
      prop="createTime"
      :label="$t('commons.create_time')"
      sortable
      min-width="200px"
    />
    <!--    <fu-table-operations
      v-bind="tableConfig.tableOperations"
      fixed="right"
      :ellipsis="1"
    />-->
    <el-table-column min-width="150px" label="操作" fixed="right">
      <template #default="scope">
        <el-space wrap>
          <!--          <el-switch
            v-model="scope.row.enabled"
            :loading="scope.switch_loading"
            @change="handleSwitchStatus(scope.row)"
          />-->
          <EnableUserSwitch
            v-model="scope.row.enabled"
            :final-function="listUser"
            :function-props="scope.row"
          />

          <MoreOptionsButton
            :buttons="tableConfig.tableOperations.buttons"
            :row="scope.row"
          />
        </el-space>
      </template>
    </el-table-column>
    <template #buttons>
      <CeTableColumnSelect :columns="columns" />
    </template>
  </ce-table>

  <!-- 修改密码弹出框 -->
  <ModifyPwd
    ref="modifyPwdRef"
    :userId="activeUserId"
    style="min-width: 600px"
  />

  <!-- 通知设置弹出框 -->
  <el-dialog
    v-model="msgConfigDialogVisible"
    :title="$t('user.notify_setting')"
    width="25%"
    destroy-on-close
    :close-on-click-modal="false"
    class="custom-dialog"
    style="min-width: 600px"
  >
    <MsgConfig
      :userId="activeUserId"
      v-model:visible="msgConfigDialogVisible"
    />
  </el-dialog>

  <!-- 添加角色弹出框 -->
  <el-dialog
    v-model="batchAddRoleDialogVisible"
    :title="$t('user.add_role')"
    width="40%"
    class="custom-dialog"
    destroy-on-close
    style="min-width: 600px"
  >
    <AddRole
      :userIds="selectedUserIds"
      v-model:visible="batchAddRoleDialogVisible"
      @refresh="refresh"
    />
  </el-dialog>

  <CreateOrEdit
    ref="createEditFormRef"
    @confirm="editConfirmed"
    :id="selectedId"
  />
</template>

<style lang="scss" scoped>
:deep(.el-table__body) {
  .el-table__placeholder {
    display: none;
  }
}

.role_display {
  height: 24px;
  line-height: 24px;
  display: flex;
  .role_numbers {
    cursor: pointer;
    margin-left: 8px;
    border-radius: 2px;
    padding: 0 6px;
    height: 24px;
    font-size: 14px;
    background-color: rgba(31, 35, 41, 0.1);
  }
  .role_numbers:hover {
    background-color: #ebf1ff;
    color: #3370ff;
  }
}

.inner-table-th {
  border-bottom: 1px dashed #eff0f1;
  font-style: normal;
  font-weight: 400;
  font-size: 12px;
  color: #646a73;
}

.inner-table-padding {
  padding: 8px;
}

.inner-table-role {
  font-style: normal;
  font-weight: 400;
  font-size: 12px;
}

.inner-table-source {
  font-style: normal;
  font-weight: 500;
  font-size: 12px;
}
</style>
