<script lang="ts" setup>
import TopBar from "@commons/components/layout/top-bar/index.vue";
import AsideStartMenu from "@commons/components/layout/aside-start-menu/index.vue";

import { useHomeStore } from "@commons/stores/modules/home";
import { onMounted } from "vue";
import CeIcon from "@commons/components/ce-icon/index.vue";

import UserInfo from "./UserInfo.vue";
import BaseModuleGroup from "./BaseModuleGroup.vue";

const homeStore = useHomeStore();

onMounted(() => {
  console.log("home page load!");
});
</script>
<template>
  <el-container class="contentContainer" direction="vertical">
    <div class="breadcrumb"><h3>首页</h3></div>
    <div class="content">
      <el-row :gutter="20" type="flex">
        <el-col :span="16">
          <BaseModuleGroup />
        </el-col>
        <el-col :span="8">
          <UserInfo />
        </el-col>
      </el-row>
    </div>
  </el-container>
</template>

<style scoped lang="scss">
.contentContainer {
  height: calc(100vh - var(--ce-header-height));
  padding: 0;
  background-color: #f2f2f2;
  overflow-y: auto;
  overflow-x: hidden;
}
.breadcrumb {
  height: var(--ce-main-breadcrumb-height, 50px);
  width: 100%;
  display: flex;
  align-items: center;
  margin-left: var(--ce-main-breadcrumb-margin-left, 30px);
}
.content {
  width: 100%;
  margin: var(--ce-main-content-margin-top, 10px)
    var(--ce-main-content-margin-left, 30px)
    var(--ce-main-content-margin-right, 30px)
    var(--ce-main-content-margin-bottom, 30px);
  height: calc(
    100% - var(--ce-main-breadcrumb-height, 50px) -
      var(--ce-main-content-margin-top, 10px) -
      var(--ce-main-content-margin-bottom, 30px)
  );
  width: calc(
    100% - var(--ce-main-content-margin-left, 30px) -
      var(--ce-main-content-margin-right, 30px)
  );
  overflow-y: auto;
  overflow-x: hidden;
}
.el-row {
  margin-bottom: 20px;
}
.el-row:last-child {
  margin-bottom: 0;
}
.card {
  border-radius: var(--ce-main-content-border-radius, 10px);
  background-color: var(--ce-main-content-bg-color, #fff);
  width: 100%;
  height: 100%;
}
</style>
