<script setup lang="ts">
import Detailed from "./items/resource_detailed/Detailed.vue";
import ComputerResourceAllocatedRate from "@/views/base_resource_analysis/item/BaseResourceAllocationRate.vue";
import CloudServerOrgWorkspaceSpread from "@/views/server_analysis/item/CloudServerOrgWorkspaceSpread.vue";
import CloudServerIncreaseTrend from "@/views/server_analysis/item/CloudServerIncreaseTrend.vue";
import ComputerResourceUseRate from "@/views/base_resource_analysis/item/BaseResourceUseRate.vue";
import { ref, onMounted } from "vue";
import CommonApi from "@/api/common/index";
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
<template>
  <el-container class="contentContainer" direction="vertical">
    <div style="padding: 0px 24px 0px 24px">
      <div style="float: left">
        <h3>总览</h3>
      </div>
      <div style="float: right; margin-top: 15px">
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
    </div>
    <div class="content">
      <el-row :gutter="16">
        <el-col :span="24">
          <div class="flex-content">
            <div class="flex-div-1 divide-info">
              <Detailed :cloud-account-id="currentAccountId"></Detailed>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="16">
        <el-col :span="12">
          <div class="flex-content">
            <div class="flex-div-1 divide-info">
              <ComputerResourceAllocatedRate
                :cloud-account-id="currentAccountId"
              ></ComputerResourceAllocatedRate>
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="flex-content">
            <div class="flex-div-1 divide-info">
              <ComputerResourceUseRate></ComputerResourceUseRate>
            </div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="16">
        <el-col :span="12">
          <div class="flex-content">
            <div class="flex-div-1 divide-info">
              <CloudServerOrgWorkspaceSpread
                :cloud-account-id="currentAccountId"
              ></CloudServerOrgWorkspaceSpread>
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="flex-content">
            <div class="flex-div-1 divide-info">
              <CloudServerIncreaseTrend
                :cloud-account-id="currentAccountId"
              ></CloudServerIncreaseTrend>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </el-container>
</template>
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
  margin-bottom: 14px;
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
