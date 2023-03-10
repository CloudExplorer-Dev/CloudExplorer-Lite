<script lang="ts" setup>
import { computed, onMounted } from "vue";

import UserInfo from "./items/UserInfo.vue";
import BaseModuleGroup from "./items/BaseModuleGroup.vue";
import BillModuleGroup from "./items/BillModuleGroup.vue";
import SecurityInfo from "./items/SecurityInfo.vue";
import QuickAccess from "./items/QuickAccess.vue";
import BillTrend from "./items/BillTrend.vue";
import ServerIncreaseTrend from "./items/ServerIncreaseTrend.vue";
import ServerDistribution from "./items/ServerDistribution.vue";
import ServerMetrics from "./items/ServerMetrics.vue";
import ServerOptimization from "./items/ServerOptimization.vue";

import { getHistoryTrend } from "./items/api";
import _ from "lodash";

import { useUserStore } from "@commons/stores/modules/user";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";

const userStore = useUserStore();
const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();

function show(module: string, permission: any, roles: string[]): boolean {
  return (
    _.some(moduleStore.runningModules, (m) => m.id === module) &&
    permissionStore.hasPermission(permission) &&
    _.includes(roles, userStore.currentRole)
  );
}

const adminShowBillTrend = computed<boolean>(() => {
  return show("finance-management", "[finance-management]BILL_ViEW:READ", [
    "ADMIN",
    "ORGADMIN",
  ]);
});

const adminShowServerIncreaseTrend = computed<boolean>(() => {
  return show(
    "operation-analysis",
    [
      "[operation-analysis]BASE_RESOURCE_ANALYSIS:READ",
      "[operation-analysis]OVERVIEW:READ",
    ],
    ["ADMIN", "ORGADMIN"]
  );
});

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
          <div class="flex-content">
            <BaseModuleGroup />
            <QuickAccess class="flex-div-1 divide-info" />
            <BillTrend
              class="flex-div-1 divide-info"
              :need-roles="['USER']"
              :getHistoryTrend="getHistoryTrend"
              head-position="left"
            />
          </div>
        </el-col>
        <el-col :span="8">
          <div class="flex-content">
            <UserInfo class="flex-div-2 divide-info" />
            <BillModuleGroup />
            <SecurityInfo class="flex-div-2 divide-info" />
            <ServerDistribution
              class="flex-div-2 divide-info"
              :need-roles="['USER']"
              type="byAccount"
              title="资源分布"
            />
          </div>
        </el-col>
      </el-row>
      <el-row
        :gutter="20"
        type="flex"
        v-if="adminShowBillTrend || adminShowServerIncreaseTrend"
      >
        <el-col :span="adminShowBillTrend ? 12 : 24">
          <ServerIncreaseTrend
            style="height: 100%"
            :need-roles="['ADMIN', 'ORGADMIN']"
          />
        </el-col>
        <el-col :span="adminShowServerIncreaseTrend ? 12 : 24">
          <BillTrend
            style="height: 100%"
            :need-roles="['ADMIN', 'ORGADMIN']"
            :getHistoryTrend="getHistoryTrend"
            head-position="left"
          />
        </el-col>
      </el-row>
      <el-row :gutter="20" type="flex" v-if="userStore.currentRole === 'USER'">
        <el-col :span="6">
          <ServerDistribution
            class="flex-div-2 divide-info"
            :need-roles="['USER']"
            type="byStatus"
            title="我的资源"
            :height="300"
          />
        </el-col>
        <el-col :span="18">
          <ServerMetrics
            class="flex-div-2 divide-info"
            :need-roles="['USER']"
            title="资源监控"
          />
        </el-col>
      </el-row>
      <el-row :gutter="20" type="flex">
        <el-col :span="24">
          <ServerOptimization />
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
