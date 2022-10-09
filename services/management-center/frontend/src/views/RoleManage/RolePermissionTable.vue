<template>
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
        <el-table-column label="操作对象" prop="name" min-width="100px" />
        <el-table-column label="权限" min-width="300px">
          <template #default="scope">
            <el-checkbox-group
              v-model="_permissionData"
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
<script setup lang="ts">
import type { SimpleMap } from "@commons/api/base/type";
import { GroupPermission, ModulePermission, Permission } from "@/api/role/type";

const props = defineProps<{
  id?: string;
  loading: boolean;
  editPermission: boolean;
  parentRoleId?: string;
  permissionData: Array<string>;
}>();

const emit = defineEmits(["update:permissionData", "update:loading"]);

import { computed, onMounted, ref, watch } from "vue";
import _ from "lodash";
import RoleApi from "@/api/role";
import { listModules } from "@commons/api/module";
import type { Module } from "@commons/api/module/type";
import { useI18n } from "vue-i18n";

const { t } = useI18n();

const _loading = ref(props.loading);

const _permissionData = computed({
  get() {
    return props.permissionData;
  },
  set(value) {
    emit("update:permissionData", value);
  },
});

const originPermissions = ref<SimpleMap<ModulePermission>>();
const modules = ref<Array<Module>>([]);

const modulesPanels = computed(() => {
  return _.map(modules.value, (m: Module) => {
    return { value: m.id, label: m.name };
  });
});

const defaultSelectedModule = ref<string>();

const permissionTableData = computed<Array<GroupPermission>>(() => {
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
          (v) => _permissionData.value.indexOf(v) >= 0
        );
      },
      set(value: boolean) {
        //设置每组的全选按钮
        if (value) {
          _permissionData.value = _.union(
            _permissionData.value,
            groupPermissionIds
          );
        } else {
          _permissionData.value = _.pullAll(
            _permissionData.value,
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
      _permissionData.value = _.union(
        _permissionData.value,
        currentModulePermissions
      );
    } else {
      _permissionData.value = _.pullAll(
        _permissionData.value,
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
const onPermissionChecked = (
  permission: Permission,
  groupPermissions: Array<Permission>
) => {
  const checked = _.includes(_permissionData.value, permission.id);
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
        _permissionData.value = _.union(_permissionData.value, [requiredId]);
      }
    }
  } else {
    //当前操作为取消选中
    //找到子id列表
    const ids = _.map(
      _.filter(groupPermissions, (p) => p.require === permission.operate),
      (v) => v.id
    );
    _permissionData.value = _.pullAll(_permissionData.value, ids);
  }
};

const onModuleSelect = (index: any) => {
  console.log(index);
};

const isEdit = computed(() => {
  return props.editPermission;
});

watch(
  isEdit,
  (isEdit) => {
    //恢复选中值
    _permissionData.value = props.permissionData;
  },
  { immediate: true }
);

const currentParentRoleId = computed(() => {
  return props.parentRoleId;
});

watch(
  currentParentRoleId,
  (parentRoleId) => {
    console.log("parentRoleId:" + parentRoleId);
    if (parentRoleId) {
      RoleApi.getModulePermissions(parentRoleId, _loading).then((ok) => {
        originPermissions.value = ok.data;
        console.log(originPermissions.value);
      });
    }
  },
  { immediate: true }
);

const init = () => {
  //权限
  if (props.id) {
    RoleApi.getRolePermissions(props.id, _loading).then((ok) => {
      _permissionData.value = ok.data;
    });
  }
  //模块
  listModules(_loading).then((ok) => {
    modules.value = ok.data;
    defaultSelectedModule.value = modules.value[0].id;
  });
};

onMounted(() => {
  init();
});
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
