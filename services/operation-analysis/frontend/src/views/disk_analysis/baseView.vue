<template>
  <div class="content">
    <el-row :gutter="12" class="header-row">
      <el-col :span="24">
        <div class="search-bar">
          <el-select v-model="currentAccount" class="padding-r-12">
            <template v-slot:prefix>
              <span> 云账号 </span>
            </template>
            <el-option label="全部云账号" value="all" />
            <el-option
              v-for="item in accounts"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
          <el-select v-model="currentUnit">
            <template v-slot:prefix>
              <span> 单位 </span>
            </template>
            <el-option label="数量(块)" value="block" />
            <el-option label="容量(GB)" value="capacity" />
          </el-select>
        </div>
      </el-col>
    </el-row>
    <DiskDoughnutChartGroup
      :cloud-account-id="currentAccount"
      :current-unit="currentUnit"
    ></DiskDoughnutChartGroup>
    <el-row :gutter="16" class="row">
      <el-col :span="12">
        <DiskIncreaseTrend
          :cloud-account-id="currentAccount"
          :current-unit="currentUnit"
        ></DiskIncreaseTrend>
      </el-col>
      <el-col :span="12">
        <DIskOrgWorkspaceSpread
          :cloud-account-id="currentAccount"
          :current-unit="currentUnit"
        ></DIskOrgWorkspaceSpread>
      </el-col>
    </el-row>
  </div>
</template>
<script lang="ts" setup>
import { onMounted, ref } from "vue";
import CommonApi from "@/api/common/index";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import DiskDoughnutChartGroup from "./item/DiskDoughnutChartGroup.vue";
import DiskIncreaseTrend from "./item/DiskIncreaseTrend.vue";
import DIskOrgWorkspaceSpread from "@/views/disk_analysis/item/DiskOrgWorkspaceSpread.vue";

//条件
const currentAccount = ref<string>("all");
const currentUnit = ref<string>("block");
//查询所有私有云云账号
const accounts = ref<Array<CloudAccount>>();
const getSearchCondition = () => {
  CommonApi.listAll().then((res) => (accounts.value = res.data));
};
onMounted(() => {
  getSearchCondition();
});
</script>

<style scoped lang="scss">
.content {
  min-width: 1000px;
  .header-row {
    padding: 5px 0 10px 0;
    text-align: left;
    .header-title {
      font-style: normal;
      font-weight: 500;
      font-size: 16px;
      line-height: 32px;
      color: #1f2329;
    }
    .search-bar {
      line-height: 32px;
      .el-select {
        width: 200px;
      }
      .padding-r-12 {
        padding-right: 12px;
      }
    }
  }
  .row {
    padding: 5px 0 10px 0;
    text-align: left;
    .header-title {
      font-style: normal;
      font-weight: 500;
      font-size: 16px;
      line-height: 32px;
      color: #1f2329;
    }
  }
}
</style>
