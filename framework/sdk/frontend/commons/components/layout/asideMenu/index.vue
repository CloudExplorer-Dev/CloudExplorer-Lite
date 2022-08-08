<script setup lang="ts">
import { ref, onMounted } from "vue";
import { getMenuByModule } from "../../../api/menu";
import { getCurrentModule } from "../../../api/module";
import { Menu } from "../../../api/menu/type";
import { Module } from "../../../api/module/type";
import SubMenu from "./SubMenu.vue";
const menus = ref<Array<Menu>>();
const currentModule = ref<Module>();
onMounted(() => {
  getMenuByModule("management-center").then((ok) => {
    menus.value = ok.data;
  });
  getCurrentModule().then((ok) => {
    currentModule.value = ok.data;
  });
});
</script>
<template>
  <el-menu class="menuContainer" :collapse="false" :router="true">
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
