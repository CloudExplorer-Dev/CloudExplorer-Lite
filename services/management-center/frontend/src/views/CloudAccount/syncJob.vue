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
 * 提交定时任务
 */
const submitForm = () => {
  if (router.currentRoute.value.params.id) {
    const data = getUpdateJobParams(
      router.currentRoute.value.params.id as string
    );
    cloudAccountApi.updateJobs(data, jobLoading).then(() => {
      router.push({ name: "cloud_account_list" });
      ElMessage.success(t("commons.msg.op_success", "操作成功"));
    });
  }
};
defineProps<{
  id: string;
}>();

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
    cloudAccountId: router.currentRoute.value.params.id as string,
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
  if (router.currentRoute.value.params.id) {
    init(router.currentRoute.value.params.id as string);
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
            <layout-container>
              <template #header
                ><h4>
                  {{ t("cloud_account.sync.range", "同步范围") }}
                </h4></template
              >
              <template #content>
                <el-form-item
                  v-loading="regionloading"
                  :label="t('cloud_account.sync.region', '区域')"
                >
                  <el-checkbox-group
                    @change="change"
                    v-model="moduleCheckedRegion[activeModuleName]"
                  >
                    <el-checkbox
                      style="width: 25%"
                      v-for="region in regions"
                      :key="region.regionId"
                      :label="region.regionId"
                      size="large"
                      >{{ region.name }}</el-checkbox
                    >
                  </el-checkbox-group>
                </el-form-item>
              </template>
            </layout-container>
            <layout-container>
              <template #header
                ><h4>
                  {{ t("cloud_account.sync.timing", "定时同步") }}
                </h4></template
              >
              <template #content>
                <div
                  style="width: 100%"
                  v-for="details in activeJob.jobDetailsList"
                  :key="details.jobName"
                >
                  <el-checkbox
                    style="width: 30%; margin-left: 30px"
                    :checked="details.active"
                    :label="details.active"
                    v-model="details.active"
                  >
                    <span>{{ details.description }}:</span>
                    <span style="margin-left: 10px">{{
                      t("cloud_account.sync.interval", "每隔")
                    }}</span>
                    <el-input-number
                      style="width: 80px"
                      @click.stop.prevent
                      size="small"
                      v-model="details.timeInterval"
                      controls-position="right"
                      :disabled="!details.active"
                    />
                    <el-select
                      @click.stop.prevent
                      style="width: 60px"
                      v-model="details.unit"
                      class="m-2"
                      placeholder="Select"
                      size="small"
                      :disabled="!details.active"
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
                    <span>{{ t("cloud_account.sync.once", "同步一次") }}</span>
                  </el-checkbox>
                </div></template
              >
            </layout-container>
            <layout-container>
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
