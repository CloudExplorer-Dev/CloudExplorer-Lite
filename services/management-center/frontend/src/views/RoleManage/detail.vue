<template>
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
            </el-form-item>
          </el-form>

          <el-radio-group v-model="roleData.parentRoleId">
            <el-radio-button
              v-for="baseRole in originRoles"
              :key="baseRole.id"
              :label="baseRole.id"
            >
              {{ baseRole.name }}
            </el-radio-button>
          </el-radio-group>
        </template>
      </layout-container>

      <layout-container>
        <template #header><h4>角色权限</h4></template>
        <template #content> </template>
      </layout-container>

      <layout-container v-if="!edit">
        <el-button @click="back">返回</el-button>
        <el-button type="primary" @click="toEdit">编辑</el-button>
      </layout-container>
      <layout-container v-if="edit">
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
  id: string;
}>();

import { computed, onMounted, reactive, ref, watch } from "vue";
import { useRouter } from "vue-router";
import { endsWith } from "lodash";
import type { FormRules, FormInstance } from "element-plus";
import { ElMessage } from "element-plus";
import RoleApi from "@/api/role";
import { Role } from "@commons/api/role/type";
import { UpdateRoleRequest } from "@/api/role/type";
import { useI18n } from "vue-i18n";
const { t } = useI18n();
const ruleFormRef = ref<FormInstance>();
const route = useRouter();

const roleData = ref<Role>(new Role(props.id, "", "", ""));
const originRoles = ref<Array<Role>>([]);

const roleFormData = ref<UpdateRoleRequest>(new UpdateRoleRequest(props.id));

const edit = computed(() => {
  return endsWith(route.currentRoute.value.path, `/edit/${props.id}`);
});

const loading = ref<boolean>(false);

onMounted(() => {
  init();
});

watch(edit, () => {
  init();
});

const init = () => {
  console.log(props.id);

  RoleApi.listRoles({ type: "origin" }, loading).then((ok) => {
    originRoles.value = ok.data;
  });
  //角色
  RoleApi.getRoleById(props.id, loading).then((ok) => {
    roleData.value = ok.data;
    roleFormData.value = ok.data;
  });
  //权限
};

const back = () => {
  route.push({ name: "role_list" });
};

const toEdit = () => {
  route.push({
    path: route.currentRoute.value.path.replace(
      `/detail/${props.id}`,
      `/edit/${props.id}`
    ),
  });
};

const rules = reactive<FormRules>({
  name: [
    {
      message: "名称不为空",
      trigger: "blur",
      type: "string",
      required: true,
    },
  ],
});

const submitForm = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid) => {
    if (valid && roleFormData.value) {
      RoleApi.updateRole(roleFormData.value).then((ok) => {
        back();
        ElMessage.success(t("commons.msg.save_success"));
      });
    }
  });
};
</script>
<style lang="scss"></style>
