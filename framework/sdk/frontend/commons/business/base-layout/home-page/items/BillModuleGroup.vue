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
    roles: ["ADMIN", "ORGADMIN"],
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
    roles: ["ADMIN", "ORGADMIN"],
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
  <el-row
    :gutter="20"
    type="flex"
    v-for="(row, index) in list"
    :key="index"
    style="height: 100%"
  >
    <el-col :span="12" v-for="(obj, x) in row" :key="x">
      <BaseModule
        style="height: 100%"
        :icon="obj.icon"
        :name="obj.name"
        :redirect="obj.redirect"
        :func="obj.path"
        :unit="obj.unit"
        :type="obj.type"
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
