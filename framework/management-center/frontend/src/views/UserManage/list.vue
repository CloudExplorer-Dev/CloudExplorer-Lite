<script setup lang="ts">
import { ref, onMounted } from "vue";
import { deleteUserById, listUser, changeUserStatus } from "@/api/user";
import { useRouter } from "vue-router";
import type { User } from "@/api/user/type";
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

const { t } = useI18n();
const useRoute = useRouter();
const permissionStore = usePermissionStore();
const columns = ref([]);
const tableData = ref<Array<User>>();
const table = ref();
const selectedUsers = ref<Array<User>>([]); // checkbox选中的用户数据
const selectedUserIds: string[] = [];
const activeUserId = ref<string>();
const batchAddRoleDialogVisible = ref<boolean>(false);
const msgConfigDialogVisible = ref<boolean>(false);

onMounted(() => {
  search(new TableSearch());
});

// 刷新列表
const refresh = () => {
  table.value.search();
};

const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  const workspaceId = useRoute.currentRoute.value.query.workspaceId;
  if (workspaceId) {
    params["workspaceId"] = workspaceId;
    useRoute.currentRoute.value.query.workspaceId = null;
  }
  listUser({
    currentPage: tableConfig.value.paginationConfig.currentPage,
    pageSize: tableConfig.value.paginationConfig.pageSize,
    ...params,
  }).then((res) => {
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
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace("/list", "/detail"),
    query: { id: row.id },
  });
};

const create = () => {
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace("/list", "/create"),
  });
};

const edit = (row: User) => {
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace("/list", "/update"),
    query: { id: row.id },
  });
};

const deleteUser = (row: User) => {
  ElMessageBox.confirm(t("user.delete_confirm"), {
    confirmButtonText: t("commons.message_box.confirm"),
    cancelButtonText: t("commons.btn.cancel"),
    type: "warning",
  })
    .then(() => {
      deleteUserById(row.id).then(() => {
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

/**
 * 启停用户
 * @param row
 */
const handleSwitchStatus = (row: User) => {
  changeUserStatus(row)
    .then(() => {
      ElMessage.success(t("commons.msg.op_success"));
    })
    .catch(() => {
      refresh();
    });
};

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
      { label: "ID", value: "username" },
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
      "EditPen",
      undefined,
      permissionStore.hasPermission("[management-center]USER:EDIT")
    ),
    TableOperations.buildButtons().newInstance(
      t("commons.btn.delete"),
      "danger",
      deleteUser,
      "Delete",
      undefined,
      permissionStore.hasPermission("[management-center]USER:DELETE")
    ),
    TableOperations.buildButtons().newInstance(
      t("commons.personal.edit_pwd"),
      "primary",
      showPwdDialog,
      "Lock",
      undefined,
      permissionStore.hasPermission("[management-center]USER:EDIT_PASSWORD")
    ),
    TableOperations.buildButtons().newInstance(
      t("user.notify_setting"),
      "primary",
      showMsgConfigDialog,
      "Bell",
      undefined,
      permissionStore.hasPermission(
        "[management-center]USER:NOTIFICATION_SETTING"
      )
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
    row-key="id"
    height="100%"
    table-layout="auto"
  >
    <template #toolbar>
      <el-button
        @click="create"
        type="primary"
        v-hasPermission="'[management-center]USER:CREATE'"
      >
        {{ $t("commons.btn.create") }}
      </el-button>
      <el-button
        @click="addRole"
        v-hasPermission="'[management-center]USER:EDIT'"
      >
        {{ $t("user.add_role") }}
      </el-button>
    </template>
    <el-table-column type="selection" />
    <el-table-column prop="username" label="ID" fixed>
      <template #default="scope">
        <span
          style="cursor: pointer; color: var(--el-color-primary)"
          @click="showUserDetail(scope.row)"
        >
          {{ scope.row.username }}
        </span>
      </template>
    </el-table-column>
    <el-table-column prop="name" :label="$t('user.name')" />
    <el-table-column prop="email" :label="$t('user.email')" min-width="160px" />
    <el-table-column prop="roles" :label="$t('user.role')">
      <template #default="scope">
        <div v-for="role in scope.row.roles" :key="role.id">
          <span style="color: var(--el-color-primary)">
            {{ role.name }}
            <br />
          </span>
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
    <el-table-column
      prop="enabled"
      :label="$t('user.status')"
      sortable
      min-width="160px"
    >
      <template #default="scope">
        <el-switch
          v-model="scope.row.enabled"
          @change="handleSwitchStatus(scope.row)"
        />
      </template>
    </el-table-column>
    <el-table-column prop="phone" :label="$t('commons.personal.phone')" />
    <el-table-column
      prop="createTime"
      :label="$t('commons.create_time')"
      sortable
      min-width="200px"
    />
    <fu-table-operations v-bind="tableConfig.tableOperations" fixed="right" />
    <template #buttons>
      <CeTableColumnSelect :columns="columns"/>
    </template>
  </ce-table>

  <!-- 修改密码弹出框 -->
  <ModifyPwd ref="modifyPwdRef" :userId="activeUserId" style="min-width: 600px"/>

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
</template>

<style lang="scss" scoped>
:deep(.el-table__body) {
  .el-table__placeholder {
    display: none;
  }
}
</style>
