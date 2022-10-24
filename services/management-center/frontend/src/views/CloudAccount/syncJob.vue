<template>
  <Job :read-only="true" :border="true" ref="job" :account-id="accountId"></Job>
</template>
<script setup lang="ts">
import Job from "@/componnets/job/Job.vue";
// import Job from "./job.vue";
import { ref } from "vue";
import { useRouter } from "vue-router";
const router = useRouter();
const accountId = ref<string>(router.currentRoute.value.params.id as string);
</script>
<style lang="scss"></style>
