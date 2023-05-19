<template>
  <component :is="statusMap[status].iconReader()"></component>
</template>
<script setup lang="ts">
import CeIcon from "@commons/components/ce-icon/index.vue";
import { h } from "vue";

defineProps<{
  status: string;
}>();

const statusMap = {
  Running: {
    iconReader: () => {
      return h("el-icon", { style: "display:flex" }, [
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
              d: "M512 981.333333c259.2 0 469.333333-210.133333 469.333333-469.333333S771.2 42.666667 512 42.666667 42.666667 252.8 42.666667 512s210.133333 469.333333 469.333333 469.333333z",
              fill: "#34C724",
            }),
            h("path", {
              d: "M798.634667 527.104a21.333333 21.333333 0 0 0 0-30.208l-30.165334-30.165333h-0.042666l-113.322667-113.365334a21.333333 21.333333 0 0 0-30.165333 0l-30.165334 30.208a21.333333 21.333333 0 0 0 0 30.165334L650.368 469.333333H405.333333a21.333333 21.333333 0 0 0-21.333333 21.333334v42.666666a21.333333 21.333333 0 0 0 21.333333 21.333334h245.034667l-55.594667 55.594666a21.333333 21.333333 0 0 0 0 30.165334l30.165334 30.165333a21.333333 21.333333 0 0 0 30.165333 0l113.322667-113.322667 30.208-30.165333zM234.666667 469.333333a21.333333 21.333333 0 0 0-21.333334 21.333334v42.666666a21.333333 21.333333 0 0 0 21.333334 21.333334h42.666666a21.333333 21.333333 0 0 0 21.333334-21.333334v-42.666666a21.333333 21.333333 0 0 0-21.333334-21.333334h-42.666666z",
              fill: "#FFFFFF",
            }),
          ]
        ),
      ]);
    },
  },
  Deleted: {
    iconReader: () => {
      return h(CeIcon, {
        code: "icon_deleting",
        size: "14px",
        color: "#6E748E",
        style: "display:flex",
      });
    },
  },
  Stopped: {
    iconReader: () => {
      return h(CeIcon, {
        code: "guanji-",
        size: "14px",
        color: "#000000",
        style: "display:flex",
      });
    },
  },
  Starting: {
    iconReader: () => {
      return h(CeIcon, {
        class: "is-loading",
        code: "shuaxin4",
        size: "14px",
        color: "#34C724",
        style: "display:flex",
      });
    },
  },
  Stopping: {
    iconReader: () => {
      return h(CeIcon, {
        class: "is-loading",
        code: "shuaxin4",
        size: "14px",
        color: "#000000",
        style: "display:flex",
      });
    },
  },
  Rebooting: {
    iconReader: () => {
      return h(CeIcon, {
        class: "is-loading",
        code: "shuaxin4",
        size: "14px",
        color: "#34C724",
        style: "display:flex",
      });
    },
  },
  Deleting: {
    iconReader: () => {
      return h(CeIcon, {
        class: "is-loading",
        code: "shuaxin4",
        size: "14px",
        color: "#6E748E",
        style: "display:flex",
      });
    },
  },
  Creating: {
    iconReader: () => {
      return h(CeIcon, {
        class: "is-loading",
        code: "icon_testing",
        size: "14px",
        color: "#3370FF",
        style: "display:flex",
      });
    },
  },
  Unknown: {
    iconReader: () => {
      return h(CeIcon, {
        code: "icon_disable",
        size: "14px",
        color: "#6E748E",
        style: "display:flex",
      });
    },
  },
  Failed: {
    iconReader: () => {
      return h("el-icon", { style: "display:flex" }, [
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
              fill: "#F54A45",
            }),
            h("path", {
              d: "M512 451.669333L385.066667 324.736a21.333333 21.333333 0 0 0-30.165334 0l-30.165333 30.165333a21.333333 21.333333 0 0 0 0 30.165334L451.669333 512l-126.933333 126.933333a21.333333 21.333333 0 0 0 0 30.165334l30.165333 30.165333a21.333333 21.333333 0 0 0 30.165334 0L512 572.330667l126.933333 126.933333a21.333333 21.333333 0 0 0 30.165334 0l30.165333-30.165333a21.333333 21.333333 0 0 0 0-30.165334L572.330667 512l126.933333-126.933333a21.333333 21.333333 0 0 0 0-30.165334l-30.165333-30.165333a21.333333 21.333333 0 0 0-30.165334 0L512 451.669333z",
              fill: "#FFFFFF",
            }),
          ]
        ),
      ]);
    },
  },
  ToBeRecycled: {
    iconReader: () => {
      return h(CeIcon, {
        code: "icon_retrieve",
        size: "14px",
        color: "#FF8802",
        style: "display:flex",
      });
    },
  },
  WaitCreating: {
    iconReader: () => {
      return h(CeIcon, {
        class: "is-loading",
        code: "shuaxin4",
        size: "14px",
        color: "#3370FF",
        style: "display:flex",
      });
    },
  },
  ConfigChanging: {
    iconReader: () => {
      return h(CeIcon, {
        class: "is-loading",
        code: "shuaxin4",
        size: "14px",
        color: "#34C724",
        style: "display:flex",
      });
    },
  },
};
</script>
<style lang="scss" scoped></style>
