<script lang="ts" setup>
import { computed, onMounted, ref } from "vue";
import { useModuleStore } from "@commons/stores/modules/module";
import _ from "lodash";
import { usePermissionStore } from "@commons/stores/modules/permission";

import BaseModule from "./BaseModule.vue";
import { useUserStore } from "@commons/stores/modules/user";
import type { BaseModuleInfo } from "@commons/business/base-layout/home-page/items/BaseModuleType";

const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();
const userStore = useUserStore();

/**
 *当前月份
 */
const currentMonth =
  new Date().getFullYear().toString() +
  "-" +
  ((new Date().getMonth() + 1).toString().length === 0
    ? "0" + (new Date().getMonth() + 1).toString()
    : (new Date().getMonth() + 1).toString());

const baseList: Array<BaseModuleInfo> = [
  {
    icon: "yun",
    name: "云账号",
    hasPermission: permissionStore.hasPermission(
      "[management-center]CLOUD_ACCOUNT:READ"
    ),
    module: "management-center",
    path: "/management-center/api/cloud_account/count",
    redirect: "/management-center#/cloud_account/list",
    roles: ["ADMIN"],
  },
  {
    icon: "yonghuguanli_huaban",
    name: "用户",
    hasPermission: permissionStore.hasPermission(
      "[management-center]USER:READ"
    ),
    module: "management-center",
    path: "/management-center/api/user/count",
    redirect: "/management-center#/user_tenant/user/list",
    roles: ["ADMIN", "ORGADMIN"],
  },
  {
    icon: "zuzhijiagou1",
    name: "组织",
    hasPermission: permissionStore.hasPermission(
      "[management-center]ORGANIZATION:READ"
    ),
    module: "management-center",
    path: "/management-center/api/organization/count",
    redirect: "/management-center#/user_tenant/org/list",
    roles: ["ADMIN", "ORGADMIN"],
  },
  {
    icon: "project_space",
    name: "工作空间",
    hasPermission: permissionStore.hasPermission(
      "[management-center]WORKSPACE:READ"
    ),
    module: "management-center",
    path: "/management-center/api/workspace/count",
    redirect: "/management-center#/user_tenant/workspace/list",
    roles: ["ADMIN", "ORGADMIN"],
  },
  {
    icon: "xuniyunzhuji",
    name: "云主机",
    hasPermission: permissionStore.hasPermission(
      "[vm-service]CLOUD_SERVER:READ"
    ),
    module: "vm-service",
    path: "/vm-service/api/server/count",
    redirect: "/vm-service#/vm_cloud_server/list",
    roles: ["ADMIN", "ORGADMIN", "USER"],
  },
  {
    icon: "yuncunchu",
    name: "云磁盘",
    hasPermission: permissionStore.hasPermission("[vm-service]CLOUD_DISK:READ"),
    module: "vm-service",
    path: "/vm-service/api/disk/count",
    redirect: "/vm-service#/vm_cloud_disk/list",
    roles: ["ADMIN", "USER"],
  },
  {
    icon: "xuniji1",
    name: "宿主机",
    hasPermission: permissionStore.hasPermission([
      "[operation-analysis]BASE_RESOURCE_ANALYSIS:READ",
      "[operation-analysis]OVERVIEW:READ",
    ]),
    module: "operation-analysis",
    path: "/operation-analysis/api/base_resource_analysis/host/count",
    redirect:
      "/operation-analysis#/resource_analysis/base_resource_analysis/list",
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
    path: "/operation-analysis/api/base_resource_analysis/datastore/count",
    redirect:
      "/operation-analysis#/resource_analysis/base_resource_analysis/list",
    roles: ["ADMIN"],
  },
  {
    icon: "wj-zd",
    name: "当月花费",
    hasPermission: permissionStore.hasPermission(
      "[finance-management]BILL_ViEW:READ"
    ),
    module: "finance-management",
    path: "/finance-management/api/bill_view/expenses/MONTH/" + currentMonth,
    redirect: "/finance-management#/bill_view",
    /*unit: "元",*/
    type: "currency",
    roles: ["USER"],
  },
  {
    icon: "zichanzonglan",
    name: "年总花费",
    hasPermission: permissionStore.hasPermission(
      "[finance-management]BILL_ViEW:READ"
    ),
    module: "finance-management",
    path:
      "/finance-management/api/bill_view/expenses/YEAR/" +
      new Date().getFullYear().toString(),
    redirect: "/finance-management#/bill_view",
    /*unit: "元",*/
    type: "currency",
    roles: ["USER"],
  },
];

const list = computed(() => {
  const result: Array<Array<BaseModuleInfo>> = [];
  _.forEach(
    _.filter(
      baseList,
      (obj) =>
        _.some(
          moduleStore.runningModules,
          (module) => module.id === obj.module
        ) &&
        obj.hasPermission &&
        _.includes(obj.roles, userStore.currentRole)
    ),
    (obj, index) => {
      const i = Math.floor(index / 4);
      if (result.length <= i) {
        result.push([]);
      }
      result[i].push(obj);
    }
  );
  return result;
});
</script>
<template>
  <el-row :gutter="20" type="flex" v-for="(row, index) in list" :key="index">
    <el-col :span="6" v-for="(obj, x) in row" :key="x">
      <BaseModule
        style="height: 100%"
        :icon="obj.icon"
        :name="obj.name"
        :redirect="obj.redirect"
        :func="obj.path"
        :type="obj.type"
        :unit="obj.unit"
      />
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">
.el-row {
  margin-bottom: 20px;
}
.el-row:last-child {
  margin-bottom: 0;
}
</style>
