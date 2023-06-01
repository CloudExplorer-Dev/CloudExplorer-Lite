<template>
  <div style="width: 100%" v-loading="jobLoading">
    <el-empty description="不存在定时任务" v-if="validModuleJobs.length == 0" />
    <el-tabs v-model="activeModule" v-if="validModuleJobs.length > 0">
      <template v-for="mod in validModuleJobs" :key="mod.name">
        <el-tab-pane :label="mod.name" :name="mod.module"></el-tab-pane>
      </template>
    </el-tabs>
    <el-main>
      <SyncBillSettingView
        v-if="billJob.length > 0 && cloudAccount"
        :job-details="billJob"
        :cloud-account="cloudAccount"
      />
      <SyncResourceSettingView
        v-if="resourceJob.length > 0 && cloudAccount"
        :job-details="resourceJob"
        :regions="regions"
        :cloud-account="cloudAccount"
      />
      <SyncComplianceSettingView
        v-if="complianceJob.length > 0 && cloudAccount"
        :job-details="complianceJob"
        :cloud-account="cloudAccount"
      />
    </el-main>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import type {
  CloudAccount,
  JobDetails,
  ModuleJob,
  Region,
} from "@/api/cloud_account/type";
import JobModuleItem from "@/componnets/job/JobModuleItem.vue";
import { ElMessage } from "element-plus";
import cloudAccountApi from "@/api/cloud_account";
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import _ from "lodash";

import SyncBillSettingView from "./view/SyncBillSettingView.vue";
import SyncComplianceSettingView from "./view/SyncComplianceSettingView.vue";
import SyncResourceSettingView from "./view/SyncResourceSettingView.vue";

const { t } = useI18n();
const router = useRouter();

const jobModule = ref<Array<InstanceType<typeof JobModuleItem>> | null>(null);
/**
 * 任务加载器
 */
const jobLoading = ref<boolean>(false);

/**
 * 选中的模块
 */
const activeModule = ref<string>();

/**
 * 当前云账号区域
 */
const regions = ref<Array<Region>>([]);
/**
 *云账号对象
 */
const cloudAccount = ref<CloudAccount>();
/**
 * 提交表单
 */
const submitForm: (isRouter: boolean) => void = (isRouter = true) => {
  if (jobModule.value) {
    return Promise.all(jobModule.value.map((item) => item.validate())).then(
      (ok) => {
        return cloudAccountApi
          .updateJobs(
            {
              cloudAccountModuleJobs: moduleJobs.value,
              cloudAccountId: props.id as string,
            },
            jobLoading
          )
          .then(() => {
            if (isRouter) {
              router.push({ name: "cloud_account_list" });
            }
            ElMessage.success(t("commons.msg.op_success", "操作成功"));
            return true;
          });
      }
    );
  }
};

const clear = () => {
  router.push({ name: "cloud_account_list" });
};

const beforeLeave = () => {
  if (jobModule.value) {
    return Promise.all(jobModule.value.map((item) => item.validate()));
  }
  return true;
};

/**
 * 模块定时任务
 */
const moduleJobs = ref<Array<ModuleJob>>([]);
/**
 *存储数据用于回滚
 */
const cloneModuleJobs = ref<Array<ModuleJob>>([]);

const props = defineProps<{
  id: string;
}>();

/**
 *回滚
 */
const rollBack = () => {
  moduleJobs.value = _.cloneDeep(cloneModuleJobs.value);
};

onMounted(() => {
  init(props.id);
});
/**
 * 支持的 任务组
 */
const supportJobGroups = [
  "CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP",
  "CLOUD_ACCOUNT_BILL_SYNC_GROUP",
  "CLOUD_COMPLIANCE_RESOURCE_SYNC_GROUP",
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

/**
 * 初始化定时任务数据
 */
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
defineExpose({
  rollBack,
  submitForm,
});
</script>
<style lang="scss" scoped>
.el-main {
  padding: 0 0 var(--el-main-padding) 0;
}
</style>
