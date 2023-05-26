<script setup lang="ts">
import CeDrawer from "@commons/components/ce-drawer/index.vue";
import { computed, reactive, ref, watch } from "vue";
import {
  type CreateUserRequest,
  Role,
  type RoleInfo,
  UpdateUserRequest,
  User,
} from "@/api/user/type";
import { createUser, getUser, updateUser } from "@/api/user";
import DetailFormTitle from "@/componnets/DetailFormTitle.vue";
import DetailFormLabel from "@/componnets/DetailFormLabel.vue";
import DetailFormValue from "@/componnets/DetailFormValue.vue";
import type { InternalRuleItem } from "async-validator/dist-types/interface";
import { useI18n } from "vue-i18n";
import type { FormRules, FormInstance } from "element-plus";
import _ from "lodash";

import { roleConst } from "@commons/utils/constants";

import { tree as orgTree } from "@commons/api/organization";
import type { OrganizationTree } from "@/api/organization/type";
import { workspaceTree } from "@commons/api/workspace";
import type { WorkspaceTree } from "@commons/api/workspace/type";
import { listRoles } from "@commons/api/role";
import { ElMessage } from "element-plus";

const props = withDefaults(
  defineProps<{
    id?: string;
  }>(),
  {}
);
const emit = defineEmits(["confirm", "cancel"]);

const ceDrawerRef = ref<InstanceType<typeof CeDrawer>>();
const formRef = ref<FormInstance | undefined>();
const { t } = useI18n();

const loading = ref<boolean>(false);

const user = ref<User | undefined>();

const form = ref<CreateUserRequest | UpdateUserRequest | any>({});

const orgTreeData = ref<OrganizationTree[]>();
const workspaceTreeData = ref<WorkspaceTree[]>();
const roles = ref<Role[]>([]);

const operationType = computed<"edit" | "create">(() => {
  if (props.id) {
    return "edit";
  } else {
    return "create";
  }
});

function getOrgTree() {
  orgTree().then((res) => {
    orgTreeData.value = res.data;
  });
}

function getWorkspaceTree() {
  workspaceTree().then((res) => {
    workspaceTreeData.value = res.data;
  });
}

