<template v-loading="loading">
  <el-container class="create-catalog-container">
    <el-main ref="create-catalog-container">
      <div class="form-div">
        <el-row>
          <el-col span="24">
            <FormTitle>{{ t("commons.basic_info", "基本信息") }}</FormTitle>
          </el-col>
        </el-row>
        <el-row>
          <RoleInfoTable
            :id="id"
            :loading="loading"
            :edit-info="true"
            v-model:role-data="roleFormData"
            v-model:role-form-data="roleFormData"
            v-model:rule-form-ref="ruleFormRef"
            :create-new="!id"
          />
        </el-row>
        <el-row>
          <el-col span="24">
            <FormTitle>角色权限</FormTitle>
          </el-col>
        </el-row>
        <el-row style="border: 1px solid; color: var(--el-border-color)">
          <RolePermissionTable
            :id="id"
            :loading="loading"
            :parent-role-id="currentParentRoleId"
            :edit-permission="true"
            v-model:permission-data="permissionData"
          />
        </el-row>
      </div>
    </el-main>
    <el-footer>
      <div class="footer">
        <div class="form-div">
          <div class="footer-btn">
            <el-button class="cancel-btn" @click="back">取消</el-button>
            <el-button
              class="save-btn"
              type="primary"
              @click="submitForm(ruleFormRef)"
            >
              保存
            </el-button>
          </div>
        </div>
      </div>
    </el-footer>
  </el-container>
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
import FormTitle from "@/componnets/from_title/FormTitle.vue";

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
