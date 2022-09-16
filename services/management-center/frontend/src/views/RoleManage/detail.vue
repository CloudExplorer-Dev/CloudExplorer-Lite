<template>
  <layout-container :border="false">
    <template #content>
      <layout-container v-loading="loading">
        <template #header>
          <h4>基本信息</h4>
        </template>
        <template #btn>
          <el-button
            key="edit"
            type="primary"
            text
            @click="changeToEditInfo"
            v-if="showEditInfoButton"
          >
            修改
          </el-button>

          <el-button v-if="editInfo" @click="cancelEditInfo"> 取消 </el-button>
          <el-button
            type="primary"
            v-if="editInfo"
            @click="submitRoleForm(roleFormRef)"
          >
            保存
          </el-button>
        </template>
        <template #content>
          <el-form
            :rules="rules"
            label-position="right"
            :model="roleFormData"
            :inline="false"
            ref="roleFormRef"
            status-icon
          >
            <el-form-item label="名称" label-width="100px" prop="name">
              <el-col :span="14">
                <el-input v-model="roleFormData.name" v-if="editInfo" />
                <span v-if="!editInfo">{{ roleData.name }}</span>
              </el-col>
            </el-form-item>

            <el-form-item label="描述" label-width="100px" prop="description">
              <el-col :span="14">
                <el-input v-model="roleFormData.description" v-if="editInfo" />
                <span v-if="!editInfo">{{ roleData.description }}</span>
              </el-col>
            </el-form-item>

            <el-form-item
              label="继承角色"
              label-width="100px"
              prop="parentRoleId"
            >
              <el-radio-group v-model="roleData.parentRoleId">
                <el-radio-button
                  v-for="baseRole in originRoles"
                  :key="baseRole.id"
                  :label="baseRole.id"
                  :disabled="baseRole.id !== roleData.parentRoleId"
                >
                  {{ baseRole.name }}
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-form>
        </template>
      </layout-container>

      <layout-container v-loading="loadingPermission">
        <template #header>
          <h4>角色权限</h4>
        </template>
        <template #btn>
          <el-button
            key="edit"
            type="primary"
            text
            @click="changeToEditPermission"
            v-if="showEditPermissionButton"
          >
            修改
          </el-button>

          <el-button v-if="editPermission" @click="cancelEditPermission">
            取消
          </el-button>
          <el-button
            type="primary"
            v-if="editPermission"
            @click="submitRolePermission(permissionData)"
          >
            保存
          </el-button>
        </template>
        <template #content>
          <el-container class="permission-container">
            <el-aside width="182px" class="module-selector">
              <el-cascader-panel
                :options="modulesPanels"
                v-model="defaultSelectedModule"
                style="width: 180px; height: 99%; padding: 0"
                @change="onModuleSelect"
              />
            </el-aside>
            <el-main style="padding: 4px">
              <el-table :data="permissionTableData" style="width: 100%">
                <el-table-column
                  label="操作对象"
                  prop="name"
                  min-width="100px"
                />
                <el-table-column label="权限" min-width="300px">
                  <template #default="scope">
                    <el-checkbox-group
                      v-model="permissionData"
                      :disabled="!editPermission"
                    >
                      <el-checkbox
                        v-for="p in scope.row.permissions"
                        :key="p.id"
                        :label="p.id"
                        @change="onPermissionChecked(p, scope.row.permissions)"
                      >
                        {{ p.name }}
                      </el-checkbox>
                    </el-checkbox-group>
                  </template>
                </el-table-column>

                <el-table-column label="全选" align="right" width="100px">
                  <template #header>
                    <el-checkbox
                      v-model="checkedAll"
                      :label="true"
                      :disabled="!editPermission"
                    >
                      全选
                    </el-checkbox>
                  </template>
                  <template #default="scope">
                    <el-checkbox
                      v-model="scope.row.checked"
                      :label="true"
                      :disabled="!editPermission"
                    >
                      全选
                    </el-checkbox>
                  </template>
                </el-table-column>
              </el-table>
            </el-main>
          </el-container>
        </template>
      </layout-container>

      <layout-container v-if="!editInfo && !editPermission">
        <el-button @click="back">返回</el-button>
      </layout-container>
    </template>
  </layout-container>
</template>
<script setup lang="ts">
const props = defineProps<{
  id: string;
}>();

