<script lang="ts" setup>
import { computed, onMounted, ref } from "vue";
import _ from "lodash";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useUserStore } from "@commons/stores/modules/user";
import {
  baseOptimizeSuggests,
  type OptimizeSuggest,
  paramOptimizationRequestMap,
} from "@commons/api/resource_optimization/type";
import { ResourceAnalysisRequest } from "@commons/api/resource_spread_view/type";
import ResourceOptimizationViewApi from "@commons/api/resource_optimization";
import type { SimpleMap } from "@commons/api/base/type";

const props = withDefaults(
  defineProps<{
    needRoles?: Array<"ADMIN" | "ORGADMIN" | "USER">;
    permission?: any;
    module?: string;
    title?: string;
    cardShadow?: "always" | "hover" | "never";
  }>(),
  {
    needRoles: () => ["ADMIN"],
    permission: [
      "[operation-analytics]BASE_RESOURCE_ANALYSIS:READ",
      "[operation-analytics]OVERVIEW:READ",
    ],
    module: "operation-analytics",
    title: "优化建议",
    cardShadow: "always",
  }
);

const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();
const userStore = useUserStore();

//优化建议
const optimizeParam = ref<any>();

const params = ref<ResourceAnalysisRequest | undefined>();

const optimizeSuggests = ref<Array<OptimizeSuggest>>(
  _.clone(baseOptimizeSuggests)
);

const loading = ref(false);

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

const getOptimizeSuggests = () => {
  _.forEach(optimizeSuggests.value, (value, index: number) => {
    getSearchParams(value);
    _.merge(params, optimizeParam.value);
    ResourceOptimizationViewApi.listServer(
      {
        currentPage: 1,
        pageSize: 10,
        ...params,
      },
      loading
    ).then((res) => {
      value.value = res.data.total;
      value.data = res.data.records;
    });
  });
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

onMounted(() => {
  if (!show.value) {
    return;
  }
  _.set(params, "accountIds", []);
  getOptimizeSuggests();
});
</script>
<template>
  <el-card class="server-optimization" v-if="show" :shadow="cardShadow">
    <div class="echart-title">
      <div class="echart-title-left">{{ title }}</div>
    </div>
    <el-row :gutter="12" v-loading="loading">
      <el-col :span="6" v-for="o in optimizeSuggests" :key="o.code">
        <el-card :body-style="{ padding: '0px' }" shadow="hover">
          <div class="boxCounter">
            <div class="CenterTheBox">
              <span style="font-size: 24px">
                {{ o.value }}
              </span>
              台
            </div>
            <div class="BottomTheBox" :style="{ 'background-color': o.color }">
              <span>{{ o.name }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </el-card>
</template>

<style scoped lang="scss">
.server-optimization {
  height: 100%;

  .echart-title {
    height: 20px;
    font-weight: bold;
    font-size: 16px;
    padding-bottom: 26px;
  }
  .echart-title-left {
    float: left;
  }

  .boxCounter {
    height: 100px;
    overflow: hidden;
    position: relative;
    //按钮
    button {
      position: absolute;
      display: inline-block;
      color: #f7ba2a;
      width: 100%;
      bottom: 5px;
      right: 0;
      text-align: center;
    }
    //角标主要样式
    .AngleOfTheBox {
      position: absolute;
      padding: 0 5px;
      display: flex;
      align-items: center;
      width: 15%;
      height: 26px;
      color: #fff;
      background-color: #ff9899;
      //文字
      span {
        position: absolute;
        display: inline-block;
        color: #fff;
        width: 100%;
        bottom: 5px;
        left: 0;
        text-align: center;
      }
    }
    .AngleOfTheBox::after {
      content: "";
      position: absolute;
      left: 100%;
      top: 0;
      border-color: transparent transparent transparent #ff9899;
      border-width: 13px 0 13px 13px;
      border-style: solid;
    }
    .BtnOfTheBox {
      cursor: pointer;
      position: absolute;
      padding: 0 5px;
      display: flex;
      align-items: center;
      width: 20px;
      height: 26px;
      right: 0;
    }
    .CenterTheBox {
      border-radius: 1px;
      width: 100%;
      height: 30px;
      position: absolute;
      bottom: 40px;
      text-align: center;
    }
    .BottomTheBox {
      border-radius: 1px;
      width: 100%;
      height: 40px;
      position: absolute;
      bottom: 0px;
      text-align: center;
      line-height: 40px;
    }
  }
}
</style>
