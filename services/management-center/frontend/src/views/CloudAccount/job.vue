<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { ElMessage } from "element-plus";
import type {
  Region,
  ModuleJob,
  UpdateJobsRequest,
} from "@/api/cloud_account/type";
import cloudAccountApi from "@/api/cloud_account";
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import type { SimpleMap } from "@commons/api/base/type";
const router = useRouter();
const { t } = useI18n();
const jobLoading = ref<boolean>(false);
const regionloading = ref<boolean>(false);
/**
 * 选中的区域id
 */
const checkedRegionIds = ref<Array<string>>();
/**
 * 模块选中的区域
 */
const moduleCheckedRegion = ref<SimpleMap<Array<string>>>({});

/**
 * 模块定时任务
 */
const moduleJobs = ref<Array<ModuleJob>>([]);
/**
 * 区域
 */
const regions = ref<Array<Region>>([]);

/**
 * 是否全选
 */
const checkAll = computed(() => {
  return (
    moduleCheckedRegion.value[activeModuleName.value].length ===
    regions.value.length
  );
});

/**
 * 全选改变触发函数
 * @param val 改变的值
 */
const handleCheckAllChange = (val: boolean) => {
  if (val) {
    moduleCheckedRegion.value[activeModuleName.value] = regions.value.map(
      (region) => region.regionId
    );
  } else {
    moduleCheckedRegion.value[activeModuleName.value] = [];
  }
};

/**
 * 提交定时任务
 */
const submitForm = (isRouter?: true) => {
  if (props.accountId) {
    const data = getUpdateJobParams(props.accountId);
    cloudAccountApi.updateJobs(data, jobLoading).then(() => {
      if (isRouter) {
        router.push({ name: "cloud_account_list" });
      }
      ElMessage.success(t("commons.msg.op_success", "操作成功"));
    });
  }
};

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
 * 获取更新定时任务所需的参数
 */
const getUpdateJobParams: (
  cloud_account_id: string
) => UpdateJobsRequest = () => {
  moduleJobs.value.forEach((module) => {
    module.jobDetailsList.forEach((job) => {
      job.regions = regions.value.filter((item) =>
        moduleCheckedRegion.value[module.module].includes(item.regionId)
      );
    });
  });
  return {
    cloudAccountId: props.accountId as string,
    selectRegion: regions.value.filter((item) =>
      checkedRegionIds.value?.includes(item.regionId)
    ),
    cloudAccountModuleJobs: moduleJobs.value,
  };
};

/**
 * 组建挂载
 */
onMounted(() => {
  if (props.accountId) {
    init(props.accountId as string);
  }
});

/**
 * 初始化定时任务数据
 */
const init = (cloud_account_id: string) => {
  cloudAccountApi.getRegions(cloud_account_id, regionloading).then((ok) => {
    regions.value = ok.data;
  });
  cloudAccountApi.getJobs(cloud_account_id, jobLoading).then((ok) => {
    moduleJobs.value = ok.data.cloudAccountModuleJobs;
    if (moduleJobs.value.length > 0) {
      activeModuleName.value = moduleJobs.value[0].module;
    }

    moduleCheckedRegion.value = moduleJobs.value
      .map((module) => {
        return {
          [module.module]: module.jobDetailsList[0].regions.map(
            (i) => i.regionId
          ),
        };
      })
      .reduce((s1: SimpleMap<Array<string>>, s2: SimpleMap<Array<string>>) => {
        return { ...s1, ...s2 };
      }, {});
  });
};
/**
 * 取消,去列表页面
 */
const clear = () => {
  router.push({ name: "cloud_account_list" });
};
/**
 * 回滚数据
 */
const rollBack = () => {
  moduleCheckedRegion.value = moduleJobs.value
    .map((module) => {
      return {
        [module.module]: module.jobDetailsList[0].regions.map(
          (i) => i.regionId
        ),
      };
    })
    .reduce((s1: SimpleMap<Array<string>>, s2: SimpleMap<Array<string>>) => {
      return { ...s1, ...s2 };
    }, {});
};
const activeModuleName = ref<string>("management-center");

const activeJob = computed(() => {
  const job = moduleJobs.value.find((module) => {
    return module.module === activeModuleName.value;
  });
  if (job) {
    return job;
  }
  return { jobDetailsList: [] };
});

const change = (selectRegion: Array<string>) => {
  if (moduleCheckedRegion.value) {
    moduleCheckedRegion.value[activeModuleName.value] = selectRegion;
  }
};

