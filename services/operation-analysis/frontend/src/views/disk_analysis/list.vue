<template>
  <el-tabs v-model="activeName">
    <el-tab-pane label="磁盘分析" name="resourceSpread" :lazy="true">
      <div v-if="activeName === 'resourceSpread'">
        <BaseView></BaseView>
      </div>
    </el-tab-pane>
    <el-tab-pane label="资源明细" name="host">
      <div v-if="activeName === 'host'">
        <DiskList ref="host"></DiskList>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>
<script lang="ts" setup>
import { ref } from "vue";
import DiskList from "./diskList.vue";
import BaseView from "@/views/disk_analysis/baseView.vue";
const activeName = ref("resourceSpread");
</script>
