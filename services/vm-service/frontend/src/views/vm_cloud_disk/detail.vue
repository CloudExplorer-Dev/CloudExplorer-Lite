<script setup lang="ts">
import { ref } from "vue";
import type { TabsPaneContext } from "element-plus";
import BasicInfo from "@/views/vm_cloud_disk/BasicInfo.vue";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const loading = ref<boolean>(false);
const activeName = ref("BasicInfo");

const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log("click");
};
</script>

<template>
  <el-tabs
    v-model="activeName"
    class="tabs-box"
    @tab-click="handleClick"
    height="100%"
  >
    <el-tab-pane :label="t('commons.basic_info', '基本信息')" name="BasicInfo">
      <BasicInfo />
    </el-tab-pane>
    <el-tab-pane label="监控" name="Monitor"> 监控信息 </el-tab-pane>
  </el-tabs>
</template>

<style lang="scss"></style>