defineExpose({
  submitForm,
  rollBack,
});
</script>
<template>
  <el-tabs v-loading="jobLoading" v-model="activeModuleName" class="demo-tabs">
    <el-tab-pane
      :label="mod.name"
      :name="mod.module"
      v-for="mod in moduleJobs"
      :key="mod.name"
    >
      <el-form
        v-loading="jobLoading"
        :inline="true"
        status-icon
        label-width="120px"
        label-suffix=":"
        label-position="left"
      >
        <layout-container :border="false">
          <template #content>
            <layout-container :border="border">
              <template #header
                ><h4>
                  {{ t("cloud_account.sync.range", "同步范围") }}
                </h4></template
              >
              <template #content>
                <el-checkbox
                  style="margin-bottom: 10px"
                  v-model="checkAll"
                  @change="handleCheckAllChange"
                  >全选</el-checkbox
                >
                <el-checkbox-group
                  @change="change"
                  v-model="moduleCheckedRegion[activeModuleName]"
                >
                  <el-checkbox
                    :title="region.name"
                    v-for="region in regions"
                    :key="region.regionId"
                    :label="region.regionId"
                    size="large"
                    :disabled="readOnly"
                    ><span
                      style="
                        display: inline-block;
                        width: 120px;
                        overflow: hidden;
                        text-overflow: ellipsis;
                      "
                    >
                      {{ region.name }}
                    </span>
                  </el-checkbox>
                </el-checkbox-group>
              </template>
            </layout-container>
            <layout-container :border="border">
              <template #header
                ><h4>
                  {{ t("cloud_account.sync.timing", "资源同步频率") }}
                </h4></template
              >
              <template #content>
                <div
                  style="width: 100%"
                  v-for="details in activeJob.jobDetailsList"
                  :key="details.jobName"
                >
                  <el-checkbox
                    style="width: 30%"
                    :checked="details.active"
                    :label="details.active"
                    v-model="details.active"
                    :disabled="readOnly"
                  >
                    <div style="display: flex; align-items: center">
                      <span style="width: 100px"
                        >{{ details.description }}:</span
                      >
                      <span style="width: 40px">{{
                        t("cloud_account.sync.interval", "每隔")
                      }}</span>
                      <div style="width: 100px">
                        <el-input-number
                          style="width: 80px"
                          @click.stop.prevent
                          size="small"
                          v-model="details.timeInterval"
                          controls-position="right"
                          :disabled="
                            !details.active || (details.active && readOnly)
                          "
                        />
                      </div>
                      <div style="width: 80px">
                        <el-select
                          @click.stop.prevent
                          style="width: 60px"
                          v-model="details.unit"
                          class="m-2"
                          placeholder="Select"
                          size="small"
                          :disabled="
                            !details.active || (details.active && readOnly)
                          "
                        >
                          <el-option
                            v-for="item in [
                              {
                                value: 'MILLISECOND',
                                label: t(
                                  'cloud_account.sync.interval_time_unit.millisecond',
                                  '毫秒'
                                ),
                              },
                              {
                                value: 'SECOND',
                                label: t(
                                  'cloud_account.sync.interval_time_unit.second',
                                  '秒'
                                ),
                              },
                              {
                                value: 'MINUTE',
                                label: t(
                                  'cloud_account.sync.interval_time_unit.minute',
                                  '分钟'
                                ),
                              },
                              {
                                value: 'HOUR',
                                label: t(
                                  'cloud_account.sync.interval_time_unit.hour',
                                  '小时'
                                ),
                              },
                              {
                                value: 'DAY',
                                label: t(
                                  'cloud_account.sync.interval_time_unit.hour',
                                  '天'
                                ),
                              },
                            ]"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                          />
                        </el-select>
                      </div>

                      <span style="width: 80px">{{
                        t("cloud_account.sync.once", "同步一次")
                      }}</span>
                    </div>
                  </el-checkbox>
                </div></template
              >
            </layout-container>
            <layout-container v-if="operation">
              <el-button type="primary" @click="clear">{{
                t("commons.btn.cancel", "取消")
              }}</el-button>
              <el-button type="primary" @click="submitForm">{{
                t("commons.btn.save", "保存")
              }}</el-button></layout-container
            >
          </template>
        </layout-container>
      </el-form>
    </el-tab-pane>
  </el-tabs>
</template>

<style lang="scss"></style>
