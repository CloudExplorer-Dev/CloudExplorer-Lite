<script setup lang="ts">
import { ref } from "vue";

const collapse = ref<boolean>(true);
</script>
<template>
  <el-container class="contentNav">
    <el-aside class="menuContainer" v-show="collapse">
      <asideMenu></asideMenu>
    </el-aside>
    <collapseView
      class="collapseView"
      :collapse="collapse"
      :changeCollapse="() => (collapse = !collapse)"
    ></collapseView>
    <el-main class="contentContainer">
      <router-view></router-view>
    </el-main>
  </el-container>
</template>

<style lang="scss" scoped>
.contentNav {
  height: calc(100vh - var(--ce-header-height));
  width: calc(100vw - var(--ce-star-menu-width));
}
.menuContainer {
  z-index: 100;
  --el-aside-width: 200px;
  position: relative;
  height: 100%;
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
.collapseView {
  z-index: 1001;
}
</style>
