<script setup lang="ts">
import SubMenu from "./SubMenu.vue";
import { ref, watch } from "vue";
import { useRouter } from "vue-router";
import { useModuleStore } from "@commons/stores/modules/module";
const router = useRouter();
const moduleStore = useModuleStore();
const activeMenu = ref<string>();
watch(
  router.currentRoute,
  (pre) => {
    activeMenu.value = pre.meta.sourceMenu as string;
  },
  { immediate: true }
);
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

.el-menu-item.is-active {
  color: var(--el-menu-active-color);
  background-color: var(--el-menu-hover-bg-color);
}

.fu-table-header {
  background-color: rebeccapurple;
}
</style>
