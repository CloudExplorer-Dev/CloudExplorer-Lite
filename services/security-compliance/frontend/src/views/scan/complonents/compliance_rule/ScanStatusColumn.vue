<template>
  <el-popover placement="right" :width="800" trigger="click">
    <template #reference>
      <div style="display: flex">
        <div>{{ complainceScanResult.updateTime }}</div>
        <div style="flex: auto"></div>
        <scan_job_status_icon
          :show-text="false"
          :status="scanStatus(complainceScanResult.resourceType, undefined)"
        ></scan_job_status_icon>
        <div style="width: 20%"></div>
      </div>
    </template>
    <el-table :data="accountJobRecords">
      <el-table-column property="resourceId" label="云账号">
        <template #default="scope">
          {{
            cloudAccountSourceList.find((c) => c.id === scope.row.resourceId)
              ?.name
          }}
        </template>
      </el-table-column>
      <el-table-column label="云平台">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <el-image
              style="margin-right: 20%; display: flex"
              :src="
                platformIcon[
                  orElse(
                    cloudAccountSourceList.find(
                      (c) => c.id === scope.row.resourceId
                    )?.platform
                  )
                ].oldIcon
              "
            ></el-image>
            <span>{{
              platformIcon[
                orElse(
                  cloudAccountSourceList.find(
                    (c) => c.id === scope.row.resourceId
                  )?.platform
                )
              ].name
            }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column property="description" label="任务描述" />
      <el-table-column property="status" label="任务状态">
        <template #default="scope">
          <scan_job_status_icon
            :status="
              scanStatus(
                complainceScanResult.resourceType,
                scope.row.resourceId
              )
            "
            :show-text="true"
          ></scan_job_status_icon>
        </template>
      </el-table-column>
      <el-table-column property="createTime" label="扫描时间" width="150px" />
      <el-table-column label="详情">
        <template #default="scope">
          <ce-icon
            @click="openDetailsJobView(scope.row)"
            style="
              color: var(--el-color-info);
              cursor: pointer;
              font-size: 20px;
            "
            code="InfoFilled"
          ></ce-icon
        ></template>
      </el-table-column>
    </el-table>
  </el-popover>
</template>
<script setup lang="ts">
import { computed } from "vue";
import type { ComplianceScanResultResponse } from "@/api/compliance_scan_result/type";
import type {
  AccountJobRecord,
  CloudAccount,
} from "@commons/api/cloud_account/type";
import { platformIcon } from "@commons/utils/platform";
import scan_job_status_icon from "@/views/scan/complonents/compliance_rule/ScanJobStatusIcon.vue";
const props = defineProps<{
  complainceScanResult: ComplianceScanResultResponse;
  accountJobRecordList: Array<AccountJobRecord>;
  cloudAccountSourceList: Array<CloudAccount>;
  /**
   * 打开任务详情
   */
  openDetailsJobView: (accountJobRecord: AccountJobRecord) => void;
  /**
   * 云账号id
   */
  cloudAccountId?: string;
}>();

const accountJobRecords = computed(() => {
  if (props.cloudAccountId && props.cloudAccountId !== "all") {
    return props.accountJobRecordList.filter(
      (item) =>
        item.resourceId === props.cloudAccountId &&
        item.resourceType === props.complainceScanResult.resourceType
    );
  } else {
    return props.accountJobRecordList.filter(
      (item) =>
        getPlatformByCloudAccountId(item.resourceId) ===
          props.complainceScanResult.platform &&
        item.resourceType === props.complainceScanResult.resourceType
    );
  }
});

const getPlatformByCloudAccountId = (cloudAccountId: string) => {
  const cloudAccount = props.cloudAccountSourceList.find(
    (account) => account.id === cloudAccountId
  );
  if (cloudAccount) {
    return cloudAccount.platform;
  }
  return cloudAccountId;
};
/**
 * 扫描状态
 * @param resourceType       资源类型
 * @param cloud_acclount_id  云账号id
 */
const scanStatus = (resourceType: string, cloud_acclount_id?: string) => {
  const status = accountJobRecords.value
    .filter((item) => {
      if (cloud_acclount_id) {
        return (
          item.resourceType === resourceType &&
          item.resourceId === cloud_acclount_id
        );
      } else {
        return item.resourceType === resourceType;
      }
    })
    .map((item) => item.status);
  if (status.includes("SYNCING")) {
    return "SYNCING";
  }
  if (status.includes("FAILED")) {
    return "FAILED";
  }
  if (status.includes("TIME_OUT")) {
    return "TIME_OUT";
  }
  if (status.includes("SUCCESS")) {
    return "SUCCESS";
  }
  return "INIT";
};

/**
 * 给指定不确定的值一个默认值
 * @param value 将不确定的类型转换为string
 */
const orElse = (value?: string) => {
  return value ? value : "";
};
</script>
<style lang=""></style>
