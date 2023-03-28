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
          <el-select v-model="currentCluster" class="padding-r-12">
            <template v-slot:prefix>
              <span> 集群 </span>
            </template>
            <el-option label="全部集群" value="all" />
            <el-option
              v-for="item in clusters"
              :key="item.id"
              :label="item.name"
              :value="item.id"
              v-show="
                item.accountId === currentAccount || currentAccount === 'all'
              "
            />
          </el-select>
          <el-select v-model="currentHost" class="padding-r-12">
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
                (item.accountId === currentAccount ||
                  currentAccount === 'all') &&
                (item.zone === currentCluster || currentCluster === 'all')
              "
            />
          </el-select>
          <el-select v-model="currentDatastore">
            <template v-slot:prefix>
              <span> 存储器 </span>
            </template>
            <el-option label="全部存储器" value="all" />
            <el-option
              v-for="item in datastoreList"
              :key="item.id"
              :label="item.datastoreName"
              :value="item.id"
              v-show="
                (item.accountId === currentAccount ||
                  currentAccount === 'all') &&
                (item.zone === currentCluster || currentCluster === 'all')
              "
            />
          </el-select>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="16" class="row">
      <el-col :span="12">
        <ComputerResourceAllocatedRate
          :cloud-account-id="currentAccount"
          :cluster-id="currentCluster"
          :datastore-id="currentDatastore"
          :host-id="currentHost"
          style="height: 198px; border: 1px solid rgba(223, 224, 227, 1)"
        ></ComputerResourceAllocatedRate>
      </el-col>
      <el-col :span="12">
        <ComputerResourceUseRate
          :cloud-account-id="currentAccount"
          :cluster-id="currentCluster"
          :datastore-id="currentDatastore"
          :host-id="currentHost"
          style="height: 198px; border: 1px solid rgba(223, 224, 227, 1)"
        ></ComputerResourceUseRate>
      </el-col>
    </el-row>
    <el-row :gutter="16" class="row">
      <el-col :span="12">
        <CloudAccountHostSpread
          :cloud-account-id="currentAccount"
          :cluster-id="currentCluster"
          :datastore-id="currentDatastore"
          :host-id="currentHost"
        ></CloudAccountHostSpread>
      </el-col>
      <el-col :span="12">
        <HostCloudServerSpread
          :cloud-account-id="currentAccount"
          :cluster-id="currentCluster"
          :datastore-id="currentDatastore"
          :host-id="currentHost"
        ></HostCloudServerSpread>
      </el-col>
    </el-row>
    <el-row :gutter="16" class="row">
      <el-col :span="24">
        <DatastoreUseRate
          :cloud-account-id="currentAccount"
          :cluster-id="currentCluster"
          :datastore-id="currentDatastore"
          :host-id="currentHost"
        ></DatastoreUseRate>
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
import type { VmCloudDatastoreVO } from "@/api/vm_cloud_datastore/type";
import ComputerResourceAllocatedRate from "@/views/base_resource_analysis/item/BaseResourceAllocationRate.vue";
import CloudAccountHostSpread from "@/views/base_resource_analysis/item/BaseResourceSpread.vue";
import HostCloudServerSpread from "@/views/base_resource_analysis/item/HostServerSpread.vue";
import ComputerResourceUseRate from "@/views/base_resource_analysis/item/BaseResourceUseRate.vue";
import DatastoreUseRate from "@/views/base_resource_analysis/item/BaseResourceUseRateTrend.vue";

//条件
const currentAccount = ref<string>("all");
const currentCluster = ref<string>("all");
const currentHost = ref<string>("all");
const currentDatastore = ref<string>("all");
const params = ref<ResourceAnalysisRequest>();
//查询所有私有云云账号
const accounts = ref<Array<CloudAccount>>();
//查询所有集群
const clusters = ref<any>();
//查询所有宿主机
const hosts = ref<Array<VmCloudHostVO>>();
//查询所有存储器
const datastoreList = ref<Array<VmCloudDatastoreVO>>();
const getSearchCondition = () => {
  ResourceSpreadViewApi.listPrivateAccounts().then(
    (res) => (accounts.value = res.data)
  );
  ResourceSpreadViewApi.listClusters(params).then(
    (res) => (clusters.value = res.data)
  );
  ResourceSpreadViewApi.listHost(params).then(
    (res) => (hosts.value = res.data)
  );
  ResourceSpreadViewApi.listDatastores(params).then(
    (res) => (datastoreList.value = res.data)
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
