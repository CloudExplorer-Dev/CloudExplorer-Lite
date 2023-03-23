<script lang="ts" setup>
import { computed, ref } from "vue";
import _ from "lodash";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useUserStore } from "@commons/stores/modules/user";
import {
  baseOptimizeSuggests,
  type ListOptimizationRequest,
  type OptimizeSuggest,
  paramOptimizationRequestMap,
} from "@commons/api/resource_optimization/type";
import ServerOptimizationCard from "@commons/business/base-layout/home-page/items/operation/ServerOptimizationCard.vue";

const props = withDefaults(
  defineProps<{
    needRoles?: Array<"ADMIN" | "ORGADMIN" | "USER">;
    permission?: any;
    module?: string;
    title?: string;
    cloudAccountId?: string;
  }>(),
  {
    needRoles: () => ["ADMIN", "ORGADMIN", "USER"],
    permission: [
      "[operation-analysis]SERVER_OPTIMIZATION:READ",
      "[operation-analysis]OVERVIEW:READ",
    ],
    module: "operation-analysis",
    title: "云主机优化建议",
  }
);

const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();
const userStore = useUserStore();

//优化建议
const optimizeParam = ref<any>();

const optimizeSuggests = computed<Array<ListOptimizationRequest>>(() => {
  return _.map(baseOptimizeSuggests, (s) => {
    const v = _.clone(s);
    getSearchParams(v);
    _.merge(v, optimizeParam.value);
    _.set(
      v,
      "accountIds",
      props.cloudAccountId && props.cloudAccountId !== "all"
        ? [props.cloudAccountId]
        : []
    );
    return { ...v, currentPage: 1, pageSize: 1 };
  });
});

const getSearchParams = (o: OptimizeSuggest) => {
  if (localStorage.getItem(o.code)) {
    const str = localStorage.getItem(o.code);
    if (str) {
      try {
        optimizeParam.value = JSON.parse(str);
      } catch (e) {
        console.error("get default dialogFormData error", e);
        optimizeParam.value = paramOptimizationRequestMap.get(o.code);
      }
    }
  } else {
    optimizeParam.value = paramOptimizationRequestMap.get(o.code);
  }
};

const show = computed<boolean>(
  () =>
    _.some(
      moduleStore.runningModules,
      (module) => module.id === props.module
    ) &&
    permissionStore.hasPermission(props.permission) &&
    _.includes(props.needRoles, userStore.currentRole)
);
</script>
<template>
  <div class="info-card" v-if="show">
    <div class="title">{{ title }}</div>

    <el-row :gutter="16" class="div-content">
      <el-col :span="6" v-for="o in optimizeSuggests" :key="o.code">
        <ServerOptimizationCard :req="o" :show="show" />
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">
.info-card {
  background: #ffffff;
  border-radius: 4px;
  padding: 24px;
  overflow: hidden;

  .title {
    font-style: normal;
    font-weight: 500;
    font-size: 16px;
    line-height: 24px;
  }
  .div-content {
    margin-top: 16px;

    .div-card {
      cursor: pointer;
      background: #ffffff;
      border: 1px solid #dee0e3;
      border-radius: 4px;
      padding: 16px;

      .text {
        font-style: normal;
        font-weight: 400;
        font-size: 14px;
        line-height: 22px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      .main {
        font-style: normal;
        font-weight: 500;
        font-size: 28px;
        line-height: 36px;
      }
    }

    .div-card:hover {
      box-shadow: 0 6px 24px rgba(31, 35, 41, 0.08);
    }
  }
}
</style>
