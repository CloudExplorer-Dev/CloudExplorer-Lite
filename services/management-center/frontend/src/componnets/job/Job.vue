<template>
  <el-tabs
    v-loading="jobLoading"
    v-model="activeModuleName"
    :before-leave="beforeLeave"
    class="demo-tabs"
  >
    <el-tab-pane
      :label="mod.name"
      :name="mod.module"
      v-for="mod in moduleJobs"
      :key="mod.name"
    >
      <JobModuleItem
        ref="jobModule"
        :module="mod"
        :regions="regions"
        :cloudAccount="cloudAccount"
        :readOnly="readOnly"
        :border="border"
      >
      </JobModuleItem>
      <layout-container :boder="true" v-if="operation">
        <el-button type="primary" @click="clear">{{
          t("commons.btn.cancel", "取消")
        }}</el-button>
        <el-button type="primary" @click="submitForm">{{
          t("commons.btn.save", "保存")
        }}</el-button>
      </layout-container>
    </el-tab-pane>
  </el-tabs>
</template>
<script setup lang="ts">
import { ref, onMounted } from "vue";
import type { CloudAccount, ModuleJob, Region } from "@/api/cloud_account/type";

import JobModuleItem from "@/componnets/job/JobModuleItem.vue";
import { ElMessage } from "element-plus";
import cloudAccountApi from "@/api/cloud_account";
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import _ from "lodash";
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
const activeModuleName = ref<string>();

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
    Promise.all(jobModule.value.map((item) => item.validate())).then((ok) => {
      cloudAccountApi
        .updateJobs(
          {
            cloudAccountModuleJobs: moduleJobs.value,
            cloudAccountId: props.accountId as string,
          },
          jobLoading
        )
        .then(() => {
          if (isRouter) {
            router.push({ name: "cloud_account_list" });
          }
          ElMessage.success(t("commons.msg.op_success", "操作成功"));
        });
    });
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

const props = defineProps({
  accountId: {
    type: String,
    requerid: true,
  },
  readOnly: {
    type: Boolean,
    default: false,
  },
  border: {
    type: Boolean,
    default: true,
  },
  operation: {
    type: Boolean,
    default: true,
  },
});

/**
 *回滚
 */
const rollBack = () => {
  moduleJobs.value = _.cloneDeep(cloneModuleJobs.value);
};

onMounted(() => {
  if (props.accountId) {
    init(props.accountId);
  }
});
/**
 * 初始化定时任务数据
 */
const init = (cloud_account_id: string) => {
  // 获取所有模块的定时任务
  cloudAccountApi.getJobs(cloud_account_id, jobLoading).then((ok) => {
    moduleJobs.value = ok.data.cloudAccountModuleJobs;
    cloneModuleJobs.value = _.cloneDeep(ok.data.cloudAccountModuleJobs);
    if (moduleJobs.value.length > 0) {
      // 设置第一个为选中的
      activeModuleName.value = moduleJobs.value[0].module;
    }
  });
  // 获取区域
  cloudAccountApi.getRegions(cloud_account_id).then((ok) => {
    regions.value = ok.data;
  });

  cloudAccountApi.getCloudAccount(cloud_account_id).then((ok) => {
    cloudAccount.value = ok.data;
  });
};
defineExpose({
  rollBack,
  submitForm,
});
</script>
<style lang="scss"></style>
