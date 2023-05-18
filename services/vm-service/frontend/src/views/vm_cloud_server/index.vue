<script setup lang="ts">
import bus from "@commons/bus";
import { onBeforeMount, ref } from "vue";
const style = ref<any>();
onBeforeMount(() => {
  bus.on("update::style", (styleObj: any) => {
    style.value = styleObj;
  });
});
</script>

<template>
  <layout-content :style="style">
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <router-view></router-view>
  </layout-content>
</template>

<style lang="scss" scoped></style>
