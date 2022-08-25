<script lang="ts" setup>
import TopBar from "@commons/components/layout/top-bar/index.vue";
</script>
<template>
  <el-container class="rootContainer">
    <el-header class="handlerContainer"><TopBar /></el-header>
    <el-container>
      <el-aside class="starMenuContainer"
        ><asideStartMenu></asideStartMenu
      ></el-aside>
      <el-container>
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
