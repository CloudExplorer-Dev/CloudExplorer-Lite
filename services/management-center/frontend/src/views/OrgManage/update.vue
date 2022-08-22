<template>
  <div>修改</div>
</template>
<script setup lang="ts">
import { useRouter } from "vue-router";
const route = useRouter();
console.log(route.currentRoute.value);
</script>
<style lang=""></style>
