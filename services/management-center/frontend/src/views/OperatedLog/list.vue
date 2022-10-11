<template>
  <el-tabs v-model="activeName" class="tabs-box" @tab-click="handleTab"  height="100%">
    <el-tab-pane :label="$t('log_manage.login')" name="LoginLogList"></el-tab-pane>
    <el-tab-pane :label="$t('log_manage.vm')" name="VmOperatedLogList"></el-tab-pane>
    <el-tab-pane :label="$t('log_manage.disk')" name="DiskOperatedLogList"></el-tab-pane>
    <el-tab-pane :label="$t('log_manage.platform')" name="AllOperatedLogList"  height="100%"></el-tab-pane>
    <div>
      <component v-bind:is="currentCom"></component>
    </div>
  </el-tabs>
</template>
<script lang="ts" setup>
import { ref, markRaw, onMounted } from "vue";
import LoginLogList from "./LoginLogList.vue";
import VmOperatedLogList from "./VmOperatedLogList.vue";
import DiskOperatedLogList from "./DiskOperatedLogList.vue";
import AllOperatedLogList from "./AllOperatedLogList.vue";
const activeName = ref("LoginLogList");
const currentCom = ref();
onMounted(() => {
  currentCom.value = markRaw(LoginLogList);
});
const handleTab = (name: string) => {
  const lookup: {
    // 为了ts不报错，声明一个索引签名
    [name: string]: any;
  } = {
    LoginLogList,
    VmOperatedLogList,
    DiskOperatedLogList,
    AllOperatedLogList,
  };

  currentCom.value = markRaw(lookup[name["paneName"]]);
};
</script>
<style>
.tabs-box {
  height: 80%;
}
</style>
