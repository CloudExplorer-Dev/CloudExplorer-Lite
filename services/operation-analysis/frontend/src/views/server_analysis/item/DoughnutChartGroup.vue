<template>
  <el-row :gutter="16" class="row">
    <el-col :span="8">
      <DoughnutChartSpread
          :type-text="'云账号分布'"
          :type="'byAccount'"
          :api-data="apiData"
          :loading="loading"
      ></DoughnutChartSpread>
    </el-col>
    <el-col :span="8">
      <DoughnutChartSpread
          :type-text="'运行状态'"
          :type="'byStatus'"
          :api-data="apiData"
          :loading="loading"
      ></DoughnutChartSpread>
    </el-col>
    <el-col :span="8">
      <DoughnutChartSpread
          :type-text="'付费方式'"
          :type="'byChargeType'"
          :api-data="apiData"
          :loading="loading"
      ></DoughnutChartSpread>
    </el-col>
  </el-row>
</template>
<script lang="ts" setup>
import { ref, watch } from "vue";
import ResourceSpreadViewApi from "@/api/server_analysis/index";
import _ from "lodash";
import type { ResourceAnalysisRequest } from "@commons/api/server_analysis/type";
import DoughnutChartSpread from "./DoughnutChartSpread.vue"
import { useUserStore } from "@commons/stores/modules/user";
const userStore = useUserStore();
const adminRole = ref<boolean>(userStore.currentRole==='ADMIN');
const props = defineProps<{
  cloudAccountId?: string | undefined;
  clusterId?: string | undefined;
  datastoreId?: string | undefined;
  hostId?: string | undefined;
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
  props.clusterId
      ? _.set(
          params,
          "clusterIds",
           props.clusterId==="all" ?  [] : [props.clusterId]
      )
      : "";
  props.datastoreId
      ? _.set(
          params,
          "datastoreIds",
           props.datastoreId==="all" ?  [] : [props.datastoreId]
      )
      : "";
  props.hostId
      ? _.set(params, "hostIds",  props.hostId==="all" ?  [] : [props.hostId])
      : "";
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
