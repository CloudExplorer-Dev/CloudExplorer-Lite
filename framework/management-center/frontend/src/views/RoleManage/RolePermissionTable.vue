<template>
  <el-container class="permission-container">
    <el-aside width="182px">
      <div class="module-title">
        <div class="title">模块名称</div>
      </div>
      <div class="module-selector">
        <template v-for="m in modulesPanels" :key="m.value">
          <div
            @click="onModuleSelect(m)"
            class="module-btn"
            :class="{ active: defaultSelectedModule === m.value }"
          >
            {{ m.label }}
          </div>
        </template>
      </div>
    </el-aside>
    <el-main style="padding: 0px; min-height: 200px; width: 100%">
      <el-table
        height="100%"
        :data="permissionTableData"
        style="width: 100%; padding: 0px; min-height: 200px"
        :header-cell-style="{
          'background-color': '#F5F6F7',
          'font-weight': 400,
          'font-size': '14px',
          color: '#646a73',
        }"
      >
        <el-table-column min-width="100px">
          <template #header>
            <el-checkbox
              v-model="checkedAll"
              :indeterminate="isCheckAllIndeterminate"
              :label="false"
              :disabled="!editPermission"
            >
              <span class="checkbox-title">操作对象</span>
            </el-checkbox>
          </template>
          <template #default="scope">
            <el-checkbox
              v-model="scope.row.checked"
              :indeterminate="scope.row.indeterminate"
              :label="false"
              :disabled="!editPermission"
            >
              {{ t(scope.row.name) }}
            </el-checkbox>
          </template>
        </el-table-column>
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
                {{ t(p.name) }}
              </el-checkbox>
            </el-checkbox-group>
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

const _loading = computed({
  get() {
    return props.loading;
  },
  set(value) {
    emit("update:loading", value);
  },
});

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

const availableModules = computed(() => {
  return _.filter(modules.value, (m: Module) => {
    const p: ModulePermission | undefined = _.get(
      originPermissions.value,
      m.id
    );
    if (p === undefined) {
      return false;
    }
    return p.groups.length > 0;
  });
});

const modulesPanels = computed(() => {
  return _.map(availableModules.value, (m: Module) => {
    return { value: m.id, label: m.name };
  });
});

const selectedModule = ref<string | undefined>(undefined);

const defaultSelectedModule = computed<string | undefined>({
  get() {
    if (selectedModule.value == undefined) {
      return availableModules.value[0]?.id;
    }
    if (!_.some(availableModules.value, (m) => m.id === selectedModule.value)) {
      return availableModules.value[0]?.id;
    }

    return selectedModule.value;
  },
  set(value) {
    selectedModule.value = value;
  },
});

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

    resultElement.indeterminate = computed<boolean>(() => {
      return (
        !_.every(
          groupPermissionIds,
          (v) => _permissionData.value.indexOf(v) >= 0
        ) &&
        _.some(groupPermissionIds, (v) => _permissionData.value.indexOf(v) >= 0)
      );
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

const isCheckAllIndeterminate = computed<boolean>(() => {
  return (
    !checkedAll.value && _.some(permissionTableData.value, (row) => row.checked)
  );
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

const onModuleSelect = (module: { value: string; label: string }) => {
  defaultSelectedModule.value = module.value;
};

const isEdit = computed(() => {
  return props.editPermission;
});

function getRolePermissions(id?: string) {
  if (id) {
    RoleApi.getRolePermissions(id, _loading).then((ok) => {
      _permissionData.value = ok.data;
    });
  }
}

const currentParentRoleId = computed(() => {
  return props.parentRoleId;
});

watch(
  currentParentRoleId,
  (parentRoleId) => {
    console.debug("parentRoleId:" + parentRoleId);
    if (parentRoleId) {
      RoleApi.getModulePermissions(parentRoleId, _loading).then((ok) => {
        originPermissions.value = ok.data;
        console.debug(originPermissions.value);
      });
    }
  },
  { immediate: true }
);

watch(
  () => [props.id, isEdit.value],
  ([_id, _isEdit], [old_id, old_isEdit]) => {
    if (_id === old_id) {
      if (!_isEdit) {
        getRolePermissions(_id as string);
      }
    } else {
      getRolePermissions(_id as string);
    }
  }
);

const init = () => {
  //模块
  listModules(_loading).then((ok) => {
    modules.value = ok.data;
  });
};

onMounted(() => {
  init();
});

defineExpose({ init });
</script>
<style lang="scss" scoped>
.edit-button-container {
  text-align: center;
  line-height: 50px;
  align-items: center;
}

.permission-container {
  width: 100%;
  height: 100%;
  min-height: 100px;

  .module-title {
    width: 100%;
    background: #f5f6f7;
    padding: 8px 0;
    line-height: 23px;
    height: 32px;
    border-bottom: 1px solid #ebeef5;
    font-weight: 400;
    font-size: 14px;
    color: #646a73;
    align-items: center;
    display: flex;
    .title {
      padding: 0 12px;
    }
  }

  .module-selector {
    width: calc(100% - 1px);
    height: calc(100% - 50px);
    border-bottom: rgba(31, 35, 41, 0.15) solid 1px;
    border-right: rgba(31, 35, 41, 0.15) solid 1px;

    .module-btn {
      height: 22px;
      line-height: 22px;
      padding: 8px 12px;
      font-style: normal;
      font-weight: 400;
      font-size: 14px;

      color: #1f2329;
      cursor: pointer;

      &.active {
        color: #3370ff;
        background: #f5f8ff;
      }
      &:hover {
        background: linear-gradient(
            0deg,
            rgba(51, 112, 255, 0.15),
            rgba(51, 112, 255, 0.15)
          ),
          #ffffff;
      }
    }
  }
}

.el-main {
  --el-main-padding: 0px;
}

.el-checkbox {
  --el-checkbox-disabled-checked-input-fill: var(--el-color-primary);
  --el-checkbox-disabled-checked-icon-color: #ffffff;
}

.checkbox-title {
  background-color: #f5f6f7;
  font-weight: 400;
  font-size: 14px;
  color: #646a73;
}
</style>
