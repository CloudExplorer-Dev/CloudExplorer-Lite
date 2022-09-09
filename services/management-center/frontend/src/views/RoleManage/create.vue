<template v-loading="loading">
  <layout-container :border="false">
    <template #content>
      <layout-container>
        <template #header><h4>基本信息</h4></template>
        <template #content>
          <el-form
            :rules="rules"
            label-position="right"
            :model="roleFormData"
            :inline="false"
            ref="ruleFormRef"
            status-icon
            v-loading="loading"
          >
            <el-form-item label="名称" label-width="100px" prop="name">
              <el-col :span="11">
                <el-input v-model="roleFormData.name" />
              </el-col>
            </el-form-item>
            <el-form-item
              label="描述"
              label-width="100px"
              prop="description"
              :rules="rules.description"
            >
              <el-input v-model="roleFormData.description" />
            </el-form-item>

            <el-form-item
              label="继承角色"
              label-width="100px"
              prop="parentRoleId"
              :rules="rules.description"
            >
              <el-radio-group v-model="roleFormData.parentRoleId">
                <el-radio-button
                  v-for="baseRole in originRoles"
                  :key="baseRole.id"
                  :label="baseRole.id"
                >
                  {{ baseRole.name }}
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-form>
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
import { computed, onMounted, reactive, ref, watch } from "vue";
import type { Role } from "@commons/api/role/type";
import type { CreateRoleRequest } from "@/api/role/type";
import { useRouter } from "vue-router";
import { endsWith } from "lodash";
import type { FormRules, FormInstance } from "element-plus";
import { ElMessage } from "element-plus";
import RoleApi from "@/api/role";
import { useI18n } from "vue-i18n";

const { t } = useI18n();

const route = useRouter();

const roleFormData = ref<CreateRoleRequest>({});

//表单校验
const ruleFormRef = ref<FormInstance>();

const loading = ref<boolean>(false);

const originRoles = ref<Array<Role>>([]);

onMounted(() => {
  RoleApi.listRoles({ type: "origin" }, loading).then((ok) => {
    originRoles.value = ok.data;
    roleFormData.value.parentRoleId = originRoles.value[0]?.parentRoleId;
  });
});

const back = () => {
  route.push({ name: "role_list" });
};

const toEdit = (id: string) => {
  route.push({
    path: route.currentRoute.value.path.replace("/create", `/detail/${id}`),
    params: { edit: "permissions" },
  });
};

const rules = reactive<FormRules>({
  name: [
    {
      message: "角色名称不为空",
      trigger: "blur",
      type: "string",
      required: true,
    },
  ],
});

const submitForm = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid) => {
    if (valid) {
      RoleApi.addRole(roleFormData.value, loading).then((response) => {
        toEdit(response.data.id);
        ElMessage.success(t("commons.msg.save_success"));
      });
    }
  });
};
</script>
<style lang="scss"></style>
