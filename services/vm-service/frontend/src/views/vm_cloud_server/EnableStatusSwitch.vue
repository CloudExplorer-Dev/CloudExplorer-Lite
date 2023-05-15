<script setup lang="ts">
import { computed, ref, watch } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useI18n } from "vue-i18n";
import type { VmCloudServerVO } from "@/api/vm_cloud_server/type";
import VmCloudServerApi from "@/api/vm_cloud_server";
const { t } = useI18n();

const props = defineProps<{
  finalFunction: any;
  functionProps: VmCloudServerVO;
}>();

const loading = ref<boolean>(false);

const _disable = ref<boolean>(false);
watch(
  () => props.functionProps.instanceStatus,
  () => {
    _disable.value =
      props.functionProps.instanceStatus != "Running" &&
      props.functionProps.instanceStatus != "Stopped";
  }
);
const _instanceStatus = computed(() => {
  return (
    props.functionProps.instanceStatus === "Running" ||
    props.functionProps.instanceStatus === "Stopping"
  );
});
/**
 * 验证VMTools状态
 * @param vm
 */
const checkVmToolsStatus = (vm: VmCloudServerVO) => {
  if (vm.platform === "fit2cloud_vsphere_platform") {
    return vm.vmToolsStatus == "guestToolsRunning";
  }
  return true;
};
/**
 * 开机
 * @param row
 */
const powerOn = (row: VmCloudServerVO) => {
  ElMessageBox.confirm(
    t("vm_cloud_server.message_box.confirm_power_on", "确认启动"),
    t("commons.message_box.prompt", "提示"),
    {
      confirmButtonText: t("commons.message_box.confirm", "确认"),
      cancelButtonText: t("commons.btn.cancel", "取消"),
      type: "warning",
    }
  )
    .then(() => {
      VmCloudServerApi.powerOn(row.id as string)
        .then((res) => {
          ElMessage.success(t("commons.msg.op_success"));
          _disable.value = true;
          loading.value = false;
          refresh();
        })
        .catch((err) => {
          loading.value = false;
          ElMessage.error(err.response.data.message);
        });
    })
    .catch(() => {
      loading.value = false;
    });
};
//关机
const shutdown = (row: VmCloudServerVO) => {
  let label = t("vm_cloud_server.message_box.confirm_shutdown", "确认关机");
  let powerOff = false;
  if (!checkVmToolsStatus(row)) {
    label = t(
      "vm_cloud_server.message_box.check_vm_tools_status_confirm_shutdown",
      "当前虚拟机未安装VmTools或VmTools未运行，无法软关机，若继续操作则关闭电源，是否继续？"
    );
    powerOff = true;
  }
  ElMessageBox.confirm(label, t("commons.message_box.prompt", "提示"), {
    confirmButtonText: t("commons.message_box.confirm", "确认"),
    cancelButtonText: t("commons.btn.cancel", "取消"),
    type: "warning",
  })
    .then(() => {
      if (powerOff) {
        VmCloudServerApi.powerOff(row.id as string)
          .then((res) => {
            _disable.value = true;
            loading.value = false;
            refresh();
          })
          .catch((err) => {
            loading.value = false;
            ElMessage.error(err.response.data.message);
          });
      } else {
        VmCloudServerApi.shutdownInstance(row.id as string)
          .then((res) => {
            _disable.value = true;
            loading.value = false;
            refresh();
          })
          .catch((err) => {
            loading.value = false;
            ElMessage.error(err.response.data.message);
          });
      }
    })
    .catch(() => {
      loading.value = false;
    });
};
/**
 * 开关机
 */
const handleSwitchStatus = (row: VmCloudServerVO) => {
  if (!_disable.value) {
    loading.value = true;
    _instanceStatus.value ? shutdown(row) : powerOn(row);
  }
};
</script>

<template>
  <el-switch
    :disabled="_disable"
    v-model="_instanceStatus"
    :loading="loading"
    @click.prevent="handleSwitchStatus(functionProps)"
  />
</template>

<style lang="scss" scoped></style>
