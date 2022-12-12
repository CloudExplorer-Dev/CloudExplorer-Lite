<template>
  <el-tabs v-model="activeName" :animated="false">
    <el-tab-pane :label="$t('log_manage.login')" name="LoginLogList">
      <div class="log-table">
        <LoginLogList
          height="100%"
          width="100%"
          v-if="activeName === 'LoginLogList'"
        ></LoginLogList>
      </div>
    </el-tab-pane>
    <el-tab-pane :label="$t('log_manage.vm')" name="VmOperatedLogList">
      <div class="log-table">
        <VmOperatedLogList
          height="100%"
          width="100%"
          v-if="activeName === 'VmOperatedLogList'"
        ></VmOperatedLogList>
      </div>
    </el-tab-pane>
    <el-tab-pane :label="$t('log_manage.disk')" name="DiskOperatedLogList">
      <div class="log-table">
        <DiskOperatedLogList
          height="100%"
          width="100%"
          v-if="activeName === 'DiskOperatedLogList'"
        ></DiskOperatedLogList>
      </div>
    </el-tab-pane>
    <el-tab-pane :label="$t('log_manage.platform')" name="AllOperatedLogList">
      <div class="log-table">
        <AllOperatedLogList
          height="100%"
          width="100%"
          v-if="activeName === 'AllOperatedLogList'"
        ></AllOperatedLogList>
      </div>
    </el-tab-pane>
    <div></div>
  </el-tabs>
</template>
<script lang="ts" setup>
import { ref } from "vue";
import LoginLogList from "./LoginLogList.vue";
import VmOperatedLogList from "./VmOperatedLogList.vue";
import DiskOperatedLogList from "./DiskOperatedLogList.vue";
import AllOperatedLogList from "./AllOperatedLogList.vue";
import ClearLogConfig from "@/views/OperatedLog/ClearLogConfig.vue";
const activeName = ref("LoginLogList");
</script>
<style scoped>
.log-table {
  width: 100%;
  height: calc(100vh - 270px);
}
</style>
