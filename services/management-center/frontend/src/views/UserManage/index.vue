<script setup lang="ts">
import { ref, onMounted } from "vue";
import { deleteUserById, listUser, User } from "@/api/user";
import { useRouter } from "vue-router";
import {
  PaginationConfig,
  SearchConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "ce-base/commons/components/ce-table";
import ModifyPwd from "@/views/UserManage/ModifyPwd.vue";
import MsgConfig from "@/views/UserManage/MsgConfig.vue";
import { useI18n } from "vue-i18n";
import { ElMessageBox, ElMessage } from "element-plus/es";

const { t } = useI18n();
const useRoute = useRouter();
const columns = ref([]);
const tableData = ref<Array<User>>();
const table: any = ref(null);

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
    path: useRoute.currentRoute.value.path + "/update",
    query: { id: row.id },
  });
};

const deleteUser = (row: User) => {
  ElMessageBox.confirm(t("确认删除用户"), {
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
const showPwdDialog = () => {
  modifyPwdRef.value.dialogVisible = true;
};

const sourceFilter = (userSource: string) => {
  if (userSource.toLowerCase() === "local") {
    return t("本地创建");
  }
  if (userSource.toLowerCase() === "extra") {
    return t("第三方");
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
        "创建时间"
      ),
    ],
    searchOptions: [
      { label: "ID", value: "username" },
      { label: "姓名", value: "name" },
      { label: "角色", value: "role_id" },
      { label: "Email", value: "email" },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([
    TableOperations.buildButtons().newInstance("编辑", "primary", edit, "Edit"),
    TableOperations.buildButtons().newInstance(
      "删除",
      "danger",
      deleteUser,
      "Delete"
    ),
    TableOperations.buildButtons().newInstance(
      "修改密码",
      "primary",
      showPwdDialog,
      "EditPen"
    ),
    TableOperations.buildButtons().newInstance(
      "通知设置",
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
      <breadcrumb :breadcrumbs="[{ to: {}, title: '用户管理' }]"></breadcrumb>
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
        <el-button @click="create" type="primary">创建</el-button>
        <el-button @click="addRole">添加角色</el-button>
      </template>
      <el-table-column type="selection" />
      <el-table-column prop="username" label="ID" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="roles" label="角色">
        <template #default="scope">
          <div
            v-for="role in scope.row.roles"
            @click="showUserRoleDetails(scope.row)"
            tyle="cursor:pointer"
          >
            <a style="color: var(--el-color-primary)">{{ role.name }}<br /></a>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="source" label="来源" sortable>
        <template #default="scope">
          <span>{{ sourceFilter(scope.row.source) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="enabled" label="状态" sortable>
        <template #default="scope">
          <el-switch
            v-model="scope.row.enabled"
            @change="handleSwitchStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号码" />
      <el-table-column
        prop="createTime"
        label="创建时间"
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
  <ModifyPwd ref="modifyPwdRef" />

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
