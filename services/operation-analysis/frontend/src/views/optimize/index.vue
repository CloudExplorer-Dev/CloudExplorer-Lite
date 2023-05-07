<script setup lang="ts"></script>
<template>
  <layout-auto-height-content>
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <router-view></router-view>
  </layout-auto-height-content>
</template>
