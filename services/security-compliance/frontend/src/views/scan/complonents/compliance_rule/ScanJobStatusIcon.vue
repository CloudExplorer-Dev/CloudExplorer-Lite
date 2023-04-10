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
      return h("el-icon", {}, [
        h(
          "svg",
          {
            viewBox: "0 0 1024 1024",
            version: "1.1",
            style: "height:14px;width:14px",
            xmlns: "http://www.w3.org/2000/svg",
          },
          [
            h("path", {
              d: "M42.666667 512c0 259.2 210.133333 469.333333 469.333333 469.333333s469.333333-210.133333 469.333333-469.333333S771.2 42.666667 512 42.666667 42.666667 252.8 42.666667 512z",
              fill: "#F76964",
            }),
            h("path", {
              d: "M512 451.669333L385.066667 324.736a21.333333 21.333333 0 0 0-30.165334 0l-30.165333 30.165333a21.333333 21.333333 0 0 0 0 30.165334L451.669333 512l-126.933333 126.933333a21.333333 21.333333 0 0 0 0 30.165334l30.165333 30.165333a21.333333 21.333333 0 0 0 30.165334 0L512 572.330667l126.933333 126.933333a21.333333 21.333333 0 0 0 30.165334 0l30.165333-30.165333a21.333333 21.333333 0 0 0 0-30.165334L572.330667 512l126.933333-126.933333a21.333333 21.333333 0 0 0 0-30.165334l-30.165333-30.165333a21.333333 21.333333 0 0 0-30.165334 0L512 451.669333z",
              fill: "#FFFFFF",
            }),
          ]
        ),
      ]);
    },
    label: "扫描失败",
  },
  SUCCESS: {
    iconReader: () => {
      return h("el-icon", {}, [
        h(
          "svg",
          {
            viewBox: "0 0 1024 1024",
            version: "1.1",
            style: "height:14px;width:14px",
            xmlns: "http://www.w3.org/2000/svg",
          },
          [
            h("path", {
              d: "M42.666667 512c0 259.242667 210.090667 469.333333 469.333333 469.333333s469.333333-210.090667 469.333333-469.333333S771.242667 42.666667 512 42.666667 42.666667 252.757333 42.666667 512z",
              fill: "#34C724",
            }),
            h("path", {
              d: "M742.272 418.986667a29.312 29.312 0 0 0 0-41.514667l-20.736-20.736a29.354667 29.354667 0 0 0-41.514667 0l-233.173333 233.173333-86.528-86.485333a29.312 29.312 0 0 0-41.472 0l-20.736 20.736a29.312 29.312 0 0 0 0 41.472l124.416 124.458667a29.44 29.44 0 0 0 30.037333 7.082666 29.269333 29.269333 0 0 0 20.053334-8.533333l269.653333-269.653333z",
              fill: "#FFFFFF",
            }),
          ]
        ),
      ]);
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
<style lang="scss" scoped></style>
