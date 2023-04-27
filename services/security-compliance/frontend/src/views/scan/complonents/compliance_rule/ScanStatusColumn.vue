<template>
  <el-popover placement="right" :width="800" trigger="click">
    <template #reference>
      <ScanJobStatusIcon
        :status="scanStatus(complainceScanResult.resourceType)"
      />
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
            <PlatformIcon
              :platform="
                cloudAccountSourceList.find(
                  (c) => c.id === scope.row.resourceId
                )?.platform || '-'
              "
            />
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
          <ScanJobStatusIcon
            :status="
              scanStatus(
                complainceScanResult.resourceType,
                scope.row.resourceId
              )
            "
            :show-text="true"
          />
        </template>
      </el-table-column>
      <el-table-column property="createTime" label="扫描时间" width="160px" />
      <el-table-column label="详情">
        <template #default="scope">
          <ce-icon
            @click="openDetailsJobView(scope.row)"
            class="icon"
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
import ScanJobStatusIcon from "@/views/scan/complonents/compliance_rule/ScanJobStatusIcon.vue";
import PlatformIcon from "@commons/components/platform-icon/index.vue";
const props = defineProps<{
  /**
   * 扫描结果
   */
  complainceScanResult: ComplianceScanResultResponse;
  /**
   *账号任务
   */
  accountJobRecordList: Array<AccountJobRecord>;
  /**
   *云账号
   */
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
const scanStatus: (
  resourceType: string,
  cloud_acclount_id?: string
) => "SYNCING" | "FAILED" | "SUCCESS" | "NOT_HAVE" = (
  resourceType: string,
  cloud_acclount_id?: string
) => {
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
    return "FAILED";
  }
  if (status.includes("SUCCESS")) {
    return "SUCCESS";
  }
  return "NOT_HAVE";
};

/**
 * 给指定不确定的值一个默认值
 * @param value 将不确定的类型转换为string
 */
const orElse = (value?: string) => {
  return value ? value : "";
};
</script>
<style lang="scss" scoped>
.icon {
  color: var(--el-color-info);
  cursor: pointer;
  font-size: 20px;
}
</style>
