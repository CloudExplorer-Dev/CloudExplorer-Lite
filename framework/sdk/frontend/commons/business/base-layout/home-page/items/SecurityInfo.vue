<script lang="ts" setup>
import { useUserStore } from "@commons/stores/modules/user";
import { computed, onMounted, ref } from "vue";
import _ from "lodash";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import API from "./api";
import type { SimpleMap } from "@commons/api/base/type";
import CeIcon from "@commons/components/ce-icon/index.vue";

const props = withDefaults(
  defineProps<{
    needRoles?: Array<"ADMIN">;
    permission?: any;
    module?: string;
    cardShadow?: "always" | "hover" | "never";
  }>(),
  {
    needRoles: () => ["ADMIN"],
    permission: "[security-compliance]OVERVIEW:READ",
    module: "security-compliance",
    cardShadow: "always",
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

const resourceCount = ref<SimpleMap<any>>({});
const ruleCount = ref<SimpleMap<any>>({});

const getResourceCount = () => {
  if (!show.value) {
    return;
  }
  API.getComplianceViewResourceCount({}).then((res) => {
    resourceCount.value = res.data;
  });
};
const getRuleCount = () => {
  if (!show.value) {
    return;
  }
  API.getComplianceViewRuleCount({}).then((res) => {
    ruleCount.value = res.data;
  });
};

onMounted(() => {
  getResourceCount();
  getRuleCount();
});
</script>
<template>
  <el-card class="security-info-card" v-if="show" :shadow="cardShadow">
    <div class="title">
      安全合规
      <CeIcon
        code="a-3cxjd"
        size="18px"
        color="green"
        style="margin-left: 6px"
      />
    </div>
    <el-row :gutter="20" type="flex">
      <el-col :span="12">
        <div class="subtitle">不合规规则数</div>
        <span class="number">
          {{ ruleCount.notComplianceCount }}
        </span>
        <span class="total">/{{ ruleCount.total }}</span>
      </el-col>
      <el-col :span="12">
        <div class="subtitle">不合规规则数</div>
        <span class="number">
          {{ resourceCount.notComplianceCount }}
        </span>
        <span class="total">/{{ resourceCount.total }}</span>
      </el-col>
    </el-row>
  </el-card>
</template>

<style scoped lang="scss">
.security-info-card {
  .title {
    height: 20px;
    font-weight: bold;
    font-size: 16px;
    padding-bottom: 26px;
  }
  .subtitle {
    font-weight: bold;
    font-size: 14px;
    padding-bottom: 8px;
  }
  .number {
    font-weight: bold;
    font-size: 20px;
  }
  .total {
    font-weight: bold;
    font-size: 16px;
    color: var(--el-text-color-secondary);
  }
}
</style>
