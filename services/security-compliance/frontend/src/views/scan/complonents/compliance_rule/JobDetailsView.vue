<template>
  <el-drawer
    v-model="visible"
    :title="accountJobRecord ? accountJobRecord.description : '任务详情'"
  >
    <JobStepLine
      style="margin-left: 40px"
      v-if="accountJobRecord?.params['steps']"
      :steps="accountJobRecord?.params['steps']"
    ></JobStepLine>
  </el-drawer>
</template>
<script setup lang="ts">
import { ref } from "vue";
import type { AccountJobRecord } from "@commons/api/cloud_account/type";
import JobStepLine from "./JobStepLine.vue";
defineProps<{ accountJobRecord?: AccountJobRecord }>();
/**
 * 任务详情展开控制器
 */
const visible = ref<boolean>(false);
/**
 * 打开任务详情
 */
const open = () => {
  visible.value = true;
};
/**
 * 关闭任务详情
 */
const close = () => {
  visible.value = false;
};
defineExpose({ open, close });
</script>
<style lang="scss"></style>
