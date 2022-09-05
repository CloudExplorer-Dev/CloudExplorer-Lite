<script setup lang="ts">
import SubMenu from "./SubMenu.vue";
import { computed } from "vue";
import { useRouter } from "vue-router";
import { useModuleStore } from "@commons/stores/modules/module";

const moduleStore = useModuleStore();
const currentRoute = useRouter().currentRoute.value;
const activeMenu = computed(() => {
  if (currentRoute.matched.length > 2) {
    return currentRoute.matched[currentRoute.matched.length - 2].path;
  }
  return currentRoute.path;
});
</script>
<template>
  <el-menu class="menuContainer" :router="true" :default-active="activeMenu">
    <div class="title">
      <h1>
        {{ moduleStore.currentModule?.name }}
      </h1>
    </div>
    <div class="splitLine"></div>
    <template v-for="item in moduleStore.currentModuleMenu" :key="item.name">
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
