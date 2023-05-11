<script lang="ts" setup>
import { computed, onMounted, ref, watch } from "vue";
import _ from "lodash";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useUserStore } from "@commons/stores/modules/user";
import type {
  OptimizeSuggest,
  PageOptimizeBaseRequest,
  OptimizeBaseRequest,
} from "@commons/api/optimize/type";
import OptimizeViewApi from "@commons/api/optimize";
import ServerOptimizationCard from "@commons/business/base-layout/home-page/items/operation/optimize/ServerOptimizationCard.vue";
import OptimizeStrategyDialog from "@commons/business/base-layout/home-page/items/operation/optimize/OptimizeStrategyDialog.vue";
import { useRouter } from "vue-router";
import MicroAppRouterUtil from "@commons/router/MicroAppRouterUtil";
const socRef = ref<Array<InstanceType<typeof ServerOptimizationCard>>>([]);

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
    checkId?: number;
    checkable?: boolean;
    showSettingIcon?: boolean;
    tableLoading?: boolean;
    tableSearchParams?: OptimizeBaseRequest;
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
    showSettingIcon: false,
    tableLoading: false,
  }
);

const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();
const userStore = useUserStore();

const router = useRouter();

//获取优化建议
const apiOptimizeSuggestList = ref<Array<OptimizeSuggest>>();

const getOptimizeSuggests = () => {
  OptimizeViewApi.getOptimizeSuggestList().then((res) => {
    apiOptimizeSuggestList.value = res.data;
  });
};
const optimizeSuggestList = computed<Array<OptimizeSuggest>>(() => {
  return _.map(apiOptimizeSuggestList.value, (s) => {
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
  getOptimizeSuggests();
});

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
function changeStrategy(o: OptimizeSuggest) {
  emit("update:checkId", o.index);
  emit("change");
  socRef.value[o.index - 1]?.getOptimizeServerList();
}
const modifyConditionRef = ref();
const currentOptimizeSuggest = ref<OptimizeSuggest>();
const showOptimizeStrategyDialog = (o: OptimizeSuggest) => {
  currentOptimizeSuggest.value = o;
  modifyConditionRef.value.dialogVisible = true;
};
const checkedId = computed({
  get() {
    return props.checkId;
  },
  set(value) {
    if (checkedId.value !== value) {
      emit("update:checkId", value);
      emit("change");
    }
  },
});

function getCheckedSearchParams(
  index: number,
  tableSearchParams: any
): PageOptimizeBaseRequest | undefined {
  return _.assign(
    _.clone(_.find(optimizeSuggestList.value, (s) => s.index === index)),
    tableSearchParams
  );
}

defineExpose({
  getCheckedSearchParams,
});

function checkDiv(req: OptimizeSuggest) {
  if (props.tableLoading) {
    return;
  }
  if (!props.checkable) {
    goTo(req.index, req.optimizeSuggestCode);
    return;
  }
  checkedId.value = req.index;
}

const showSettingIcon = computed<boolean>(() => props.showSettingIcon);

function goTo(id: number, code: string) {
  const queryParam: any = { checked: id };
  if (props.cloudAccountId && props.cloudAccountId !== "all") {
    queryParam.accountIds = encodeURI(JSON.stringify([props.cloudAccountId]));
  }
  if (import.meta.env.VITE_APP_NAME === "operation-analysis") {
    router.push({
      name: "resource_optimization_list",
      query: queryParam,
    });
  } else {
    MicroAppRouterUtil.jumpToChildrenPath(
      "operation-analysis",
      "/operation-analysis/server_optimization/list?checked=" +
        queryParam.checked +
        (code ? "&optimizeSuggestCode=" + code : "") +
        (queryParam.accountIds ? "&accountIds=" + queryParam.accountIds : ""),
      router
    );
  }
}
</script>
<template>
  <div
    class="info-card"
    :class="{ 'no-padding': noPadding }"
    v-if="show"
    v-bind="$attrs"
  >
    <div class="title" v-if="!noTitle">{{ title }}</div>

    <el-row :gutter="16" :class="{ 'div-content': !noTitle }">
      <el-col
        :span="6"
        v-for="o in optimizeSuggestList"
        :key="o.optimizeSuggestCode"
      >
        <ServerOptimizationCard
          ref="socRef"
          :optimize-suggest="o"
          :table-search-params="props.tableSearchParams"
          :show="show"
          :show-setting-icon="showSettingIcon"
          :checked="checkedId === o.index"
          @showStrategyDialog="showOptimizeStrategyDialog"
          @click="checkDiv(o)"
        />
      </el-col>
    </el-row>
  </div>
  <OptimizeStrategyDialog
    ref="modifyConditionRef"
    :optimize-suggest="currentOptimizeSuggest"
    @changeStrategy="changeStrategy"
    style="min-width: 600px"
  />
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
