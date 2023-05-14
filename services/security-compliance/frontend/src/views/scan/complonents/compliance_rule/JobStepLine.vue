<template>
  <div id="timeline">
    <el-timeline>
      <el-timeline-item
        :color="
          item.status === 'SUCCESS'
            ? 'var(--el-color-success)'
            : item.status === 'FAILED'
            ? 'var(--el-color-error)'
            : '#fff'
        "
        :icon="
          item.status === 'SUCCESS'
            ? 'CircleCheck'
            : item.status === 'FAILED'
            ? 'CircleClose'
            : createVNode(ceIcon, {
                class: 'is-loading',
                code: 'Loading',
                style: 'color:#000;font-size:15px',
              })
        "
        :hollow="true"
        v-for="(item, index) in steps"
        :class="index === steps.length - 1 ? 'last-node' : 'node'"
        :key="index"
        :timestamp="item.name"
        placement="top"
      >
        <div v-if="item.children">
          <JobStepLine :steps="item.children"> </JobStepLine>
        </div>
        <div
          v-if="
            item.status === 'SUCCESS' &&
            item.result.data &&
            item.result.data.size !== undefined
          "
        >
          同步资源数量: {{ item.result.data.size }}
        </div>
        <div
          :style="{
            color: 'var(--el-color-error)',
          }"
          v-if="item.status === 'FAILED'"
        >
          失败:{{ item.result.message }}
        </div>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>
<script setup lang="ts">
import { createVNode } from "vue";
import ceIcon from "@commons/components/ce-icon/index.vue";
interface SetpResult {
  code: number;
  data: any;
  message: string;
}
interface Step {
  id: string;
  name: string;
  result: SetpResult;
  status: "SUCCESS" | "EXECUTING" | "FAILED";
  children: Array<Step>;
}
defineProps<{ steps: Array<Step> }>();
</script>
<style lang="scss" scoped>
:deep(.el-timeline) {
  padding: 0;
}
.node {
  :deep(> .el-timeline-item__tail) {
    display: block !important;
  }
}

.last-node {
  :deep(> .el-timeline-item__tail) {
    display: none !important;
  }
}
</style>
