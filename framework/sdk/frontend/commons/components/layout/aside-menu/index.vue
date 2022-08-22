<script setup lang="ts">
import SubMenu from "./SubMenu.vue";
import { moduleStore } from "../../../stores/module";
import { useRouter } from "vue-router";
const { menus, currentModule } = moduleStore();
const currentRoute = useRouter().currentRoute;
</script>
<template>
  <el-menu
    class="menuContainer"
    :router="true"
    :default-active="currentRoute.path"
  >
    <div class="title">
      <h1>
        {{ currentModule?.title }}
      </h1>
    </div>
    <div class="splitLine"></div>
    <template v-for="item in menus" :key="item.name">
      <SubMenu :menuInfo="item" />
    </template>
  </el-menu>
</template>
<style lang="scss">
.menuContainer {
  .title {
    height: 50px;
    width: calc(100% - 1px);
    display: flex;
    justify-content: flex-start;
    align-items: center;
    flex-wrap: wrap;
    h1 {
      margin-left: 20px;
      font-size: 14px;
    }
  }
  .splitLine {
    height: 1px;
    width: 100%;
    background-color: rgb(204, 204, 204);
  }
}
</style>
