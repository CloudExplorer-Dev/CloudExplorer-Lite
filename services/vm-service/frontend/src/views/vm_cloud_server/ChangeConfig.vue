<script setup lang="ts">
import { computed, ref, watch, h } from "vue";
import VmCloudServerApi from "@/api/vm_cloud_server";
import BaseCloudAccountApi from "@commons/api/cloud_account";
import type { VmCloudServerVO } from "@/api/vm_cloud_server/type";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import type { FormInstance } from "element-plus";
import { useI18n } from "vue-i18n";
import DetailPage from "@commons/components/detail-page/index.vue";
import type { SimpleMap } from "@commons/api/base/type";
import IpArray from "@commons/components/detail-page/IpArray.vue";
import PlatformIcon from "@commons/components/detail-page/PlatformIcon.vue";
import { ElMessageBox } from "element-plus";
const { t } = useI18n();

const id = ref<string>();
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
    newInstanceType: changeConfigData.value.newInstanceType,
    instanceChargeType: vmCloudServer.value?.instanceChargeType,
  };
});

const vmInfo = ref();

const handleSave = (formEl: FormInstance) => {
  formEl.validate().then(() => {
    ElMessageBox.confirm(
      t(
        "vm_cloud_server.message_box.confirm_config_update",
        "配置变更将会对实例执行关机操作，确认继续"
      ),
      t("commons.message_box.prompt", "提示"),
      {
        confirmButtonText: t("commons.message_box.confirm", "确认"),
        cancelButtonText: t("commons.btn.cancel", "取消"),
        type: "warning",
      }
    ).then(() => {
      if (vmCloudServer.value) {
        VmCloudServerApi.changeServerConfig(
          {
            id: vmCloudServer.value.id,
            // todo 解决报错 changeConfigData里面就有所需要的newInstanceType
            newInstanceType: "",
            ...changeConfigData.value,
          },
          loading
        ).then(() => {
          close();
        });
      }
    });
  });
};
const changeConfigData = ref<SimpleMap<any>>({});

watch(
  () => changeConfigData,
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
  },
  {
    deep: true,
  }
);
const drawer = ref<boolean>(false);
const close = () => {
  drawer.value = false;
};
const open = (cloudServerId: string) => {
  // 初始化表单对象
  changeConfigData.value = {};
  // 初始化渲染对象
  configUpdateFormData.value = {};
  // 初始化配置费用数据
  configUpdatePrice.value = undefined;
  // 初始化云账号数据
  cloudAccount.value = undefined;
  // 打开抽屉
  drawer.value = true;
  // 保存虚拟机id
  id.value = cloudServerId;
  // 打开转圈
  loading.value = true;
  // 获取云主机详情
  VmCloudServerApi.getVmCloudServerById(id.value)
    .then((res) => {
      vmCloudServer.value = res.data;
      // 主机信息要展示的内容
      vmInfo.value = [
        {
          label: t("vm_cloud_server.label.cloudVm", "云主机"),
          value: vmCloudServer.value.instanceName,
        },
        {
          label: "云账号",
          render: () => {
            return h(
              "div",
              {
                style:
                  "display:flex;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;",
              },
              [
                h(PlatformIcon, {
                  platform: res.data.platform as string,
                  style: "margin-left:0",
                }),
                h("div", {
                  style:
                    "margin:0 14px;overflow: hidden;text-overflow: ellipsis;",
                  innerHTML: cloudAccount.value?.name,
                }),
              ]
            );
          },
        },
        {
          label: "实例规格",
          value: `${vmCloudServer.value.instanceType}(${vmCloudServer.value.instanceTypeDescription})`,
        },
        {
          label: "IP地址",
          value: vmCloudServer.value.ipArray,
          hideValue: true,
          render: () => {
            return h(IpArray, {
              ipArray: vmCloudServer.value?.ipArray || [],
              remoteIp: vmCloudServer.value?.remoteIp || "",
            });
          },
        },
      ];
      return res;
    })
    .then((res) => {
      // 获取云账号详情
      return BaseCloudAccountApi.getCloudAccount(
        res.data.accountId as string
      ).then((res) => {
        cloudAccount.value = res.data;
        return res;
      });
    })
    .then((res) => {
      // 获取相应云平台配置变更表单数据
      return VmCloudServerApi.getConfigUpdateForm(
        res.data.platform,
        loadingInstanceType
      ).then((res) => {
        configUpdateFormData.value = res.data;
      });
    })
    .finally(() => {
      loading.value = false;
    });
};
const labelPosition = computed(() => {
  if (cloudAccount.value?.platform === "fit2cloud_vsphere_platform") {
    return "left";
  }
  return "top";
});
defineExpose({ open, close });
</script>

<template>
  <div>
    <el-drawer
      style="--ce-base-container-form-width: 100%"
      v-model="drawer"
      title="配置变更"
      direction="rtl"
      size="840px"
      :before-close="close"
    >
      <el-alert
        title=""
        type="warning"
        :closable="false"
        style="height: 40px"
        show-icon="true"
      >
        <template #title>
          <span style="color: #1f2329; font-size: 14px"
            >配置变更将会对实例执行 [关机] 操作</span
          >
        </template>
      </el-alert>

      <base-container
        v-loading="loading"
        style="--ce-base-container-height: auto; margin-top: 24px"
      >
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
              <span>{{
                $t("vm_cloud_server.btn.change_config", "配置变更")
              }}</span>
            </template>
            <template #content>
              <CeForm
                ref="formRef"
                width="100%"
                require-asterisk-position="right"
                label-suffix=""
                label-width=""
                :label-position="labelPosition"
                v-if="configUpdateFormData?.forms"
                v-model="changeConfigData"
                :formViewData="configUpdateFormData.forms"
                :otherParams="otherParams"
              ></CeForm>
            </template>
          </base-container>
        </template>
      </base-container>
      <template #footer>
        <div class="price-container">
          <div class="price" v-loading="loadingPrice">
            <span v-if="configUpdatePrice != null">
              配置费用：<span class="price-value">{{ configUpdatePrice }}</span>
            </span>
          </div>
          <div>
            <el-button @click="close()"
              >{{ $t("commons.btn.cancel") }}
            </el-button>
            <el-button type="primary" @click="handleSave(formRef)"
              >确认变更
            </el-button>
          </div>
        </div>
      </template>
    </el-drawer>
  </div>
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
    font-weight: 400;
    font-size: 14px;
    line-height: 22px;
    color: #1f2329;
    padding-left: 16px;
    .price-value {
      font-weight: 500;
      font-size: 18px;
      line-height: 22px;
      color: #f54a45;
    }
  }
}
</style>
