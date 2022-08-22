<template>
  <el-breadcrumb separator="/" class="breadcrumb">
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
import { defineProps } from "vue";
import type { Breadcrumb } from "./type";
defineProps<{
  breadcrumbs: Array<Breadcrumb>;
}>();
</script>
<style lang="scss">
.breadcrumb {
  height: 50px;
  width: 100%;
  display: flex;
  margin-left: 30px;
}
</style>
