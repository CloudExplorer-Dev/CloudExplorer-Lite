<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import { useI18n } from "vue-i18n";

import _ from "lodash";

import { useUserStore } from "@commons/stores/modules/user";
import type { SourceTreeObject } from "@commons/api/organization/type";
import type { UserRole } from "@commons/api/user/type";
import type { SimpleMap } from "@commons/api/base/type";
import { useHomeStore } from "@commons/stores/modules/home";
import { useRouter } from "vue-router";
import type { Role } from "@commons/api/role/type";
import RoleTag from "@commons/business/person-setting/RoleTag.vue";

const userStore = useUserStore();

const router = useRouter();

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
  if (!getRoleObj(data)) {
    return;
  }
  //console.log(data);
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

  window.location.reload();
}

const adminRoleName = computed<Role[] | undefined>(() => {
  const adminRole: UserRole | undefined = _.head(
    _.get(userStore.currentUser?.roleMap, "ADMIN")
  );
  return adminRole?.roles;
});

const orgAdminRoleNameMap = computed<SimpleMap<Role[] | undefined>>(() => {
  return getRoleObjMap("ORGADMIN");
});

const userRoleNameMap = computed<SimpleMap<Role[] | undefined>>(() => {
  return getRoleObjMap("USER");
});

function getRoleObjMap(role: string): SimpleMap<Role[] | undefined> {
  const orgRoles: Array<UserRole> | undefined = _.get(
    userStore.currentUser?.roleMap,
    role
  );
  const map: SimpleMap<Role[]> = {};

  _.forEach(orgRoles, (role) => {
    if (role.source) {
      _.set(map, role.source, role?.roles);
    }
  });
  return map;
}

function getRoleObj(data: SourceTreeObject): Role[] | undefined {
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

onMounted(() => {
  onOpen();
});
</script>

<template>
  <el-tree
    :data="userStore.sourceTree"
    default-expand-all
    :expand-on-click-node="false"
    class="source-tree"
  >
    <template #default="{ node, data }">
      <div
        class="custom-tree-node"
        :class="{
          active: isCurrent(data),
          'custom-button': getRoleObj(data) !== undefined,
        }"
        @click="click(data)"
        :key="node.id"
      >
        <span>{{ data.label }}</span>
        <RoleTag v-for="r in getRoleObj(data)" :key="r" :role="r" />
        <div style="flex: 1; min-width: 50px"></div>
        <el-icon v-if="isCurrent(data)"><Check /></el-icon>
        <div v-else style="height: 1em; width: 1em"></div>
      </div>
    </template>
  </el-tree>
</template>

<style scoped lang="scss">
.source-tree {
  .el-tree-node {
    cursor: default;

    .active {
      color: var(--el-color-primary);
    }

    .custom-tree-node {
      flex: 1;
      cursor: default;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      font-size: 14px;
      padding-right: 8px;
    }

    .custom-button {
      cursor: pointer;
    }
  }
}
</style>
