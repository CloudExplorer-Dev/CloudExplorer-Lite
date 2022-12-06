<template>
  <layout-container :border="false">
    <template #content>
      <layout-container v-loading="loading">
        <template #header>
          <h4>任务信息</h4>
        </template>
        <template #content>
          <el-descriptions :column="1" border size="small">
            <el-descriptions-item
              label-class-name="label-class"
              class-name="content-class"
              label="任务类型"
              colon="true"
            >
              {{ _.get(JobTypeConst, data.type, data.type) }}
            </el-descriptions-item>
            <el-descriptions-item
              label-class-name="label-class"
              class-name="content-class"
              label="任务ID"
            >
              {{ data.id }}
            </el-descriptions-item>
            <el-descriptions-item
              label-class-name="label-class"
              class-name="content-class"
              label="任务描述"
            >
              {{ data.description }}
            </el-descriptions-item>
            <el-descriptions-item
              label-class-name="label-class"
              class-name="content-class"
              label="任务状态"
            >
              {{ _.get(JobStatusConst, data.status, data.status) }}
            </el-descriptions-item>
            <el-descriptions-item
              label-class-name="label-class"
              class-name="content-class"
              label="任务对象"
            >
              <div v-for="server in data.servers" :key="server.id">
                <a @click="jumpToServer(server)"
                  >云主机: {{ server.instanceName }}</a
                >
              </div>
              <div v-for="disk in data.disks" :key="disk.id">
                <a @click="jumpToDisk(disk)">磁盘: {{ disk.diskName }}</a>
              </div>
            </el-descriptions-item>
            <el-descriptions-item
              label-class-name="label-class"
              class-name="content-class"
              label="开始时间"
            >
              {{ data.createTime }}
            </el-descriptions-item>
            <el-descriptions-item
              label-class-name="label-class"
              class-name="content-class"
              label="结束时间"
            >
              {{ data.finishTime }}
            </el-descriptions-item>
            <el-descriptions-item
              label-class-name="label-class"
              class-name="content-class"
              label="持续时间"
            >
              {{
                data.finishTime
                  ? MillisecondToDate(
                      new Date(data.finishTime).getTime() -
                        new Date(data.createTime).getTime()
                    )
                  : ""
              }}
            </el-descriptions-item>
            <el-descriptions-item
              label-class-name="label-class"
              class-name="content-class"
              label="失败原因"
            >
              {{ data.result }}
            </el-descriptions-item>
          </el-descriptions>
        </template>
      </layout-container>

      <layout-container>
        <el-button @click="back">返回</el-button>
      </layout-container>
    </template>
  </layout-container>
</template>
<script setup lang="ts">
const props = defineProps<{
  id: string;
}>();

import { onMounted, type Ref, ref } from "vue";
import { useRouter } from "vue-router";
import { JobTypeConst, JobStatusConst } from "@commons/utils/constants";
import _ from "lodash";
import JobsApi from "@/api/jobs";
import type { JobInfo } from "@/api/jobs/type";
import { useI18n } from "vue-i18n";
import { type VmCloudDiskVO } from "@/api/vm_cloud_disk/type";
import { type VmCloudServerVO } from "@/api/vm_cloud_server/type";

const { t } = useI18n();
const useRoute = useRouter();

const loading: Ref<boolean> | undefined = ref<boolean>(false);

const data = ref<JobInfo>({});

const back = () => {
  useRoute.push({ name: "job_list" });
};

const jumpToServer = (server: VmCloudServerVO) => {
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace(
      `jobs/detail/${props.id}`,
      `vm_cloud_server/detail/${server.id}`
    ),
  });
};
const jumpToDisk = (disk: VmCloudDiskVO) => {
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace(
      `jobs/detail/${props.id}`,
      `vm_cloud_disk/detail/${disk.id}`
    ),
  });
};

function MillisecondToDate(msd: number) {
  const time = msd / 1000.0;

  if (time > 60 && time < 60 * 60) {
    return time / 60.0 + "分钟" + (time / 60.0 - time / 60.0) * 60 + "秒";
  } else if (time >= 60 * 60 && time < 60 * 60 * 24) {
    return (
      time / 3600.0 +
      "小时" +
      (time / 3600.0 - time / 3600.0) * 60 +
      "分钟" +
      ((time / 3600.0 - time / 3600.0) * 60 -
        (time / 3600.0 - time / 3600.0) * 60) *
        60 +
      "秒"
    );
  }

  return time + "秒";
}

onMounted(() => {
  JobsApi.getJobById(props.id, loading).then((ok) => {
    data.value = ok.data;
  });
});
</script>
<style lang="scss">
:deep(.el-descriptions__cell) {
  border: 0 !important;
}
:deep(.is-bordered-label) {
  background-color: transparent !important;
}
:deep(.el-descriptions__cell) {
  font-size: 12px !important;
}
.content-class {
  border: 0 !important;
  background-color: transparent !important;
}
.label-class {
  border: 0 !important;
  background-color: transparent !important;
}
</style>
