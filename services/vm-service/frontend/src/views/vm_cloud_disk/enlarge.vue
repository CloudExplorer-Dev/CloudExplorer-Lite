<script setup lang="ts">
import { ref, h, computed } from "vue";
import { useRouter } from "vue-router";
import VmCloudDiskApi from "@/api/vm_cloud_disk";
import type {
  EnlargeDiskRequest,
  VmCloudDiskVO,
} from "@/api/vm_cloud_disk/type";
import { ElMessage } from "element-plus";
import { useI18n } from "vue-i18n";
import DetailPage from "@commons/components/detail-page/index.vue";
import PlatformIcon from "@commons/components/detail-page/PlatformIcon.vue";
const router = useRouter();
const { t } = useI18n();
const id = ref<string>(router.currentRoute.value.params.id as string);
const diskInfo = ref<VmCloudDiskVO | any>({});
const newDiskSize = ref<number>();
const instanceUuid = ref<string>();

const handleSave = () => {
  if (newDiskSize.value) {
    const req: EnlargeDiskRequest = {
      id: id.value,
      instanceUuid: instanceUuid.value,
      newDiskSize: newDiskSize.value,
    };
    VmCloudDiskApi.enlarge(req).then(() => {
      close();
      ElMessage.success(t("commons.msg.op_success"));
    });
  }
};

const loading = ref(false);
const info = ref();

const drawer = ref<boolean>(false);
const open = (diskId: string) => {
  drawer.value = true;
  id.value = diskId;
  VmCloudDiskApi.showCloudDiskById(id.value, loading).then((res) => {
    diskInfo.value = res.data;
    newDiskSize.value = res.data.size;
    instanceUuid.value = res.data.instanceUuid;

    // 磁盘信息要展示的内容
    info.value = [
      {
        label: t("vm_cloud_disk.label.disk_name", "磁盘名称"),
        value: res.data.diskName,
      },
      {
        label: t("vm_cloud_disk.label.vm", "所属云主机"),
        value: res.data.vmInstanceName,
      },
      {
        label: t("commons.cloud_account.native", "云账号"),

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
                innerHTML: res.data.accountName,
              }),
            ]
          );
        },
      },
      {
        label: "磁盘大小",
        value: res.data.size + " GB",
      },
    ];
  });
};

const diskMin = computed(() => {
  return diskInfo.value?.size || 10;
});

const close = () => {
  drawer.value = false;
};
defineExpose({ open, close });
</script>

<template>
  <div>
    <el-drawer
      style="--ce-base-container-form-width: 100%"
      v-model="drawer"
      title="扩容"
      direction="rtl"
      size="840"
      :before-close="close"
    >
      <base-container
        v-loading="loading"
        style="--ce-base-container-height: auto"
      >
        <template #form>
          <base-container>
            <template #header>
              <span>{{ $t("vm_cloud_disk.label.disk_info", "磁盘信息") }}</span>
            </template>
            <template #content>
              <DetailPage :content="info" />
            </template>
          </base-container>

          <base-container>
            <template #header>
              <span>{{
                $t("vm_cloud_disk.label.change_config", "配置变更")
              }}</span>
            </template>

            <template #content>
              <el-form>
                <div class="title">
                  数据盘
                  <span class="unit">(GB)</span>
                </div>

                <el-form-item class="disk-size-content">
                  <LineNumber
                    v-number="{ min: diskMin }"
                    style="width: 200px"
                    special-step="10"
                    v-model.number="newDiskSize"
                    :readonly="diskInfo.value"
                    :min="diskMin"
                    :step="10"
                    required
                  >
                    <template #perfix>
                      <div>磁盘大小</div>
                    </template>
                  </LineNumber>
                </el-form-item>
              </el-form>
              <div class="active">
                配置变更后磁盘大小:
                <span class="active-value">{{ newDiskSize }}GB</span>
              </div>
            </template>
          </base-container>
        </template>

        <template #formFooter>
          <el-button @click="close()"
            >{{ $t("commons.btn.cancel") }}
          </el-button>
          <el-button
            type="primary"
            @click="handleSave()"
            :disabled="diskInfo.size >= (newDiskSize || -1)"
            >确认变更
          </el-button>
        </template>
      </base-container></el-drawer
    >
  </div>
</template>

<style lang="scss" scoped>
.title {
  color: #1f2329;
  font-weight: 400;
  font-size: 14px;
  line-height: 22px;
  .unit {
    color: rgba(143, 149, 158, 1);
  }
}
.active {
  font-weight: 400;
  font-size: 14px;
  line-height: 22px;
  color: #646a73;
  .active-value {
    color: rgba(51, 112, 255, 1);
  }
}
.disk-size-content {
  height: 56px;
  width: 100%;
  background: #f7f9fc;
  border-radius: 4px;
}
</style>