import { computed, onMounted, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import _ from "lodash";
import type { FormRules, FormInstance } from "element-plus";
import { ElMessage } from "element-plus";
import RoleApi from "@/api/role";
import { listModules } from "@commons/api/module";
import type { Module } from "@commons/api/module/type";
import { Role } from "@commons/api/role/type";
import { UpdateRoleRequest } from "@/api/role/type";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const roleFormRef = ref<FormInstance>();
const route = useRouter();

const roleData = ref<Role>(new Role(props.id, "", "", ""));
const originRoles = ref<Array<Role>>([]);

const roleFormData = ref<UpdateRoleRequest>(new UpdateRoleRequest(props.id));

const permissionData = ref<Array<string>>([]);
let permissionFormData: Array<string> = [];
const originPermissions = ref<any>();
const modules = ref<Array<Module>>([]);
const modulesPanels = computed(() => {
  return _.map(modules.value, (m: Module) => {
    return { value: m.id, label: m.name };
  });
});

const editInfo = ref<boolean>(false);

const showEditInfoButton = computed(() => {
  return roleData.value
    ? !editInfo.value && roleData.value.type !== "origin"
    : false;
});
const showEditPermissionButton = computed(() => {
  return permissionData.value && roleData.value
    ? !editPermission.value && roleData.value.type !== "origin"
    : false;
});

const editPermission = ref<boolean>(false);

const loading = ref<boolean>(false);
const loadingPermission = ref<boolean>(false);
const defaultSelectedModule = ref<string>();

const permissionTableData = computed(() => {
  const result =
    originPermissions.value && defaultSelectedModule.value
      ? originPermissions.value[defaultSelectedModule.value].groups
      : [];

  //每组权限展示处理
  for (const resultElement of result) {
    const groupPermissionIds = _.map(resultElement.permissions, (p) => {
      return p.id;
    });

    resultElement.checked = computed<boolean>({
      get() {
        //每组的全选按钮展示
        return _.every(
          groupPermissionIds,
          (v) => permissionData.value.indexOf(v) >= 0
        );
      },
      set(value: boolean) {
        //设置每组的全选按钮
        if (value) {
          permissionData.value = _.union(
            permissionData.value,
            groupPermissionIds
          );
        } else {
          permissionData.value = _.pullAll(
            permissionData.value,
            groupPermissionIds
          );
        }
      },
    });
  }

  return result;
});

const checkedAll = computed<boolean>({
  get() {
    return _.every(permissionTableData.value, (row) => row.checked);
  },
  set(value: boolean) {
    //设置最上级全选按钮
    const currentModulePermissions = _.flatMap(
      permissionTableData.value,
      (group) => {
        return _.map(group.permissions, (p) => p.id);
      }
    );
    if (value) {
      permissionData.value = _.union(
        permissionData.value,
        currentModulePermissions
      );
    } else {
      permissionData.value = _.pullAll(
        permissionData.value,
        currentModulePermissions
      );
    }
  },
});

/**
 * 处理连带的权限选中状态
 * @param permission
 * @param groupPermissions
 */
const onPermissionChecked = (permission, groupPermissions) => {
  const checked = _.includes(permissionData.value, permission.id); //这里触发的时候 permissionData的数据还没有更新，所以取反
  if (checked) {
    //当前操作为选中
    if (permission.require) {
      //如果有require权限，则需要把require加到列表中
      //从permissions中找到require
      const requiredId = _.find(
        groupPermissions,
        (p) => permission.require === p.operate
      )?.id;
      console.log(requiredId);
      if (requiredId) {
        permissionData.value = _.union(permissionData.value, [requiredId]);
      }
    }
  } else {
    //当前操作为取消选中
    //找到子id列表
    const ids = _.map(
      _.filter(groupPermissions, (p) => p.require === permission.operate),
      (v) => v.id
    );
    permissionData.value = _.pullAll(permissionData.value, ids);
  }
};

onMounted(() => {
  init();
});

const init = () => {
  //console.log(props.id);

  RoleApi.listRoles({ type: "origin" }, loading).then((ok) => {
    originRoles.value = ok.data;
  });
  //角色
  RoleApi.getRoleById(props.id, loading).then((ok) => {
    roleData.value = ok.data;
    //模块权限
    RoleApi.getModulePermissions(
      roleData.value.parentRoleId,
      loadingPermission
    ).then((ok) => {
      originPermissions.value = ok.data;
      console.log(originPermissions.value);
    });
  });
  //权限
  RoleApi.getRolePermissions(props.id, loadingPermission).then((ok) => {
    permissionData.value = ok.data;
    permissionFormData = ok.data;
  });
  //模块
  listModules(loadingPermission).then((ok) => {
    modules.value = ok.data;
    defaultSelectedModule.value = modules.value[0].id;
  });
};

const back = () => {
  route.push({ name: "role_list" });
};

const changeToEditInfo = () => {
  editInfo.value = true;
  roleFormData.value = JSON.parse(JSON.stringify(roleData.value));
};

const cancelEditInfo = () => {
  editInfo.value = false;
};

const changeToEditPermission = () => {
  editPermission.value = true;
};

const cancelEditPermission = () => {
  editPermission.value = false;
  permissionData.value = JSON.parse(JSON.stringify(permissionFormData));
};

const onModuleSelect = (index: any) => {
  console.log(index);
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

const submitRoleForm = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid) => {
    if (valid && roleFormData.value) {
      RoleApi.updateRole(
        UpdateRoleRequest.newInstance(roleFormData.value),
        loading
      ).then((ok) => {
        init();
        cancelEditInfo();
        ElMessage.success(t("commons.msg.save_success"));
      });
    }
  });
};

const submitRolePermission = (permissionIds: Array<string>) => {
  console.log(permissionIds);
  RoleApi.updateRolePermissions(
    props.id,
    permissionIds,
    loadingPermission
  ).then((ok) => {
    permissionData.value = ok.data;
    permissionFormData = ok.data;
    cancelEditPermission();
    ElMessage.success(t("commons.msg.save_success"));
  });
};
</script>
<style lang="scss">
.edit-button-container {
  text-align: center;
  line-height: 50px;
  align-items: center;
}

.permission-container {
  width: 100%;
  min-height: 100px;
  .module-selector {
  }
}
</style>
