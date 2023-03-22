<script setup lang="ts">
import { ref } from "vue";
import type { TabsPaneContext } from "element-plus";
import Info from "@/views/vm_cloud_disk/info.vue";
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
    <el-tab-pane label="详情" name="BasicInfo">
      <Info />
    </el-tab-pane>
  </el-tabs>
</template>

<style lang="scss"></style>
