<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import _ from "lodash";
import type { FormRules } from "element-plus";
import type { FormInstance } from "element-plus/es";
import { useI18n } from "vue-i18n";
import type { RoleInfo, Role } from "@/api/user/type";
import { createUser, getRoleInfo, listRole, workspaceTree } from "@/api/user";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus/es";
import { tree } from "@/api/organization";
import type { OrganizationTree } from "@/api/organization/type";
import { $tv } from "@commons/base-locales";
import { roleConst } from "@commons/utils/constants";

const router = useRouter();
const { t } = useI18n();
const subTitle = ref(t("commons.btn.create"));
const formRef = ref<FormInstance | undefined>();
const form = reactive<any>({
  username: "", // 用户ID
  name: "", // 姓名
  email: "",
  phone: "",
  password: "",
  confirmPassword: "",
  roleInfoList: [],
});

const confirmPwdValidator = (rule: any, value: any, callback: any) => {
  if (value !== form.password) {
    callback(new Error(t("commons.validate.confirm_pwd")));
  } else {
    callback();
  }
};

// 表单校验规则
const rule: FormRules = {
  username: [
    {
      required: true,
      message: $tv("commons.validate.required", "ID"),
      trigger: "blur",
    },
    {
      min: 1,
      max: 30,
      message: $tv("commons.validate.limit", "1", "30"),
      trigger: "blur",
    },
  ],
  name: [
    {
      required: true,
      message: $tv("commons.validate.required", "user.name"),
      trigger: "blur",
    },
    {
      min: 2,
      max: 30,
      message: $tv("commons.validate.limit", "2", "30"),
      trigger: "blur",
    },
  ],
  phone: [
    {
      pattern: /^1[3|4|5|7|8][0-9]{9}$/,
      message: t("user.validate.phone_format"),
      trigger: "blur",
    },
  ],
  email: [
    {
      required: true,
      message: $tv("commons.validate.required", "user.email"),
      trigger: "blur",
    },
    {
      required: true,
      pattern: /^[a-zA-Z0-9_._-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
      message: t("user.validate.email_format"),
      trigger: "blur",
    },
  ],
  password: [
    {
      required: true,
      message: $tv("commons.validate.required", "user.password"),
      trigger: "blur",
    },
    {
      required: true,
      pattern: /^(?!.*\s)(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[\W_]).{8,30}$/,
      message: t("commons.validate.pwd"),
      trigger: "blur",
    },
  ],
  confirmPassword: [
    {
      required: true,
      message: $tv(
        "commons.validate.input",
        "commons.personal.confirm_password"
      ),
      trigger: "blur",
    },
    { validator: confirmPwdValidator, trigger: "blur" },
  ],
};

const roles = ref<Role[]>();
const orgTreeData = ref<OrganizationTree[]>();
const workspaceTreeData = ref<OrganizationTree[]>();
const isAddLineAble = ref(true);

// 添加用户类型选项
const addLine = () => {
  const roleInfo: RoleInfo = {
    roleId: "",
    roleType: "",
    organizationIds: [],
    workspaceIds: [],
    selects: [],
  };
  form.roleInfoList.forEach((item: RoleInfo) => {
    if (roleInfo.selects) {
      roleInfo.selects.push(item.roleId);
    }
  });
  form.roleInfoList.push(roleInfo);
  if (form.roleInfoList && roles) {
    if (roles.value && form.roleInfoList.length === roles.value.length) {
      isAddLineAble.value = false;
    }
  }
};

// 清除用户类型选项
const subtractLine = (roleInfo: RoleInfo) => {
  _.remove(form.roleInfoList, (item) => {
    return item === roleInfo;
  });
  if (roles.value && form.roleInfoList.length !== roles.value.length) {
    isAddLineAble.value = true;
  }
};

const filterRole = (role: Role, roleInfo: RoleInfo) => {
  if (!roleInfo.selects) return;
  let value = true;
  if (roleInfo.selects.length === 0) {
    value = true;
  } else {
    roleInfo.selects.forEach((roleId) => {
      if (role.id === roleId) {
        value = false;
      }
    });
  }
  return value;
};

const handleCancel = (formEl: FormInstance) => {
  formEl.resetFields();
  backToUserList();
};

const handleCreate = (formEl: FormInstance) => {
  if (!formEl) return;
  formEl.validate((valid) => {
    if (valid) {
      const param = form;
      param.source = "local";
      createUser(param)
        .then(() => {
          ElMessage.success(t("commons.msg.save_success"));
          backToUserList();
        })
        .catch((err) => {
          ElMessage.error(err.response.data.message);
        });
    } else {
      return false;
    }
  });
};

const backToUserList = () => {
  router.push({ name: "user" });
};

onMounted(() => {
  const userId = router.currentRoute.value.query.id;
  if (userId) {
    subTitle.value = t("commons.btn.edit");
  }

  getRoleInfo(userId as string).then((res) => {
    console.log(res.data);
  });
  addLine();
});
</script>

<template>
  <div style="height: 100%">
    <layout-content>
      <template #breadcrumb>
        <breadcrumb
          :breadcrumbs="[
            { to: { name: 'user' }, title: $t('user.manage') },
            { to: {}, title: subTitle },
          ]"
        ></breadcrumb>
      </template>
      <el-form
        :model="form"
        :rules="rule"
        ref="formRef"
        label-width="100px"
        label-position="right"
      >
        <layout-container>
          <template #header>
            <span>{{ $t("commons.basic_info") }}</span>
          </template>
          <template #content>
            <el-row>
              <el-col :span="10">
                <el-form-item label="ID" prop="username">
                  <el-input
                    v-model="form.username"
                    type="text"
                    maxlength="30"
                    show-word-limit
                  />
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item :label="$t('user.name')" prop="name">
                  <el-input
                    v-model="form.name"
                    type="text"
                    maxlength="30"
                    show-word-limit
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="10">
                <el-form-item label="Email" prop="email">
                  <el-input
                    v-model="form.email"
                    type="text"
                    maxlength="50"
                    show-word-limit
                  />
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item
                  :label="$t('commons.personal.phone')"
                  prop="phone"
                >
                  <el-input v-model="form.phone" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="10">
                <el-form-item :label="$t('user.password')" prop="password">
                  <el-input
                    v-model="form.password"
                    type="password"
                    clearable
                    show-password
                    autocomplete="new-password"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item
                  :label="$t('commons.personal.confirm_password')"
                  prop="confirmPassword"
                >
                  <el-input
                    v-model="form.confirmPassword"
                    type="password"
                    clearable
                    show-password
                    autocomplete="new-password"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </template>
        </layout-container>

        <layout-container>
          <template #header>
            <span>{{ $t("user.set_role") }}</span>
          </template>
          <template #content>
            <div v-for="(roleInfo, index) in form.roleInfoList" :key="index">
              <!-- 用户类型 -->
              <el-row>
                <el-col :span="20">
                  <el-form-item :label="$t('user.type')">
                    <el-select
                      v-model="roleInfo.roleId"
                      :placeholder="$t('user.type')"
                      style="width: 100%"
                    >
                      <el-option
                        v-for="role in roles"
                        :key="role.id"
                        :label="role.name"
                        :value="role.id"
                        v-show="filterRole(role, roleInfo)"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="2" style="padding-left: 60px">
                  <el-tooltip
                    class="box-item"
                    effect="dark"
                    :content="$t('user.delete_role')"
                    placement="bottom"
                  >
                    <el-button
                      v-if="form.roleInfoList.length > 1"
                      @click="subtractLine(roleInfo)"
                      type="danger"
                      icon="minus"
                      circle
                    ></el-button>
                  </el-tooltip>
                </el-col>
              </el-row>

              <!-- 选择组织 -->
              <el-row v-if="roleInfo.roleId === roleConst.orgAdmin">
                <el-col :span="20">
                  <el-form-item :label="$t('user.add_org')">
                    <el-tree-select
                      v-model="roleInfo.organizationIds"
                      node-key="id"
                      :props="{ label: 'name' }"
                      :data="orgTreeData"
                      :render-after-expand="false"
                      filterable
                      multiple
                      show-checkbox
                      check-strictly
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
              </el-row>

              <!-- 选择工作空间 -->
              <el-row v-if="roleInfo.roleId === roleConst.user">
                <el-col :span="20">
                  <el-form-item :label="$t('user.add_workspace')">
                    <el-tree-select
                      v-model="roleInfo.workspaceIds"
                      node-key="id"
                      :props="{ label: 'name' }"
                      :data="workspaceTreeData"
                      :render-after-expand="false"
                      filterable
                      multiple
                      show-checkbox
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
            </div>

            <div style="text-align: center">
              <el-tooltip
                class="box-item"
                effect="dark"
                :content="$t('user.add_role')"
                placement="bottom"
              >
                <el-button
                  @click="addLine"
                  :disabled="!isAddLineAble"
                  type="primary"
                  icon="plus"
                  circle
                ></el-button>
              </el-tooltip>
            </div>
          </template>
        </layout-container>
        <layout-container>
          <el-button @click="handleCancel(formRef)">{{
            $t("commons.btn.cancel")
          }}</el-button>
          <el-button type="primary" @click="handleCreate(formRef)">{{
            $t("commons.btn.create")
          }}</el-button>
        </layout-container>
      </el-form>
    </layout-content>
  </div>
</template>

<style lang="scss"></style>
