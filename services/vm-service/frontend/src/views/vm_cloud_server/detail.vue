<template>
  <el-tabs
    v-model="activeName"
    class="tabs-box"
    @tab-click="handleTab"
    height="100%"
  >
    <el-tab-pane label="基本信息" name="Info"></el-tab-pane>
    <el-tab-pane label="监控" name="Monitor"></el-tab-pane>
    <div>
      <component v-bind:is="currentCom"></component>
    </div>
  </el-tabs>
</template>
<script lang="ts" setup>
import { ref, markRaw, onMounted } from "vue";
import Info from "./info.vue";
import Monitor from "./monitor.vue";
import { useRouter } from "vue-router";
const activeName = ref("Info");
const currentCom = ref();
const route = useRouter();
onMounted(() => {
  currentCom.value = markRaw(Info);
});
const handleTab = (name: string) => {
  const lookup: {
    // 为了ts不报错，声明一个索引签名
    [name: string]: any;
  } = {
    Info,
    Monitor,
  };
  currentCom.value = markRaw(lookup[name["paneName" as any]]);
};
</script>
<style>
.tabs-box {
  height: 80%;
}
</style>
