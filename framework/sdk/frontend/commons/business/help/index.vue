<script setup lang="ts">
import { ref } from "vue";
import { findHelpLink } from "@commons/api/system_parameter";

const helpLink = ref("https://cloudexplorer.io/docs/");
findHelpLink().then((res) => {
  if (res.data) {
    helpLink.value = res.data;
  }
});
</script>

<template>
  <a target="_blank" :href="helpLink">
    <ce-icon size="19px" code="icon-maybe_outlined" />
  </a>
</template>
