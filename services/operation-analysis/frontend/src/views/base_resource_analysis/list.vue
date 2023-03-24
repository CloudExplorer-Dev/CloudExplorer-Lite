<template>
  <div class="tab-box">
    <el-tabs v-model="activeName">
      <el-tab-pane label="资源分析" name="resourceSpread" :lazy="true">
        <div v-if="activeName === 'resourceSpread'">
          <BaseView></BaseView>
        </div>
      </el-tab-pane>
      <el-tab-pane label="宿主机明细" name="host">
        <div v-if="activeName === 'host'">
          <HostList ref="host"></HostList>
        </div>
      </el-tab-pane>
      <el-tab-pane label="存储器明细" name="storage">
        <div>
          <StorageList v-if="activeName === 'storage'"></StorageList>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script lang="ts" setup>
import { ref } from "vue";
import HostList from "./hostList.vue";
import StorageList from "./storageList.vue";
import BaseView from "./baseView.vue";
const activeName = ref("resourceSpread");
</script>
<style scoped lang="scss">
.tab-box {
  :deep(.el-tabs__item) {
    //font-family: 'PingFang SC' !important;
    //font-style: normal;
    //font-weight: 500;
    //font-size: 16px !important;
    //line-height: 24px !important;
  }
}
</style>
