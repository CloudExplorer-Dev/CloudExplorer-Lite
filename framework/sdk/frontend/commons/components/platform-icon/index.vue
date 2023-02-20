<template>
  <component
    style="margin-right: 20%; display: flex"
    :is="platformIcon[platform]?.component"
    v-bind="platformIcon[platform]?.icon"
    :color="platformIcon[platform]?.color"
    size="16px"
  ></component>
</template>
<script setup lang="ts">
import { platformIcon } from "@commons/utils/platform.ts";
defineProps<{
  /**
   * 云平台
   */
  platform: string;
}>();
</script>
<style lang="scss"></style>
