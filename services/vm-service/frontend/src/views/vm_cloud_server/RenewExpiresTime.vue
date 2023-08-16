<template>
  <div class="renew-expires-time">{{ renewExpiresTime }}</div>
</template>
<script setup lang="ts">
import type { VmCloudServerVO } from "@/api/vm_cloud_server/type";
import VmCloudServerApi from "@/api/vm_cloud_server";
import { computed, ref, watch } from "vue";
const props = defineProps<{ instance: VmCloudServerVO; formData: any }>();

const renewExpiresTime = ref<string>("");
const loading = ref<boolean>(false);
const renewExpiresTimeRequest = computed(() => {
  return {
    ...props.formData,
    region: props.instance.region,
    accountId: props.instance.accountId,
    instanceUuid: props.instance.instanceUuid,
    expiredTime: props.instance.expiredTime,
    platform: props.instance.platform,
  };
});

watch(
  () => props.formData.periodNum,
  () => {
    VmCloudServerApi.renewExpiresTime(
      renewExpiresTimeRequest.value,
      loading
    ).then((ok) => {
      renewExpiresTime.value = ok.data;
    });
  },
  {
    deep: true,
    immediate: true,
  }
);
</script>
<style lang="scss" scoped>
.renew-expires-time {
  color: red;
}
</style>
