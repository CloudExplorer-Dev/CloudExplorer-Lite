<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item
      :to="breadcrumb.to"
      v-for="(breadcrumb, index) in auto ? AutoBreadcrumbs : breadcrumbs"
      :key="index"
      ><h4 v-if="index === 0">{{ breadcrumb.title }}</h4>
      <h5 v-else>{{ breadcrumb.title }}</h5>
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>
<script setup lang="ts">
import { ref, watch } from "vue";
import type { Breadcrumb } from "./type";
import { useRouter } from "vue-router";
const router = useRouter();
const AutoBreadcrumbs = ref<Array<Breadcrumb>>();

const props = defineProps<{
  breadcrumbs?: Array<Breadcrumb>;
  auto?: false;
}>();
if (props.auto) {
  AutoBreadcrumbs.value = router.currentRoute.value.matched
    .filter((route) => route.meta.title)
    .map((route) => {
      return { title: route.meta.title as string, to: { path: route.path } };
    });
}
watch(router.currentRoute, () => {
  AutoBreadcrumbs.value = router.currentRoute.value.matched
    .filter((route) => route.meta.title)
    .map((route) => {
      return { title: route.meta.title as string, to: { path: route.path } };
    });
});
</script>
<style lang="scss"></style>
