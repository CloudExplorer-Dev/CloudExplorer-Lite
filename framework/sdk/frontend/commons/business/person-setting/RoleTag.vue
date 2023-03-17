<script setup lang="ts">
import type { Role } from "@commons/api/role/type";

const props = withDefaults(
  defineProps<{
    role: Role;
    defaultTag?: boolean;
  }>(),
  {}
);
</script>

<template>
  <div
    class="role-tag"
    :class="{
      'extend-role': props.role.type !== 'origin' && !defaultTag,
      default: defaultTag,
    }"
  >
    {{ props.role.name }}
  </div>
</template>

<style scoped lang="scss">
.role-tag {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
  border-radius: 2px;
  line-height: 20px;
  font-size: 12px;
  margin-left: 4px;
  padding: 1px 6px;
  width: fit-content;
  background-color: rgba(51, 112, 255, 0.2);
  color: var(--el-color-primary);
}

.role-tag.default {
  background-color: rgba(255, 255, 255, 0.2);
  color: #ffffff;
}

.role-tag.extend-role {
  background: rgba(255, 136, 0, 0.2);
  color: rgba(255, 136, 0, 1);
}
</style>
