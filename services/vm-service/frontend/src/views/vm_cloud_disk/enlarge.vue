<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import VmCloudDiskApi from "@/api/vm_cloud_disk";
import type {
  EnlargeDiskRequest,
  VmCloudDiskVO,
} from "@/api/vm_cloud_disk/type";
import { platformIcon } from "@/utils/platform";
import { ElMessage } from "element-plus";
import { useI18n } from "vue-i18n";

const router = useRouter();
const { t } = useI18n();
const id = ref<string>(router.currentRoute.value.params.id as string);
const diskInfo = ref<VmCloudDiskVO | unknown>({});
const newDiskSize = ref<number>();

const backToDiskList = () => {
  router.push({ name: "vm_cloud_disk" });
};

const handleSave = () => {
  if (newDiskSize.value) {
    const req: EnlargeDiskRequest = {
      id: id.value,
      newDiskSize: newDiskSize.value,
    };
    VmCloudDiskApi.enlarge(req).then(() => {
      backToDiskList();
      ElMessage.success(
        t("commons.msg.success", [t("vm_cloud_disk.btn.enlarge")])
      );
    });
  }
};

const loading = ref(false);
onMounted(async () => {
  if (router.currentRoute.value.params.id) {
    const res = await VmCloudDiskApi.showCloudDiskById(id.value, loading);
    diskInfo.value = res.data;
    newDiskSize.value = res.data.size;
  }
});
</script>

<template>
  <div v-loading="loading">
    <layout-container style="margin-right: 20px">
      <template #header>
        <span>{{ $t("vm_cloud_disk.label.disk_info", "磁盘信息") }}</span>
      </template>
      <template #content>
        <div class="div-flex disk_info_container">
          <div class="div-flex item">
            <div class="label">
              <label
                >{{ $t("vm_cloud_disk.label.disk_name", "磁盘名称") }}：</label
              >
            </div>
            <div class="value">
              <div>
                {{ diskInfo.diskName }}
              </div>
            </div>
          </div>
          <div class="div-flex item">
            <div class="label">
              <label>{{ $t("vm_cloud_disk.label.vm", "所属云主机") }}：</label>
            </div>
            <div class="value">
              <div>{{ diskInfo.vmInstanceName }}</div>
            </div>
          </div>
          <div class="div-flex item">
            <div class="label">
              <label
                >{{ $t("commons.cloud_account.native", "云平台") }}：</label
              >
            </div>
            <div class="value">
              <div class="div-flex">
                <span>{{ diskInfo.accountName }}</span>
                <el-image
                  style="margin-left: 10px"
                  :src="platformIcon[diskInfo.platform]?.icon"
                ></el-image>
              </div>
            </div>
          </div>
          <div class="div-flex item">
            <div class="label">
              <label>{{ $t("commons.workspace", "工作空间") }}：</label>
            </div>
            <div class="value">
              <div>{{ diskInfo.workspaceName }}</div>
            </div>
          </div>
        </div>
      </template>
    </layout-container>

    <layout-container>
      <template #header>
        <span>{{ $t("vm_cloud_disk.label.change_config", "配置变更") }}</span>
      </template>

      <template #content>
        <div class="div-flex">
          <div class="config_card">
            <div class="header">
              <span>{{
                $t("vm_cloud_disk.label.current_config", "当前配置")
              }}</span>
            </div>
            <div class="line"></div>
            <div class="div-flex content">
              <div class="label margin">
                <label
                  >{{
                    $t("vm_cloud_disk.label.disk_size", "磁盘大小")
                  }}：</label
                >
              </div>
              <div class="margin">{{ diskInfo.size }}GB</div>
            </div>
          </div>
          <div class="config_card">
            <div class="header">
              <span>{{
                $t("vm_cloud_disk.label.after_config", "变更后配置")
              }}</span>
            </div>
            <div class="line"></div>
            <div class="div-flex content">
              <div class="label margin">
                <label
                  >{{
                    $t("vm_cloud_disk.label.disk_size", "磁盘大小")
                  }}：</label
                >
              </div>
              <div class="div-flex">
                <el-form>
                  <el-form-item>
                    <el-input-number
                      v-model="newDiskSize"
                      :min="diskInfo.size"
                    />
                    <span style="padding: 0 10px">GB</span>
                  </el-form-item>
                </el-form>
              </div>
            </div>
          </div>
        </div>
      </template>
    </layout-container>

    <layout-container>
      <el-button @click="backToDiskList()"
        >{{ $t("commons.btn.cancel") }}
      </el-button>
      <el-button
        type="primary"
        @click="handleSave()"
        :disabled="diskInfo.size >= newDiskSize"
        >{{ $t("commons.btn.ok") }}
      </el-button>
    </layout-container>
  </div>
</template>

<style lang="scss" scoped>
.div-flex {
  display: flex;
}

.disk_info_container {
  width: 100%;
  padding: 0 20px;
  overflow: auto;

  .item {
    width: 25%;
    padding: 0 20px;
  }
}

.label {
  padding-right: 10px;
}

.margin {
  margin-bottom: 18px;
}

.value {
  width: 50%;
}

.config_card {
  width: 50%;
  height: 100%;
  margin: 0 20px;
  border: 1px solid var(--el-border-color);

  .header {
    height: 50px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    font-size: 15px;
    background-color: var(--el-menu-hover-bg-color);
  }

  .line {
    height: 1px;
    background-color: var(--el-border-color);
  }

  .content {
    align-items: center;
    justify-content: center;
    height: 200px;
  }
}
</style>
