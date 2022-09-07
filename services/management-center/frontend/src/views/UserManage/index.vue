<script setup lang="ts">
import { ref, onMounted } from "vue";
import { deleteUserById, listUser } from "@/api/user";
import { useRouter } from "vue-router";
import type { User } from "@/api/user/type";
import {
  PaginationConfig,
  SearchConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table";
import ModifyPwd from "@/views/UserManage/ModifyPwd.vue";
import MsgConfig from "@/views/UserManage/MsgConfig.vue";
import { useI18n } from "vue-i18n";
import { ElMessageBox, ElMessage } from "element-plus/es";

const { t } = useI18n();
const useRoute = useRouter();
const columns = ref([]);
const tableData = ref<Array<User>>();
const table: any = ref(null);

const activeUserId = ref<String>();

onMounted(() => {
  search(new TableSearch());
});

const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
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

const create = () => {
  useRoute.push({ path: useRoute.currentRoute.value.path + "/create" });
};

const edit = (row: User) => {
  useRoute.push({
    path: useRoute.currentRoute.value.path + "/create",
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
        if (table.value) {
          table.value?.search();
          ElMessage.success(t("commons.msg.delete_success"));
        }
      });
    })
    .catch(() => {
      ElMessage.info(t("commons.msg.delete_canceled"));
    });
};

const addRole = () => {
  console.log("addRole");
};

const showUserRoleDetails = (row: User) => {
  alert("用户角色树");
  //TODO 展示用户角色树
};

const handleSwitchStatus = (row: User) => {
  alert("切换用户状态");
  //TODO 切换用户状态
};

const msgConfigRef = ref();
const showMsgConfigDialog = () => {
  msgConfigRef.value.dialogVisible = true;
};

const modifyPwdRef = ref();
const showPwdDialog = (row: User) => {
  activeUserId.value = row.id
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
      { label: t("user.role"), value: "role_id" },
      { label: "Email", value: "email" },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([
    TableOperations.buildButtons().newInstance(
      t("commons.btn.edit"),
      "primary",
      edit,
      "Edit"
    ),
    TableOperations.buildButtons().newInstance(
      t("commons.btn.delete"),
      "danger",
      deleteUser,
      "Delete"
    ),
    TableOperations.buildButtons().newInstance(
      t("commons.personal.edit_pwd"),
      "primary",
      showPwdDialog,
      "EditPen"
    ),
    TableOperations.buildButtons().newInstance(
      t("user.notify_setting"),
      "primary",
      showMsgConfigDialog,
      "Bell"
    ),
  ]),
});
</script>

<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb
        :breadcrumbs="[{ to: {}, title: $t('user.manage') }]"
      ></breadcrumb>
    </template>
    <ce-table
      ref="table"
      :expand-row-keys="expandRowKeys"
      :columns="columns"
      :data="tableData"
      :tableConfig="tableConfig"
      @selection-change="handleSelectionChange"
      row-key="id"
    >
      <template #toolbar>
        <el-button @click="create" type="primary">{{
          $t("commons.btn.create")
        }}</el-button>
        <el-button @click="addRole">{{ $t("user.add_role") }}</el-button>
      </template>
      <el-table-column type="selection" />
      <el-table-column prop="username" label="ID" />
      <el-table-column prop="name" :label="$t('user.name')" />
      <el-table-column prop="email" :label="$t('user.email')" />
      <el-table-column prop="roles" :label="$t('user.role')">
        <template #default="scope">
          <div
            v-for="role in scope.row.roles"
            @click="showUserRoleDetails(scope.row)"
            style="cursor: pointer"
          >
            <a style="color: var(--el-color-primary)">{{ role.name }}<br /></a>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="source" :label="$t('user.source')" sortable>
        <template #default="scope">
          <span>{{ sourceFilter(scope.row.source) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="enabled" :label="$t('user.status')" sortable>
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
        min-width="120"
        sortable
      />
      <fu-table-operations v-bind="tableConfig.tableOperations" fix />
      <template #buttons>
        <fu-table-column-select type="icon" :columns="columns" size="small" />
      </template>
    </ce-table>
  </layout-content>

  <!-- 修改密码弹出框 -->
  <ModifyPwd ref="modifyPwdRef" :userId="activeUserId" />

  <!-- 通知设置弹出框 -->
  <MsgConfig ref="msgConfigRef" />
</template>

<style lang="scss" scoped>
:deep(.el-table__body) {
  .el-table__placeholder {
    display: none;
  }
}
</style>
