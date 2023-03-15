<script setup lang="ts">
import StartMenuItem from "./StartMenuItem.vue";
import CeIcon from "@commons/components/ce-icon/index.vue";
import { ref } from "vue";
import { useUserStore } from "@commons/stores/modules/user";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useRouter } from "vue-router";
import CollectMenu from "@commons/components/layout/collect-menu/index.vue";
import type { Module } from "@commons/api/module/type";
import microApp from "@micro-zoe/micro-app";
// 路由
const router = useRouter();
// 模块存储
const moduleStore = useModuleStore();
// 权限存储
const permissionStore = usePermissionStore();
// 用户存储
const userStore = useUserStore();
// 菜单展开
const hover = ref<boolean>(false);
// 当前模块
const currentModule = ref(import.meta.env.VITE_APP_NAME);
// 模块所有菜单是否折叠
const collapse = ref<boolean>(false);

//菜单服务模块
const root = ref<Module>({
  id: "",
  name: "服务列表",
  icon: "caidan2",
  order: 0,
});
// 首页菜单
const home = ref<Module>({
  id: "base",
  name: "首页",
  icon: "shouye3",
  order: -1,
});
/**
 * 菜单显示
 */
const hoverHandler = () => {
  collapse.value = true;
};
/**
 * 路由去指定模块
 * @param moduleName 模块名称
 */
const goModule = (moduleName: string) => {
  hover.value = false;
  currentModule.value = moduleName;
  if (moduleName === getCurrentModuleByMicroApp()) {
    return;
  }
  if (moduleName === home.value.id) {
    router.push("/");
  } else {
    router.push(moduleName);
  }
};

const getCurrentModuleByMicroApp = () => {
  const activeApps = microApp.getActiveApps();
  console.log(activeApps);
  if (activeApps && activeApps.length > 0) {
    return activeApps[0];
  }
  return "base";
};
</script>
<template>
  <div
    class="startMenu"
    @mouseenter="() => (hover = true)"
    @mouseleave="() => (hover = false)"
    :class="hover ? 'hover_start_menu' : ''"
  >
    <div class="serviceList">
      <StartMenuItem
        :hover="hover"
        :start-menu-item="root"
        :hover-handler="hoverHandler"
        :leave-handler="() => (collapse = false)"
        :rootItem="true"
      >
        <CollectMenu
          :collapse="collapse"
          :runningModules="moduleStore.runningModules"
          :runningModuleMenus="moduleStore.runningMenus"
          :runningModulePermissions="permissionStore.userPermissions"
          :currentRole="userStore.currentRole"
        />
      </StartMenuItem>
    </div>
    <div class="starMenuLine"></div>
    <div class="runningModule">
      <!--      <StartMenuItem
        :hover="hover"
        @click="goModule(home.id)"
        :start-menu-item="home"
        :root-item="true"
        :class="home.id === currentModule ? 'active' : ''"
      >
        <div class="move">
          <CeIcon
            size="var(&#45;&#45;ce-star-menu-icon-width,18px)"
            code="yidongshu"
          ></CeIcon>
        </div>
      </StartMenuItem>-->
      <StartMenuItem
        :hover="hover"
        v-for="item in moduleStore.runningModules"
        :key="item.id"
        @click="goModule(item.id)"
        :start-menu-item="item"
        :root-item="false"
        v-hasPermission="item.requiredPermissions"
        :class="item.id === currentModule ? 'active' : ''"
      >
        <div class="move">
          <CeIcon
            size="var(--ce-star-menu-icon-width,18px)"
            code="yidongshu"
          ></CeIcon>
        </div>
      </StartMenuItem>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.startMenu {
  height: 100%;
  width: var(--ce-star-menu-width);
  background-color: var(--ce-star-menu-bg-color);
  transition: 0.5s;
  border-right: 1px solid var(--ce-star-menu-border-color);

  .active {
    background-color: var(--ce-star-menu-active-bg-color);
    .icon {
      .el-icon {
        color: var(--ce-star-menu-active-icon-color);
      }
    }
  }
  .starMenuLine {
    margin-bottom: 8px;
    height: 1px;
    background-color: var(--ce-star-menu-active-bg-color);
  }
  .starMenuLineMenu {
    margin-top: 8px;
    height: 1px;
    background-color: var(--ce-star-menu-active-bg-color);
  }
}
.hover_start_menu {
  overflow: auto;
  position: fixed;
  width: var(--ce-star-menu-hover-width);
}
</style>
