<template>
  <el-tabs v-model="activeName" class="tabs-box" @tab-click="handleTab">
    <el-tab-pane label="登录日志" name="LoginLogList"></el-tab-pane>
    <el-tab-pane label="虚拟机操作日志" name="VmOperatedLogList"></el-tab-pane>
    <el-tab-pane label="磁盘操作日志" name="DiskOperatedLogList"></el-tab-pane>
    <el-tab-pane label="平台管理日志" name="AllOperatedLogList"></el-tab-pane>
    <div>
      <component v-bind:is="currentCom"></component>
    </div>
  </el-tabs>
</template>
<script lang="ts" setup>
import {ref, markRaw, onMounted} from "vue";
import LoginLogList from "./LoginLogList.vue";
import VmOperatedLogList from "./VmOperatedLogList.vue";
import DiskOperatedLogList from "./DiskOperatedLogList.vue";
import AllOperatedLogList from "./AllOperatedLogList.vue";
const activeName = ref("LoginLogList");
const currentCom = ref()
onMounted(()=>{
  currentCom.value = markRaw(LoginLogList)
})
const handleTab = (name: string) => {
  debugger;
  const lookup: {
    // 为了ts不报错，声明一个索引签名
    [name: string]: any
  } =
      { LoginLogList, VmOperatedLogList, DiskOperatedLogList, AllOperatedLogList}

  currentCom.value = markRaw(lookup[name["paneName"]]);
}
</script>
<style>
.tabs-box {
  height: 80%;
}
</style>
