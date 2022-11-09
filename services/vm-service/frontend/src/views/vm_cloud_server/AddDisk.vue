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
  };
});

onMounted(() => {
  if (router.currentRoute.value.params.id) {
    // 获取云主机详情
    VmCloudServerApi.getVmCloudServerById(id.value, loading).then((res) => {
      vmCloudServer.value = res.data;

      // 获取云账号详情
      BaseCloudAccountApi.getCloudAccount(
        vmCloudServer.value.accountId as string,
        loading
      ).then((res) => {
        cloudAccount.value = res.data;

        // 获取相应云平台创建磁盘表单数据
        VmCloudDiskApi.getCreateDiskForm(
          cloudAccount.value.platform,
          loading
        ).then((res) => {
          createDiskFormData.value = res.data;
        });
      });
    });
  }
});

const handleCancel = () => {
  router.push({ name: "vm_cloud_server" });
};

const handleCreate = () => {
  ceFormRef.value.validate();
  const dataInfo = {
    regionId: vmCloudServer.value?.region,
    ...vmCloudServer.value,
    ...cloudAccount.value,
    ...ceFormRef.value.getFormData(),
  };
  // 获取相应云平台创建磁盘表单数据
  VmCloudDiskApi.createDisk(dataInfo, loading).then(() => {
    router.push({ name: "vm_cloud_server" });
  });
};
</script>

<template>
  <layout-container :border="false" v-loading="loading">
    <template #content>
      <layout-container>
        <template #header>
          <h4>{{ $t("vm_cloud_disk.label.vm", "所属云主机") }}</h4>
        </template>
        <template #content>
          {{ $t("vm_cloud_disk.label.cloudVm", "云主机") }}：{{
            vmCloudServer?.instanceName
          }}
        </template>
      </layout-container>

      <layout-container>
        <template #header>
          <h4>{{ $t("vm_cloud_disk.label.disk_info", "磁盘信息") }}</h4>
        </template>
        <template #content v-if="createDiskFormData?.forms">
          <CeForm
            ref="ceFormRef"
            :formViewData="createDiskFormData.forms"
            :otherParams="otherParams"
          ></CeForm>
        </template>
      </layout-container>

      <layout-container>
        <el-button @click="handleCancel()"
          >{{ $t("commons.btn.cancel") }}
        </el-button>
        <el-button type="primary" @click="handleCreate()"
          >{{ $t("commons.btn.save") }}
        </el-button>
      </layout-container>
    </template>
  </layout-container>
</template>
