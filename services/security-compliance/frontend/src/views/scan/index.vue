<template>
  <layout-auto-height-content :style="{ minWidth: '1000px' }">
    <template #breadcrumb>
      <breadcrumb :auto="true">
        <div
          class="operate"
          v-loading="cloudAccountLoading"
          style="--el-text-color-regular: #1f2329"
        >
          <el-select v-model="activeCloudAccount" placeholder="Select">
            <el-option
              v-for="item in cloudAccountOptions"
              :key="item.value"
              :label="item.key"
              :value="item.value"
            />
          </el-select>
          <el-button
            class="scan"
            @click="() => jobScan?.open()"
            v-hasPermission="'[security-compliance]SCAN:SEND_JOB'"
            >一键扫描</el-button
          >
          <JobScan
            :cloud-account-list="cloudAccountSourceList"
            ref="jobScan"
          ></JobScan>
        </div>
      </breadcrumb>
    </template>
    <ComplianceRuleGroup :cloud-account-id="activeCloudAccount" />
    <ComplianceRule
      :cloud-account-id="activeCloudAccount"
      :cloud-account-list="cloudAccountSourceList"
    />
  </layout-auto-height-content>
</template>
<script setup lang="ts">
import ComplianceRuleGroup from "@/views/scan/complonents/compliance_rule_group/index.vue";
import ComplianceRule from "@/views/scan/complonents/compliance_rule/index.vue";
import cloudAccountApi from "@commons/api/cloud_account";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import JobScan from "@/views/scan/complonents/job_scan/index.vue";
import { ref, computed, onMounted } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();
const cloudAccountLoading = ref<boolean>(false);
// 查询云账号数据
cloudAccountApi.listAll(cloudAccountLoading).then((ok) => {
  cloudAccountSourceList.value = ok.data;
});
const jobScan = ref<InstanceType<typeof JobScan>>();
// 云账号原始数据列表
const cloudAccountSourceList = ref<Array<CloudAccount>>([]);
// 选中的云账号
const activeCloudAccount = ref<string>("all");

onMounted(() => {
  if (route.query.cloudAccountId) {
    activeCloudAccount.value = route.query.cloudAccountId as string;
  }
});
// 下拉框
const cloudAccountOptions = computed(() => {
  return [
    { key: "全部云账号", value: "all" },
    ...cloudAccountSourceList.value.map((cloudAccount) => ({
      key: cloudAccount.name,
      value: cloudAccount.id,
    })),
  ];
});
</script>
<style lang="scss" scoped>
.operate {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 24px;
  .scan {
    background: #3370ff;
    color: #fff;
    margin-left: 12px;
  }
}
</style>
