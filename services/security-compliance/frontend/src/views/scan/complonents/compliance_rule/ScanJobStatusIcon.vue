<template>
  <scan_status
    v-if="statusMap[status]"
    :icon-reader="statusMap[status].iconReader"
    :label="statusMap[status].label"
  ></scan_status>
</template>
<script setup lang="ts">
import scan_status from "@/views/scan/complonents/compliance_rule/ScanStatus.vue";
import CeIcon from "@commons/components/ce-icon/index.vue";
import { h } from "vue";
withDefaults(
  defineProps<{
    status: "FAILED" | "SUCCESS" | "SYNCING" | "NOT_HAVE";
  }>(),
  {}
);

const statusMap = {
  FAILED: {
    iconReader: () => {
      return h(CeIcon, {
        code: "icon_close_filled",
        size: "14px",
        color: "rgba(247, 105, 100, 1)",
      });
    },
    label: "扫描失败",
  },
  SUCCESS: {
    iconReader: () => {
      return h(CeIcon, {
        code: "icon_succeed_filled",
        size: "14px",
        color: "rgba(52, 199, 36, 1)",
      });
    },
    label: "已完成",
  },
  SYNCING: {
    iconReader: () => {
      return h(CeIcon, {
        code: "icon_testing",
        size: "14px",
        color: "rgba(51, 112, 255, 1)",
        class: "is-loading",
      });
    },
    label: "扫描中",
  },
  NOT_HAVE: {
    iconReader: () => h("span"),
    label: "-",
  },
};
</script>
<style lang="scss"></style>
