<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb
        :auto="true"
        :excludes="[{ field: 'name', value: 'disk_list' }]"
      ></breadcrumb>
    </template>
    <router-view></router-view>
  </layout-content>
</template>

<script setup lang="ts"></script>

<style lang="scss" scoped></style>
