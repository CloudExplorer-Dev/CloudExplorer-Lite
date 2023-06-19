<template>
  <div class="content">
    <el-row :gutter="12" class="header-row">
      <el-col :span="24">
        <div class="search-bar">
          <el-select v-model="currentAccount" class="padding-r-12">
            <template v-slot:prefix>
              <span> 云账号 </span>
            </template>
            <el-option label="全部云账号" value="all" />
            <el-option
              v-for="item in accounts"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
          <el-select v-model="currentHost">
            <template v-slot:prefix>
              <span> 宿主机 </span>
            </template>
            <el-option label="全部宿主机" value="all" />
            <el-option
              v-for="item in hosts"
              :key="item.id"
              :label="item.hostName"
              :value="item.hostId"
              v-show="
                item.accountId === currentAccount || currentAccount === 'all'
              "
            />
          </el-select>
        </div>
      </el-col>
    </el-row>
    <DoughnutChartGroup
      :cloud-account-id="currentAccount"
      :host-id="currentHost"
    ></DoughnutChartGroup>
    <el-row :gutter="16" class="row">
      <el-col :span="12">
        <CloudServerIncreaseTrend
          :permission="[
            '[operation-analysis]SERVER_ANALYSIS:READ',
            '[operation-analysis]OVERVIEW:READ',
          ]"
          :cloud-account-id="currentAccount"
          :host-id="currentHost"
          style="height: 250px; border: 1px solid rgba(223, 224, 227, 1)"
        ></CloudServerIncreaseTrend>
      </el-col>
      <el-col :span="12">
        <CloudServerOrgWorkspaceSpread
          :cloud-account-id="currentAccount"
          :host-id="currentHost"
          style="height: 250px; border: 1px solid rgba(223, 224, 227, 1)"
        ></CloudServerOrgWorkspaceSpread>
      </el-col>
    </el-row>
    <el-row :gutter="16" class="row">
      <el-col :span="24">
        <ResourceUseRateTrend
          :cloud-account-id="currentAccount"
          :host-id="currentHost"
        ></ResourceUseRateTrend>
      </el-col>
    </el-row>
  </div>
</template>
<script lang="ts" setup>
import { onMounted, ref, watch } from "vue";
import ResourceSpreadViewApi from "@/api/server_analysis/index";
import CommonApi from "@/api/common/index";
import { ResourceAnalysisRequest } from "@commons/api/resource_spread_view/type";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import type { VmCloudHostVO } from "@/api/vm_cloud_host/type";
import CloudServerOrgWorkspaceSpread from "./item/CloudServerOrgWorkspaceSpread.vue";
import CloudServerIncreaseTrend from "@commons/business/base-layout/home-page/items/operation/CloudServerIncreaseTrend.vue";
import ResourceUseRateTrend from "./item/CloudServerResourceUseRateTrend.vue";
import DoughnutChartGroup from "@/views/server_analysis/item/DoughnutChartGroup.vue";
import _ from "lodash";
import { useUserStore } from "@commons/stores/modules/user";
const userStore = useUserStore();
const adminRole = ref<boolean>(userStore.currentRole === "ADMIN");
//条件
const currentAccount = ref<string>("all");
const currentHost = ref<string>("all");
const params = ref<ResourceAnalysisRequest>();
//查询所有私有云云账号
const accounts = ref<Array<CloudAccount>>();
//查询所有宿主机
const hosts = ref<Array<VmCloudHostVO>>();
const getSearchCondition = () => {
  CommonApi.listAll().then((res) => {
    accounts.value = res.data;
    if (res.data && res.data.length > 0) {
      _.map(accounts.value, "id")
        ? _.set(params, "accountIds", _.map(accounts.value, "id"))
        : "";
      ResourceSpreadViewApi.listHost(params).then(
        (res) => (hosts.value = res.data)
      );
    }
  });
};
onMounted(() => {
  getSearchCondition();
});
watch(
  currentAccount,
  () => {
    currentHost.value = "all";
  },
  { immediate: true }
);
</script>

<style scoped lang="scss">
.content {
  min-width: 1000px;
  .header-row {
    padding: 5px 0 10px 0;
    text-align: left;
    .header-title {
      font-style: normal;
      font-weight: 500;
      font-size: 16px;
      line-height: 32px;
      color: #1f2329;
    }
    .search-bar {
      line-height: 32px;
      .el-select {
        width: 200px;
      }
      .padding-r-12 {
        padding-right: 12px;
      }
    }
  }
  .row {
    padding: 5px 0 10px 0;
    text-align: left;
    .header-title {
      font-style: normal;
      font-weight: 500;
      font-size: 16px;
      line-height: 32px;
      color: #1f2329;
    }
  }
}
</style>
