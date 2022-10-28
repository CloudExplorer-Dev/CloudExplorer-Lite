<script setup lang="ts">
import { onMounted, ref } from "vue";
import VmCloudDiskApi from "@/api/vm_cloud_disk";
import _ from "lodash";
import { useRouter } from "vue-router";
import type { VmCloudDiskVO } from "@/api/vm_cloud_disk/type";
import { platformIcon } from "@/utils/platform";
import { CHARGE_TYPE } from "@/utils/constants";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const router = useRouter();
const loading = ref(false);
const contentSpan = ref(2);
const vmCloudDiskDetail = ref<VmCloudDiskVO>();
const top = ref<HTMLElement>();

onMounted(() => {
  const id = router.currentRoute.value.params.id;
  VmCloudDiskApi.showCloudDiskById(id as string, loading).then((res) => {
    vmCloudDiskDetail.value = _.cloneDeep(res.data);
  });

  // 根据窗口大小设置每行展示几列
  window.onresize = () => {
    if (top.value) {
      if (top.value.clientWidth <= 600) {
        contentSpan.value = 1;
      } else if (top.value.clientWidth >= 1000) {
        contentSpan.value = 3;
      } else {
        contentSpan.value = 2;
      }
    }
  };
});
</script>
<template>
  <layout-container :border="false">
    <template #content>
      <layout-container v-loading="loading">
        <template #header>
          <h4>{{ $t("commons.basic_info", "基本信息") }}</h4>
        </template>
        <template #content>
          <div ref="top">
            <el-descriptions :column="contentSpan" border class="small-text">
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                :label="$t('commons.name', '名称') + ':'"
                colon="true"
              >
                {{ vmCloudDiskDetail?.diskName }}
              </el-descriptions-item>
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                :label="$t('vm_cloud_disk.label.disk_id', '磁盘ID') + ':'"
                >{{ vmCloudDiskDetail?.diskId }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                :label="$t('commons.cloud_account.native', '云账号') + ':'"
              >
                <div style="display: flex">
                  <el-image
                    style="margin-top: 3px; width: 16px; height: 16px"
                    :src="platformIcon[vmCloudDiskDetail?.platform]?.icon"
                  ></el-image>
                  <span style="margin-left: 10px">{{
                    vmCloudDiskDetail?.accountName
                  }}</span>
                </div>
              </el-descriptions-item>
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                :label="$t('vm_cloud_disk.label.cloudVm', '云主机') + ':'"
                >{{ vmCloudDiskDetail?.vmInstanceName }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                :label="$t('vm_cloud_disk.label.size', '大小') + ':'"
                >{{ vmCloudDiskDetail?.size }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                :label="$t('commons.charge_type.native', '付费方式') + ':'"
                >{{
                  CHARGE_TYPE[vmCloudDiskDetail?.diskChargeType.toUpperCase()]
                }}
              </el-descriptions-item>
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                :label="
                  $t('vm_cloud_disk.label.disk_category', '磁盘种类') + ':'
                "
                >{{
                  vmCloudDiskDetail?.bootable
                    ? t("vm_cloud_disk.label.system_disk")
                    : t("vm_cloud_disk.label.data_disk")
                }}
              </el-descriptions-item>
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                :label="$t('vm_cloud_disk.label.disk_type', '磁盘类型') + ':'"
                >{{ vmCloudDiskDetail?.diskType }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                :label="
                  $t('vm_cloud_disk.label.delete_with_instance', '随实例删除') +
                  ':'
                "
                >{{
                  vmCloudDiskDetail?.deleteWithInstance === "YES"
                    ? t("commons.btn.yes")
                    : t("commons.btn.no")
                }}
              </el-descriptions-item>
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                :label="$t('commons.status', '状态') + ':'"
                >{{ vmCloudDiskDetail?.status }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                :label="$t('vm_cloud_disk.label.mount_info', '挂载信息') + ':'"
                >{{ vmCloudDiskDetail?.device }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                :label="$t('commons.workspace', '工作空间') + ':'"
                >{{ vmCloudDiskDetail?.workspaceName }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                :label="
                  $t('commons.cloud_account.region', '区域') +
                  '/' +
                  $t('commons.cloud_account.data_center', '数据中心') +
                  ':'
                "
                >{{ vmCloudDiskDetail?.region }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                :label="
                  $t('commons.cloud_account.zone', '可用区') +
                  '/' +
                  $t('commons.cloud_account.cluster', '集群') +
                  ':'
                "
                >{{ vmCloudDiskDetail?.zone }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                :label="$t('commons.create_time', '创建时间') + ':'"
                >{{ vmCloudDiskDetail?.createTime }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                :label="$t('commons.cloud_server.applicant', '申请人') + ':'"
                >-</el-descriptions-item
              >
            </el-descriptions>
          </div>
        </template>
      </layout-container>
    </template>
  </layout-container>
</template>
<style lang="scss">
.content-class {
  min-width: 230px;
  width: 300px;
  max-width: 300px;
}
.label-class {
  width: 150px;
  min-width: 150px;
}
.el-descriptions__body
  .el-descriptions__table.is-bordered
  .el-descriptions__cell {
  border: 0px;
}
.el-descriptions__label.el-descriptions__cell.is-bordered-label {
  background-color: transparent;
}
.el-descriptions__body .el-descriptions__table .el-descriptions__cell {
  font-size: 12px;
}
</style>
