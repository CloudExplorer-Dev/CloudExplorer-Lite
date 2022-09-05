<script setup lang="ts">
import CeIcon from "@commons/components/ce-icon/index.vue";

defineProps({
  menuInfo: {
    type: Object,
    default: () => ({}),
  },
});
</script>
<template>
  <el-sub-menu
    :index="menuInfo.path"
    :key="menuInfo.name"
    v-if="menuInfo.children"
  >
    <template #title>
      <CeIcon
        class="icon"
        :code="menuInfo.icon"
        :size="'var(--ce-menu-icon-size,14px)'"
      ></CeIcon>
      <span>{{ menuInfo.title }}</span></template
    >
    <template v-for="item in menuInfo.children" :key="item.name">
      <template v-if="!item.children || item.children.length === 0">
        <el-menu-item
          v-hasPermission="item.requiredPermissions"
          :index="item.path"
          :key="item.name"
        >
          <CeIcon
            class="icon"
            :code="item.icon"
            :size="'var(--ce-menu-icon-size,14px)'"
          ></CeIcon>
          <span>{{ item.title }}</span>
        </el-menu-item>
      </template>
      <template v-else-if="item.children.length > 0">
        <sub-menu :menu-info="item" />
      </template>
    </template>
  </el-sub-menu>
  <el-menu-item
    v-else
    v-hasPermission="menuInfo.requiredPermissions"
    :index="menuInfo.path"
  >
    <CeIcon
      class="icon"
      :code="menuInfo.icon"
      :size="'var(--ce-menu-icon-size,14px)'"
    ></CeIcon>
    <span>{{ menuInfo.title }}</span>
  </el-menu-item>
</template>
<style lang="scss" scoped>
.icon {
  width: var(--ce-menu-icon-width, 30px);
}
</style>
