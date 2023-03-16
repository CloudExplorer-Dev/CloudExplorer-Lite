<script setup lang="ts">
import SwitchLang from "@commons/business/switch-lang/index.vue";
import PersonSetting from "@commons/business/person-setting/index.vue";
import { useUserStore } from "@commons/stores/modules/user";
import { useRouter } from "vue-router";
import { useHomeStore } from "@commons/stores/modules/home";
import CeIcon from "@commons/components/ce-icon/index.vue";
import SourceChangeDialog from "@commons/business/person-setting/SourceChangeDialog.vue";

import CeMainLogo from "@commons/assets/CloudExplorer-Lite-02.svg";
import { ref } from "vue";

const userStore = useUserStore();
const homeStore = useHomeStore();

const router = useRouter();

function goHome() {
  //只有基座有首页
  if (homeStore.isBase) {
    router.push("/");
  }
}

const roleSelectOpen = ref<boolean>(false);

function roleSelectVisibleChange(visible: boolean) {
  roleSelectOpen.value = visible;
}
</script>

<template>
  <div class="top-nav">
    <el-image :src="CeMainLogo" fit="contain" style="width: 145px" />
    <el-divider direction="vertical" class="header-divider" />
    <div class="header-button home-button" @click="goHome">
      <CeIcon code="icon_home_outlined" size="16px" /> 首页
    </div>
    <el-dropdown trigger="click" @visible-change="roleSelectVisibleChange">
      <div class="header-button" style="display: flex; flex-direction: row">
        <div>
          {{ userStore.currentRoleSourceName?.sourceName }}
        </div>
        <div
          class="role-tag"
          v-for="role in userStore.currentRoleSourceName?.roles"
          :key="role.id"
        >
          {{ role.name }}
        </div>
        <el-icon class="role-cart" v-if="!roleSelectOpen"
          ><CaretBottom
        /></el-icon>
        <el-icon class="role-cart" v-else><CaretTop /></el-icon>
      </div>
      <template #dropdown>
        <div
          style="
            min-height: 40px;
            min-width: 348px;
            padding: 10px;
            margin: auto;
          "
        >
          <SourceChangeDialog v-if="roleSelectOpen" />
        </div>
      </template>
    </el-dropdown>
    <div class="flex-auto"></div>
    <SwitchLang />
    <PersonSetting />
  </div>
</template>

<style lang="scss">
.top-nav {
  height: 100%;
  display: flex;
  color: white;
  align-items: center;

  .header-divider {
    border-color: rgba(255, 255, 255, 0.15);
    margin-left: 20px;
  }

  .home-button {
    width: 64px;
  }

  .header-button {
    color: #ffffff;
    padding: 8px;
    box-sizing: border-box;
    margin: auto 8px;
    cursor: pointer;
    border-radius: 4px;
    line-height: 22px;
    font-size: 14px;

    .role-tag {
      border-radius: 2px;
      line-height: 20px;
      font-size: 12px;
      background-color: rgba(255, 255, 255, 0.2);
      margin-left: 4px;
      padding: 1px 6px;
    }
    .role-cart {
      margin-left: 4px;
      margin-top: auto;
      margin-bottom: auto;
    }
  }
  .header-button:hover {
    background-color: rgba(255, 255, 255, 0.1);
  }

  .flex-auto {
    flex: 1 1 auto;
  }
}
</style>
