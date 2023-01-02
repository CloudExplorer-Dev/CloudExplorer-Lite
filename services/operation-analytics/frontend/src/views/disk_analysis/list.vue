<template>
  <el-tabs v-model="activeName">
    <el-tab-pane label="资源分布" name="resourceSpread" :lazy="true">
      <div class="log-table" v-if="activeName === 'resourceSpread'">
        <ResourceSpreadView></ResourceSpreadView>
      </div>
    </el-tab-pane>
    <el-tab-pane label="资源明细" name="host">
      <div class="log-table" v-if="activeName === 'host'">
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
<style lang="scss" scoped>
.log-table {
  width: 100%;
  height: calc(100vh - 270px);
}
</style>
