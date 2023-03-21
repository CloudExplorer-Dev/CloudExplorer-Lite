<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import _ from "lodash";
import type { FormInstance } from "element-plus/es";
import { useI18n } from "vue-i18n";
import type { RoleInfo, Role } from "@/api/user/type";
import { userAddRole } from "@/api/user";
import { listRoles } from "@commons/api/role";
import { useUserStore } from "@commons/stores/modules/user";
import { ElMessage } from "element-plus/es";
import { tree } from "@commons/api/organization";
import type { OrganizationTree } from "@/api/organization/type";
import { workspaceTree } from "@commons/api/workspace";
import type { WorkspaceTree } from "@commons/api/workspace/type";
import { roleConst } from "@commons/utils/constants";
import CeIcon from "@commons/components/ce-icon/index.vue";

const props = defineProps({
  visible: {
    type: Boolean,
    required: true,
  },
  userIds: {
    type: Array,
    required: true,
  },
});
const emits = defineEmits(["update:visible", "refresh"]);

const { t } = useI18n();
const formRef = ref<FormInstance | undefined>();
const form = reactive<Record<string, RoleInfo[]>>({
  roleInfoList: [],
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
  emits("update:visible", false);
  formEl.resetFields();
};

const handleCreate = (formEl: FormInstance) => {
  if (!formEl) return;
  formEl.validate((valid) => {
    if (valid) {
      const param = {
        userIdList: props.userIds,
        roleInfoList: form.roleInfoList,
      };
      userAddRole(param)
        .then(() => {
          emits("update:visible", false);
          emits("refresh");
          ElMessage.success(t("commons.msg.save_success"));
        })
        .catch((err) => {
          ElMessage.error(err.response.data.message);
        });
    } else {
      return false;
    }
  });
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

  Promise.all([getOrgTree, getWsTree, getRoles]).then(() => {
    addLine();
  });
});
</script>

<template>
  <el-container class="create-catalog-container">
    <el-form
      :model="form"
      ref="formRef"
      label-width="100px"
      label-position="top"
      require-asterisk-position="right"
      style="width: 100%"
    >
      <el-row v-for="(roleInfo, index) in form.roleInfoList" :key="index">
        <!-- 用户角色 -->
        <el-row :gutter="10" style="width: 100%">
          <el-col :span="23">
            <el-form-item :label="$t('user.type')">
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
          <el-col :span="1" style="padding-top: 38px">
            <div
              class="delete-button-class"
              v-if="form.roleInfoList.length > 1"
              @click="subtractLine(roleInfo)"
            >
              <el-tooltip
                class="box-item"
                effect="dark"
                :content="$t('user.delete_role')"
                placement="bottom"
              >
                <CeIcon
                  size="var(--ce-star-menu-icon-width,13.33px)"
                  code="icon_delete-trash_outlined1"
                ></CeIcon>
              </el-tooltip>
            </div>
          </el-col>
        </el-row>

        <!-- 选择组织 -->
        <el-row :gutter="10" v-if="roleInfo.roleType === roleConst.orgAdmin">
          <el-col :span="23">
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
        <el-row
          :gutter="10"
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

      <div class="dialog_footer footer-btn">
        <el-button @click="handleCancel(formRef)">{{
          $t("commons.btn.cancel")
        }}</el-button>
        <el-button type="primary" @click="handleCreate(formRef)">{{
          $t("commons.btn.save")
        }}</el-button>
      </div>
    </el-form>
  </el-container>
</template>

<style lang="scss">
.dialog_footer {
  text-align: right;
  padding-top: 30px;
}
</style>
