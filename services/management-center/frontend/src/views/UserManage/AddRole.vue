<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import _ from "lodash";
import type { FormInstance } from "element-plus/es";
import { useI18n } from "vue-i18n";
import type { RoleInfo, Role } from "@/api/user/type";
import { userAddRole, listCurrentUserRole, workspaceTree } from "@/api/user";
import { ElMessage } from "element-plus/es";
import { tree } from "@/api/organization";
import type { OrganizationTree } from "@/api/organization/type";
import { roleConst } from "@commons/utils/constants";

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
const workspaceTreeData = ref<OrganizationTree[]>();
const isAddLineAble = ref(true);

// 添加用户类型选项
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

// 清除用户类型选项
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

  const getRoles = listCurrentUserRole().then((res) => {
    roles.value = res.data;
  });

  Promise.all([getOrgTree, getWsTree, getRoles]).then(() => {
    addLine();
  });
});
</script>

<template>
  <el-form
    :model="form"
    ref="formRef"
    label-width="100px"
    label-position="right"
  >
    <div v-for="(roleInfo, index) in form.roleInfoList" :key="index">
      <!-- 用户类型 -->
      <el-row>
        <el-col :span="20">
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
      <el-row v-if="roleInfo.roleType === roleConst.orgAdmin">
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
      <el-row v-if="roleInfo.roleType === roleConst.user">
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
    <div class="dialog_footer">
      <el-button @click="handleCancel(formRef)">{{
        $t("commons.btn.cancel")
      }}</el-button>
      <el-button type="primary" @click="handleCreate(formRef)">{{
        $t("commons.btn.save")
      }}</el-button>
    </div>
  </el-form>
</template>

<style lang="scss">
.dialog_footer {
  text-align: right;
  padding-top: 30px;
}
</style>
