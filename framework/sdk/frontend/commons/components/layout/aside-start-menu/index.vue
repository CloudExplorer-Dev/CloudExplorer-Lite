<script setup lang="ts">
import StartMenuItem from "./StartMenuItem.vue";
import CeIcon from "@commons/components/ce-icon/index.vue";
import { ref } from "vue";
import { useModuleStore } from "@commons/stores/modules/module";
import { useRouter } from "vue-router";
import type { Module } from "@commons/api/module/type";
import microApp from "@micro-zoe/micro-app";

// 路由
const router = useRouter();
// 模块存储
const moduleStore = useModuleStore();

// 菜单展开
const hover = ref<boolean>(false);
// 首页菜单
const home = ref<Module>({
  id: "base",
  name: "首页",
  icon: "shouye3",
  order: -1,
});

/**
 * 路由去指定模块
 * @param moduleName 模块名称
 */
const goModule = (moduleName: string) => {
  hover.value = false;
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
    <div class="runningModule">
      <StartMenuItem
        :hover="hover"
        v-for="item in moduleStore.runningModules"
        :key="item.id"
        @click="goModule(item.id)"
        :start-menu-item="item"
        :root-item="false"
        v-hasPermission="item.requiredPermissions"
        :class="item.id === moduleStore.currentModuleName ? 'active' : ''"
        :active="item.id === moduleStore.currentModuleName"
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
  box-sizing: border-box;
  height: 100%;
  width: var(--ce-star-menu-width);
  background-color: var(--ce-star-menu-bg-color);
  transition: 0.5s;
  border-right: 1px solid var(--ce-star-menu-border-color, #dcdfe3);

  .active {
    //background-color: var(--ce-star-menu-active-bg-color);
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
