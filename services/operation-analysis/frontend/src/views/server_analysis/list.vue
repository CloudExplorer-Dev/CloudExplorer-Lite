<template>
  <el-tabs v-model="activeName">
    <el-tab-pane label="云主机分析" name="resourceSpread" :lazy="true">
      <div v-if="activeName === 'resourceSpread'">
        <baseView></baseView>
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
import { ref } from "vue";
import ServerList from "./serverList.vue";
import baseView from "./baseView.vue";
const activeName = ref("resourceSpread");
</script>
