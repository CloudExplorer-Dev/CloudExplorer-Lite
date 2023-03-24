<script setup lang="ts">
import { useI18n } from "vue-i18n";

import InstanceStatusUtils from "@commons/utils/vm_cloud_server/InstanceStatusUtils";

const { t } = useI18n();
const props = defineProps<{
  instanceStatus: string;
}>();
</script>
<template>
  <el-tag class="tag" v-if="instanceStatus">
    {{ InstanceStatusUtils.getStatusName(instanceStatus) }}
    <el-icon
      v-show="
        instanceStatus === 'Starting' ||
        instanceStatus === 'Stopping' ||
        instanceStatus === 'Rebooting' ||
        instanceStatus === 'Deleting' ||
        instanceStatus === 'Creating'
      "
      class="is-loading"
      ><Loading
    /></el-icon>
  </el-tag>
</template>
<style lang="scss" scoped>
.tag {
  margin-left: 5px;
  margin-top: -7px;
}
</style>
