<template>
  <template v-if="$attrs?.modelValue !== undefined">
    <el-switch v-bind="$attrs" />
  </template>
</template>
<script setup lang="ts"></script>
<style lang="scss"></style>
