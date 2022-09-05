<script setup lang="ts">
import { ref } from "vue";
import type { Breadcrumb } from "./type";
const breadcrumbs = ref<Array<Breadcrumb>>([{ to: {}, title: "云账号" }]);

const addBreadcrumbs = (breadcrumbItem: Breadcrumb) => {
  breadcrumbs.value.push(breadcrumbItem);
};
const deleteBreadcrumbs = (title: string) => {
  breadcrumbs.value = breadcrumbs.value.filter((breadcrumb) => {
    return breadcrumb.title !== title;
  });
};
</script>
<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb :breadcrumbs="breadcrumbs"></breadcrumb>
    </template>
    <router-view
      :addBreadcrumbs="addBreadcrumbs"
      :deleteBreadcrumbs="deleteBreadcrumbs"
    ></router-view>
  </layout-content>
</template>

<style lang="scss"></style>
