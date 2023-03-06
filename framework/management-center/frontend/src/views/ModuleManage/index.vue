<script setup lang="ts"></script>

<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb
        :auto="true"
        :excludes="[{ field: 'name', value: 'module_manage_page' }]"
      ></breadcrumb>
    </template>
    <router-view></router-view>
  </layout-content>
</template>

<style lang="scss" scoped></style>
