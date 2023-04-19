<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useUserStore } from "@commons/stores/modules/user";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import CollectMenu from "@commons/components/layout/collect-menu/index.vue";
const emit = defineEmits(["update:modelValue", "close"]);
// 模块存储
const moduleStore = useModuleStore();
// 权限存储
const permissionStore = usePermissionStore();
// 用户存储
const userStore = useUserStore();

const props = withDefaults(
  defineProps<{
    modelValue: boolean;
  }>(),
  {
    modelValue: false,
  }
);
const module = ref();
const offsetLeft = ref<number>(0);
onMounted(() => {
  offsetLeft.value = module.value.parentElement.offsetLeft;
});
const close = () => {
  emit("update:modelValue", false);
};
</script>
<template>
  <div
    ref="module"
    v-show="props.modelValue"
    class="el-overlay"
    style="z-index: 2011; top: var(--ce-header-height)"
  >
    <transition name="module_content">
      <div
        class="module_content"
        v-show="props.modelValue"
        :style="{ left: offsetLeft + 'px' }"
      >
        <CollectMenu
          @close="close"
          :runningModules="moduleStore.runningModules"
          :runningModuleMenus="moduleStore.runningMenus"
          :runningModulePermissions="permissionStore.userPermissions"
          :currentRole="userStore.currentRole"
        ></CollectMenu>
      </div>
    </transition>
  </div>
</template>
<style lang="scss" scoped>
.module_content {
  width: 860px;
  height: 790px;
  overflow-y: hidden;
  overflow-x: hidden;
  position: fixed;
  cursor: default;
  top: var(--ce-header-height);
  left: 195px;
  z-index: 10000;
}
/** 动画进行时的class **/
.module_content-enter-active,
.module_content-leave-active {
  transition: 0.3s;
}

.module_content-enter-from {
  top: -1000px;
}

.module_content-leave-to {
  top: -1000px;
}
.module_content-enter-form {
  top: 0;
}
.module_content-leave-form {
  top: 0;
}
</style>
