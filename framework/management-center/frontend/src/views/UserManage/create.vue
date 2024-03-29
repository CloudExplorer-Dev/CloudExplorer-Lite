<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import _ from "lodash";
import type { FormRules } from "element-plus";
import type { FormInstance } from "element-plus/es";
import { useI18n } from "vue-i18n";
import type { RoleInfo, Role } from "@/api/user/type";
import { createUser, updateUser, getRoleInfo } from "@/api/user";
import { listRoles } from "@commons/api/role";
import { useUserStore } from "@commons/stores/modules/user";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus/es";
import { tree } from "@commons/api/organization";
import type { OrganizationTree } from "@/api/organization/type";
import { workspaceTree } from "@commons/api/workspace";
import type { WorkspaceTree } from "@commons/api/workspace/type";
import { roleConst } from "@commons/utils/constants";
import type { InternalRuleItem } from "async-validator/dist-types/interface";
import CeIcon from "@commons/components/ce-icon/index.vue";
import FormTitle from "@/componnets/form_title/FormTitle.vue";
const router = useRouter();
const { t } = useI18n();
const operationType = ref<string>("create");
const subTitle = ref<string>(t("commons.btn.create"));
const formRef = ref<FormInstance | undefined>();
const form = reactive<any>({
  id: null, // id
  username: "", // 用户ID
  name: "", // 姓名
  email: "",
  phone: "",
  password: "",
  confirmPassword: "",
  roleInfoList: [],
});
const confirmPwdValidator = (
  rule: InternalRuleItem,
  value: string,
  callback: (error?: string | Error) => void
) => {
  if (value !== form.password) {
    callback(new Error(t("commons.validate.confirm_pwd")));
  } else {
    callback();
  }
};

