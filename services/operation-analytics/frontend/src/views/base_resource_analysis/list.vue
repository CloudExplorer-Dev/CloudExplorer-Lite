<template>
  <el-tabs v-model="activeName" :animated="false">
    <el-tab-pane label="资源分布" name="distribution">
      <div class="log-table">
        <div class="log-table">
          <DistributionView
            height="100%"
            width="100%"
            v-if="activeName === 'distribution'"
          ></DistributionView>
        </div>
      </div>
    </el-tab-pane>
    <el-tab-pane label="宿主机明细" name="host">
      <div class="log-table">
        <HostList
          height="100%"
          width="100%"
          v-if="activeName === 'host'"
        ></HostList>
      </div>
    </el-tab-pane>
    <el-tab-pane label="存储器明细" name="storage">
      <div class="log-table">
        <StorageList
          height="100%"
          width="100%"
          v-if="activeName === 'storage'"
        ></StorageList>
      </div>
    </el-tab-pane>
    <el-tab-pane label="告警明细" name="alarm">
      <div class="log-table">
        <AlarmList
          height="100%"
          width="100%"
          v-if="activeName === 'alarm'"
        ></AlarmList>
      </div>
    </el-tab-pane>
    <div></div>
  </el-tabs>
</template>
<script lang="ts" setup>
import { ref } from "vue";
import AlarmList from "./alarmList.vue";
import HostList from "./hostList.vue";
import StorageList from "./storageList.vue";
import DistributionView from "./distributionView.vue";
const activeName = ref("host");
</script>
<style scoped>
.log-table {
  width: 100%;
  height: calc(100vh - 270px);
}
</style>
