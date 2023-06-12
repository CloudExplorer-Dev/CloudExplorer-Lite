<template>
  <component :is="reader()"></component>
</template>
<script setup lang="ts">
import { h } from "vue";
const reader = () => {
  return h(
    "el-icon",
    { style: "display:flex", height: "20px", width: "20px" },
    [
      h(
        "svg",
        {
          viewBox: "0 0 1024 1024",
          version: "1.1",
          style: "height:20px;width:20px",
          xmlns: "http://www.w3.org/2000/svg",
        },
        [
          h("path", {
            d: "M512 42.666667c259.2 0 469.333333 210.133333 469.333333 469.333333s-210.133333 469.333333-469.333333 469.333333S42.666667 771.2 42.666667 512 252.8 42.666667 512 42.666667z m0 170.666666a42.666667 42.666667 0 0 0-42.666667 42.666667v213.333333H256a42.666667 42.666667 0 0 0 0 85.333334h213.333333v213.333333a42.666667 42.666667 0 0 0 85.333334 0v-213.333333h213.333333a42.666667 42.666667 0 0 0 0-85.333334h-213.333333V256a42.666667 42.666667 0 0 0-42.666667-42.666667z",
            fill: "#3370FF",
          }),
        ]
      ),
    ]
  );
};
</script>
<style lang="scss"></style>
