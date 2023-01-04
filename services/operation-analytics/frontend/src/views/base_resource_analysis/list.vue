<template>
  <el-tabs v-model="activeName">
    <el-tab-pane label="资源分析" name="resourceSpread" :lazy="true">
      <div class="log-table" v-if="activeName === 'resourceSpread'">
        <ResourceAnalysisView></ResourceAnalysisView>
      </div>
    </el-tab-pane>
    <el-tab-pane label="宿主机明细" name="host">
      <div class="log-table" v-if="activeName === 'host'">
        <HostList ref="host"></HostList>
      </div>
    </el-tab-pane>
    <el-tab-pane label="存储器明细" name="storage">
      <div class="log-table">
        <StorageList v-if="activeName === 'storage'"></StorageList>
      </div>
    </el-tab-pane>
    <el-tab-pane label="告警明细" name="alarm">
      <div class="log-table">
        <AlarmList v-if="activeName === 'alarm'"></AlarmList>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>
<script lang="ts" setup>
import { onMounted, ref } from "vue";
import AlarmList from "./alarmList.vue";
import HostList from "./hostList.vue";
import StorageList from "./storageList.vue";
import ResourceAnalysisView from "./resourceAnalysisView.vue";
const activeName = ref("resourceSpread");
onMounted(() => {});
</script>
<style lang="scss" scoped>
.log-table {
  width: 100%;
  height: calc(100vh - 270px);
}
</style>
