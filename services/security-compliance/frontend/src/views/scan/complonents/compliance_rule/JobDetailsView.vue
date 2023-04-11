<template>
  <el-drawer
      v-model="visible"
      :title="accountJobRecord ? accountJobRecord.description : '任务详情'"
  >
    <el-timeline>
      <el-timeline-item
          :color="
          item.code === 200
            ? 'var(--el-color-success)'
            : 'var(--el-color-error)'
        "
          :icon="item.code === 200 ? 'CircleCheck' : 'CircleClose'"
          :hollow="true"
          v-for="(item, index) in accountJobRecord?.params[
          'SECURITY_COMPLIANCE_CLOUD_ACCOUNT_SYNC_JOB'
        ]"
          :key="index"
          :timestamp="item.link.description"
          placement="top"
      >
        <div
            v-if="
            item.code === 200 && item.link.type === 'CLOUD_SYNC_REGION_RESOURCE'
          "
        >
          同步资源数量: {{ item.data.size }}
        </div>
        <div
            :style="{ color: item.code === 500 ? 'var(--el-color-error)' : '' }"
            v-if="item.code === 500"
        >
          失败:{{ item.message }}
        </div>
      </el-timeline-item>
      <el-timeline-item
          v-if="accountJobRecord?.status === 'SYNCING'"
          timestamp="执行中"
          color="#fff"
          :icon="
          createVNode(ceIcon, {
            class: 'is-loading',
            code: 'Loading',
            style: 'color:#000;',
          })
        "
          placement="top"
      >
      </el-timeline-item>
    </el-timeline>
  </el-drawer>
</template>
<script setup lang="ts">
import {ref, createVNode} from "vue";
import ceIcon from "@commons/components/ce-icon/index.vue";
import type {AccountJobRecord} from "@commons/api/cloud_account/type";

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
defineExpose({open, close});
</script>
<style lang="scss"></style>
