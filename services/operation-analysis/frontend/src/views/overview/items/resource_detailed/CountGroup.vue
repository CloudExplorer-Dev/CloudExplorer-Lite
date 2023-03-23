<script lang="ts" setup>
import { computed } from "vue";
import _ from "lodash";
import { usePermissionStore } from "@commons/stores/modules/permission";
import CountCard from "./CountCard.vue";
import { useUserStore } from "@commons/stores/modules/user";
import type { CountType } from "./CountType";
const permissionStore = usePermissionStore();
const userStore = useUserStore();
const props = defineProps<{
  cloudAccountId: string;
}>();
const baseList: Array<CountType> = [
  {
    icon: "yun",
    name: "云账号",
    hasPermission: permissionStore.hasPermission(
      "[vm-service]CLOUD_SERVER:READ"
    ),
    module: "operation-analysis",
    path: "/api/common/cloud_account/count",
    redirect: "/management-center#/cloud_account/list",
    roles: ["ADMIN", "ORGADMIN", "USER"],
  },
  {
    icon: "xuniyunzhuji",
    name: "云主机",
    hasPermission: permissionStore.hasPermission(
      "[vm-service]CLOUD_SERVER:READ"
    ),
    module: "operation-analysis",
    path: "/api/server_analysis/cloud_account/cloud_server/count",
    redirect: "/vm-service#/vm_cloud_server/list",
    roles: ["ADMIN", "ORGADMIN", "USER"],
  },
  {
    icon: "yuncunchu",
    name: "磁盘",
    hasPermission: permissionStore.hasPermission("[vm-service]CLOUD_DISK:READ"),
    module: "operation-analysis",
    path: "/api/disk_analysis/cloud_account/disk/count",
    redirect: "/vm-service#/vm_cloud_disk/list",
    roles: ["ADMIN", "ORGADMIN", "USER"],
  },
  {
    icon: "xuniji1",
    name: "宿主机",
    hasPermission: permissionStore.hasPermission([
      "[operation-analysis]BASE_RESOURCE_ANALYSIS:READ",
      "[operation-analysis]OVERVIEW:READ",
    ]),
    module: "operation-analysis",
    path: "/api/base_resource_analysis/cloud_account/host/count",
    redirect: "/base_resource_analysis/list",
    roles: ["ADMIN"],
  },
  {
    icon: "yidongyunkongzhitaiicon06",
    name: "存储器",
    hasPermission: permissionStore.hasPermission([
      "[operation-analysis]BASE_RESOURCE_ANALYSIS:READ",
      "[operation-analysis]OVERVIEW:READ",
    ]),
    module: "operation-analysis",
    path: "/api/base_resource_analysis/cloud_account/datastore/count",
    redirect: "/resource_analysis/base_resource_analysis/list",
    roles: ["ADMIN"],
  },
];

const list = computed(() => {
  const result: Array<Array<CountType>> = [];
  _.forEach(
    _.filter(
      baseList,
      (obj) => obj.hasPermission && _.includes(obj.roles, userStore.currentRole)
    ),
    (obj, index) => {
      if (result.length <= index) {
        result.push([]);
      }
      result[index].push(obj);
    }
  );
  return result;
});
</script>
<template>
  <div
    style="
      font-weight: bold;
      font-size: 16px;
      padding-top: 4px;
      padding-bottom: 16px;
    "
  >
    资源概览
  </div>
  <el-row :gutter="16">
    <el-col
      :span="userStore.currentRole === 'ADMIN' ? 6 : 8"
      v-for="(row, index) in list"
      :key="index"
    >
      <div v-for="(obj, x) in row" :key="x">
        <CountCard
          :name="obj.name"
          :func="obj.path"
          :cloud-account-id="props.cloudAccountId"
        />
      </div>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">
.el-row:last-child {
  margin-bottom: 0;
}
.el-col-6 {
  max-width: 20% !important;
}
</style>
