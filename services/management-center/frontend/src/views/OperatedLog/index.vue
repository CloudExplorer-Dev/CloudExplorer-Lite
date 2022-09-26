<script setup lang="ts"></script>
<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <router-view></router-view>
  </layout-content>
</template>

<style lang="scss"></style>
