<template>
  <Job :read-only="false" :border="true" ref="job" :account-id="id"></Job>
</template>
<script setup lang="ts">
import Job from "@/componnets/job/Job.vue";

defineProps<{
  id: string;
}>();
</script>
<style lang="scss"></style>
