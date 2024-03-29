<template>
  <layout-auto-height-content
    :style="{ backgroundColor: '#f2f2f2', height: 'auto' }"
    style="
      --ce-main-content-padding-top: 0;
      --ce-main-content-padding-left: 0;
      --ce-main-content-padding-right: 0;
      --ce-main-content-padding-bottom: 0;
    "
  >
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
      <div
        style="
          height: 100%;
          display: flex;
          align-items: center;
          margin-right: 24px;
        "
      >
        <el-select v-model="currentAccountId">
          <el-option label="全部云账号" value="all" />
          <el-option
            v-for="item in accounts"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </div>
    </template>

    <div class="content">
      <el-row class="row" :gutter="16">
        <el-col :span="24">
          <Detailed :cloud-account-id="currentAccountId" />
        </el-col>
      </el-row>
      <el-row
        class="row"
        :gutter="16"
        v-show="userStore.currentRole === 'ADMIN'"
      >
        <el-col :span="12">
          <ComputerResourceAllocatedRate
            style="height: 198px"
            :cloud-account-id="currentAccountId"
          />
        </el-col>
        <el-col :span="12">
          <ComputerResourceUseRate
            style="height: 198px"
            :cloud-account-id="currentAccountId"
          />
        </el-col>
      </el-row>
      <el-row class="row" :gutter="16">
        <el-col :span="12">
          <CloudServerOrgWorkspaceSpread
            style="height: 249px"
            :cloud-account-id="currentAccountId"
          />
        </el-col>
        <el-col :span="12">
          <div class="flex-content">
            <div class="flex-div-1 divide-info">
              <CloudServerIncreaseTrend
                style="height: 249px"
                :cloud-account-id="currentAccountId"
              />
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <ServerOptimization :cloud-account-id="currentAccountId" />
        </el-col>
      </el-row>
    </div>
  </layout-auto-height-content>
</template>
<script setup lang="ts">
import Detailed from "./items/resource_detailed/Detailed.vue";
import ComputerResourceAllocatedRate from "@/views/base_resource_analysis/item/BaseResourceAllocationRate.vue";
import CloudServerOrgWorkspaceSpread from "@/views/server_analysis/item/CloudServerOrgWorkspaceSpread.vue";
import CloudServerIncreaseTrend from "@commons/business/base-layout/home-page/items/operation/CloudServerIncreaseTrend.vue";
import ComputerResourceUseRate from "@/views/base_resource_analysis/item/BaseResourceUseRate.vue";
import ServerOptimization from "@commons/business/base-layout/home-page/items/operation/optimize/ServerOptimization.vue";
import { ref, onMounted } from "vue";
import CommonApi from "@/api/common/index";
import { useUserStore } from "@commons/stores/modules/user";
const userStore = useUserStore();
import type { CloudAccount } from "@commons/api/cloud_account/type";
const currentAccountId = ref<string>("all");
const accounts = ref<Array<CloudAccount>>([]);
const getAccounts = () => {
  CommonApi.listAll().then((res) => {
    accounts.value = res.data;
  });
};
onMounted(() => {
  getAccounts();
});
</script>
<style scoped lang="scss">
.contentContainer {
  height: calc(100vh - var(--ce-header-height));
  min-width: 1000px;
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
.breadcrumb-right {
  height: var(--ce-main-breadcrumb-height, 50px);
  width: 100%;
  display: flex;
  align-items: center;
  margin-left: var(--ce-main-breadcrumb-margin-right, 30px);
}
.content {
  height: calc(100% - var(--ce-main-breadcrumb-height, 50px));
  overflow: hidden;
  &:hover {
    overflow-y: auto;
  }
}
.el-row {
  margin-bottom: 16px;
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
