<script lang="ts" setup>
import { computed, onMounted, ref } from "vue";
import { useModuleStore } from "@commons/stores/modules/module";
import _ from "lodash";
import { usePermissionStore } from "@commons/stores/modules/permission";

import BaseModule from "./BaseModule.vue";

const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();

interface BaseModuleInfo {
  icon: string;
  name: string;
  hasPermission: boolean;
  module: string;
  path: string;
  redirect: string;
}

const baseList: Array<BaseModuleInfo> = [
  {
    icon: "yun",
    name: "云账号",
    hasPermission: permissionStore.hasPermission(
      "[management-center]CLOUD_ACCOUNT:READ"
    ),
    module: "management-center",
    path: "/management-center/api/cloud_account/count",
    redirect: "/management-center/cloud_account/list",
  },
  {
    icon: "yonghuguanli_huaban",
    name: "用户",
    hasPermission: permissionStore.hasPermission(
      "[management-center]USER:READ"
    ),
    module: "management-center",
    path: "/management-center/api/user/count",
    redirect: "/management-center/user_tenant/user/list",
  },
  {
    icon: "zuzhijiagou",
    name: "组织",
    hasPermission: permissionStore.hasPermission(
      "[management-center]ORGANIZATION:READ"
    ),
    module: "management-center",
    path: "/management-center/api/organization/count",
    redirect: "/management-center/user_tenant/org/list",
  },
  {
    icon: "project_space",
    name: "工作空间",
    hasPermission: permissionStore.hasPermission(
      "[management-center]WORKSPACE:READ"
    ),
    module: "management-center",
    path: "/management-center/api/workspace/count",
    redirect: "/management-center/user_tenant/workspace/list",
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
        ) && obj.hasPermission
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
        :icon="obj.icon"
        :name="obj.name"
        :redirect="obj.redirect"
        :func="obj.path"
      />
    </el-col>
  </el-row>
</template>

<style scoped lang="scss"></style>
