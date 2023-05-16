<script setup lang="ts">
import { useI18n } from "vue-i18n";
import CeIcon from "@commons/components/ce-icon/index.vue";
import InstanceStatusUtils from "@commons/utils/vm_cloud_server/InstanceStatusUtils";

const { t } = useI18n();
const props = defineProps<{
  instanceStatus: string;
}>();
</script>
<template>
  <div style="margin-right: 7px">
    <CeIcon
      :code="InstanceStatusUtils.getInstanceStatus(props.instanceStatus)?.icon"
      :color="
        InstanceStatusUtils.getInstanceStatus(props.instanceStatus)?.color
      "
      size="1em"
      :is-loading="
        props.instanceStatus === 'Starting' ||
        props.instanceStatus === 'Stopping' ||
        props.instanceStatus === 'Rebooting' ||
        props.instanceStatus === 'Deleting' ||
        props.instanceStatus === 'Creating' ||
        props.instanceStatus === 'ConfigChanging'
      "
    >
    </CeIcon>
  </div>
  <!--  <el-tag class="tag" v-if="instanceStatus">-->
  <!--    {{ InstanceStatusUtils.getStatusName(instanceStatus) }}-->
  <!--    <el-icon-->
  <!--      v-show="-->
  <!--        instanceStatus === 'Starting' ||-->
  <!--        instanceStatus === 'Stopping' ||-->
  <!--        instanceStatus === 'Rebooting' ||-->
  <!--        instanceStatus === 'Deleting' ||-->
  <!--        instanceStatus === 'Creating'-->
  <!--      "-->
  <!--      class="is-loading"-->
  <!--      ><Loading-->
  <!--    /></el-icon>-->
  <!--  </el-tag>-->
</template>
<style lang="scss" scoped>
.tag {
  margin-right: 5px;
  margin-top: -7px;
}
</style>
