<script setup lang="ts">
import { ref, onMounted } from "vue";
import { ElMessage } from "element-plus";
import type {
  Region,
  ModuleJob,
  UpdateJobsRequest,
} from "@/api/cloud_account/type";
import cloudAccountApi from "@/api/cloud_account";
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
const router = useRouter();
const { t } = useI18n();
const loading = ref<boolean>(false);
/**
 * 选中的区域id
 */
const checkedRegionIds = ref<Array<string>>();
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
    cloudAccountApi.updateJobs(data, loading).then(() => {
      ElMessage.success("保存定时任务成功");
      router.push({ name: "cloud_account_list" });
    });
  }
};

/**
 * 获取更新定时任务所需的参数
 */
const getUpdateJobParams: (
  cloud_account_id: string
) => UpdateJobsRequest = () => {
  return {
    cloudAccountId: router.currentRoute.value.params.id as string,
    selectRegion: regions.value.filter((item) =>
      checkedRegionIds.value?.includes(item.regionId)
    ),
    cloudAccountSyncJobs: moduleJobs.value,
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
  cloudAccountApi.getResgions(cloud_account_id, loading).then((ok) => {
    regions.value = ok.data;
  });
  cloudAccountApi.getJobs(cloud_account_id, loading).then((ok) => {
    moduleJobs.value = ok.data.cloudAccountSyncJobs;
    checkedRegionIds.value = ok.data.selectRegion.map((item) => item.regionId);
  });
};
/**
 * 取消,去列表页面
 */
const clear = () => {
  router.push({ name: "cloud_account_list" });
};
</script>
<template>
  <el-form
    v-loading="loading"
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
            ><h4>{{ t("cloud_account.sync.range", "同步范围") }}</h4></template
          >
          <template #content>
            <el-form-item :label="t('cloud_account.sync.region', '区域')">
              <el-checkbox-group v-model="checkedRegionIds">
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
            ><h4>{{ t("cloud_account.sync.timing", "定时同步") }}</h4></template
          >
          <template #content>
            <el-form-item
              style="width: 100%"
              v-for="job in moduleJobs"
              :key="job.name"
            >
              <h3 style="width: 100%">{{ job.name }}</h3>
              <div
                style="width: 100%"
                v-for="details in job.jobDetailsList"
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
                        { value: 'MILLISECOND', label: '毫秒' },
                        { value: 'SECOND', label: '秒' },
                        { value: 'MINUTE', label: '分钟' },
                        { value: 'HOUR', label: '小时' },
                        { value: 'DAY', label: '天' },
                      ]"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                  <span>{{ t("cloud_account.sync.once", "同步一次") }}</span>
                </el-checkbox>
              </div>
            </el-form-item>
          </template>
        </layout-container>
        <layout-container>
          <el-button type="primary" @click="clear">取消</el-button>
          <el-button type="primary" @click="submitForm"
            >保存</el-button
          ></layout-container
        >
      </template>
    </layout-container>
  </el-form>
</template>

<style lang="scss"></style>
