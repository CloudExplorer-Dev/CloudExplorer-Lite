<script setup lang="ts">
import CeIcon from "@commons/components/ce-icon/index.vue";

const props = withDefaults(
  defineProps<{
    size?: string;
    iconSize?: string;
  }>(),
  {
    size: "33px",
    iconSize: "1.1em",
  }
);
</script>

<template>
  <el-button type="primary" :style="{ width: size, height: size }" circle>
    <CeIcon code="jurassic_user1" :size="iconSize" />
  </el-button>
</template>
