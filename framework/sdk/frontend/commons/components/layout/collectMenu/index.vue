<script setup lang="ts">
defineProps({
  collapse: {
    type: Boolean,
    requied: true,
  },
});
</script>

<template>
  <transition name="collectMenu">
    <div class="collectMenu" v-show="collapse"></div>
  </transition>
</template>

<style lang="scss">
.collectMenu {
  height: 100%;
  width: 750px;
  background-color: aqua;
  position: fixed;
  z-index: -1;
  cursor: default;
  top: var(--ce-header-height);
  left: var(--ce-star-menu-hover-width);
}

/** 动画进行时的class **/
.collectMenu-enter-active,
.collectMenu-leave-active {
  transition: 0.5s;
}

.collectMenu-enter-from {
  left: -1000px;
}

.collectMenu-leave-to {
  left: -1000px;
}
.collectMenu-enter-form {
  left: 0;
}
.collectMenu-leave-form {
  left: 0;
}
</style>
