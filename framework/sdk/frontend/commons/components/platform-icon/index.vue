<template>
  <div class="platform-wapper" :innerHTML="platformIconS"></div>
</template>
<script setup lang="ts">
import { computed } from "vue";
import { usePlatformStore } from "@commons/stores/modules/platform";
import type { Platform } from "@commons/api/cloud_account/type";
const platformStore = usePlatformStore();

const platformIconS = computed(() => {
  const find: Platform = platformStore.platforms.find(
    (p: Platform) => p.field === props.platform
  );
  if (find) {
    return props.type == "icon" ? find.icon : find.logo;
  }
  return "";
});
const props = withDefaults(
  defineProps<{
    type?: "logo" | "icon";
    /**
     * 云平台
     */
    platform: string;
  }>(),
  { type: "icon" }
);
</script>
<style lang="scss" scoped>
.platform-wapper {
  width: 16px;
  height: 16px;
}
</style>
