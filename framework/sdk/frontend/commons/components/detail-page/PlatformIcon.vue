<script setup lang="ts">
import { platformIcon } from "@commons/utils/platform";
const props = withDefaults(
  defineProps<{
    platform: string;
    style?: string | object;
    size?: string;
  }>(),
  { style: "margin-right: 5px; width: 16px; height: 16px", size: "16px" }
);
</script>
<template>
  <component
    :style="style"
    :is="platformIcon[platform]?.component"
    v-bind="platformIcon[platform]?.icon"
    :color="platformIcon[platform]?.color"
    :size="size"
    v-if="platform"
  ></component>
</template>
<style lang="scss" scoped></style>
