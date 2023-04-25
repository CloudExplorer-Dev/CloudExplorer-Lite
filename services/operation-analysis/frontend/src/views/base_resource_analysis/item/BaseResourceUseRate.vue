<template>
  <div class="info-card">
    <el-row :gutter="10">
      <el-col :span="24">
        <div class="title">基础资源使用率</div>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="8">
        <UsedRate
          :api-data="apiData"
          :type="'cpu'"
          :type-text="'CPU'"
          :unit="'MHz'"
          :loading="loading"
        ></UsedRate>
      </el-col>
      <el-col :span="8">
        <UsedRate
          :api-data="apiData"
          :type="'memory'"
          :type-text="'内存'"
          :unit="'GB'"
          :loading="loading"
        ></UsedRate>
      </el-col>
      <el-col :span="8">
        <UsedRate
          :api-data="apiData"
          :type="'datastore'"
          :type-text="'存储'"
          :unit="'GB'"
          :loading="loading"
        ></UsedRate>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import { ref, watch } from "vue";
import "echarts-liquidfill";
import _ from "lodash";
import ResourceSpreadViewApi from "@/api/resource_spread_view/index";
import type { ResourceAnalysisRequest } from "@commons/api/resource_spread_view/type";
import UsedRate from "./used_rate_item/UsedRate.vue";
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
        props.cloudAccountId === "all" ? [] : [props.cloudAccountId]
      )
    : "";
  props.clusterId
    ? _.set(
        params,
        "clusterIds",
        props.clusterId === "all" ? [] : [props.clusterId]
      )
    : "";
  props.datastoreId
    ? _.set(
        params,
        "datastoreIds",
        props.datastoreId === "all" ? [] : [props.datastoreId]
      )
    : "";
  props.hostId
    ? _.set(params, "hostIds", props.hostId === "all" ? [] : [props.hostId])
    : "";
};
//获取分配情况
const getUsedComputerInfo = () => {
  setParams();
  ResourceSpreadViewApi.getUsedInfo(params, loading).then(
    (res) => (apiData.value = res.data)
  );
};

watch(
  props,
  () => {
    getUsedComputerInfo();
  },
  { immediate: true }
);
</script>
<style scoped lang="scss">
.info-card {
  background: #ffffff;
  border-radius: 4px;
  padding: 24px;
  overflow: hidden;
}
.chart {
  min-height: 120px;
  width: 100%;
}
.title {
  font-weight: bold;
  font-size: 16px;
  padding-top: 4px;
  padding-bottom: 16px;
}
.echarts-content {
  margin-top: 7px;
}
.echarts-footer {
  margin-top: 15px;
  margin-bottom: 10px;
  position: initial;

  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 12px;
  text-align: center;
  color: #1f2329;
}
</style>
