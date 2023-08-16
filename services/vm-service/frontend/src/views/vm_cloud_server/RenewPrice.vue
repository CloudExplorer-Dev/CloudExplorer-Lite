<template>
  <div v-loading="loading" class="price">{{ price }}</div>
</template>
<script setup lang="ts">
import type { VmCloudServerVO } from "@/api/vm_cloud_server/type";
import { watch, computed, ref } from "vue";
import VmCloudServerApi from "@/api/vm_cloud_server";
const props = defineProps<{
  modelValue: number;
  instance: VmCloudServerVO;
  formData: any;
}>();
const emit = defineEmits(["update:modelValue"]);

const renewPriceRequest = computed(() => {
  return {
    ...props.formData,
    region: props.instance.region,
    accountId: props.instance.accountId,
    instanceUuid: props.instance.instanceUuid,
    uuid: props.instance.id,
  };
});

const loading = ref<boolean>(false);
const price = ref<number>(0);
watch(
  () => props.formData.periodNum,
  () => {
    VmCloudServerApi.renewPrice(renewPriceRequest.value, loading).then((ok) => {
      price.value = ok.data;
      emit("update:modelValue", price.value);
    });
  }
);
</script>
<style lang="scss" scoped>
.price {
  color: red;
}
</style>
