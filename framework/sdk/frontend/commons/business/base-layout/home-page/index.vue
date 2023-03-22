<script lang="ts" setup>
import { computed, onMounted } from "vue";

import UserInfo from "./items/UserInfo.vue";
import MyResources from "./items/MyResources.vue";
import QuickAccess from "./items/QuickAccess.vue";

import BillModuleGroup from "./items/bill/BillModuleGroup.vue";
import SecurityInfo from "./items/SecurityInfo.vue";

import BillTrend from "./items/bill/BillTrend.vue";
import ServerIncreaseTrend from "./items/ServerIncreaseTrend.vue";
import ServerOptimization from "./items/ServerOptimization.vue";

onMounted(() => {
  console.log("home page load!");
});
</script>
<template>
  <el-container class="contentContainer" direction="vertical">
    <el-row :gutter="20">
      <el-col :span="16">
        <QuickAccess />
        <SecurityInfo />
        <ServerOptimization />
        <BillTrend head-position="left" />
        <ServerIncreaseTrend />
      </el-col>
      <el-col :span="8">
        <UserInfo />
        <MyResources />
        <BillModuleGroup />
      </el-col>
    </el-row>
  </el-container>
</template>

<style scoped lang="scss">
.contentContainer {
  height: calc(100vh - var(--ce-header-height));
  padding: 24px;
  background-color: #f2f2f2;
  overflow-y: auto;
  overflow-x: hidden;
}

.info-card {
  margin-bottom: 20px;
}
.info-card:last-child {
  margin-bottom: 0;
}

.breadcrumb {
  height: var(--ce-main-breadcrumb-height, 50px);
  width: 100%;
  display: flex;
  align-items: center;
  margin-left: var(--ce-main-breadcrumb-margin-left, 30px);
}
.content {
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
  overflow: hidden;
  &:hover {
    overflow-y: auto;
  }
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
.flex-content {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;

  .flex-div-1 {
    flex: 1 1 auto;
  }

  .flex-div-2 {
    flex: 1 1 auto;
  }

  .divide-info {
    margin-bottom: 20px;
  }
  .divide-info:last-child {
    margin-bottom: 0;
  }
}
</style>
