<script setup lang="ts">
import { h, onMounted, ref } from "vue";
import VmCloudServerApi from "@/api/vm_cloud_server";
import _ from "lodash";
import { filterChargeType } from "@commons/utils/util";
import VmServerStatusIcon from "@/views/vm_cloud_server/VmServerStatusIcon.vue";
import IpArray from "@commons/components/detail-page/IpArray.vue";
import SecurityGroup from "@commons/components/detail-page/SecurityGroup.vue";
import PlatformIcon from "@commons/components/platform-icon/index.vue";
const loading = ref<boolean>(false);
const props = defineProps<{
  id: string;
}>();

const infoVmCloudServer = ref<any>({});
const basicInfo = ref();
const networkInfo = ref();

onMounted(() => {
  VmCloudServerApi.getVmCloudServerById(props.id, loading).then((res) => {
    infoVmCloudServer.value = _.cloneDeep(res.data);
    /**
     * 基本信息展示内容
     */
    basicInfo.value = [
      { label: "云主机ID", value: infoVmCloudServer.value.instanceUuid },
      {
        label: "名称",
        value: infoVmCloudServer.value.instanceName,
        render: () => {
          return h(VmServerStatusIcon, {
            status: infoVmCloudServer.value.instanceStatus,
            style: { marginRight: "8px" },
          });
        },
      },
      {
        label: "云账号",
        value: infoVmCloudServer.value.accountName,
        render: () => {
          return h(PlatformIcon, {
            platform: infoVmCloudServer.value.platform,
            style: { marginRight: "8px" },
          });
        },
      },
      { label: "区域/数据中心", value: infoVmCloudServer.value.region },
      {
        label: "实例规格",
        value: infoVmCloudServer.value.instanceTypeDescription,
      },
      { label: "规格名称", value: infoVmCloudServer.value.instanceType },
      { label: "操作系统", value: infoVmCloudServer.value.osInfo },
      { label: "可用区/集群", value: infoVmCloudServer.value.zone },
      {
        label: "付费方式",
        value: filterChargeType(infoVmCloudServer.value.instanceChargeType),
      },
      { label: "组织", value: infoVmCloudServer.value.organizationName },
      { label: "工作空间", value: infoVmCloudServer.value.workspaceName },
      { label: "创建人", value: infoVmCloudServer.value.applyUserName },
      { label: "创建时间", value: infoVmCloudServer.value.createTime },
      { label: "到期时间", value: infoVmCloudServer.value.expiredTime },
      { label: "备注", value: infoVmCloudServer.value.remark },
    ];

    /**
     * 网络信息展示内容
     */
    networkInfo.value = [
      {
        label: "IP地址",
        value: infoVmCloudServer.value.ipArray,
        hideValue: true,
        render: () => {
          return h(IpArray, {
            ipArray: infoVmCloudServer.value.ipArray,
            remoteIp: infoVmCloudServer.value.remoteIp,
          });
        },
      },
      { label: "子网", value: infoVmCloudServer.value.subnetId },
      { label: "VPC", value: infoVmCloudServer.value.vpcId },
      {
        label: "安全组",
        value: infoVmCloudServer.value.securityGroupIds,
        hideValue: true,
        render: () => {
          return h(SecurityGroup, {
            securityGroupIds: infoVmCloudServer.value.securityGroupIds,
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
        <span>基本信息</span>
      </template>
      <template #content>
        <detail-page
          :content="basicInfo"
          :item-width="'33.33%'"
          :item-bottom="'28px'"
        />
      </template>
    </base-container>
    <base-container :border="false">
      <template #header>
        <span>网络信息</span>
      </template>
      <template #content>
        <detail-page
          :content="networkInfo"
          :item-width="'33.33%'"
          :item-bottom="'28px'"
        />
      </template>
    </base-container>
  </div>
</template>

<style lang="scss" scoped></style>
