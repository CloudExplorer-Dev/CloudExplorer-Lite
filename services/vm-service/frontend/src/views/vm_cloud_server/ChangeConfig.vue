<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import VmCloudServerApi from "@/api/vm_cloud_server";
import BaseCloudAccountApi from "@commons/api/cloud_account";
import { useRouter } from "vue-router";
import type { VmCloudServerVO } from "@/api/vm_cloud_server/type";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import type { FormInstance } from "element-plus";
import { useI18n } from "vue-i18n";
import DetailPage from "@/views/detail-page/index.vue";

const { t } = useI18n();
const router = useRouter();
const id = ref<string>(router.currentRoute.value.params.id as string);
const loading = ref(false);
const loadingInstanceType = ref(false);
const loadingPrice = ref(false);

const vmCloudServer = ref<VmCloudServerVO>();
const cloudAccount = ref<CloudAccount>();
const formRef = ref<any>();
const configUpdateFormData = ref();
const configUpdatePrice = ref();
const otherParams = computed(() => {
  return {
    ...cloudAccount.value,
    regionId: vmCloudServer.value?.region,
    zoneId: vmCloudServer.value?.zone,
    instanceUuid: vmCloudServer.value?.instanceUuid,
    currentInstanceType: vmCloudServer.value?.instanceType,
    newInstanceType: formRef.value?.getFormData().newInstanceType,
    instanceChargeType: vmCloudServer.value?.instanceChargeType,
  };
});

const vmInfo = ref();
onMounted(() => {
  if (router.currentRoute.value.params.id) {
    // 获取云主机详情
    VmCloudServerApi.getVmCloudServerById(id.value, loading).then((res) => {
      vmCloudServer.value = res.data;

      // 主机信息要展示的内容
      vmInfo.value = [
        {
          label: t("vm_cloud_server.label.cloudVm", "云主机"),
          value: vmCloudServer.value.instanceName,
        },
        {
          label: t("commons.cloud_account.native", "云账号"),
          value: vmCloudServer.value.accountName,
          platform: res.data.platform,
          components: ["PlatformIcon"],
        },
        {
          label: t("commons.organization", "组织"),
          value: vmCloudServer.value.organizationName,
        },
        {
          label: t("commons.workspace", "工作空间"),
          value: vmCloudServer.value.workspaceName,
        },
      ];

      // 获取云账号详情
      BaseCloudAccountApi.getCloudAccount(
        vmCloudServer.value.accountId as string,
        loading
      ).then((res) => {
        cloudAccount.value = res.data;

        // 获取相应云平台配置变更表单数据
        VmCloudServerApi.getConfigUpdateForm(
          cloudAccount.value.platform,
          loadingInstanceType
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

watch(
  () => formRef.value?.getFormData().newInstanceType,
  () => {
    if (cloudAccount.value?.platform) {
      VmCloudServerApi.getConfigUpdatePrice(
        cloudAccount.value?.platform,
        { ...otherParams.value },
        loadingPrice
      ).then((ok) => {
        configUpdatePrice.value = ok.data;
      });
    }
  }
);
</script>

<template>
  <base-container v-loading="loading">
    <template #form>
      <base-container>
        <template #header>
          <span>{{ $t("vm_cloud_server.label.info", "主机信息") }}</span>
        </template>
        <template #content>
          <DetailPage :content="vmInfo" />
        </template>
      </base-container>

      <base-container>
        <template #header>
          <span>{{ $t("vm_cloud_server.btn.change_config", "配置变更") }}</span>
        </template>
        <template #content>
          <div style="display: flex">
            <el-card
              class="box-card"
              style="margin-right: var(--ce-main-content-margin-right, 24px)"
            >
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
                    vmCloudServer?.instanceType !==
                    vmCloudServer?.instanceTypeDescription
                  "
                  >{{ vmCloudServer?.instanceTypeDescription }}</span
                >
              </div>
            </el-card>

            <el-card class="box-card" v-loading="loadingInstanceType">
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
      </base-container>
    </template>

    <template #formFooter>
      <div class="price-container">
        <div class="price" v-loading="loadingPrice">
          <span v-if="configUpdatePrice != null">
            配置费用：{{ configUpdatePrice }}
          </span>
        </div>
        <div>
          <el-button @click="handleCancel()"
            >{{ $t("commons.btn.cancel") }}
          </el-button>
          <el-button type="primary" @click="handleSave(formRef)"
            >{{ $t("commons.btn.save") }}
          </el-button>
        </div>
      </div>
    </template>
  </base-container>
</template>

<style lang="scss" scoped>
.box-card {
  width: 388px;
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

.price-container {
  width: 100%;
  display: flex;
  justify-content: space-between;

  .price {
    padding-left: 60px;
  }
}
</style>
