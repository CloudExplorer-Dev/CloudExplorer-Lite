<template>
  <layout-container :border="false">
    <template #content>
      <layout-container v-loading="loading">
        <template #header>
          <h4>基本信息</h4>
        </template>
        <template #content>
          {{ data }}
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
import JobsApi from "@/api/jobs";
import type { JobInfo } from "@/api/jobs/type";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const route = useRouter();

const loading: Ref<boolean> | undefined = ref<boolean>(false);

const data = ref<JobInfo>({});

const back = () => {
  route.push({ name: "job_list" });
};

onMounted(() => {
  JobsApi.getJobById(props.id, loading).then((ok) => {
    data.value = ok.data;
  });
});
</script>
<style lang="scss"></style>
