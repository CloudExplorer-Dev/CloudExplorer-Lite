<template>
  <el-container
    direction="vertical"
    style="height: 100%; width: 100%; background-color: #f2f2f2"
    v-loading="jobLoading"
  >
    <el-main style="padding: 0">
      <el-empty
        description="不存在定时任务"
        v-if="validModuleJobs.length == 0"
      />
      <el-tabs
        v-model="activeModule"
        v-if="validModuleJobs.length > 0"
        :before-leave="beforeLeave"
      >
        <template v-for="mod in validModuleJobs" :key="mod.name">
          <el-tab-pane :label="mod.name" :name="mod.module"></el-tab-pane>
        </template>
      </el-tabs>
      <el-main style="padding: 0; height: calc(100% - 54px)">
        <SyncBillSetting
          style="height: 100%"
          v-if="billJob.length > 0 && cloudAccount"
          :job-details="billJob"
          :cloud-account="cloudAccount"
          ref="jobModule"
        />
        <SyncResourceSetting
          style="height: 100%"
          v-if="resourceJob.length > 0 && cloudAccount"
          :job-details="resourceJob"
          :metric-job-details="metricResourceJob"
          :regions="regions"
          :cloud-account="cloudAccount"
          ref="jobModule"
        />
        <SyncComplianceSetting
          style="height: 100%"
          v-if="complianceJob.length > 0 && cloudAccount"
          :job-details="complianceJob"
          :cloud-account="cloudAccount"
          ref="jobModule"
        />
      </el-main>
    </el-main>

    <el-footer
      v-if="validModuleJobs.length > 0"
      style="
        display: flex;
        flex-direction: row;
        justify-content: flex-end;
        align-items: center;
        height: 80px;
        box-shadow: 0 -1px 4px rgba(31, 35, 41, 0.1);
        background-color: var(--ce-main-content-bg-color, #fff);
      "
    >
      <el-button @click="close"> 取消 </el-button>
      <el-button type="primary" @click="save"> 保存 </el-button>
    </el-footer>
  </el-container>
</template>
<script setup lang="ts">
import SyncResourceSetting from "@/componnets/job/view/SyncResourceSetting.vue";
import SyncComplianceSetting from "@/componnets/job/view/SyncComplianceSetting.vue";
import SyncBillSetting from "@/componnets/job/view/SyncBillSetting.vue";
import { computed, onMounted, ref } from "vue";
import type { JobDetails, ModuleJob } from "@/api/cloud_account/type";
import type { CloudAccount, Region } from "@/api/cloud_account/type";
import cloudAccountApi from "@/api/cloud_account";
import _ from "lodash";
import { useRouter } from "vue-router";
import { getRoute } from "@commons/router";
import { ElMessage } from "element-plus";
import { useI18n } from "vue-i18n";

const { t } = useI18n();

const props = defineProps<{
  id: string;
}>();

const jobModule =
  ref<
    InstanceType<
      | typeof SyncBillSetting
      | typeof SyncResourceSetting
      | typeof SyncComplianceSetting
    >
  >(null);

const route = getRoute();

const jobLoading = ref<boolean>(false);

const cloudAccount = ref<CloudAccount>();

const regions = ref<Array<Region>>([]);

/**
 * 模块定时任务
 */
const moduleJobs = ref<Array<ModuleJob>>([]);

const cloneModuleJobs = ref<Array<ModuleJob>>([]);

const activeModule = ref<string>();

/**
 * 支持的 任务组
 */
const supportJobGroups = [
  "CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP",
  "CLOUD_ACCOUNT_BILL_SYNC_GROUP",
  "CLOUD_COMPLIANCE_RESOURCE_SYNC_GROUP",
  "CLOUD_RESOURCE_METRIC_SYNC_GROUP",
];

const validModuleJobs = computed<Array<ModuleJob>>(() => {
  return _.filter(moduleJobs.value, (mod) => {
    return mod.jobDetailsList.some((job) =>
      supportJobGroups.includes(job.jobGroup)
    );
  });
});

const selectedModuleJob = computed<ModuleJob | undefined>(() => {
  return _.find(
    validModuleJobs.value,
    (mod) => mod.module === activeModule.value
  );
});

/**
 * 区域定时任务
 */

const resourceJob = computed<Array<JobDetails>>(() =>
  _.filter(
    selectedModuleJob.value?.jobDetailsList,
    (j) => j.jobGroup === "CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP"
  )
);

const metricResourceJob = computed<Array<JobDetails>>(() =>
  _.filter(
    selectedModuleJob.value?.jobDetailsList,
    (j) => j.jobGroup === "CLOUD_RESOURCE_METRIC_SYNC_GROUP"
  )
);

/**
 * 账单设置定时任务
 */
const billJob = computed<Array<JobDetails>>(() =>
  _.filter(
    selectedModuleJob.value?.jobDetailsList,
    (j) => j.jobGroup === "CLOUD_ACCOUNT_BILL_SYNC_GROUP"
  )
);

const complianceJob = computed<Array<JobDetails>>(() =>
  _.filter(
    selectedModuleJob.value?.jobDetailsList,
    (j) => j.jobGroup === "CLOUD_COMPLIANCE_RESOURCE_SYNC_GROUP"
  )
);

const init = (cloud_account_id: string) => {
  jobLoading.value = true;
  // 获取所有模块的定时任务
  const p1 = cloudAccountApi.getJobs(cloud_account_id).then((ok) => {
    moduleJobs.value = ok.data.cloudAccountModuleJobs;
    cloneModuleJobs.value = _.cloneDeep(ok.data.cloudAccountModuleJobs);
    if (moduleJobs.value.length > 0) {
      // 设置第一个为选中的
      activeModule.value = moduleJobs.value[0].module;
    }
  });
  // 获取区域
  const p2 = cloudAccountApi.getRegions(cloud_account_id).then((ok) => {
    regions.value = ok.data;
  });
  // 获取云账户
  const p3 = cloudAccountApi.getCloudAccount(cloud_account_id).then((ok) => {
    cloudAccount.value = ok.data;
  });
  Promise.all([p1, p2, p3])
    .then(() => (jobLoading.value = false))
    .catch(() => {
      jobLoading.value = false;
    });
};

const router = useRouter();
function close() {
  const back = route?.history?.state?.back;
  if (back === "/cloud_account/detail/" + props.id) {
    router.push({
      name: "cloud_account_detail",
      params: { id: props.id },
    });
  } else {
    router.push({ name: "cloud_account_list" });
  }
}

function save() {
  if (jobModule.value) {
    jobModule.value.validate().then((ok: any) => {
      return cloudAccountApi
        .updateJobs(
          {
            cloudAccountModuleJobs: moduleJobs.value,
            cloudAccountId: props.id as string,
          },
          jobLoading
        )
        .then(() => {
          ElMessage.success(t("commons.msg.op_success", "操作成功"));
          close();
          return true;
        });
    });
  }
}

const beforeLeave = () => {
  if (jobModule.value) {
    return jobModule.value.validate();
  }
  return true;
};

onMounted(() => {
  init(props.id);
});
</script>
<style lang="scss"></style>
