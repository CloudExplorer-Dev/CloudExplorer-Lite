<script setup lang="ts"></script>
<template>
  <layout-content
    style="
      --ce-main-content-padding-top: 0px;
      --ce-main-content-padding-left: 0px;
      --ce-main-content-padding-right: 0px;
      --ce-main-content-padding-bottom: 0px;
    "
  >
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <router-view></router-view>
  </layout-content>
</template>

<style lang="scss"></style>
