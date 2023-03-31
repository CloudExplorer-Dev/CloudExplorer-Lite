<script lang="ts" setup>
import { useUserStore } from "@commons/stores/modules/user";
import { computed, onMounted, ref, watch } from "vue";
import _ from "lodash";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import API from "@commons/api/compliance-view/index";
import type { SimpleMap } from "@commons/api/base/type";
import MicroAppRouterUtil from "@commons/router/MicroAppRouterUtil";
import { useRouter } from "vue-router";

const props = withDefaults(
  defineProps<{
    needRoles?: Array<"ADMIN">;
    permission?: any;
    module?: string;
  }>(),
  {
    needRoles: () => ["ADMIN"],
    permission: "[security-compliance]OVERVIEW:READ",
    module: "security-compliance",
  }
);

const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();
const userStore = useUserStore();

const router = useRouter();

const show = computed<boolean>(
  () =>
    _.some(
      moduleStore.runningModules,
      (module) => module.id === props.module
    ) &&
    permissionStore.hasPermission(props.permission) &&
    _.includes(props.needRoles, userStore.currentRole)
);

const resourceCount = ref<SimpleMap<any>>({
  notComplianceCount: 0,
  total: 0,
  complianceCount: 0,
});
const ruleCount = ref<
  SimpleMap<{
    notComplianceCount: number;
    total: number;
    complianceCount: number;
  }>
>({});

const resourceCountLoading = ref<boolean>(false);
const ruleCountLoading = ref<boolean>(false);

const getResourceCount = (cloudAccountId?: string) => {
  if (!show.value) {
    return;
  }
  API.getComplianceViewResourceCount(
    { cloudAccountId },
    resourceCountLoading
  ).then((res) => {
    resourceCount.value = res.data;
  });
};
const getRuleCount = (cloudAccountId?: string) => {
  if (!show.value) {
    return;
  }
  API.getComplianceViewRuleCount({ cloudAccountId }, ruleCountLoading).then(
    (res) => {
      ruleCount.value = res.data;
    }
  );
};

function jump(level: number) {
  const p = `scan?scanStatus=NOT_COMPLIANCE&riskLevel=${level}`;
  if (import.meta.env.VITE_APP_NAME === "base") {
    MicroAppRouterUtil.jumpToChildrenPath(
      "security-compliance",
      `/security-compliance/${p}`,
      router
    );
  } else {
    router.push(p);
  }
}

onMounted(() => {
  getResourceCount();
  getRuleCount();
});
const refresh = (cloudAccountId?: string) => {
  getResourceCount(cloudAccountId);
  getRuleCount(cloudAccountId);
};
defineExpose({ refresh });
</script>
<template>
  <el-row :gutter="16" v-if="show" type="flex">
    <el-col :span="12">
      <div class="info-card" v-loading="resourceCountLoading">
        <div class="title">不合规资源</div>
        <div class="resource-div">
          <div class="subtitle">
            不合规数量
            <span class="main-count">{{
              resourceCount.notComplianceCount
            }}</span>
            /
            <span>{{ resourceCount.total }}</span>
          </div>
          <div class="value bar">
            <el-tooltip effect="light">
              <template #content>
                <div class="tip-title">
                  <div class="tip-title-icon"></div>
                  不合规数量
                </div>
                <div>数量（个）：{{ resourceCount.notComplianceCount }}</div>
                <div>
                  占比：{{
                    _.round(
                      (resourceCount.notComplianceCount / resourceCount.total) *
                        100,
                      2
                    )
                  }}
                  %
                </div>
              </template>
              <div
                class="not-compliance"
                :style="{
                  width:
                    _.round(
                      (resourceCount.notComplianceCount / resourceCount.total) *
                        100,
                      2
                    ) + '%',
                }"
              ></div>
            </el-tooltip>
            <el-tooltip effect="light">
              <template #content>
                <div class="tip-title">
                  <div class="tip-title-icon compliance"></div>
                  合规数量
                </div>
                <div>数量（个）：{{ resourceCount.complianceCount }}</div>
                <div>
                  占比：{{
                    _.round(
                      (resourceCount.complianceCount / resourceCount.total) *
                        100,
                      2
                    )
                  }}
                  %
                </div>
              </template>
              <div class="compliance"></div>
            </el-tooltip>
          </div>
        </div>
      </div>
    </el-col>
    <el-col :span="12">
      <div class="info-card" v-loading="ruleCountLoading">
        <div class="title">不合规规则</div>
        <div>
          <el-row :gutter="8">
            <el-col :span="8">
              <div class="base-div" @click="jump(1)">
                <div class="subtitle">高风险</div>
                <div class="value value-high">
                  {{
                    ruleCount["HIGH"] ? ruleCount["HIGH"].notComplianceCount : 0
                  }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="base-div" @click="jump(0)">
                <div class="subtitle">中风险</div>
                <div class="value value-middle">
                  {{
                    ruleCount["MIDDLE"]
                      ? ruleCount["MIDDLE"].notComplianceCount
                      : 0
                  }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="base-div" @click="jump(-1)">
                <div class="subtitle">低风险</div>
                <div class="value">
                  {{
                    ruleCount["LOW"] ? ruleCount["LOW"].notComplianceCount : 0
                  }}
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-col>
  </el-row>
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

  .resource-div {
    padding-top: 8px;
    padding-bottom: 8px;
  }
  .base-div {
    cursor: pointer;
    padding: 8px;
    border-radius: 4px;
  }
  .base-div:hover {
    background: rgba(31, 35, 41, 0.1);
  }

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

    .main-count {
      font-weight: 500;
      font-size: 20px;
      line-height: 28px;
      margin-left: 10px;
      color: #f54a45;
    }
  }

  .value {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    font-style: normal;
    font-weight: 500;
    font-size: 28px;
    line-height: 36px;
    height: 36px;
    color: #1f2329;
    width: fit-content;
    min-width: 28px;
  }
  .value-high {
    color: #f54a45;
  }
  .value-middle {
    color: #ff8800;
  }

  .bar {
    display: flex;
    width: 100%;
    flex-direction: row;
    flex-wrap: nowrap;
    align-items: flex-end;

    .compliance {
      height: 20px;
      margin-left: 1px;
      flex: 1;
      background-color: #dee0e3;
      cursor: pointer;
      border-bottom-right-radius: 2px;
      border-top-right-radius: 2px;
    }
    .not-compliance {
      height: 20px;
      margin-right: 1px;
      background-color: #f76964;
      cursor: pointer;

      border-bottom-left-radius: 2px;
      border-top-left-radius: 2px;
    }
  }
}

.tip-title {
  display: inline-block;
  line-height: 22px;
  height: 22px;

  .tip-title-icon {
    display: inline-block;
    height: 8px;
    width: 8px;
    border-radius: 4px;
    background-color: #f76964;

    line-height: 22px;
    vertical-align: middle;

    margin-top: auto;
    margin-bottom: auto;
    margin-right: 4px;
  }
  .compliance {
    background-color: #dee0e3;
  }
}
</style>
