<script lang="ts" setup>
import { useUserStore } from "@commons/stores/modules/user";
import CeIcon from "@commons/components/ce-icon/index.vue";

const userStore = useUserStore();
</script>
<template>
  <el-card class="info-card">
    <div style="font-weight: bold; font-size: 16px; padding-bottom: 26px">
      基本信息
    </div>
    <el-row type="flex" :gutter="20">
      <el-col :span="8">
        <el-row type="flex">
          <el-col :span="24" class="card-left-info">
            <CeIcon code="wodezhanghu" size="40px" style="height: 48px" />
          </el-col>
        </el-row>
        <el-row type="flex" justify="center">
          <el-col :span="24" class="card-left-info">
            <div style="font-weight: bold">
              {{ userStore.currentUser.name }}
            </div>
          </el-col>
        </el-row>
        <el-row type="flex" justify="center">
          <el-col :span="24" class="card-left-info">
            <div>{{ userStore.currentUser.username }}</div>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="16">
        <el-row type="flex" align="middle">
          <el-col :span="8" class="card-label">当前角色</el-col>
          <el-col :span="1">:</el-col>
          <el-col :span="15">
            <div style="font-weight: bold; color: var(--el-color-primary)">
              {{ userStore.currentRoleSourceName.roleName }}
              <div
                style="font-size: small"
                v-if="userStore.currentRoleSourceName?.sourceName"
              >
                【{{ userStore.currentRoleSourceName?.sourceName }}】
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row type="flex" align="middle">
          <el-col :span="8" class="card-label">手机号</el-col>
          <el-col :span="1">:</el-col>
          <el-col :span="15">
            {{ userStore.currentUser.phone }}
          </el-col>
        </el-row>
        <el-row type="flex" align="middle">
          <el-col :span="8" class="card-label">登录IP</el-col>
          <el-col :span="1">:</el-col>
          <el-col :span="15">
            {{ userStore.currentUser.ip }}
          </el-col>
        </el-row>
        <el-row type="flex" align="middle">
          <el-col :span="8" class="card-label">登录时间</el-col>
          <el-col :span="1">:</el-col>
          <el-col :span="15">
            {{ userStore.currentUser.loginTime }}
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </el-card>
</template>

<style scoped lang="scss">
.info-card {
  /*min-width: 340px;*/
  min-height: 200px;

  .card-label {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    padding-right: 1px;
  }

  .card-value {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    padding-left: 1px;
  }

  .card-left-info {
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .el-row {
    margin-bottom: 10px;
  }

  .el-row:last-child {
    margin-bottom: 0;
  }
}
</style>
