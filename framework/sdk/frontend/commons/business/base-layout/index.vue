<script lang="ts" setup>
import TopBar from "@commons/components/layout/top-bar/index.vue";
import AsideStartMenu from "@commons/components/layout/aside-start-menu/index.vue";

import { useHomeStore } from "@commons/stores/modules/home";

import HomePage from "@commons/business/base-layout/home-page/index.vue";

const homeStore = useHomeStore();
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
