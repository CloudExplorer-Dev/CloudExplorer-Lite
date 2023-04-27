<script setup lang="ts">
import { ref, onMounted, computed, watch } from "vue";
import _ from "lodash";
import type { SimpleMap } from "@commons/api/base/type";
import { RolePageRequest } from "@commons/api/role/type";
import RoleApi from "@/api/role";
import { useRouter } from "vue-router";
import {
  PaginationConfig,
  SearchConfig,
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

const { t } = useI18n();

const useRoute = useRouter();
const permissionStore = usePermissionStore();
const columns = ref([]);

const table = ref<any>(null);
const tableData = ref<Array<Role>>();
const selectedRowData = ref<Array<Role>>();

const originRoles = ref<Array<Role>>();
const originRoleNameMap = ref<Array<SimpleMap<string>>>([]);
const rolePermissionTableRef = ref<InstanceType<typeof RolePermissionTable>>();

const showAddDialog = ref<boolean>(false);
const roleLoading = ref<boolean>(false);
const userLoading = ref<boolean>(false);
const permissionLoading = ref<boolean>(false);
const roles = ref<Array<Role>>([]);
//表单校验
const ruleFormRef = ref<FormInstance>();

const selectedRole = ref<Role>();

const editPermission = ref<boolean>(false);

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

//todo 改成国际化
const typeMap = ref<Array<SimpleMap<string>>>([
  { text: "系统", value: "origin" },
  { text: "自定义", value: "inherit" },
]);

const id = ref<string>();

const roleFormData = ref<Role>(new Role("", "", "", ""));

const permissionData = ref<Array<string>>([]);

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

watch(permissionLoading, (re) => {
  console.log(re);
});

onMounted(() => {
  listRoles();
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
          v-hasPermission="'[management-center]ROLE:EDIT'"
          style="color: #3370ff; font-size: 20px; cursor: pointer"
          @click="addRole"
        >
          <CirclePlusFilled />
        </el-icon>
      </div>

      <div
        class="role-btn role-item"
        v-for="role in inheritRoles"
        :class="{ active: selectedRole?.id === role.id }"
        :key="role.id"
        @click="select(role)"
      >
        <div class="role-text">
          {{ role.name }}
        </div>
        <MoreOptionsButton
          class="more-btn"
          :buttons="tableOperations.buttons"
          :row="role"
        />
      </div>
    </el-aside>
    <el-container direction="vertical">
      <el-header>header</el-header>
      <el-main class="permission-main" v-loading="permissionLoading">
        <RolePermissionTable
          :id="selectedRole?.id"
          :loading="permissionLoading"
          :parent-role-id="selectedRole?.parentRoleId"
          :edit-permission="editPermission && selectedRole?.type === 'inherit'"
          v-model:permission-data="permissionData"
          ref="rolePermissionTableRef"
        />
      </el-main>
      <el-footer
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
          修改
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
        <el-button @click="cancelAddRole"> 取消 </el-button>
        <el-button type="primary" @click="submitForm(ruleFormRef)">
          创建
        </el-button>
      </template>
    </el-dialog>
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
  padding: 24px 24px 4px;
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
</style>
