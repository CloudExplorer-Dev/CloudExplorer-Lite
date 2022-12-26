<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue";
import VmCloudServerApi from "@/api/vm_cloud_server";
import BaseCloudAccountApi from "@commons/api/cloud_account";
import { useRouter } from "vue-router";
import type { VmCloudServerVO } from "@/api/vm_cloud_server/type";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import type { FormInstance } from "element-plus";
import { useI18n } from "vue-i18n";
const { t } = useI18n();

const router = useRouter();
const id = ref<string>(router.currentRoute.value.params.id as string);
const loading = ref(false);
const vmCloudServer = ref<VmCloudServerVO>();
const cloudAccount = ref<CloudAccount>();
const formRef = ref<any>();
const configUpdateFormData = ref();
const otherParams = computed(() => {
  return {
    ...cloudAccount.value,
    regionId: vmCloudServer.value?.region,
    zoneId: vmCloudServer.value?.zone,
    instanceUuid: vmCloudServer.value?.instanceUuid,
    currentInstanceType: vmCloudServer.value?.instanceType,
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

        // 获取相应云平台配置变更表单数据
        VmCloudServerApi.getConfigUpdateForm(
          cloudAccount.value.platform,
          loading
        ).then((res) => {
          configUpdateFormData.value = res.data;
        });
      });
    });
  }
});

const handleCancel = () => {
  router.push({ name: "vm_cloud_server" });
};

const handleSave = (formEl: FormInstance) => {
  formEl.validate().then(() => {
    const dataInfo = {
      id: vmCloudServer.value?.id,
      ...formRef.value.getFormData(),
    };
    VmCloudServerApi.changeServerConfig(dataInfo, loading).then(() => {
      router.push({ name: "vm_cloud_server" });
    });
  });
};
</script>

<template>
  <layout-container :border="false">
    <template #content>
      <layout-container>
        <template #header>
          <h4>{{ $t("vm_cloud_server.label.info", "主机信息") }}</h4>
        </template>
        <template #content>
          <div style="display: flex; justify-content: space-between">
            <div>
              <span>
                {{ $t("vm_cloud_server.label.cloudVm", "云主机") }}：
              </span>
              <span>
                {{ vmCloudServer?.instanceName }}
              </span>
            </div>
            <div>
              <span>
                {{ $t("commons.cloud_account.native", "云账号") }}：
              </span>
              <span>
                {{ vmCloudServer?.accountName }}
              </span>
            </div>
            <div>
              <span> {{ $t("commons.workspace", "工作空间") }}： </span>
              <span>
                {{ vmCloudServer?.workspaceName }}
              </span>
            </div>
          </div>
        </template>
      </layout-container>

      <layout-container>
        <template #header>
          <h4>{{ $t("vm_cloud_server.btn.change_config", "配置变更") }}</h4>
        </template>
        <template #content>
          <div style="display: flex; justify-content: space-around">
            <el-card class="box-card">
              <template #header>
                <div class="card-header">
                  <span>{{
                    $t("vm_cloud_server.label.current_config", "当前配置")
                  }}</span>
                </div>
              </template>
              <div
                style="
                  display: flex;
                  justify-content: space-evenly;
                  align-items: center;
                  min-height: 100px;
                "
              >
                <span
                  >{{
                    $t("vm_cloud_server.label.instance_type", "实例规格")
                  }}：</span
                >
                <span>{{ vmCloudServer?.instanceType }}</span>
                <span
                  v-if="
                    vmCloudServer?.instanceType !=
                    vmCloudServer?.instanceTypeDescription
                  "
                  >{{ vmCloudServer?.instanceTypeDescription }}</span
                >
              </div>
            </el-card>

            <el-card class="box-card" v-loading="loading">
              <template #header>
                <div class="card-header">
                  <span>{{
                    $t("vm_cloud_server.label.new_config", "变更后配置")
                  }}</span>
                </div>
              </template>
              <div
                style="
                  display: flex;
                  justify-content: space-evenly;
                  align-items: center;
                  min-height: 100px;
                "
                v-if="configUpdateFormData?.forms"
              >
                <CeForm
                  ref="formRef"
                  :formViewData="configUpdateFormData.forms"
                  :otherParams="otherParams"
                ></CeForm>
              </div>
            </el-card>
          </div>
        </template>
      </layout-container>

      <layout-container>
        <el-button @click="handleCancel()"
          >{{ $t("commons.btn.cancel") }}
        </el-button>
        <el-button type="primary" @click="handleSave(formRef)"
          >{{ $t("commons.btn.save") }}
        </el-button>
      </layout-container>
    </template>
  </layout-container>
</template>

<style lang="scss" scoped>
.box-card {
  width: 400px;
}
.card-header {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
}
:deep(.el-card__header) {
  background-color: var(--el-color-primary-light-9);
}
.item {
  margin-bottom: 18px;
}
</style>
