<script setup lang="ts">
import { computed, ref } from "vue";
import { useI18n } from "vue-i18n";

import _ from "lodash";

import { useUserStore } from "@commons/stores/modules/user";
import type { SourceTreeObject } from "@commons/api/organization/type";
import type { UserRole } from "@commons/api/user/type";
import type { SimpleMap } from "@commons/api/base/type";

const userStore = useUserStore();

const { t } = useI18n();

const dialogVisible = ref(false);
defineExpose({
  dialogVisible,
});

const loading = ref<boolean>(false);

function onOpen() {
  userStore.getCurrentUser(loading);
  userStore.refreshSourceTree(loading);
}

function click(data: SourceTreeObject) {
  console.log(data);
  if (isCurrent(data)) {
    dialogVisible.value = false;
    return;
  }
  if (data.root) {
    userStore.changeRole("ADMIN");
  } else if (!data.root && !data.workspace && data.id) {
    userStore.changeRole("ORGADMIN", data.id);
  } else if (!data.root && data.workspace && data.id) {
    userStore.changeRole("USER", data.id);
  } else {
    userStore.changeRole("ANONYMOUS");
  }
  //刷新页面
  window.location.reload();
}

const adminRoleName = computed<string>(() => {
  const adminRole: UserRole | undefined = _.head(
    _.get(userStore.currentUser?.roleMap, "ADMIN")
  );
  return _.join(
    _.flatMap(adminRole?.roles, (o) => o.name),
    ", "
  );
});

const orgAdminRoleNameMap = computed<SimpleMap<string>>(() => {
  return getRoleNameMap("ORGADMIN");
});

const userRoleNameMap = computed<SimpleMap<string>>(() => {
  return getRoleNameMap("USER");
});

function getRoleNameMap(role: string): SimpleMap<string> {
  const orgRoles: Array<UserRole> | undefined = _.get(
    userStore.currentUser?.roleMap,
    role
  );
  const map: SimpleMap<string> = {};

  _.forEach(orgRoles, (role) => {
    if (role.source) {
      _.set(
        map,
        role.source,
        _.join(
          _.flatMap(role?.roles, (o) => o.name),
          ", "
        )
      );
    }
  });
  return map;
}

function getRoleName(data: SourceTreeObject): string | undefined {
  if (data.root) {
    return adminRoleName.value;
  }
  if (data.workspace) {
    return _.get(userRoleNameMap.value, data.id);
  }
  return _.get(orgAdminRoleNameMap.value, data.id);
}

function isCurrent(data: SourceTreeObject): boolean {
  if (userStore.currentRole === "ADMIN") {
    if (data.root) {
      return true;
    }
  } else if (userStore.currentRole === "ORGADMIN") {
    if (!data.root && !data.workspace) {
      return data.id === userStore.currentSource;
    }
  } else if (userStore.currentRole === "USER") {
    if (!data.root && data.workspace) {
      return data.id === userStore.currentSource;
    }
  }
  return false;
}
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    title="切换角色"
    node-key="id"
    destroy-on-close
    align-center
    lock-scroll
    @open="onOpen"
  >
    <el-tree
      :data="userStore.sourceTree"
      default-expand-all
      :expand-on-click-node="false"
      class="source-tree"
    >
      <template #default="{ node, data }">
        <span
          class="custom-tree-node"
          :class="{ active: isCurrent(data) }"
          :key="node.id"
        >
          <span>{{ data.label }}</span>
          <span
            @click="click(data)"
            class="custom-button"
            v-if="getRoleName(data)"
          >
            [{{ getRoleName(data) }}]
            <el-icon v-if="!isCurrent(data)"><Right /></el-icon>
            <el-icon v-if="isCurrent(data)"><Aim /></el-icon>
          </span>
        </span>
      </template>
    </el-tree>
  </el-dialog>
</template>

<style lang="scss">
.source-tree {
  .el-tree-node__content {
    cursor: default;

    .el-tree-node__label {
      width: 100%;
      cursor: default;

      .active {
        color: var(--el-color-primary);
      }

      .custom-tree-node {
        cursor: default;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 14px;
        padding-right: 8px;

        .custom-button {
          cursor: pointer;
        }
      }
    }
  }
}
</style>
