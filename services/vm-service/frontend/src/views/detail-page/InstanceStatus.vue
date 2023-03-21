<script setup lang="ts">
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const props = defineProps<{
  instanceStatus: string;
}>();

//状态
const instanceStatusMap: Map<string, string> = new Map();
instanceStatusMap.set("Running", t("", "运行中"));
instanceStatusMap.set("Deleted", t("", "已删除"));
instanceStatusMap.set("Stopped", t("", "已关机"));
instanceStatusMap.set("Starting", t("", "启动中"));
instanceStatusMap.set("Stopping", t("", "关机中"));
instanceStatusMap.set("Rebooting", t("", "重启中"));
instanceStatusMap.set("Deleting", t("", "删除中"));
instanceStatusMap.set("Creating", t("", "创建中"));
instanceStatusMap.set("Unknown", t("", "未知"));
instanceStatusMap.set("Failed", t("", "失败"));
</script>
<template>
  <el-tag class="tag" v-if="instanceStatus">
    {{ instanceStatusMap.get(instanceStatus) }}
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
