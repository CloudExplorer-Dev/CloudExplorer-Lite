<script lang="ts" setup>
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { BaseModuleInfo } from "@commons/business/base-layout/home-page/items/BaseModuleType";
import BaseModule from "@commons/business/base-layout/home-page/items/BaseModule.vue";
import { computed } from "vue";
import _ from "lodash";

const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();

const baseList1: Array<BaseModuleInfo> = [
  new BaseModuleInfo(
    "yun",
    "云账号",
    ["ADMIN"],
    "[management-center]CLOUD_ACCOUNT:READ",
    "management-center",
    "/management-center/api/cloud_account/count",
    "/management-center#/cloud_account/list"
  ),
  new BaseModuleInfo(
    "xuniyunzhuji",
    "云主机",
    ["ADMIN", "ORGADMIN", "USER"],
    "[vm-service]CLOUD_SERVER:READ",
    "vm-service",
    "/vm-service/api/server/count",
    "/vm-service#/vm_cloud_server/list"
  ),
  new BaseModuleInfo(
    "yuncunchu",
    "云磁盘",
    ["ADMIN", "USER"],
    "[vm-service]CLOUD_DISK:READ",
    "vm-service",
    "/vm-service/api/disk/count",
    "/vm-service#/vm_cloud_disk/list"
  ),
];

const baseList2: Array<BaseModuleInfo> = [
  new BaseModuleInfo(
    "xuniji1",
    "宿主机",
    ["ADMIN"],
    [
      "[operation-analysis]BASE_RESOURCE_ANALYSIS:READ",
      "[operation-analysis]OVERVIEW:READ",
    ],
    "operation-analysis",
    "/operation-analysis/api/base_resource_analysis/host/count",
    "/operation-analysis#/resource_analysis/base_resource_analysis/list"
  ),
  new BaseModuleInfo(
    "yidongyunkongzhitaiicon06",
    "存储器",
    ["ADMIN"],
    [
      "[operation-analysis]BASE_RESOURCE_ANALYSIS:READ",
      "[operation-analysis]OVERVIEW:READ",
    ],
    "operation-analysis",
    "/operation-analysis/api/base_resource_analysis/datastore/count",
    "/operation-analysis#/resource_analysis/base_resource_analysis/list"
  ),
];

const showRow1 = computed<boolean>(() => {
  return _.some(baseList1, (obj) => obj.show.value);
});
const showRow2 = computed<boolean>(() => {
  return _.some(baseList2, (obj) => obj.show.value);
});
</script>
<template>
  <div class="info-card" v-if="showRow1 || showRow2">
    <div class="title">我的资源</div>

    <el-row v-if="showRow1" :gutter="8">
      <template v-for="(info, index) in baseList1" :key="index">
        <el-col :span="8" v-if="info.show.value">
          <BaseModule
            :name="info.name"
            :redirect="info.redirect"
            :func="info.path"
            :type="info.type"
            :unit="info.unit"
          />
        </el-col>
      </template>
    </el-row>

    <el-row v-if="showRow2" :gutter="8">
      <template v-for="(info, index) in baseList2" :key="index">
        <el-col :span="8" v-if="info.show.value">
          <BaseModule
            :name="info.name"
            :redirect="info.redirect"
            :func="info.path"
            :type="info.type"
            :unit="info.unit"
          />
        </el-col>
      </template>
    </el-row>
  </div>
</template>

<style scoped lang="scss">
.info-card {
  border-radius: 4px;
  background-color: #ffffff;
  padding: 24px;

  .title {
    font-style: normal;
    font-weight: 500;
    font-size: 16px;
    line-height: 24px;
    margin-bottom: 14px;
  }

  .el-row {
    margin-bottom: 20px;
  }

  .el-row:last-child {
    margin-bottom: 0;
  }
}
</style>
