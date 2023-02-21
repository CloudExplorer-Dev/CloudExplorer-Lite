<script lang="ts" setup>
import TopBar from "@commons/components/layout/top-bar/index.vue";
import AsideStartMenu from "@commons/components/layout/aside-start-menu/index.vue";

import { useHomeStore } from "@commons/stores/modules/home";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useUserStore } from "@commons/stores/modules/user";

import HomePage from "@commons/business/base-layout/home-page/index.vue";
import CreateCloudAccount from "@commons/business/cloud-account/CreateCloudAccount.vue";

import { computed, ref, watch } from "vue";
import _ from "lodash";
import { get } from "@commons/request";

const homeStore = useHomeStore();
const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();
const userStore = useUserStore();

const showCreateAccount = ref(false);
const fetchCount = ref(false);

const hasCreateAccountPermission = computed<boolean>(() => {
  return (
    _.some(
      moduleStore.runningModules,
      (module) => module.id === "management-center"
    ) &&
    permissionStore.hasPermission("[management-center]CLOUD_ACCOUNT:CREATE") &&
    "ADMIN" === userStore.currentRole
  );
});

function fetchToShowCreateAccount() {
  if (hasCreateAccountPermission.value && !fetchCount.value) {
    get("/management-center/api/cloud_account/count").then((r) => {
      if ((r.data as number) == 0) {
        showCreateAccount.value = true;
      }
    });
    fetchCount.value = true;
  }
}

fetchToShowCreateAccount();

watch(hasCreateAccountPermission, (v) => {
  fetchToShowCreateAccount();
});
</script>
<template>
  <el-container class="rootContainer">
    <el-header class="handlerContainer"><TopBar /></el-header>
    <el-container>
      <el-aside class="starMenuContainer">
        <AsideStartMenu></AsideStartMenu>
      </el-aside>
      <el-container>
        <HomePage v-if="homeStore.showHome"></HomePage>
        <router-view></router-view>
      </el-container>
    </el-container>
  </el-container>

  <el-dialog
    class="create-account-show"
    v-model="showCreateAccount"
    destroy-on-close
    top="12vh"
    width="78%"
    :close-on-click-modal="false"
  >
    <CreateCloudAccount type="dialog" />
  </el-dialog>
</template>

<style scoped lang="scss">
.rootContainer {
  height: 100vh;
  width: 100vw;
  .handlerContainer {
    height: var(--ce-header-height);
    padding: var(--ce-header-padding);
    background-color: var(--ce-header-bg-color);
  }
  .starMenuContainer {
    width: var(--ce-star-menu-width);
    background-color: var(--ce-star-menu-bg-color);
    border-right: 1px solid var(--ce-star-menu-border-color);
    height: calc(100vh - var(--ce-header-height));
    overflow: hidden;
    z-index: 1002;
  }
}
</style>

<style lang="scss">
.create-account-show {
  height: 70%;
  min-height: 520px;
  max-height: 700px;

  .el-dialog__header {
    padding: 0;
    margin: 0;
  }
  .el-dialog__body {
    height: 100%;
    padding: 0;
    margin: 0;
  }
}
</style>
