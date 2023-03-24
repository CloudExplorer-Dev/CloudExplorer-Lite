<script setup lang="ts">
import { platformIcon } from "@commons/utils/platform";
const props = defineProps<{
  platform: string;
}>();
</script>
<template>
  <component
    style="margin-left: 5px; width: 16px; height: 16px"
    :is="platformIcon[platform]?.component"
    v-bind="platformIcon[platform]?.icon"
    :color="platformIcon[platform]?.color"
    size="16px"
    v-if="platform"
  ></component>
</template>
<style lang="scss" scoped></style>
