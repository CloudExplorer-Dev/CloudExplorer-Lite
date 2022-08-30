<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb
        :breadcrumbs="[
          { to: { name: 'org' }, title: '组织管理' },
          { to: {}, title: '修改' },
        ]"
      ></breadcrumb>
    </template>
  </layout-content>
</template>
<script setup lang="ts">
import { onMounted } from "vue";
import { useRouter } from "vue-router";
const route = useRouter();
onMounted(() => {
  console.log("xxx", route.currentRoute.value.query);
});
</script>
<style lang="scss"></style>
