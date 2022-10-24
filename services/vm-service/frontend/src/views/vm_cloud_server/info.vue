<template>
  <layout-container :border="false">
    <template #content>
      <layout-container v-loading="loading">
        <template #header>
          <h4>基本信息</h4>
        </template>
        <template #content>
          <el-descriptions class="large-text" colon="true">
            <el-descriptions-item label="名称:" colon="true" width="35%">
              <el-tooltip
                class="item"
                effect="dark"
                :content="infoVmCloudServer.instanceName"
                placement="top-start"
              >
                <el-link :class="variables_server.text_overflow">
                  {{ infoVmCloudServer.instanceName }}
                </el-link>
              </el-tooltip>
              <el-icon :size="15" style="margin-top: 5px">
                <Edit />
              </el-icon>
              <el-tag
                style="margin-left: 20px"
                :style="{
                  color: variables_server[infoVmCloudServer.instanceStatus],
                }"
              >
                {{ instanceStatusTagStyle(infoVmCloudServer.instanceStatus) }}
                <el-icon
                  v-show="
                    infoVmCloudServer.instanceStatus === 'Starting' ||
                    infoVmCloudServer.instanceStatus === 'Stopping' ||
                    infoVmCloudServer.instanceStatus === 'Rebooting' ||
                    infoVmCloudServer.instanceStatus === 'Deleting' ||
                    infoVmCloudServer.instanceStatus === 'Createding'
                  "
                  class="is-loading"
                  ><Loading
                /></el-icon>
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="云账号:">
              <el-image
                style="margin-top: 3px; width: 16px; height: 16px"
                :src="platformIcon[infoVmCloudServer.platform].icon"
                v-if="infoVmCloudServer.platform"
              ></el-image>
              {{ infoVmCloudServer.accountName }}
            </el-descriptions-item>
            <el-descriptions-item label="工作空间:">{{
              infoVmCloudServer.workspaceName
            }}</el-descriptions-item>
          </el-descriptions>
          <el-descriptions>
            <el-descriptions-item label="云主机ID:">{{
              infoVmCloudServer.instanceUuid
            }}</el-descriptions-item>
            <el-descriptions-item label="区域/数据中心:">{{
              infoVmCloudServer.region
            }}</el-descriptions-item>
            <el-descriptions-item label="到期时间:">-</el-descriptions-item>
            <el-descriptions-item label="备注:">{{
              infoVmCloudServer.remark
            }}</el-descriptions-item>
            <el-descriptions-item label="可用区/集群:">{{
              infoVmCloudServer.zone
            }}</el-descriptions-item>
            <el-descriptions-item label="创建时间:">{{
              infoVmCloudServer.createTime
            }}</el-descriptions-item>
            <el-descriptions-item label="实例规格:">{{
              infoVmCloudServer.instanceTypeDescription
            }}</el-descriptions-item>
            <el-descriptions-item label="付费方式:">-</el-descriptions-item>
            <el-descriptions-item label="申请人:">-</el-descriptions-item>
            <el-descriptions-item label="镜像名称:">-</el-descriptions-item>
          </el-descriptions>
        </template>
      </layout-container>
      <layout-container v-loading="loading">
        <template #header>
          <h4>网络与安全</h4>
        </template>
        <template #content>
          <el-descriptions>
            <el-descriptions-item label="IP地址:">{{
              infoVmCloudServer.ipArray
            }}</el-descriptions-item>
            <el-descriptions-item label="所属子网:">{{
              infoVmCloudServer.subnetId
            }}</el-descriptions-item>
            <el-descriptions-item label="安全组:">-</el-descriptions-item>
            <el-descriptions-item label="所属网络/VPC:"
              >{{ infoVmCloudServer.network }}/{{
                infoVmCloudServer.vpcId
              }}</el-descriptions-item
            >
            <el-descriptions-item label="带宽计费类型:">-</el-descriptions-item>
            <el-descriptions-item label="带宽峰值:">-</el-descriptions-item>
          </el-descriptions>
        </template>
      </layout-container>
    </template>
  </layout-container>
</template>
<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from "vue";
import VmCloudServerApi, { getVmCloudServerById } from "@/api/vm_cloud_server";
import type { SimpleMap } from "@commons/api/base/type";
import type { VmCloudServerVO } from "@/api/vm_cloud_server/type";
import { useI18n } from "vue-i18n";
import { ElMessage, ElMessageBox } from "element-plus";
import _ from "lodash";
import { useRouter } from "vue-router";
import variables_server from "../../styles/vm_cloud_server/server.module.scss";
import { platformIcon } from "@/utils/platform";
const { t } = useI18n();
const loading = ref<boolean>(false);
const useRoute = useRouter();
const infoVmCloudServer = ref<any>({});
//状态
const instanceStatusMap: Map<string, string> = new Map();
instanceStatusMap.set("Running", t("", "运行中"));
instanceStatusMap.set("Deleted", t("", "已删除"));
instanceStatusMap.set("Stopped", t("", "已关机"));
instanceStatusMap.set("Starting", t("", "启动中"));
instanceStatusMap.set("Stopping", t("", "关机中"));
instanceStatusMap.set("Rebooting", t("", "重启中"));
instanceStatusMap.set("Deleting", t("", "删除中"));
instanceStatusMap.set("Createding", t("", "创建中"));
instanceStatusMap.set("Unknown", t("", "创建中"));

//显示进行中的状态
const showLoading = ref<boolean>(true);
//状态标签样式处理
const instanceStatusTagStyle = (instanceStatus: string) => {
  switch (instanceStatus) {
    case "Running":
      showLoading.value = false;
      break;
    case "Deleted":
      showLoading.value = false;
      break;
    case "Stopped":
      showLoading.value = false;
      break;
    case "Unknown":
      showLoading.value = false;
      break;
    default:
      break;
  }
  if (showLoading.value && infoVmCloudServer.value.instanceStatus) {
    startOperateInterval(infoVmCloudServer.value);
  }
  return instanceStatusMap.get(instanceStatus);
};
/**
 * 页面挂载
 */
onMounted(() => {
  //云主机ID
  const vmId = useRoute.currentRoute.value.query.id;
  VmCloudServerApi.getVmCloudServerById(vmId as string, loading)
    .then((res) => {
      infoVmCloudServer.value = _.cloneDeep(res.data);
    })
    .catch((err) => {
      console.log(err);
    });
});
//启动定时器
const startOperateInterval = (vm: any) => {
  let cloudServerInterval: any;
  cloudServerInterval = setInterval(() => {
    console.log("info 初始化定时器：" + cloudServerInterval);
    VmCloudServerApi.getVmCloudServerById(vm.id).then((res) => {
      console.log("old:" + vm.instanceStatus);
      console.log("new:" + res.data.instanceStatus);
      if (vm.instanceStatus != res.data.instanceStatus) {
        vm.instanceStatus = res.data.instanceStatus;
        stopOperateInterval(cloudServerInterval);
      }
    });
  }, 6000);
};
//停止定时器
const stopOperateInterval = (cloudServerInterval: any) => {
  if (cloudServerInterval) {
    showLoading.value = false;
    console.log("info 关闭定时器：" + cloudServerInterval);
    clearInterval(cloudServerInterval);
  }
};

onBeforeUnmount(() => {
  clearInterval();
});
</script>

<style lang="scss">
.text-overflow {
  max-width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.large-text span {
  font-size: 20px;
}
.edit-button-container {
  text-align: center;
  line-height: 50px;
  align-items: center;
}

.permission-container {
  width: 100%;
  min-height: 100px;
}
</style>
