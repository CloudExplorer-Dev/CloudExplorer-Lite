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
  <div style="flex: 1 1 auto"></div>
  <el-affix
    v-if="
      showReturns?.some(
        (r) => router.currentRoute.value.meta[r.field] === r.value
      )
    "
    style="padding-top: 10px; padding-right: 60px"
  >
    <el-button type="info" @click="handleReturn">
      {{ $t("commons.btn.return") }}
    </el-button>
  </el-affix>
</template>
<script setup lang="ts">
import { ref, watch } from "vue";
import type { Breadcrumb } from "./type";
import { useRouter } from "vue-router";
const router = useRouter();
const AutoBreadcrumbs = ref<Array<Breadcrumb>>();

const props = withDefaults(
  defineProps<{
    /**
     *面包屑
     */
    breadcrumbs?: Array<Breadcrumb>;
    /**
     * 是否使用路由构建面包屑
     */
    auto?: boolean;
    /**
     * 不展示的面包屑
     */
    excludes?: Array<{ field: string; value: string }>;

    /**
     * 需要携带return的页面
     */
    showReturns?: Array<{ field: string; value: string }>;
  }>(),
  {
    auto: true,
    excludes: () => [{ field: "title", value: "列表" }],
    showReturns: () => [{ field: "title", value: "详情" }],
  }
);

const handleReturn = () => {
  if (router.currentRoute.value.meta.sourceMenu) {
    router.push({ path: router.currentRoute.value.meta.sourceMenu as string });
  }
};
if (props.auto) {
  AutoBreadcrumbs.value = router.currentRoute.value.matched
    .filter((route) => {
      return !props.excludes.some((ex) => route.meta[ex.field] === ex.value);
    })
    .filter((route) => route.meta.title)
    .map((route) => {
      return { title: route.meta.title as string, to: { path: route.path } };
    });
}

watch(router.currentRoute, () => {
  AutoBreadcrumbs.value = router.currentRoute.value.matched
    .filter((route) => {
      return !props.excludes.some((ex) => route.meta[ex.field] === ex.value);
    })
    .filter((route) => route.meta.title)
    .map((route) => {
      return { title: route.meta.title as string, to: { path: route.path } };
    });
});
</script>
<style lang="scss"></style>
