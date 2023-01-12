<template>
  <el-tabs v-model="activeName">
    <el-tab-pane label="云磁盘分析" name="resourceSpread" :lazy="true">
      <div v-if="activeName === 'resourceSpread'">
        <ResourceSpreadView></ResourceSpreadView>
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
import { onMounted, ref } from "vue";
import DiskList from "./diskList.vue";
import ResourceSpreadView from "./resourceAnalysisView.vue";
const activeName = ref("resourceSpread");
onMounted(() => {});
</script>
<style lang="scss" scoped></style>
