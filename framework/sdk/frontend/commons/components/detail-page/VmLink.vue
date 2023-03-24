<script setup lang="ts">
import { useRouter } from "vue-router";
import { isNotEmpty } from "@commons/utils/util";

const useRoute = useRouter();
const props = defineProps<{
  serverId: string;
  serverName: string;
}>();

const jumpToServer = (serverId: string) => {
  useRoute.push({ name: "server_detail", params: { id: serverId } });
};
</script>
<template>
  <el-tooltip
    v-if="isNotEmpty(serverId)"
    :content="serverName"
    placement="top"
    effect="light"
  >
    <span class="vmLink">
      <a @click="jumpToServer(serverId)">{{ serverName }}</a>
    </span>
  </el-tooltip>
  <span v-else>-</span>
</template>
<style lang="scss" scoped>
.vmLink {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 90%;
  white-space: nowrap;
  color: var(--el-color-primary);
}
</style>
