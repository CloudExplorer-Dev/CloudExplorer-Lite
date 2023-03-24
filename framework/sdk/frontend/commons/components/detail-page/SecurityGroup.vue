<script setup lang="ts">
import { isNotEmpty } from "@commons/utils/util";

const props = defineProps<{
  securityGroupIds: string;
}>();
</script>
<template>
  <el-dropdown :hide-on-click="false" max-height="100px">
    <span
      v-if="isNotEmpty(securityGroupIds)"
      style="cursor: pointer; color: var(--el-color-primary)"
      >{{ JSON.parse(securityGroupIds).length }}</span
    >
    <span v-else>-</span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item
          v-for="(item, index) in JSON.parse(securityGroupIds)"
          :key="index"
        >
          <span>{{ item }}</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>
<style lang="scss" scoped></style>
