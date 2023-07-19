<template>
  <div>
    <div v-if="viewCloudAccount" class="cloud-account-content">
      <PlatformIcon
        style="height: 32px; display: flex; margin-right: 8px"
        :platform="viewCloudAccount.platform"
      ></PlatformIcon>
      <span> {{ viewCloudAccount.name }} </span>
      <el-dropdown v-if="moreCloudAccountList">
        <span class="more">
          更多
          <el-icon class="el-icon--right">
            <arrow-down />
          </el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item
              v-for="cloudAccount in moreCloudAccountList"
              :key="cloudAccount.id"
            >
              <PlatformIcon
                style="margin-right: 8px"
                :platform="cloudAccount.platform"
              ></PlatformIcon
              ><span>{{ cloudAccount.name }}</span></el-dropdown-item
            >
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>
<script setup lang="ts">
import { computed } from "vue";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import PlatformIcon from "@commons/components/platform-icon/index.vue";
const porps = defineProps<{
  cloudAccountList: Array<CloudAccount>;
}>();
const viewCloudAccount = computed(() => {
  return porps.cloudAccountList.length > 0
    ? porps.cloudAccountList[0]
    : undefined;
});
const moreCloudAccountList = computed(() => {
  return porps.cloudAccountList.length > 1
    ? porps.cloudAccountList.slice(1)
    : undefined;
});
</script>
<style lang="scss" scoped>
.cloud-account-content {
  display: flex;
  align-items: center;
  .more {
    margin-left: 8px;
    color: #3370ff;
    cursor: pointer;
  }
}
</style>
