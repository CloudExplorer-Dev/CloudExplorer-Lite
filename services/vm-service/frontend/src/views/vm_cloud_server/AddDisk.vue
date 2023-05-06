<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import VmCloudDiskApi from "@/api/vm_cloud_disk";
import VmCloudServerApi from "@/api/vm_cloud_server";
import BaseCloudAccountApi from "@commons/api/cloud_account";
import { useRouter } from "vue-router";
import type { VmCloudServerVO } from "@/api/vm_cloud_server/type";
import type { CloudAccount } from "@commons/api/cloud_account/type";

const router = useRouter();
const id = ref<string>(router.currentRoute.value.params.id as string);
const loading = ref(false);
const vmCloudServer = ref<VmCloudServerVO>();
const cloudAccount = ref<CloudAccount>();
const ceFormRef = ref<any>();
const createDiskFormData = ref();
const otherParams = computed(() => {
  return {
    ...cloudAccount.value,
    regionId: vmCloudServer.value?.region,
    zone: vmCloudServer.value?.zone,
    zoneId: vmCloudServer.value?.zone,
    instanceUuid: vmCloudServer.value?.instanceUuid,
    instanceTypeDTO: vmCloudServer.value,
    instanceChargeType: vmCloudServer.value?.instanceChargeType,
    instanceType: vmCloudServer.value?.instanceType,
  };
});

// 磁盘表单
const diskFormData = ref<any>({});
/**
 * 创建磁盘
 */
const handleCreate = () => {
  ceFormRef.value.validate().then(() => {
    const dataInfo = {
      regionId: vmCloudServer.value?.region,
      ...vmCloudServer.value,
      ...cloudAccount.value,
      ...diskFormData.value,
    };
    // 获取相应云平台创建磁盘API
    VmCloudDiskApi.createDisk(dataInfo, loading).then(() => {
      // 关闭
      close();
    });
  });
};
const drawer = ref<boolean>(false);
/**
 * 打开弹框
 * @param diskId 磁盘id
 */
const open = (diskId: string) => {
  // 初始化磁盘数据
  diskFormData.value = {};
  createDiskFormData.value = {};

  // 打开抽屉
  drawer.value = true;
  // 设置需要的磁盘id
  id.value = diskId;
  loading.value = true;
  // 获取云主机详情
  VmCloudServerApi.getVmCloudServerById(diskId)
    .then((res) => {
      vmCloudServer.value = res.data;
      return res.data;
    })
    .then((cloudServer) => {
      // 获取云账号详情
      return BaseCloudAccountApi.getCloudAccount(
        cloudServer.accountId as string
      )
        .then((res) => {
          cloudAccount.value = res.data;
          // 获取相应云平台创建磁盘表单数据
          VmCloudDiskApi.getCreateDiskForm(
            cloudAccount.value.platform,
            loading
          ).then((res) => {
            createDiskFormData.value = res.data;
          });
        })
        .finally(() => {
          loading.value = false;
        });
    })
    .catch(() => {
      loading.value = false;
    });
};
const close = () => {
  drawer.value = false;
};
defineExpose({ open, close });
</script>

<template>
  <el-drawer
    style="--ce-base-container-form-width: 100%"
    v-model="drawer"
    title="添加磁盘"
    direction="rtl"
    size="600px"
    :before-close="close"
  >
    <base-container
      v-loading="loading"
      style="--ce-base-container-height: auto"
    >
      <template #form>
        <base-container>
          <template #header>
            <span>{{ $t("vm_cloud_disk.label.vm", "所属云主机") }}</span>
          </template>
          <template #content>
            {{ $t("vm_cloud_disk.label.cloudVm", "云主机") }}：{{
              vmCloudServer?.instanceName
            }}
          </template>
        </base-container>

        <base-container>
          <template #header>
            <span>{{ $t("vm_cloud_disk.label.disk_info", "磁盘信息") }}</span>
          </template>
          <template #content v-if="createDiskFormData?.forms">
            <CeForm
              ref="ceFormRef"
              require-asterisk-position="right"
              label-position="top"
              width="600px"
              :formViewData="createDiskFormData.forms"
              :otherParams="otherParams"
              v-model="diskFormData"
            ></CeForm>
          </template>
        </base-container>
      </template>
    </base-container>
    <template #footer>
      <el-button @click="close()">{{ $t("commons.btn.cancel") }} </el-button>
      <el-button type="primary" @click="handleCreate()"> 添加 </el-button>
    </template>
  </el-drawer>
</template>
<style lang="scss" scoped></style>
