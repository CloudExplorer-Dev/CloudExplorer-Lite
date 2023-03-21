<script setup lang="ts">
import { onMounted, ref } from "vue";
import VmCloudServerApi from "@/api/vm_cloud_server";
import _ from "lodash";
import DetailPage from "@/views/detail-page/index.vue";

const loading = ref<boolean>(false);
const props = defineProps<{
  id: string;
}>();

const infoVmCloudServer = ref<any>({});
const basicInfo = ref();
const networkInfo = ref();

const filterChargeType = (instanceChargeType: string) => {
  let text = instanceChargeType;
  switch (instanceChargeType) {
    case "PostPaid":
      text = "按需计费";
      break;
    case "PrePaid":
      text = "包年/包月";
      break;
    case "SpotPaid":
      text = "竞价计费";
      break;
    default:
  }
  return text;
};

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
        instanceStatus: infoVmCloudServer.value.instanceStatus,
        components: ["InstanceStatus"],
      },
      {
        label: "云账号",
        value: infoVmCloudServer.value.accountName,
        platform: infoVmCloudServer.value.platform,
        components: ["PlatformIcon"],
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
      { label: "创建人", value: infoVmCloudServer.value.applyUser },
      { label: "创建时间", value: infoVmCloudServer.value.createTime },
      { label: "到期时间", value: infoVmCloudServer.value.expiredTime },
    ];

    /**
     * 网络信息展示内容
     */
    networkInfo.value = [
      {
        label: "IP地址",
        value: infoVmCloudServer.value.ipArray,
        remoteIp: infoVmCloudServer.value.remoteIp,
        hideValue: true,
        components: ["IpArray"],
      },
      { label: "子网", value: infoVmCloudServer.value.subnetId },
      { label: "VPC", value: infoVmCloudServer.value.vpcId },
      {
        label: "安全组",
        value: infoVmCloudServer.value.securityGroupIds,
        hideValue: true,
        components: ["SecurityGroup"],
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
        <DetailPage :content="basicInfo" label="label" value="value" />
      </template>
    </base-container>
    <base-container :border="false">
      <template #header>
        <span>网络信息</span>
      </template>
      <template #content>
        <DetailPage :content="networkInfo" label="label" value="value" />
      </template>
    </base-container>
  </div>
</template>

<style lang="scss" scoped></style>
