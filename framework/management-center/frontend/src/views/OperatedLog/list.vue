<template>
  <el-tabs v-model="activeName" :animated="false">
    <el-tab-pane :label="$t('log_manage.login')" name="LoginLogList">
      <div class="log-table">
        <LoginLogList v-if="activeName === 'LoginLogList'" />
      </div>
    </el-tab-pane>
    <el-tab-pane :label="$t('log_manage.vm')" name="VmOperatedLogList">
      <div class="log-table">
        <VmOperatedLogList v-if="activeName === 'VmOperatedLogList'" />
      </div>
    </el-tab-pane>
    <el-tab-pane :label="$t('log_manage.disk')" name="DiskOperatedLogList">
      <div class="log-table">
        <DiskOperatedLogList v-if="activeName === 'DiskOperatedLogList'" />
      </div>
    </el-tab-pane>
    <el-tab-pane :label="$t('log_manage.platform')" name="AllOperatedLogList">
      <div class="log-table">
        <AllOperatedLogList v-if="activeName === 'AllOperatedLogList'" />
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
const activeName = ref("LoginLogList");
</script>
<style scoped>
.log-table {
  width: 100%;
  height: calc(100vh - 270px);
}
</style>