const confirmPwdValidator = (
  rule: InternalRuleItem,
  value: string,
  callback: (error?: string | Error) => void
) => {
  if (value !== form.value.password) {
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

function onRoleChange(roleId: string, roleInfo: RoleInfo) {
  const role = _.find(roles.value, (r) => r.id === roleId) as Role;
  roleInfo.roleName = role.name;
  if (roleInfo.roleType !== role.parentRoleId) {
    roleInfo.organizationIds = [];
    roleInfo.workspaceIds = [];
  }
  roleInfo.roleType = role.parentRoleId;
}

function removeRow(index: number) {
  _.remove(form.value.roleInfoList, (r, i) => index === i);
}

function addRow() {
  if (form.value.roleInfoList == undefined) {
    form.value.roleInfoList = [];
  }
  form.value.roleInfoList.push({ roleInfoList: [] });
}

function filterRoles(currentRoleId?: string) {
  return _.filter(
    roles.value,
    (r) =>
      _.find(
        form.value.roleInfoList,
        (roleInfo) => roleInfo.roleId === r.id
      ) === undefined || r.id === currentRoleId
  );
}

function confirmEdit() {
  if (!formRef.value) return;
  formRef.value.validate((valid) => {
    if (valid) {
      const requestMethod =
        operationType.value === "edit" ? updateUser : createUser;
      requestMethod({ ...form.value, id: props.id, source: "local" }, loading)
        .then(() => {
          ElMessage.success(t("commons.msg.save_success"));

          ceDrawerRef.value?.close();
          user.value = undefined;
          form.value = {};
          emit("confirm");
        })
        .catch((err) => {
          ElMessage.error(err.response.data.message);
        });
    }
  });
}

function cancel() {
  ceDrawerRef.value?.close();
  user.value = undefined;
  form.value = {};
  emit("cancel");
}

function open() {
  ceDrawerRef.value?.open();

  getOrgTree();
  getWorkspaceTree();

  listRoles().then((res) => {
    roles.value = res.data;
  });
}

function refreshUser() {
  if (props.id) {
    loading.value = true;
    getUser(props.id)
      .then((res) => {
        listRoles()
          .then((res2) => {
            user.value = res.data;
            const _user = new UpdateUserRequest(user.value);
            const roleIds = _.map(res2.data, (r) => r.id);
            _user.roleInfoList = _.filter(_user.roleInfoList, (r) =>
              _.includes(roleIds, r.roleId)
            );
            form.value = _user;
          })
          .finally(() => {
            loading.value = false;
          });
      })
      .catch((err) => {
        loading.value = false;
      });
  } else {
    form.value = {
      username: "",
      name: "",
      email: "",
      roleInfoList: [],
    } as CreateUserRequest;
  }
}

watch(
  () => props.id,
  () => {
    refreshUser();
  }
);

defineExpose({ open, refreshUser });
</script>

<template>
  <CeDrawer
    ref="ceDrawerRef"
    :title="(operationType === 'edit' ? '编辑' : '创建') + $t('user.user')"
    :confirm-btn-name="operationType === 'edit' ? '保存' : '创建'"
    @confirm="confirmEdit"
    @cancel="cancel"
    :disable-btn="loading"
  >
    <el-container direction="vertical" v-loading="loading">
      <el-form
        :model="form"
        :rules="rule"
        ref="formRef"
        label-width="100px"
        label-position="top"
        require-asterisk-position="right"
      >
        <el-descriptions direction="vertical" :column="1">
          <template #title>
            <DetailFormTitle :title="t('commons.basic_info', '基本信息')" />
          </template>
          <el-descriptions-item v-if="operationType === 'edit'">
            <template #label>
              <DetailFormLabel :label="$t('user.username')" />
            </template>
            <DetailFormValue :value="user?.username" />
          </el-descriptions-item>
        </el-descriptions>

        <el-form-item prop="username" v-if="operationType === 'create'">
          <template #label>
            <DetailFormLabel :label="$t('user.username')" />
          </template>
          <el-input
            v-model="form.username"
            type="text"
            maxlength="30"
            show-word-limit
          />
        </el-form-item>

        <el-form-item prop="name">
          <template #label>
            <DetailFormLabel :label="$t('user.name')" />
          </template>
          <el-input
            v-model="form.name"
            type="text"
            maxlength="30"
            show-word-limit
          />
        </el-form-item>
        <el-form-item prop="email">
          <template #label>
            <DetailFormLabel label="Email" />
          </template>
          <el-input
            v-model="form.email"
            type="text"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>
        <el-form-item prop="phone">
          <template #label>
            <DetailFormLabel :label="$t('commons.personal.phone')" />
          </template>
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item prop="password" v-if="operationType === 'create'">
          <template #label>
            <DetailFormLabel :label="$t('user.password')" />
          </template>
          <el-input
            v-model="form.password"
            type="password"
            clearable
            show-password
            autocomplete="new-password"
          />
        </el-form-item>
        <el-form-item prop="confirmPassword" v-if="operationType === 'create'">
          <template #label>
            <DetailFormLabel :label="$t('commons.personal.confirm_password')" />
          </template>
          <el-input
            v-model="form.confirmPassword"
            type="password"
            clearable
            show-password
            autocomplete="new-password"
          />
        </el-form-item>

        <DetailFormTitle class="margin-top" :title="t('user.set_role')" />
        <template v-for="(roleInfo, index) in form.roleInfoList" :key="index">
          <div class="role-title">角色{{ index + 1 }}</div>
          <div class="role-content">
            <el-form-item
              :prop="`roleInfoList.${index}.roleId`"
              :rules="rule.roleId"
            >
              <el-select
                v-model="roleInfo.roleId"
                @change="onRoleChange(roleInfo.roleId, roleInfo)"
                :placeholder="$t('user.type')"
                style="width: 200px; margin-right: 8px"
              >
                <el-option
                  v-for="role in filterRoles(roleInfo.roleId)"
                  :key="role.id"
                  :label="role.name"
                  :value="role.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item
              v-if="roleInfo.roleType === roleConst.orgAdmin"
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
                collapse-tags
                collapse-tags-tooltip
                :max-collapse-tags="3"
                style="width: 512px; margin-right: 8px"
              />
            </el-form-item>

            <el-form-item
              v-if="roleInfo.roleType === roleConst.user"
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
                collapse-tags
                collapse-tags-tooltip
                :max-collapse-tags="3"
                style="width: 512px; margin-right: 8px"
              />
            </el-form-item>

            <el-icon
              size="17px"
              color="#646A73"
              class="delete-icon"
              @click="removeRow(index)"
            >
              <Delete />
            </el-icon>
          </div>
        </template>

        <span class="add-btn" @click="addRow">+ 添加角色</span>
      </el-form>
    </el-container>
  </CeDrawer>
</template>

<style lang="scss" scoped>
.role-title {
  font-style: normal;
  font-weight: 500;
  font-size: 14px;
  line-height: 22px;
  color: #1f2329;
  margin-top: 16px;
}

.role-title:after {
  content: "*";
  color: var(--el-color-danger);
  margin-left: 4px;
}

.role-title:first-child {
  margin-top: 16px;
}

.margin-top {
  margin-top: 32px;
}

.el-form-item {
  margin-bottom: 28px;
}

.role-content {
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  padding: 12px;
  background-color: #f7f9fc;
  border-radius: 4px;
  margin-bottom: 28px;

  .el-form-item {
    margin-bottom: 0;
  }

  .delete-icon {
    cursor: pointer;
  }
}

.add-btn {
  cursor: pointer;
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 22px;

  color: #3370ff;
}
</style>
