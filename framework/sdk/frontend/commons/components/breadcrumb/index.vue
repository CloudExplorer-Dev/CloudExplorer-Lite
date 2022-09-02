<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item
      :to="breadcrumb.to"
      v-for="(breadcrumb, index) in breadcrumbs"
      :key="index"
      ><h4 v-if="index === 0">{{ breadcrumb.title }}</h4>
      <h5 v-else>{{ breadcrumb.title }}</h5>
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>
<script setup lang="ts">
import type { Breadcrumb } from "./type";
defineProps<{
  breadcrumbs: Array<Breadcrumb>;
}>();
</script>
<style lang="scss"></style>
