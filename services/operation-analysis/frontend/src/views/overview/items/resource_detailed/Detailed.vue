<script lang="ts" setup>
import CountGroup from "./CountGroup.vue";
import CountTable from "./CountTable.vue";
const props = defineProps<{
  cloudAccountId?: string | undefined;
}>();
</script>
<template>
  <el-card shadow="never" class="info-card">
    <CountGroup :cloud-account-id="props.cloudAccountId" />
    <CountTable :cloud-account-id="props.cloudAccountId" />
  </el-card>
</template>
