<script lang="ts" setup>
import { computed, onMounted, ref } from "vue";
import BillViewApi from "@commons/api/bil_view";
import CeIcon from "@commons/components/ce-icon/index.vue";
import CurrencyFormat from "@commons/utils/currencyFormat";
import { useRouter } from "vue-router";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useUserStore } from "@commons/stores/modules/user";
import _ from "lodash";
import { useModuleStore } from "@commons/stores/modules/module";
import MicroAppRouterUtil from "@commons/router/MicroAppRouterUtil";

const props = withDefaults(
  defineProps<{
    needRoles?: Array<"ADMIN" | "ORGADMIN" | "USER">;
    permission?: any;
    module?: string;
  }>(),
  {
    needRoles: () => ["ADMIN", "ORGADMIN", "USER"],
    permission: "[finance-management]BILL_ViEW:READ",
    module: "finance-management",
  }
);

const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();
const userStore = useUserStore();

const show = computed<boolean>(
  () =>
    _.some(
      moduleStore.runningModules,
      (module) => module.id === props.module
    ) &&
    permissionStore.hasPermission(props.permission) &&
    _.includes(props.needRoles, userStore.currentRole)
);

const router = useRouter();

/**
 *当前月份
 */
const currentMonth = computed<string>(() => {
  return (
    new Date().getFullYear().toString() +
    "-" +
    ((new Date().getMonth() + 1).toString().length === 0
      ? "0" + (new Date().getMonth() + 1).toString()
      : (new Date().getMonth() + 1).toString())
  );
});

const currentMonthCost = ref<number>(0);
const currentYearCost = ref<number>(0);

const loading1 = ref(false);
const loading2 = ref(false);

function getMonthCost() {
  if (!show.value) {
    return;
  }
  BillViewApi.getExpenses("MONTH", currentMonth.value, loading1).then(
    (data) => {
      currentMonthCost.value = data.data;
    }
  );
}
function getYearCost() {
  if (!show.value) {
    return;
  }
  BillViewApi.getExpenses(
    "YEAR",
    new Date().getFullYear().toString(),
    loading2
  ).then((data) => {
    currentYearCost.value = data.data;
  });
}

function jump() {
  MicroAppRouterUtil.jumpToChildrenPath(
    "finance-management",
    "/finance-management/bill_view",
    router
  );
}

onMounted(() => {
  getMonthCost();
  getYearCost();
});

defineExpose({ show });
</script>
<template>
  <div class="info-card" v-if="show">
    <div class="title">
      费用概览
      <span class="right-info" @click="jump">
        更多
        <CeIcon code="icon_right_outlined" size="14px" />
      </span>
    </div>
    <div>
      <el-row :gutter="8">
        <el-col :span="12">
          <div class="base-div">
            <div class="subtitle">本月花费</div>
            <div class="value">
              {{ CurrencyFormat.format(currentMonthCost) }}
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="base-div">
            <div class="subtitle">本年花费</div>
            <div class="value">
              {{ CurrencyFormat.format(currentYearCost) }}
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
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
    margin-bottom: 10px;

    .right-info {
      font-weight: 400;
      font-size: 14px;
      line-height: 22px;
      color: #646a73;
      float: right;
      cursor: pointer;
    }
    .right-info:hover {
      color: var(--el-color-primary);
    }
  }

  .sub-main-title {
    line-height: 22px;
    font-size: 14px;
  }
  .base-div {
    padding: 8px;
    border-radius: 4px;

    .subtitle {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      font-style: normal;
      font-weight: 400;
      font-size: 14px;
      line-height: 22px;
      height: 28px;
      color: #646a73;
    }
    .value {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      font-style: normal;
      font-weight: 500;
      font-size: 20px;
      line-height: 28px;
      height: 28px;
      color: #1f2329;
      width: fit-content;
      min-width: 28px;
      vertical-align: center;
    }
  }
}
</style>
