<template>
  <div class="breadcrumb" v-if="$slots.breadcrumb">
    <slot name="breadcrumb"></slot>
  </div>
  <div class="top" v-if="$slots.top" :style="props.style">
    <slot name="top"></slot>
  </div>

  <div class="content" :style="props.style">
    <slot></slot>
  </div>
</template>
<script setup lang="ts">
const props = withDefaults(defineProps<{ style?: any }>(), {
  style: {},
});
</script>
<style lang="scss" scoped>
.top {
  height: var(--ce-main-top-height, 0px);
  width: calc(
    100% - var(--ce-main-breadcrumb-margin-left, 30px) -
      var(--ce-main-breadcrumb-margin-right, 30px)
  );
  display: flex;
  margin-left: var(--ce-main-breadcrumb-margin-left, 30px);
}
.breadcrumb {
  height: var(--ce-main-breadcrumb-height, 50px);
  width: calc(100% - var(--ce-main-breadcrumb-margin-left, 30px));
  display: flex;
  margin-left: var(--ce-main-breadcrumb-margin-left, 30px);
}
.content {
  position: relative;
  margin: var(--ce-main-content-margin-top, 10px)
    var(--ce-main-content-margin-left, 24px)
    var(--ce-main-content-margin-bottom, 24px)
    var(--ce-main-content-margin-right, 24px);
  padding: var(--ce-main-content-padding-top, 24px)
    var(--ce-main-content-padding-left, 24px)
    var(--ce-main-content-padding-bottom, 24px)
    var(--ce-main-content-padding-right, 24px);
  height: calc(
    100% - var(--ce-main-breadcrumb-height, 50px) -
      var(--ce-main-content-margin-top, 10px) -
      var(--ce-main-content-margin-bottom, 30px) -
      var(--ce-main-content-padding-top, 30px) -
      var(--ce-main-content-padding-bottom, 30px) -
      var(--ce-main-top-height, 0px)
  );
  width: calc(
    100% - var(--ce-main-content-margin-left, 30px) -
      var(--ce-main-content-margin-right, 30px) -
      var(--ce-main-content-padding-left, 30px) -
      var(--ce-main-content-padding-right, 30px)
  );
  background-color: var(--ce-main-content-bg-color, #fff);
  border-radius: var(--ce-main-content-border-radius, 10px);
  overflow: hidden;

  &:hover {
    overflow-y: auto;
  }
}
</style>
