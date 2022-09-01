<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { roleConst } from "ce-base/commons/utils/constants";
import _ from "lodash";
import { FormRules } from "element-plus";
import { FormInstance } from "element-plus/es";
import { useI18n } from "vue-i18n";
import { RoleInfo, Role } from "@/api/user/type";
import { createUser, listRole,workspaceTree } from "@/api/user";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus/es";
import { OrganizationTree, tree } from "@/api/organization";
import { $tv } from "ce-base/commons/base-locales";

const router = useRouter();
const { t } = useI18n();
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

const rule: FormRules = {};

// const rule: FormRules = {
//   username: [
//     {
//       required: true,
//       message: "ID必填",
//       trigger: "blur",
//     },
//     {
//       min: 1,
//       max: 30,
//       message: "长度1-30",
//       trigger: "blur",
//     },
//   ],
//   name: [
//     {
//       required: true,
//       message: "姓名必填",
//       trigger: "blur",
//     },
//     {
//       min: 2,
//       max: 30,
//       message: "2-30个字符",
//       trigger: "blur",
//     },
//   ],
//   phone: [
//     {
//       pattern: /^1[3|4|5|7|8][0-9]{9}$/,
//       message: "手机号码格式不正确",
//       trigger: "blur",
//     },
//   ],
//   email: [
//     {
//       required: true,
//       message: "邮箱必填",
//       trigger: "blur",
//     },
//     {
//       required: true,
//       pattern: /^[a-zA-Z0-9_._-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
//       message: "邮箱格式不正确",
//       trigger: "blur",
//     },
//   ],
//   password: [
//     {
//       required: true,
//       message: "密码必填",
//       trigger: "blur",
//     },
//     {
//       required: true,
//       pattern: /^(?!.*\s)(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[\W_]).{8,30}$/,
//       message: t("commons.validate.pwd"),
//       trigger: "blur",
//     },
//   ],
//   confirmPassword: [
//     {
//       required: true,
//       message: $tv(
//         "commons.validate.input",
//         "commons.personal.confirm_password"
//       ),
//       trigger: "blur",
//     },
//     { validator: confirmPwdValidator, trigger: "blur" },
//   ],
// };

// const roles = ref<Array<Role>>([
//   {
//     id: roleConst.admin,
//     name: "系统管理员",
//     type: "original",
//     parentRoleId: "",
//   },
//   {
//     id: roleConst.orgAdmin,
//     name: "组织管理员",
//     type: "original",
//     parentRoleId: "",
//   },
//   {
//     id: roleConst.user,
//     name: "工作空间用户",
//     type: "original",
//     parentRoleId: "",
//   },
// ])

const roles = ref<Role[]>();
const orgTreeData = ref<OrganizationTree[]>();
const workspaceTreeData = ref<OrganizationTree[]>();
const isAddLineAble = ref(true);

// 添加用户类型选项
const addLine = () => {
  const roleInfo: any = {
    roleId: "",
    roleType: "",
    organizationIds: [],
    workspaceIds: [],
    selects: [],
  };
  form.roleInfoList.forEach((item: RoleInfo) => {
    roleInfo.selects.push(item.roleId);
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
          ElMessage.success("用户创建成功");
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
  tree().then((res) => {
    orgTreeData.value = res.data;
  });

  workspaceTree().then((res) => {
    workspaceTreeData.value = res.data;
  });

  listRole().then((res) => {
    roles.value = res.data;
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
            { to: { name: 'user' }, title: '用户管理' },
            { to: {}, title: '创建' },
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
            <span>基本信息</span>
          </template>
          <template #content>
            <el-row>
              <el-col :span="10">
                <el-form-item label="ID" prop="username">
                  <el-input
                    v-model="form.username"
                    :placeholder="$t('请输入ID')"
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
                    :placeholder="$t('请输入姓名')"
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
                    :placeholder="$t('请输入邮箱')"
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
                  <el-input
                    v-model="form.phone"
                    :placeholder="$t('请输入手机号')"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="10">
                <el-form-item :label="$t('密码')" prop="password">
                  <el-input
                    v-model="form.password"
                    :placeholder="$t('请输入密码')"
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
                    :placeholder="$t('请确认密码')"
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
            <span>设置角色</span>
          </template>
          <template #content>
            <div v-for="(roleInfo, index) in form.roleInfoList" :key="index">
              <!-- 用户类型 -->
              <el-row>
                <el-col :span="20">
                  <el-form-item label="用户类型">
                    <el-select
                      v-model="roleInfo.roleId"
                      placeholder="用户类型"
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
                    content="删除角色"
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
                  <el-form-item label="添加组织">
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
                  <el-form-item label="添加工作空间">
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
                content="添加角色"
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
