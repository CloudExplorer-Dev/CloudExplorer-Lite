<template>
  <el-tabs v-model="activeName">
    <el-tab-pane label="资源分布" name="resourceSpread" :lazy="true">
      <div v-if="activeName === 'resourceSpread'">
        <ResourceSpreadView></ResourceSpreadView>
      </div>
    </el-tab-pane>
    <el-tab-pane label="资源明细" name="host">
      <div v-if="activeName === 'host'">
        <ServerList ref="host"></ServerList>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>
<script lang="ts" setup>
import { onMounted, ref } from "vue";
import ServerList from "./serverList.vue";
import ResourceSpreadView from "./resourceAnalysisView.vue";
const activeName = ref("resourceSpread");
onMounted(() => {});
</script>
<style lang="scss" scoped></style>
