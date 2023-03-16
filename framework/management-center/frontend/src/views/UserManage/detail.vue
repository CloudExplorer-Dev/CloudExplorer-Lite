<script setup lang="ts">
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import { onMounted, reactive, ref } from "vue";
import { tree } from "@commons/api/organization";
import type { OrganizationTree } from "@/api/organization/type";
import { workspaceTree } from "@commons/api/workspace";
import type { WorkspaceTree } from "@commons/api/workspace/type";
import { getRoleInfo, updateUser } from "@/api/user";
import type { Role, RoleInfo } from "@/api/user/type";
import { listRoles } from "@commons/api/role";
import { useUserStore } from "@commons/stores/modules/user";
import { roleConst } from "@commons/utils/constants";
import _ from "lodash";
import type { UpdateUserForm } from "@/views/UserManage/type";
import type { FormInstance, FormRules } from "element-plus";
import { ElMessage } from "element-plus";
import CeIcon from "@commons/components/ce-icon/index.vue";

const router = useRouter();
const { t } = useI18n();
const basicEditable = ref(false);
const roleEditable = ref(false);
const roles = ref<Role[]>([]);
const orgTreeData = ref<OrganizationTree[]>();
const workspaceTreeData = ref<WorkspaceTree[]>();
const isAddLineAble = ref(true);
const formRef = ref<FormInstance | undefined>();
const originUserBasicData = ref();
const originUserRoleData = ref<RoleInfo[]>();
// 资源分类：基本信息/角色信息
const resourceConst = {
  basic: "BASIC",
  role: "ROLE",
};
const form = ref<UpdateUserForm>({
  id: "", // ID
  username: "", // 用户ID
  name: "", // 姓名
  email: "",
  phone: "",
  enabled: true,
  source: "", // 账号类型
  createTime: "",
  roleInfoList: [],
});

// 校验用户角色信息;
const validateRoleInfoList = (): boolean => {
  let result = true;
  if (!form.value.roleInfoList || form.value.roleInfoList.length < 1) {
    ElMessage.error(t("user.validate.role_empty"));
    result = false;
  } else {
    form.value.roleInfoList.forEach((roleInfo: RoleInfo) => {
      if (roleInfo.roleId === "") {
        ElMessage.error(t("user.validate.user_type_empty"));
        result = false;
      }
      if (
        roleInfo.roleType === roleConst.orgAdmin &&
        roleInfo.organizationIds.length < 1
      ) {
        ElMessage.error(t("user.validate.org"));
        result = false;
      }
      if (
        roleInfo.roleType === roleConst.user &&
        roleInfo.workspaceIds.length < 1
      ) {
        ElMessage.error(t("user.validate.workspace"));
        result = false;
      }
    });
  }
  return result;
};

// 表单校验规则
const rule = reactive<FormRules>({
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
});

const edit = (resource: string) => {
  if (resource === resourceConst.basic) {
    basicEditable.value = true;
  } else {
    roleEditable.value = true;
  }
};

const save = (resource: string, formEl: FormInstance) => {
  if (!formEl) return;

  // 校验基本信息
  if (resource === resourceConst.basic) {
    formEl.validate((valid) => {
      if (valid) {
        // 更新基本信息数据,不更新角色信息
        const param = JSON.parse(JSON.stringify(form.value));
        param.roleInfoList = [];
        updateUser(param).then(() => {
          ElMessage.success(t("commons.msg.save_success"));
          basicEditable.value = false;
          originUserBasicData.value = JSON.parse(JSON.stringify(form.value));
        });
      } else {
        ElMessage.error(t("user.validate.param"));
        return false;
      }
    });
  }

  // 校验角色信息
  if (resource === resourceConst.role && validateRoleInfoList()) {
    // 更新角色信息，保持基本信息不变
    const param = JSON.parse(JSON.stringify(originUserBasicData.value));
    param.roleInfoList = JSON.parse(JSON.stringify(form.value.roleInfoList));
    updateUser(param).then(() => {
      ElMessage.success(t("commons.msg.save_success"));
      roleEditable.value = false;
      originUserRoleData.value = JSON.parse(
        JSON.stringify(form.value.roleInfoList)
      );
    });
  }
};

