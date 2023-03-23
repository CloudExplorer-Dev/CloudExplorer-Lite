<template>
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
</template>
<script lang="ts" setup>
import { ref } from "vue";
import HostList from "./hostList.vue";
import StorageList from "./storageList.vue";
import BaseView from "./baseView.vue";
const activeName = ref("resourceSpread");
</script>
