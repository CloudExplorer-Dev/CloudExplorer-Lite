<template>
  <el-row :gutter="16" class="row">
    <el-col :span="8">
      <DoughnutChartSpread
          :type-text="'云账号分布'"
          :type="'byAccount'"
          :api-data="apiData"
          :loading="loading"
          :current-unit="props.currentUnit"
      ></DoughnutChartSpread>
    </el-col>
    <el-col :span="8">
      <DoughnutChartSpread
          :type-text="'挂载状态'"
          :type="'byStatus'"
          :api-data="apiData"
          :loading="loading"
          :current-unit="props.currentUnit"
      ></DoughnutChartSpread>
    </el-col>
    <el-col :span="8">
      <DoughnutChartSpread
          :type-text="'磁盘类型'"
          :type="'byType'"
          :api-data="apiData"
          :loading="loading"
          :current-unit="props.currentUnit"
      ></DoughnutChartSpread>
    </el-col>
  </el-row>
</template>
<script lang="ts" setup>
import { ref, watch } from "vue";
import ResourceSpreadViewApi from "@/api/disk_analysis/index";
import _ from "lodash";
import type { ResourceAnalysisRequest } from "@/api/disk_analysis/type";
import DoughnutChartSpread from "./DoughnutChartSpread.vue"
import { useUserStore } from "@commons/stores/modules/user";
const userStore = useUserStore();
const adminRole = ref<boolean>(userStore.currentRole==='ADMIN');
const props = defineProps<{
  cloudAccountId?: string | undefined;
  currentUnit: string;
}>();
const params = ref<ResourceAnalysisRequest>();
const loading = ref<boolean>(false);
const apiData = ref<any>();
const setParams = () => {
  props.cloudAccountId
      ? _.set(
          params,
          "accountIds",
           props.cloudAccountId==="all" ?  [] : [props.cloudAccountId]
      )
      : "";
  _.set(params, "statisticalBlock", props.currentUnit === "block");
};
//获取数宿主机按云账号分布
const getSpreadData = () => {
  setParams();
  ResourceSpreadViewApi.getSpreadData(params, loading).then(
      (res) => (apiData.value = res.data)
  );
};
watch(
    props,
    () => {
      getSpreadData();
    },
    { immediate: true }
);
</script>
