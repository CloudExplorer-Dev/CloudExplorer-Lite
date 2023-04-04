<script setup lang="ts">
import { h, onMounted, ref } from "vue";
import _ from "lodash";
import { useRouter } from "vue-router";
import type { VmCloudDiskVO } from "@/api/vm_cloud_disk/type";
import VmCloudDiskApi from "@/api/vm_cloud_disk";
import { useI18n } from "vue-i18n";
import { filterChargeType } from "@commons/utils/util";
import PlatformIcon from "@commons/components/detail-page/PlatformIcon.vue";
import VmLink from "@commons/components/detail-page/VmLink.vue";
import { DISK_TYPE, DISK_STATUS, getTextByValue } from "@/utils/constants";

const { t } = useI18n();
const router = useRouter();
const loading = ref<boolean>(false);
const vmCloudDiskDetail = ref<VmCloudDiskVO>();
const basicInfo = ref();
const vmInfo = ref();

onMounted(() => {
  const id = router.currentRoute.value.params.id;
  VmCloudDiskApi.showCloudDiskById(id as string, loading).then((res) => {
    vmCloudDiskDetail.value = _.cloneDeep(res.data);
    /**
     * 基本信息展示内容
     */
    basicInfo.value = [
      {
        label: t("vm_cloud_disk.label.disk_id", "磁盘ID"),
        value: vmCloudDiskDetail.value.diskId,
      },
      {
        label: t("commons.name", "名称"),
        value: vmCloudDiskDetail.value.diskName,
      },
      {
        label: t("commons.cloud_account.native", "云账号"),
        value: vmCloudDiskDetail.value.accountName,
        render: () => {
          return h(PlatformIcon, {
            platform: vmCloudDiskDetail.value?.platform,
          });
        },
      },
      {
        label: t("commons.status", "状态"),
        value: getTextByValue(
          DISK_STATUS.value,
          vmCloudDiskDetail.value.status
        ),
      },
      {
        label: t("vm_cloud_disk.label.size", "磁盘大小"),
        value: vmCloudDiskDetail.value.size + "GB",
      },
      {
        label: t("vm_cloud_disk.label.disk_category", "磁盘种类"),
        value: vmCloudDiskDetail.value.bootable
          ? t("vm_cloud_disk.label.system_disk")
          : t("vm_cloud_disk.label.data_disk"),
      },
      {
        label: t("vm_cloud_disk.label.disk_type", "磁盘类型"),
        value: getTextByValue(
          DISK_TYPE.value,
          vmCloudDiskDetail.value.diskType
        ),
      },
      {
        label:
          t("commons.cloud_account.region", "区域") +
          "/" +
          t("commons.cloud_account.data_center", "数据中心"),
        value: vmCloudDiskDetail.value.region,
      },
      {
        label: t("commons.charge_type.native", "付费方式"),
        value: filterChargeType(vmCloudDiskDetail.value.diskChargeType),
      },
      {
        label: t("commons.org", "组织"),
        value: vmCloudDiskDetail.value.organizationName,
      },
      {
        label: t("commons.workspace", "工作空间"),
        value: vmCloudDiskDetail.value.workspaceName,
      },
      {
        label:
          t("commons.cloud_account.zone", "可用区") +
          "/" +
          t("commons.cloud_account.cluster", "集群"),
        value: vmCloudDiskDetail.value.zone,
      },
      {
        label: t("vm_cloud_disk.label.mount_info", "挂载信息"),
        value: vmCloudDiskDetail.value.device,
      },
      {
        label: t("vm_cloud_disk.label.delete_with_instance", "随实例删除"),
        value:
          vmCloudDiskDetail.value.deleteWithInstance === "YES"
            ? t("commons.btn.yes")
            : t("commons.btn.no"),
      },
      {
        label: t("commons.create_time", "创建时间"),
        value: vmCloudDiskDetail.value.createTime,
      },
    ];

    /**
     * 所属云主机
     */
    vmInfo.value = [
      {
        label: "云主机名称",
        value: vmCloudDiskDetail.value.vmInstanceName,
        hideValue: true,
        render: () => {
          return h(VmLink, {
            serverId: vmCloudDiskDetail.value?.serverId,
            serverName: vmCloudDiskDetail.value?.vmInstanceName,
          });
        },
      },
    ];
  });
});
</script>

<template>
  <div v-loading="loading">
    <base-container>
      <template #header>
        <span>{{ $t("commons.basic_info", "基本信息") }}</span>
      </template>
      <template #content>
        <detail-page :content="basicInfo" />
      </template>
    </base-container>
    <base-container>
      <template #header>
        <span>{{ $t("vm_cloud_disk.label.vm", "所属云主机") }}</span>
      </template>
      <template #content>
        <detail-page :content="vmInfo" />
      </template>
    </base-container>
  </div>
</template>

<style lang="scss" scoped></style>
