<script lang="ts" setup>
import asideStartMenu from "../../components/layout/asideStarMenu/index.vue";
import asideMenu from "../../components/layout/asideMenu/index.vue";
import collapseVue from "../../components/collapse/index.vue";
import TopBar from "../../components/layout/topBar/index.vue";
import collapseView from "../../components/collapse/index.vue";
import { ref } from "vue";
const collapse = ref<boolean>(true);
</script>
<template>
  <el-container class="rootContainer">
    <el-header class="handlerContainer"><TopBar/></el-header>
    <el-container>
      <el-aside class="starMenuContainer"
        ><asideStartMenu></asideStartMenu
      ></el-aside>
      <el-container>
        <el-aside class="menuContainer" v-show="collapse">
          <asideMenu></asideMenu>
        </el-aside>
        <collapseView
          :collapse="collapse"
          :changeCollapse="() => (collapse = !collapse)"
        ></collapseView>
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
    padding: 0;
    overflow: hidden;
  }
}
</style>
