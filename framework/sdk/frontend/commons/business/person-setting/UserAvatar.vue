<script setup lang="ts">
import CeIcon from "@commons/components/ce-icon/index.vue";

const props = withDefaults(
  defineProps<{
    size?: string;
    iconSize?: string;
    unClickable?: boolean;
  }>(),
  {
    size: "33px",
    iconSize: "1.1em",
    unClickable: true,
  }
);
</script>

<template>
  <el-button
    type="primary"
    :style="{ width: size, height: size }"
    circle
    :class="{ unClickable: unClickable }"
  >
    <CeIcon code="jurassic_user1" :size="iconSize" />
  </el-button>
</template>

<style scoped lang="scss">
.unClickable {
  cursor: default;
}
</style>
