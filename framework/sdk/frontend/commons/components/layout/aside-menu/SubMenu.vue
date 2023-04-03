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
    v-if="menuInfo.children"
    v-hasPermission="menuInfo.requiredPermissions"
    :index="menuInfo.path"
    :key="menuInfo.name"
  >
    <template #title>
      <CeIcon
        class="icon"
        :code="menuInfo.icon"
        :size="'var(--ce-menu-icon-size,14px)'"
      />
      <span>{{ menuInfo.title }}</span>
    </template>
    <template v-for="item in menuInfo.children" :key="item.name">
      <template v-if="!item.children || item.children.length === 0">
        <el-menu-item
          v-hasPermission="item.requiredPermissions"
          :index="item.path"
          :key="item.name"
          class="mMenuItem mSubMenuItem"
        >
          <CeIcon
            class="icon"
            :code="item.icon"
            :size="'var(--ce-menu-icon-size,14px)'"
          />
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
    class="mMenuItem"
  >
    <CeIcon
      class="icon"
      :code="menuInfo.icon"
      :size="'var(--ce-menu-icon-size,14px)'"
    />
    <span>{{ menuInfo.title }}</span>
  </el-menu-item>
</template>
<style lang="scss" scoped>
.icon {
  width: var(--ce-menu-icon-width, 30px);
}
.mMenuItem.is-active {
  border-left-width: 4px;
  border-left-color: var(--ce-star-menu-active-icon-color);
  border-left-style: solid;
  padding-left: 16px !important;
}
.mSubMenuItem.is-active {
  padding-left: 36px !important;
}
</style>
