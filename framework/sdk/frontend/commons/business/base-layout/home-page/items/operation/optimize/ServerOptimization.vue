<template>
  <div
    class="info-card"
    :class="{ 'no-padding': noPadding, 'margin-bottom': !noPadding }"
    v-if="show"
    v-bind="$attrs"
  >
    <div class="title" v-if="!noTitle">{{ title }}</div>

    <el-row :gutter="16" :class="{ 'div-content': !noTitle }">
      <el-col :span="6" v-for="o in optimizationStrategyList" :key="o.id">
        <ServerOptimizationCard
          ref="serverOptimizationCardRef"
          :optimization-strategy="o"
          :table-search-params="props.tableSearchParams"
          :show="show"
          :checked="checkedId === o.id"
          :show-setting-icon="showSettingIcon"
          @showStrategyDialog="showOptimizeStrategyDialog"
          @click="checkDiv(o)"
        />
      </el-col>
    </el-row>
  </div>
  <CreateOrEdit
    ref="createEditFormRef"
    @confirm="editConfirmed"
    :id="optimizationStrategyId"
  />
</template>

<script lang="ts" setup>
import { computed, nextTick, onMounted, ref, watch } from "vue";
import _ from "lodash";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useUserStore } from "@commons/stores/modules/user";
import type {} from "@commons/api/optimize/type";
import OptimizationStrategyViewApi from "@commons/api/optimize";
import ServerOptimizationCard from "@commons/business/base-layout/home-page/items/operation/optimize/ServerOptimizationCard.vue";
import { useRouter } from "vue-router";
import type {
  OptimizationStrategy,
  ListOptimizationStrategyRequest,
} from "@commons/api/optimize/type";
import CreateOrEdit from "./CreateOrEdit.vue";
import MicroAppRouterUtil from "@commons/router/MicroAppRouterUtil";
const props = withDefaults(
  defineProps<{
    needRoles?: Array<"ADMIN" | "ORGADMIN" | "USER">;
    permission?: any;
    module?: string;
    title?: string;
    cloudAccountId?: string;
    cloudAccountIds?: Array<string>;
    noTitle?: boolean;
    noPadding?: boolean;
    checkId?: string;
    checkable?: boolean;
    showSettingIcon?: boolean;
    tableLoading?: boolean;
    tableSearchParams?: any;
  }>(),
  {
    needRoles: () => ["ADMIN", "ORGADMIN", "USER"],
    permission: [
      "[operation-analysis]OPTIMIZATION_STRATEGY:READ",
      "[operation-analysis]SERVER_OPTIMIZATION:READ",
      "[operation-analysis]OVERVIEW:READ",
    ],
    module: "operation-analysis",
    title: "云主机优化建议",
    noTitle: false,
    noPadding: false,
    checkable: false,
    showSettingIcon: false,
    tableLoading: false,
  }
);
const serverOptimizationCardRef = ref<
  Array<InstanceType<typeof ServerOptimizationCard>>
>([]);
const emit = defineEmits(["update:checkId", "change"]);
const createEditFormRef = ref<InstanceType<typeof CreateOrEdit>>();
const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();
const userStore = useUserStore();
const router = useRouter();
//获取优化建议
const apiOptimizationStrategyList = ref<Array<OptimizationStrategy>>();
const optimizationStrategyId = ref<string>();

const checkedId = computed({
  get() {
    return props.checkId;
  },
  set(value) {
    if (checkedId.value !== value || value === "") {
      emit(
        "update:checkId",
        value === "" ? optimizationStrategyList.value?.[0]?.id : value
      );
      emit("change");
    }
  },
});

function checkDiv(req: OptimizationStrategy) {
  if (props.tableLoading) {
    return;
  }
  if (!props.checkable) {
    goTo(req.id);
    return;
  }
  checkedId.value = req.id;
}
function goTo(id: string) {
  const queryParam: any = { checked: id };
  if (props.cloudAccountId && props.cloudAccountId !== "all") {
    queryParam.accountIds = encodeURI(JSON.stringify([props.cloudAccountId]));
  }
  if (import.meta.env.VITE_APP_NAME === "operation-analysis") {
    router.push({
      name: "server_optimization",
      query: queryParam,
    });
  } else {
    MicroAppRouterUtil.jumpToChildrenPath(
      "operation-analysis",
      "/operation-analysis/resource_optimization/server_optimization/list?checked=" +
        queryParam.checked +
        (queryParam.accountIds ? "&accountIds=" + queryParam.accountIds : ""),
      router
    );
  }
}
/**
 * 优化策略
 */
const getOptimizationStrategyList = () => {
  OptimizationStrategyViewApi.listOptimizationStrategy("VIRTUAL_MACHINE").then(
    (res) => {
      apiOptimizationStrategyList.value = res.data;
      if (!props.checkId) {
        checkedId.value = "";
      }
    }
  );
};
/**
 * 优化策略对象转换，添加云账号
 */
const optimizationStrategyList = computed<Array<OptimizationStrategy>>(() => {
  return _.map(apiOptimizationStrategyList.value, (s) => {
    const v = _.clone(s);
    const accountIds = [];
    if (!(props.cloudAccountId === "all" || !props.cloudAccountId)) {
      accountIds.push(props.cloudAccountId);
    }
    if (props.cloudAccountIds && props.cloudAccountIds.length > 0) {
      props.cloudAccountIds.forEach((value) => {
        accountIds.push(value);
      });
    }
    _.set(v, "accountIds", accountIds.length > 0 ? accountIds : []);
    return v;
  });
});
onMounted(() => {
  getOptimizationStrategyList();
});

function editConfirmed() {
  emit("update:checkId", optimizationStrategyId.value);
  emit("change");
  nextTick(() => {
    if (serverOptimizationCardRef.value) {
      const scoRef = _.find(
        serverOptimizationCardRef.value,
        (item) =>
          item.getCurrentOptimizationStrategyId() ===
          optimizationStrategyId.value
      );
      scoRef?.getOptimizeServerList();
    }
  });
}

const show = computed<boolean>(
  () =>
    _.some(
      moduleStore.runningModules,
      (module) => module.id === props.module
    ) &&
    permissionStore.hasPermission(props.permission) &&
    _.includes(props.needRoles, userStore.currentRole)
);

const showSettingIcon = computed<boolean>(() => props.showSettingIcon);

const showOptimizeStrategyDialog = (o: OptimizationStrategy) => {
  optimizationStrategyId.value = o.id;
  createEditFormRef.value?.open(o.id);
};
function getCheckedSearchParams(
  id: string,
  tableSearchParams: any
): ListOptimizationStrategyRequest | undefined {
  return _.assign({ optimizationStrategyId: id }, tableSearchParams);
}
function changeCard() {
  nextTick(() => {
    if (serverOptimizationCardRef.value) {
      const scoRef = _.find(
        serverOptimizationCardRef.value,
        (item) => item.getCurrentOptimizationStrategyId() === checkedId.value
      );
      scoRef?.getOptimizeServerList();
    }
  });
}
function getCheckedOptimizationStrategy() {
  return _.find(optimizationStrategyList.value, { id: checkedId.value });
}
defineExpose({
  getCheckedSearchParams,
  changeCard,
  getCheckedOptimizationStrategy,
});
</script>

<style scoped lang="scss">
.info-card {
  background: #ffffff;
  border-radius: 4px;
  padding: 20px;
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
.info-card.margin-bottom {
  margin-bottom: 20px;
}
</style>
