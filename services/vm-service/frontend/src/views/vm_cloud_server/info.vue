<template>
  <layout-container :border="false">
    <template #content>
      <layout-container v-loading="loading">
        <template #header>
          <h4>基本信息</h4>
        </template>
        <template #content>
          <div ref="top">
            <el-descriptions :column="contentSpan" border class="small-text">
              <el-descriptions-item
                :span="contentSpan + 1"
                label-class-name="label-class"
                class-name="content-class"
                label="名称:"
                colon="true"
              >
                {{ infoVmCloudServer.instanceName }}
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
                      infoVmCloudServer.instanceStatus === 'Creating'
                    "
                    class="is-loading"
                    ><Loading
                  /></el-icon>
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                label="云主机ID:"
                >{{ infoVmCloudServer.instanceUuid }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                label="云账号:"
              >
                <div style="display: flex">
                  <component
                    style="margin-top: 3px; width: 16px; height: 16px"
                    :is="platformIcon[infoVmCloudServer.platform]?.component"
                    v-bind="platformIcon[infoVmCloudServer.platform]?.icon"
                    :color="platformIcon[infoVmCloudServer.platform]?.color"
                    size="16px"
                    v-if="infoVmCloudServer.platform"
                  ></component>
                  <span style="margin-left: 10px">{{
                    infoVmCloudServer.accountName
                  }}</span>
                </div>
              </el-descriptions-item>
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                label="组织:"
                >{{ infoVmCloudServer.organizationName }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                label="工作空间:"
                >{{ infoVmCloudServer.workspaceName }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                label="区域/数据中心:"
                >{{ infoVmCloudServer.region }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                label="到期时间:"
                >{{ infoVmCloudServer.expiredTime }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                label="备注:"
                >{{ infoVmCloudServer.remark }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                label="可用区/集群:"
                >{{ infoVmCloudServer.zone }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                label="创建时间:"
                >{{ infoVmCloudServer.createTime }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                label="实例规格:"
                >{{
                  infoVmCloudServer.instanceTypeDescription
                }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                label="付费方式:"
                >{{
                  filterChargeType(infoVmCloudServer.instanceChargeType)
                }}</el-descriptions-item
              >
              <el-descriptions-item
                label-class-name="label-class"
                class-name="content-class"
                label="申请人:"
                >{{ infoVmCloudServer.applyUser }}</el-descriptions-item
              >
            </el-descriptions>
          </div>
        </template>
      </layout-container>
      <layout-container v-loading="loading">
        <template #header>
          <h4>网络与安全</h4>
        </template>
        <template #content>
          <el-descriptions :column="contentSpan" border class="small-text">
            <el-descriptions-item
              label-class-name="label-class"
              class-name="content-class"
              label="IP地址:"
            >
              <pre>{{
                filterIp(infoVmCloudServer, infoVmCloudServer.ipArray)
              }}</pre>
            </el-descriptions-item>
            <el-descriptions-item
              label-class-name="label-class"
              class-name="content-class"
              label="所属子网:"
              >{{ infoVmCloudServer.subnetId }}</el-descriptions-item
            >
            <el-descriptions-item
              label-class-name="label-class"
              class-name="content-class"
              label="安全组:"
              >{{ filterSg(infoVmCloudServer) }}</el-descriptions-item
            >
            <el-descriptions-item
              label-class-name="label-class"
              class-name="content-class"
              label="所属网络/VPC:"
              >{{ infoVmCloudServer.network }}/{{
                infoVmCloudServer.vpcId
              }}</el-descriptions-item
            >
            <el-descriptions-item
              label-class-name="label-class"
              class-name="content-class"
              label="带宽计费类型:"
              >-</el-descriptions-item
            >
            <el-descriptions-item
              label-class-name="label-class"
              class-name="content-class"
              label="带宽峰值:"
              >-</el-descriptions-item
            >
          </el-descriptions>
        </template>
      </layout-container>
    </template>
  </layout-container>
</template>
<script setup lang="ts">
const props = defineProps<{
  id: string;
}>();
import { ref, onMounted } from "vue";
import VmCloudServerApi from "@/api/vm_cloud_server";
import { useI18n } from "vue-i18n";
import _ from "lodash";
import variables_server from "../../styles/vm_cloud_server/server.module.scss";
import { platformIcon } from "@commons/utils/platform";
const { t } = useI18n();
const loading = ref<boolean>(false);
const infoVmCloudServer = ref<any>({});
const contentSpan = ref<number>(2);
const top = ref<HTMLElement | null>(null);
//状态
const instanceStatusMap: Map<string, string> = new Map();
instanceStatusMap.set("Running", t("", "运行中"));
instanceStatusMap.set("Deleted", t("", "已删除"));
instanceStatusMap.set("Stopped", t("", "已关机"));
instanceStatusMap.set("Starting", t("", "启动中"));
instanceStatusMap.set("Stopping", t("", "关机中"));
instanceStatusMap.set("Rebooting", t("", "重启中"));
instanceStatusMap.set("Deleting", t("", "删除中"));
instanceStatusMap.set("Creating", t("", "创建中"));
instanceStatusMap.set("Unknown", t("", "未知"));
instanceStatusMap.set("Failed", t("", "失败"));
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
    case "Failed":
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
  VmCloudServerApi.getVmCloudServerById(props.id, loading)
    .then((res) => {
      infoVmCloudServer.value = _.cloneDeep(res.data);
    })
    .catch((err) => {
      console.log(err);
    });
  window.onresize = () => {
    if (top.value) {
      if (top.value.clientWidth <= 600) {
        contentSpan.value = 1;
      } else if (top.value.clientWidth >= 1000) {
        contentSpan.value = 3;
      } else {
        contentSpan.value = 2;
      }

      console.log(top.value.clientWidth);
    }
  };
});

const filterIp = (infoVmCloudServer: any, ipArray: any) => {
  let ipText = "";
  if (ipArray) {
    const ips = JSON.parse(ipArray);
    let i;
    for (i in ips) {
      let ip = ips[i];
      if (
        infoVmCloudServer.remoteIpv6 === ip ||
        infoVmCloudServer.remoteIp === ip
      ) {
        ip += t("", "(公)");
      }
      if (ipText === "") {
        ipText += ip;
      } else {
        ipText += "\n" + ip;
      }
    }
  }
  return ipText;
};

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

const filterSg = (infoVmCloudServer: any) => {
  let text = "";
  if (infoVmCloudServer.securityGroupIds) {
    const sg = JSON.parse(infoVmCloudServer.securityGroupIds);
    text = sg?.join(",");
  }
  return text;
};

//启动定时器
const startOperateInterval = (vm: any) => {
  const cloudServerInterval = ref<any>();
  cloudServerInterval.value = setInterval(() => {
    console.log("info 初始化定时器：" + cloudServerInterval.value);
    VmCloudServerApi.getVmCloudServerById(vm.id).then((res) => {
      console.log("old:" + vm.instanceStatus);
      console.log("new:" + res.data.instanceStatus);
      if (vm.instanceStatus != res.data.instanceStatus) {
        vm.instanceStatus = res.data.instanceStatus;
        stopOperateInterval(cloudServerInterval.value);
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
</script>

<style lang="scss">
.text-overflow {
  max-width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
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
  border: 0px !important;
}

.el-descriptions__label.el-descriptions__cell.is-bordered-label {
  background-color: transparent !important;
}

.el-descriptions__body .el-descriptions__table .el-descriptions__cell {
  font-size: 12px !important;
}
</style>
