<template v-loading="loading">
  <layout-container :border="false">
    <template #content>
      <layout-container>
        <template #header><h4>基本信息</h4></template>
        <template #content>
          <RoleInfoTable
            :id="id"
            :loading="loading"
            :edit-info="true"
            v-model:role-data="roleFormData"
            v-model:role-form-data="roleFormData"
            v-model:rule-form-ref="ruleFormRef"
            :create-new="!id"
          />
        </template>
      </layout-container>

      <layout-container>
        <template #header><h4>角色权限</h4></template>
        <template #content>
          <RolePermissionTable
            :id="id"
            :loading="loading"
            :parent-role-id="currentParentRoleId"
            :edit-permission="true"
            v-model:permission-data="permissionData"
          />
        </template>
      </layout-container>

      <layout-container>
        <el-button @click="back">取消</el-button>
        <el-button type="primary" @click="submitForm(ruleFormRef)">
          保存
        </el-button>
      </layout-container>
    </template>
  </layout-container>
</template>
<script setup lang="ts">
const props = defineProps<{
  id?: string;
}>();

import { computed, type Ref, ref, watch } from "vue";
import { Role } from "@commons/api/role/type";
import { CreateRoleRequest, UpdateRoleRequest } from "@/api/role/type";
import { useRouter } from "vue-router";
import { type FormInstance, ElMessage } from "element-plus";
import RoleApi from "@/api/role";
import { useI18n } from "vue-i18n";
import RoleInfoTable from "./RoleInfoTable.vue";
import RolePermissionTable from "./RolePermissionTable.vue";

const { t } = useI18n();

const route = useRouter();

//表单校验
const ruleFormRef = ref<FormInstance>();
const roleFormData = ref<Role>(new Role("", "", "", ""));

const permissionData = ref<Array<string>>([]);

const loading: Ref<boolean> | undefined = ref<boolean>(false);

const currentParentRoleId = computed(() => {
  return roleFormData.value.parentRoleId;
});

watch(
  currentParentRoleId,
  (parentRoleId) => {
    if (!props.id && parentRoleId) {
      //恢复表单内输入框值
      permissionData.value = [];
    }
  },
  { immediate: true }
);

const back = () => {
  route.push({ name: "role_list" });
};

const submitForm = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid) => {
    if (valid) {
      if (!props.id) {
        RoleApi.addRole(
          CreateRoleRequest.newInstance(
            roleFormData.value,
            permissionData.value
          ),
          loading
        ).then((response) => {
          route.push({
            path: route.currentRoute.value.path.replace(
              "/create",
              `/detail/${response.data.id}`
            ),
          });
          ElMessage.success(t("commons.msg.save_success"));
        });
      } else {
        RoleApi.updateRole(
          UpdateRoleRequest.newInstance(
            roleFormData.value,
            permissionData.value
          ),
          loading
        ).then((response) => {
          /*route.push({
            path: route.currentRoute.value.path.replace(
              `/edit/${response.data.id}`,
              `/detail/${response.data.id}`
            ),
          });*/
          route.push({ name: "role_list" });
          ElMessage.success(t("commons.msg.save_success"));
        });
      }
    }
  });
};
</script>
<style lang="scss"></style>
