<script lang="ts" setup>
import { useUserStore } from "@commons/stores/modules/user";
import UserAvatar from "@commons/business/person-setting/UserAvatar.vue";
import RoleTag from "@commons/business/person-setting/RoleTag.vue";
import { BaseModuleInfo } from "@commons/business/base-layout/home-page/items/BaseModuleType";
import BaseModule from "@commons/business/base-layout/home-page/items/BaseModule.vue";
import { computed } from "vue";
import _ from "lodash";

const userStore = useUserStore();

const baseList: Array<BaseModuleInfo> = [
  new BaseModuleInfo(
    "yonghuguanli_huaban",
    "用户",
    ["ADMIN", "ORGADMIN"],
    "[management-center]USER:READ",
    "management-center",
    "/management-center/api/user/count",
    "/management-center/user_tenant/user/list"
  ),
  new BaseModuleInfo(
    "zuzhijiagou1",
    "组织",
    ["ADMIN", "ORGADMIN"],
    "[management-center]ORGANIZATION:READ",
    "management-center",
    "/management-center/api/organization/count",
    "/management-center/user_tenant/org/list"
  ),
  new BaseModuleInfo(
    "project_space",
    "工作空间",
    ["ADMIN", "ORGADMIN"],
    "[management-center]WORKSPACE:READ",
    "management-center",
    "/management-center/api/workspace/count",
    "/management-center/user_tenant/workspace/list"
  ),
];

const showManageDivs = computed<boolean>(() => {
  return _.some(baseList, (obj) => obj.show.value);
});
</script>
<template>
  <div class="info-card">
    <el-row>
      <el-col :span="24" class="user-info-row">
        <UserAvatar
          size="48px"
          icon-size="1.4em"
          style="margin-right: 12px"
          un-clickable
        />
        <div>
          <div class="user-info">
            <div class="user-name">
              {{ userStore?.currentUser?.name }}
            </div>
            <RoleTag
              :clickable="false"
              v-for="role in userStore?.currentRoleSourceName?.roles"
              :key="role.id"
              :role="role"
            />
          </div>
          <div class="user-id">ID: {{ userStore?.currentUser?.username }}</div>
        </div>
      </el-col>
    </el-row>

    <el-row v-if="showManageDivs" :gutter="8">
      <template v-for="(info, index) in baseList" :key="index">
        <el-col :span="8" v-if="info.show.value">
          <BaseModule
            :name="info.name"
            :module="info.module"
            :redirect="info.redirect"
            :func="info.path"
            :type="info.type"
            :unit="info.unit"
          />
        </el-col>
      </template>
    </el-row>
  </div>
</template>

<style scoped lang="scss">
.info-card {
  background: #ffffff;
  border-radius: 4px;
  padding: 24px;
  overflow: hidden;

  .el-row {
    margin-bottom: 20px;
  }
  .el-row:last-child {
    margin-bottom: 0;
  }

  .user-info-row {
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;

    .user-info {
      height: 24px;
      display: flex;
      flex-direction: row;
      flex-wrap: nowrap;

      .user-name {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        font-style: normal;
        font-weight: 500;
        font-size: 16px;
        line-height: 24px;
      }

      .user-id {
        margin-top: 4px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        font-style: normal;
        font-weight: 400;
        font-size: 14px;
        line-height: 22px;
        color: #646a73;
      }
    }
  }
}
</style>