const cancel = (resource: string, formEl: FormInstance) => {
  if (resource === resourceConst.basic) {
    // 还原数据
    form.value = JSON.parse(JSON.stringify(originUserBasicData.value));
    basicEditable.value = false;
    formEl.validate();
  } else {
    // 还原数据
    if (originUserRoleData.value) {
      form.value.roleInfoList = JSON.parse(
        JSON.stringify(originUserRoleData.value)
      );
    }
    roleEditable.value = false;
  }
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

// 添加用户角色选项
const addLine = () => {
  const roleInfo: RoleInfo = {
    roleId: "",
    roleType: "",
    organizationIds: [],
    workspaceIds: [],
    selectedRoleIds: [],
  };
  form.value.roleInfoList.forEach((item: RoleInfo) => {
    if (roleInfo.selectedRoleIds) {
      roleInfo.selectedRoleIds.push(item.roleId);
    }
  });
  form.value.roleInfoList.push(roleInfo);
  if (form.value.roleInfoList && roles) {
    if (form.value.roleInfoList.length === roles.value.length) {
      isAddLineAble.value = false;
    }
  }
};

// 清除用户角色选项
const subtractLine = (roleInfo: RoleInfo) => {
  _.remove(form.value.roleInfoList, (item) => {
    return item === roleInfo;
  });
  if (form.value.roleInfoList.length !== roles.value.length) {
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

const enabledFilter = (enabled: boolean) => {
  if (enabled) {
    return t("commons.btn.enable");
  } else {
    return t("commons.btn.disable");
  }
};

const sourceFilter = (userSource: string) => {
  if (userSource.toLowerCase() === "local") {
    return t("user.local");
  }
  if (userSource.toLowerCase() === "extra") {
    return t("user.extra");
  }
  return userSource;
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
    Promise.all([getOrgTree, getWsTree, getRoles]).then(() => {
      getRoleInfo(userId as string).then((res) => {
        res.data.roleInfoList.forEach((roleInfo: RoleInfo) => {
          roleInfo.roleType = getParentRoleId(roleInfo.roleId);
          roleInfo.selectedRoleIds = [];
        });
        originUserBasicData.value = res.data;
        originUserRoleData.value = res.data.roleInfoList;
        form.value = JSON.parse(JSON.stringify(res.data));
        if (form.value.roleInfoList.length === roles.value.length) {
          isAddLineAble.value = false;
        }
      });
    });
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
          <el-row :gutter="10">
            <el-col :span="12">
              <p class="tip">
                {{ t("commons.basic_info", "基本信息") }}
              </p>
            </el-col>
            <el-col :span="11" style="text-align: end">
              <el-button
                key="edit"
                type="primary"
                text
                v-if="!basicEditable"
                @click="edit(resourceConst.basic)"
                v-hasPermission="'[management-center]USER:EDIT'"
              >
                {{ $t("commons.btn.edit") }}
              </el-button>

              <el-button
                class="cancel-btn"
                v-if="basicEditable"
                @click="cancel(resourceConst.basic, formRef)"
              >
                {{ $t("commons.btn.cancel") }}
              </el-button>
              <el-button
                class="save-btn"
                type="primary"
                v-if="basicEditable"
                @click="save(resourceConst.basic, formRef)"
              >
                {{ $t("commons.btn.save") }}
              </el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="10">
              <el-form-item label="ID" prop="username">
                <span>{{ form.username }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item
                :label="$t('user.name')"
                prop="name"
                style="width: 60%"
              >
                <el-input v-model="form.name" v-if="basicEditable" />
                <span v-if="!basicEditable">{{ form.name }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item :label="$t('user.status')" prop="enabled">
                <el-switch v-model="form.enabled" v-if="basicEditable" />
                <span v-if="!basicEditable">
                  {{ enabledFilter(form.enabled) }}
                </span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="10">
              <el-form-item
                :label="$t('user.email')"
                prop="email"
                style="width: 60%"
              >
                <el-input v-model="form.email" v-if="basicEditable" />
                <span v-if="!basicEditable">{{ form.email }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item
                :label="$t('user.phone')"
                prop="phone"
                style="width: 60%"
              >
                <el-input v-model="form.phone" v-if="basicEditable" />
                <span v-if="!basicEditable">{{ form.phone }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="10">
              <el-form-item :label="$t('user.source')" prop="source">
                <span>{{ sourceFilter(form.source) }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item
                :label="$t('commons.create_time')"
                prop="createTime"
              >
                <span>{{ form.createTime }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :span="12">
              <p class="tip">
                {{ $t("user.has_role") }}
              </p>
            </el-col>
            <el-col :span="11" style="text-align: end">
              <el-button
                key="edit"
                type="primary"
                text
                v-if="!roleEditable"
                @click="edit(resourceConst.role)"
                v-hasPermission="'[management-center]USER:EDIT'"
              >
                {{ $t("commons.btn.edit") }}
              </el-button>
              <el-button
                class="cancel-btn"
                v-if="roleEditable"
                @click="cancel(resourceConst.role)"
              >
                {{ $t("commons.btn.cancel") }}
              </el-button>
              <el-button
                class="save-btn"
                type="primary"
                v-if="roleEditable"
                @click="save(resourceConst.role, formRef)"
                >{{ $t("commons.btn.save") }}</el-button
              >
            </el-col>
          </el-row>
          <el-row v-for="(roleInfo, index) in form.roleInfoList" :key="index">
            <!-- 用户角色 -->
            <el-row style="width: 100%">
              <el-col :span="23">
                <el-form-item :label="$t('user.type')">
                  <el-select
                    v-model="roleInfo.roleId"
                    @change="setRoleType(roleInfo, roleInfo.roleId)"
                    :placeholder="$t('user.type')"
                    style="width: 100%"
                    :disabled="!roleEditable"
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
                  v-if="form.roleInfoList.length > 1 && roleEditable"
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
                <el-form-item :label="$t('user.add_org')">
                  <el-tree-select
                    v-model="roleInfo.organizationIds"
                    node-key="id"
                    :props="{ label: 'name' }"
                    :data="orgTreeData"
                    :render-after-expand="false"
                    :disabled="!roleEditable"
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
                <el-form-item :label="$t('user.add_workspace')">
                  <el-tree-select
                    v-model="roleInfo.workspaceIds"
                    node-key="id"
                    :props="{ label: 'name' }"
                    :data="workspaceTreeData"
                    :render-after-expand="false"
                    :disabled="!roleEditable"
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
                  v-if="roleEditable"
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
  </el-container>
</template>
