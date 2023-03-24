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
              :value="item.id"
              v-show="
                item.accountId === currentAccount || currentAccount === 'all'
              "
            />
          </el-select>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="16" class="row">
      <el-col :span="8">
        <CloudAccountSpread
          :cloud-account-id="currentAccount"
          :host-id="currentHost"
        ></CloudAccountSpread>
      </el-col>
      <el-col :span="8">
        <StatusSpread
          :cloud-account-id="currentAccount"
          :host-id="currentHost"
        ></StatusSpread>
      </el-col>
      <el-col :span="8">
        <ChargeTypeSpread
          :cloud-account-id="currentAccount"
          :host-id="currentHost"
        ></ChargeTypeSpread>
      </el-col>
    </el-row>
    <el-row :gutter="16" class="row">
      <el-col :span="12">
        <CloudServerIncreaseTrend
          :cloud-account-id="currentAccount"
          :host-id="currentHost"
        ></CloudServerIncreaseTrend>
      </el-col>
      <el-col :span="12">
        <CloudServerOrgWorkspaceSpread
          :cloud-account-id="currentAccount"
          :host-id="currentHost"
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
import { onMounted, ref } from "vue";
import ResourceSpreadViewApi from "@/api/resource_spread_view";
import { ResourceAnalysisRequest } from "@commons/api/resource_spread_view/type";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import type { VmCloudHostVO } from "@/api/vm_cloud_host/type";
import CloudAccountSpread from "./item/CloudAccountSpread.vue";
import StatusSpread from "./item/CLoudServerStatusSpread.vue";
import ChargeTypeSpread from "./item/CloudServerChargeTypeSpread.vue";
import CloudServerOrgWorkspaceSpread from "./item/CloudServerOrgWorkspaceSpread.vue";
import CloudServerIncreaseTrend from "./item/CloudServerIncreaseTrend.vue";
import ResourceUseRateTrend from "./item/CloudServerResourceUseRateTrend.vue";

//条件
const currentAccount = ref<string>("all");
const currentHost = ref<string>("all");
const params = ref<ResourceAnalysisRequest>();
//查询所有私有云云账号
const accounts = ref<Array<CloudAccount>>();
//查询所有宿主机
const hosts = ref<Array<VmCloudHostVO>>();
const getSearchCondition = () => {
  ResourceSpreadViewApi.listPrivateAccounts().then(
    (res) => (accounts.value = res.data)
  );
  ResourceSpreadViewApi.listHost(params).then(
    (res) => (hosts.value = res.data)
  );
};
onMounted(() => {
  getSearchCondition();
});
</script>

<style scoped lang="scss">
.content {
  min-width: 1000px;
  .header-row {
    padding: 5px 0 10px 0;
    text-align: right;
    .header-title {
      font-family: "PingFang SC", serif;
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
      font-family: "PingFang SC", serif;
      font-style: normal;
      font-weight: 500;
      font-size: 16px;
      line-height: 32px;
      color: #1f2329;
    }
  }
}
</style>
