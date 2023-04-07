<script lang="ts" setup>
import { computed, ref } from "vue";
import _ from "lodash";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useUserStore } from "@commons/stores/modules/user";
import ConditionDialog from "./ConditionDialog.vue";
import {
  baseOptimizeSuggests,
  type ListOptimizationRequest,
  type OptimizeSuggest,
  paramOptimizationRequestMap,
} from "@commons/api/resource_optimization/type";
import ServerOptimizationCard from "@commons/business/base-layout/home-page/items/operation/ServerOptimizationCard.vue";
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
    showSettingIcon: false,
    tableLoading: false,
  }
);

const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();
const userStore = useUserStore();

const router = useRouter();

//优化建议
const optimizeParam = ref<any>();

const simpleOptimizeSuggests = computed<Array<ListOptimizationRequest>>(() => {
  return _.map(baseOptimizeSuggests, (s) => {
    const v = _.clone(s);
    getSearchParams(v);
    _.assign(v, optimizeParam.value);
    return { ...v, currentPage: 1, pageSize: 1 };
  });
});

const optimizeSuggests = computed<Array<ListOptimizationRequest>>(() => {
  return _.map(simpleOptimizeSuggests.value, (s) => {
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

    if (props.req) {
      _.assign(v, props.req);
    }
    return v;
  });
});

const modifyConditionRef = ref();
const optimizationSearchReq = ref<ListOptimizationRequest>();
const showConditionDialog = (req: ListOptimizationRequest) => {
  optimizationSearchReq.value = req;
  modifyConditionRef.value.dialogVisible = true;
};

const getSearchParams = (o: OptimizeSuggest) => {
  //从数据库中获取数据
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

function changeParam(req: any) {
  getSearchParams(req);
  emit("update:checkId", checkedId.value);
  emit("change", checkedId.value);
  if (checkedId.value !== undefined) {
    socRef.value[checkedId.value - 1]?.getOptimizeSuggests();
  }
}

function checkDiv(req: ListOptimizationRequest) {
  if (props.tableLoading) {
    console.log("不要着急...");
    return;
  }
  if (!props.checkable) {
    goTo(req.id);
    return;
  }
  checkedId.value = req.id;
}

const showSettingIcon = computed<boolean>(() => props.showSettingIcon);

function getCheckedSearchParams(
  id: number,
  req: ListOptimizationRequest
): ListOptimizationRequest | undefined {
  return _.assign(
    _.clone(_.find(simpleOptimizeSuggests.value, (s) => s.id === id)),
    req
  );
}

function goTo(id: number) {
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
        (queryParam.accountIds ? "&accountIds=" + queryParam.accountIds : ""),
      router
    );
  }
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
          ref="socRef"
          :req="o"
          :show="show"
          :show-setting-icon="showSettingIcon"
          :checked="checkedId === o.id"
          @showConditionDialog="showConditionDialog"
          @click="checkDiv(o)"
        />
      </el-col>
    </el-row>

    <ConditionDialog
      ref="modifyConditionRef"
      :optimization-search-req="optimizationSearchReq"
      @changeParam="changeParam"
      @showConditionDialog="showConditionDialog"
      style="min-width: 600px"
    />
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