// 表单校验规则
const rule = reactive<FormRules>({
  username: [
    {
      required: true,
      message: t("commons.validate.input", ["ID"]),
      trigger: "blur",
    },
    {
      min: 1,
      max: 30,
      message: t("commons.validate.limit", ["1", "30"]),
      trigger: "blur",
    },
  ],
  name: [
    {
      required: true,
      message: t("commons.validate.required", [t("user.name")]),
      trigger: "blur",
    },
    {
      min: 2,
      max: 30,
      message: t("commons.validate.limit", ["2", "30"]),
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
      message: t("commons.validate.required", [t("user.email")]),
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
      message: t("commons.validate.required", [t("user.password")]),
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
      message: t("commons.validate.input", [
        t("commons.personal.confirm_password"),
      ]),
      trigger: "blur",
    },
    { validator: confirmPwdValidator, trigger: "blur" },
  ],
  roleId: [
    {
      required: true,
      message: t("user.validate.user_type_empty"),
      trigger: "change",
    },
  ],
  org: [
    {
      required: true,
      message: t("user.validate.org"),
      trigger: "change",
    },
  ],
  workspace: [
    {
      required: true,
      message: t("user.validate.workspace"),
      trigger: "change",
    },
  ],
});
const roles = ref<Role[]>([]);
const orgTreeData = ref<OrganizationTree[]>();
const workspaceTreeData = ref<WorkspaceTree[]>();
const isAddLineAble = ref(true);
// 添加用户角色选项
const addLine = () => {
  const roleInfo: RoleInfo = {
    roleId: "",
    roleType: "",
    organizationIds: [],
    workspaceIds: [],
    selectedRoleIds: [],
  };
  form.roleInfoList.forEach((item: RoleInfo) => {
    if (roleInfo.selectedRoleIds) {
      roleInfo.selectedRoleIds.push(item.roleId);
    }
  });
  form.roleInfoList.push(roleInfo);
  if (form.roleInfoList && roles) {
    if (form.roleInfoList.length === roles.value.length) {
      isAddLineAble.value = false;
    }
  }
};
// 清除用户角色选项
const subtractLine = (roleInfo: RoleInfo) => {
  _.remove(form.roleInfoList, (item) => {
    return item === roleInfo;
  });
  if (form.roleInfoList.length !== roles.value.length) {
    isAddLineAble.value = true;
  }
};
const filterRole = (role: Role, roleInfo: RoleInfo) => {
  if (!roleInfo.selectedRoleIds) return;
  let value = true;
  if (roleInfo.selectedRoleIds.length === 0) {
    value = true;
  } else {
    roleInfo.selectedRoleIds.forEach((roleId) => {
      if (role.id === roleId) {
        value = false;
      }
    });
  }
  return value;
};
const setRoleType = (roleInfo: RoleInfo, roleId: string) => {
  roleInfo.roleType = getParentRoleId(roleId);
};
const handleCancel = (formEl: FormInstance) => {
  formEl.resetFields();
  backToUserList();
};
const handleCreate = (formEl: FormInstance) => {
  if (!formEl) return;
  formEl.validate((valid) => {
    if (valid) {
      const requestMethod =
        operationType.value === "edit" ? updateUser : createUser;
      const param = form;
      param.source = "local";
      requestMethod(param)
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
const getParentRoleId = (roleId: string) => {
  let parentRoleId = null;
  roles.value.forEach((role: Role) => {
    if (role.id === roleId) {
      parentRoleId = role.parentRoleId;
    }
  });
  return parentRoleId;
};
onMounted(() => {
  const getOrgTree = tree().then((res) => {
    orgTreeData.value = res.data;
  });
  const getWsTree = workspaceTree().then((res) => {
    workspaceTreeData.value = res.data;
  });
  const getRoles = listRoles({ baseRole: useUserStore().currentRole }).then(
    (res) => {
      roles.value = res.data;
    }
  );
  const userId = router.currentRoute.value.query.id;
  if (userId) {
    operationType.value = "edit";
    subTitle.value = t("commons.btn.edit");
    Promise.all([getOrgTree, getWsTree, getRoles]).then(() => {
      getRoleInfo(userId as string).then((res) => {
        form.username = res.data.username;
        form.name = res.data.name;
        form.email = res.data.email;
        form.phone = res.data.phone;
        form.id = userId;
        res.data.roleInfoList.forEach((roleInfo: RoleInfo) => {
          roleInfo.roleType = getParentRoleId(roleInfo.roleId);
          roleInfo.selectedRoleIds = [];
          form.roleInfoList.push(roleInfo);
        });
        if (form.roleInfoList.length === roles.value.length) {
          isAddLineAble.value = false;
        }
      });
    });
  } else {
    addLine();
  }
});
</script>

<template>
  <el-container class="create-catalog-container">
    <el-main ref="create-catalog-container">
      <div class="form-div">
        <el-form
          :model="form"
          :rules="rule"
          ref="formRef"
          label-width="100px"
          label-position="top"
          require-asterisk-position="right"
        >
          <el-row>
            <FormTitle>{{ t("commons.basic_info", "基本信息") }}</FormTitle>
          </el-row>
          <el-row :gutter="10">
            <el-col :span="11">
              <el-form-item label="ID" prop="username">
                <el-input
                  v-model="form.username"
                  type="text"
                  maxlength="30"
                  show-word-limit
                  :disabled="operationType === 'edit'"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
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
          <el-row :gutter="10">
            <el-col :span="11">
              <el-form-item label="Email" prop="email">
                <el-input
                  v-model="form.email"
                  type="text"
                  maxlength="50"
                  show-word-limit
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('commons.personal.phone')" prop="phone">
                <el-input v-model="form.phone" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="10" v-if="operationType === 'create'">
            <el-col :span="11">
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
            <el-col :span="12">
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
          <el-row>
            <el-col span="24">
              <FormTitle>{{ $t("user.set_role") }}</FormTitle>
            </el-col>
          </el-row>
          <el-row v-for="(roleInfo, index) in form.roleInfoList" :key="index">
            <!-- 用户角色 -->
            <el-row style="width: 100%">
              <el-col :span="23">
                <el-form-item
                  :label="$t('user.type')"
                  :prop="`roleInfoList.${index}.roleId`"
                  :rules="rule.roleId"
                >
                  <el-select
                    v-model="roleInfo.roleId"
                    @change="setRoleType(roleInfo, roleInfo.roleId)"
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
              <el-col :span="1" class="padding-top-40">
                <el-tooltip
                  class="box-item"
                  effect="dark"
                  :content="$t('user.delete_role')"
                  placement="bottom"
                  v-if="form.roleInfoList.length > 1"
                >
                  <div
                    class="delete-button-class"
                    @click="subtractLine(roleInfo)"
                  >
                    <CeIcon
                      size="var(--ce-star-menu-icon-width,13.33px)"
                      code="icon_delete-trash_outlined1"
                    ></CeIcon>
                  </div>
                </el-tooltip>
              </el-col>
            </el-row>

            <!-- 选择组织 -->
            <el-row
              style="width: 100%"
              v-if="roleInfo.roleType === roleConst.orgAdmin"
            >
              <el-col :span="23">
                <el-form-item
                  :label="$t('user.add_org')"
                  :prop="`roleInfoList.${index}.organizationIds`"
                  :rules="rule.org"
                >
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
            <el-row
              style="width: 100%"
              v-if="roleInfo.roleType === roleConst.user"
            >
              <el-col :span="23">
                <el-form-item
                  :label="$t('user.add_workspace')"
                  :prop="`roleInfoList.${index}.workspaceIds`"
                  :rules="rule.workspace"
                >
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
          </el-row>
          <el-row :gutter="10">
            <el-col :span="24">
              <el-form-item>
                <el-tooltip
                  class="box-item"
                  effect="dark"
                  :content="$t('user.add_role')"
                  placement="bottom"
                >
                  <div
                    class="add-button-class"
                    @click="addLine"
                    :disabled="!isAddLineAble"
                  >
                    <CeIcon
                      size="var(--ce-star-menu-icon-width,13.33px)"
                      code="icon_add_outlined"
                    ></CeIcon>
                    <span class="span-class">
                      {{ t("commons.btn.add", "添加") }}
                    </span>
                  </div>
                </el-tooltip>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-main>
    <el-footer>
      <div class="footer">
        <div class="form-div">
          <div class="footer-btn">
            <el-button class="cancel-btn" @click="handleCancel(formRef)">{{
              $t("commons.btn.cancel")
            }}</el-button>
            <el-button
              class="save-btn"
              type="primary"
              @click="handleCreate(formRef)"
              >{{ $t("commons.btn.save") }}</el-button
            >
          </div>
        </div>
      </div>
    </el-footer>
  </el-container>
</template>

<style lang="scss"></style>
