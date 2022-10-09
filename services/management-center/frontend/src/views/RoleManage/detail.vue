<template>
  <layout-container :border="false">
    <template #content>
      <layout-container v-loading="loading">
        <template #header>
          <h4>基本信息</h4>
        </template>
        <template #btn>
          <el-button
            key="edit"
            type="primary"
            text
            @click="changeToEditInfo"
            v-if="showEditInfoButton"
          >
            修改
          </el-button>

          <el-button v-if="editInfo" @click="cancelEditInfo"> 取消 </el-button>
          <el-button
            type="primary"
            v-if="editInfo"
            @click="submitRoleForm(ruleFormRef)"
          >
            保存
          </el-button>
        </template>
        <template #content>
          <RoleInfoTable
            :id="id"
            :loading="loading"
            :edit-info="editInfo"
            v-model:role-data="roleData"
            v-model:role-form-data="roleFormData"
            v-model:rule-form-ref="ruleFormRef"
          />
        </template>
      </layout-container>

      <layout-container v-loading="loadingPermission">
        <template #header>
          <h4>角色权限</h4>
        </template>
        <template #btn>
          <el-button
            key="edit"
            type="primary"
            text
            @click="changeToEditPermission"
            v-if="showEditPermissionButton"
          >
            修改
          </el-button>

          <el-button v-if="editPermission" @click="cancelEditPermission">
            取消
          </el-button>
          <el-button
            type="primary"
            v-if="editPermission"
            @click="submitRolePermission(permissionData)"
          >
            保存
          </el-button>
        </template>
        <template #content>
          <RolePermissionTable
            :id="id"
            :loading="loadingPermission"
            :parent-role-id="currentParentRoleId"
            :edit-permission="editPermission"
            v-model:permission-data="permissionData"
          />
        </template>
      </layout-container>

      <layout-container v-if="!editInfo && !editPermission">
        <el-button @click="back">返回</el-button>
      </layout-container>
    </template>
  </layout-container>
</template>
<script setup lang="ts">
const props = defineProps<{
  id: string;
}>();

import { computed, type Ref, ref } from "vue";
import { useRouter } from "vue-router";
import type { FormInstance } from "element-plus";
import { ElMessage } from "element-plus";
import RoleApi from "@/api/role";
import { Role } from "@commons/api/role/type";
import { UpdateRoleRequest } from "@/api/role/type";
import { useI18n } from "vue-i18n";
import RoleInfoTable from "./RoleInfoTable.vue";
import RolePermissionTable from "./RolePermissionTable.vue";

const { t } = useI18n();
const route = useRouter();

const ruleFormRef = ref<FormInstance>();

const roleData = ref<Role>(new Role(props.id, "", "", ""));
const roleFormData = ref<UpdateRoleRequest>(new UpdateRoleRequest(props.id));

const permissionData = ref<Array<string>>([]);

const editInfo = ref<boolean>(false);

const showEditInfoButton = computed(() => {
  return roleData.value
    ? !editInfo.value && roleData.value.type !== "origin"
    : false;
});
const showEditPermissionButton = computed(() => {
  return permissionData.value && roleData.value
    ? !editPermission.value && roleData.value.type !== "origin"
    : false;
});

const editPermission = ref<boolean>(false);

const loading: Ref<boolean> | undefined = ref<boolean>(false);
const loadingPermission: Ref<boolean> | undefined = ref<boolean>(false);

const currentParentRoleId = computed(() => {
  return roleData.value.parentRoleId;
});

const back = () => {
  route.push({ name: "role_list" });
};

const changeToEditInfo = () => {
  editInfo.value = true;
};

const cancelEditInfo = () => {
  editInfo.value = false;
};

const changeToEditPermission = () => {
  editPermission.value = true;
};

const cancelEditPermission = () => {
  editPermission.value = false;
};

const submitRoleForm = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid) => {
    if (valid && roleFormData.value) {
      RoleApi.updateRole(
        UpdateRoleRequest.newInstance(roleFormData.value),
        loading
      ).then((ok) => {
        roleData.value = ok.data;
        cancelEditInfo();
        ElMessage.success(t("commons.msg.save_success"));
      });
    }
  });
};

const submitRolePermission = (permissionIds: Array<string>) => {
  console.log(permissionIds);
  RoleApi.updateRolePermissions(
    props.id,
    permissionIds,
    loadingPermission
  ).then((ok) => {
    permissionData.value = ok.data;
    cancelEditPermission();
    ElMessage.success(t("commons.msg.save_success"));
  });
};
</script>
<style lang="scss">
.edit-button-container {
  text-align: center;
  line-height: 50px;
  align-items: center;
}

.permission-container {
  width: 100%;
  min-height: 100px;
  .module-selector {
  }
}
</style>
