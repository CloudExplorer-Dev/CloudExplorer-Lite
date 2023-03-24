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
    noTitle?: boolean;
    noPadding?: boolean;
    checkId?: number;
    checkable?: boolean;
    req?: ListOptimizationRequest;
  }>(),
  {
    needRoles: () => ["ADMIN", "ORGADMIN", "USER"],
    permission: [
      "[operation-analysis]SERVER_OPTIMIZATION:READ",
      "[operation-analysis]OVERVIEW:READ",
    ],
    module: "operation-analysis",
    title: "云主机优化建议",
    noTitle: false,
    noPadding: false,
    checkable: false,
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
    if (props.req) {
      _.merge(v, props.req);
    }
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

const emit = defineEmits(["update:checkId", "change"]);

const checkedId = computed({
  get() {
    return props.checkId;
  },
  set(value) {
    if (checkedId.value !== value) {
      emit("update:checkId", value);
      emit("change", value);
    }
  },
});

function checkDiv(req: ListOptimizationRequest) {
  if (!props.checkable) {
    return;
  }
  checkedId.value = req.id;
}

function getCheckedSearchParams(
  id: number
): ListOptimizationRequest | undefined {
  return _.find(optimizeSuggests.value, (s) => s.id === id);
}

defineExpose({
  getCheckedSearchParams,
});
</script>
<template>
  <div class="info-card" :class="{ 'no-padding': noPadding }" v-if="show">
    <div class="title" v-if="!noTitle">{{ title }}</div>

    <el-row :gutter="16" :class="{ 'div-content': !noTitle }">
      <el-col :span="6" v-for="o in optimizeSuggests" :key="o.code">
        <ServerOptimizationCard
          :req="o"
          :show="show"
          :checked="checkedId === o.id"
          @click="checkDiv(o)"
        />
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
  }
}

.info-card.no-padding {
  padding: 0;
}
</style>
