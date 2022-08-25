<script lang="ts" setup>
import TopBar from "@commons/components/layout/top-bar/index.vue";
import AsideStartMenu from "@commons/components/layout/aside-start-menu/index.vue";
import AsideMenu from "@commons/components/layout/aside-menu/index.vue";
import CollapseView from "@commons/components/collapse/index.vue";

import { ref } from "vue";

const collapse = ref<boolean>(true);
</script>
<template>
  <el-container class="rootContainer">
    <el-header class="handlerContainer"><TopBar /></el-header>
    <el-container>
      <el-aside class="starMenuContainer">
        <AsideStartMenu></AsideStartMenu>
      </el-aside>
      <el-container>
        <el-aside class="menuContainer" v-show="collapse">
          <AsideMenu></AsideMenu>
        </el-aside>
        <CollapseView
          :collapse="collapse"
          :changeCollapse="() => (collapse = !collapse)"
        ></CollapseView>
        <el-main class="contentContainer">
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped>
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
  .menuContainer {
    --el-aside-width: 200px;
    position: relative;
    height: calc(100vh - var(--ce-header-height));
    z-index: 100;
    overflow: hidden;
    &:hover {
      overflow-y: auto;
      overflow-x: hidden;
    }
  }

  .contentContainer {
    height: calc(100vh - var(--ce-header-height));
    padding: 0;
    overflow: hidden;
    background-color: #f2f2f2;
  }
}
</style>
