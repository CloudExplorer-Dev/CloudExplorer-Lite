<script lang="ts" setup>
import CountGroup from "./CountGroup.vue";
import CountTable from "./CountTable.vue";
const props = defineProps<{
  cloudAccountId?: string | undefined;
}>();
</script>
<template>
  <div class="info-card">
    <CountGroup :cloud-account-id="props.cloudAccountId" />
    <CountTable :cloud-account-id="props.cloudAccountId" />
  </div>
</template>

<style scoped lang="scss">
.info-card {
  background: #ffffff;
  border-radius: 4px;
  padding: 24px;
  overflow: hidden;
}
</style>
