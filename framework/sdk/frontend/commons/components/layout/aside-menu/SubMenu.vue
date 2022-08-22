<script setup lang="ts">
import { defineProps } from "vue";
import CeIcon from "../../ce-icon/index.vue";
defineProps({
  menuInfo: {
    type: Object,
    default: () => ({}),
  },
});
</script>
<template>
  <el-sub-menu :index="menuInfo.path" :key="menuInfo.name">
    <template #title>
      <CeIcon :code="menuInfo.icon"></CeIcon>
      <span>{{ menuInfo.title }}</span></template
    >
    <template v-for="item in menuInfo.children" :key="item.name">
      <template v-if="!item.children || item.children.length === 0">
        <el-menu-item
          v-hasPermission="item.requiredPermissions"
          :index="item.path"
          :key="item.name"
        >
          <CeIcon :code="item.icon"></CeIcon>
          {{ item.title }}
        </el-menu-item>
      </template>
      <template v-else-if="item.children.length > 0">
        <sub-menu :menu-info="item" />
      </template>
    </template>
  </el-sub-menu>
</template>
