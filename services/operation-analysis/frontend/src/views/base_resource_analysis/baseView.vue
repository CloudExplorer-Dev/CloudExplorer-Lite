<template>
  <div class="content">
    <el-row :gutter="10" class="row">
      <el-col :span="4">
        <el-select v-model="currentAccount" class="m-2">
          <el-option label="全部云账号" value="all" />
          <el-option
            v-for="item in accounts"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-select v-model="currentCluster" class="m-2">
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
      </el-col>
      <el-col :span="4">
        <el-select v-model="currentHost" class="m-2">
          <el-option label="全部宿主机" value="all" />
          <el-option
            v-for="item in hosts"
            :key="item.id"
            :label="item.hostName"
            :value="item.id"
            v-show="
              (item.accountId === currentAccount || currentAccount === 'all') &&
              (item.zone === currentCluster || currentCluster === 'all')
            "
          />
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-select v-model="currentDatastore" class="m-2">
          <el-option label="全部存储器" value="all" />
          <el-option
            v-for="item in datastoreList"
            :key="item.id"
            :label="item.datastoreName"
            :value="item.id"
            v-show="
              (item.accountId === currentAccount || currentAccount === 'all') &&
              (item.zone === currentCluster || currentCluster === 'all')
            "
          />
        </el-select>
      </el-col>
    </el-row>
    <el-row :gutter="10" class="row">
      <el-col :span="12">
        <ComputerResourceAllocatedRate
          :cloud-account-id="currentAccount"
          :cluster-id="currentCluster"
          :datastore-id="currentDatastore"
          :host-id="currentHost"
        ></ComputerResourceAllocatedRate>
      </el-col>
    </el-row>
    <el-row :gutter="10" class="row">
      <el-col :span="12">
        <CloudAccountHostSpread
          :cloud-account-id="currentAccount"
          :cluster-id="currentCluster"
          :datastore-id="currentDatastore"
          :host-id="currentHost"
        ></CloudAccountHostSpread>
      </el-col>
      <el-col :span="12">
        <CloudAccountDatastoreSpread
          :cloud-account-id="currentAccount"
          :cluster-id="currentCluster"
          :datastore-id="currentDatastore"
          :host-id="currentHost"
        ></CloudAccountDatastoreSpread>
      </el-col>
    </el-row>
    <el-row :gutter="10" class="row">
      <el-col :span="12">
        <HostCloudServerSpread
          :cloud-account-id="currentAccount"
          :cluster-id="currentCluster"
          :datastore-id="currentDatastore"
          :host-id="currentHost"
        ></HostCloudServerSpread>
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
import ComputerResourceAllocatedRate from "@/views/base_resource_analysis/item/ComputerResourceAllocatedRate.vue";
import CloudAccountHostSpread from "@/views/base_resource_analysis/item/CloudAccountHostSpread.vue";
import CloudAccountDatastoreSpread from "@/views/base_resource_analysis/item/CloudAccountDatastoreSpread.vue";
import HostCloudServerSpread from "@/views/base_resource_analysis/item/HostCloudServerSpread.vue";
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
  .row {
    padding: 5px 0 10px 0;
  }
}
</style>
