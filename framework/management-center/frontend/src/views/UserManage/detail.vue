<script setup lang="ts">
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import { computed, onMounted, ref } from "vue";
import {
  changeUserStatus,
  convertUserRoleSourceList,
  deleteUserById,
  getUser,
  getUserRoleList,
} from "@/api/user";

import { roleConst } from "@commons/utils/constants";
import _ from "lodash";
import { TableOperations } from "@commons/components/ce-table/type";
import type { SimpleMap } from "@commons/api/base/type";
import { sourceIdNames } from "@commons/api/organization";
import DetailFormTitle from "@/componnets/DetailFormTitle.vue";
import DetailFormLabel from "@/componnets/DetailFormLabel.vue";
import DetailFormValue from "@/componnets/DetailFormValue.vue";
import MoreOptionsButton from "@commons/components/ce-table/MoreOptionsButton.vue";
import ModifyPwd from "@/views/UserManage/ModifyPwd.vue";
import MsgConfig from "@/views/UserManage/MsgConfig.vue";
import { type UpdateUserStatusRequest, User, UserRole } from "@/api/user/type";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { ElMessage, ElMessageBox } from "element-plus";
import CreateOrEdit from "./CreateOrEdit.vue";

const props = defineProps<{
  id: string;
}>();

const router = useRouter();
const { t } = useI18n();
const user = ref<User>();
const sourceNames = ref<SimpleMap<string>>({});
const permissionStore = usePermissionStore();
const msgConfigDialogVisible = ref<boolean>(false);
const modifyPwdRef = ref();

const loading = ref<boolean>(false);

const editFormRef = ref<InstanceType<typeof CreateOrEdit>>();

const enabledFilter = (enabled: boolean) => {
  if (enabled) {
    return t("commons.btn.enable");
  } else {
    return t("commons.btn.disable");
  }
};

const sourceFilter = (userSource: string) => {
  if (userSource?.toLowerCase() === "local") {
    return t("user.local");
  }
  if (userSource?.toLowerCase() === "extra") {
    return t("user.extra");
  }
  return userSource;
};

const itemWidth = computed(() => {
  return 100 / 3.0 + "%";
});

const userRoleList = computed<Array<UserRole>>(() => {
  if (user.value?.roleMap) {
    return getUserRoleList(user.value?.roleMap);
  } else {
    return [];
  }
});

const roleList = computed(() => {
  return convertUserRoleSourceList(userRoleList.value);
});

function filterSourceList(objs: any[]) {
  const _list: string[] = [];
  _.forEach(objs, (obj) => {
    if (sourceNames.value[obj.source]) {
      _list.push(sourceNames.value[obj.source]);
    } else {
      _list.push(obj.source);
    }
  });

  return _list;
}

const edit = (row: User) => {
  editFormRef.value?.open();
  editFormRef.value?.refreshUser();
};

function editConfirmed() {
  refreshUser();
}

const showMsgConfigDialog = () => {
  msgConfigDialogVisible.value = true;
};

const showPwdDialog = () => {
  modifyPwdRef.value.dialogVisible = true;
};

const deleteUser = (row: User) => {
  ElMessageBox.confirm(t("user.delete_confirm"), {
    confirmButtonText: t("commons.message_box.confirm"),
    cancelButtonText: t("commons.btn.cancel"),
    type: "warning",
  })
    .then(() => {
      deleteUserById(row.id, loading).then(() => {
        ElMessage.success(t("commons.msg.delete_success"));
        router.push({ name: "user" });
      });
    })
    .catch(() => {
      ElMessage.info(t("commons.msg.delete_canceled"));
    });
};

const handleSwitchStatus = (row: User) => {
  loading.value = true;
  const req: UpdateUserStatusRequest = _.cloneDeep(
    row as UpdateUserStatusRequest
  );
  req.enabled = !req.enabled;
  changeUserStatus(req)
    .then((res) => {
      if (user.value) {
        user.value.enabled = !user.value.enabled;
      }
      ElMessage.success(t("commons.msg.op_success"));
    })
    .catch((e) => {
      console.error(e);
    })
    .finally(() => {
      loading.value = false;
    });
};

const tableOperations = computed(
  () =>
    new TableOperations([
      TableOperations.buildButtons().newInstance(
        "启用",
        "primary",
        handleSwitchStatus,
        undefined,
        undefined,
        permissionStore.hasPermission("[management-center]USER:EDIT") &&
          user.value &&
          !user.value?.enabled
      ),
      TableOperations.buildButtons().newInstance(
        "禁用",
        "primary",
        handleSwitchStatus,
        undefined,
        undefined,
        permissionStore.hasPermission("[management-center]USER:EDIT") &&
          user.value &&
          user.value?.enabled
      ),
      TableOperations.buildButtons().newInstance(
        t("commons.btn.edit"),
        "primary",
        edit,
        undefined,
        undefined,
        permissionStore.hasPermission("[management-center]USER:EDIT")
      ),
      TableOperations.buildButtons().newInstance(
        t("commons.personal.edit_pwd"),
        "primary",
        showPwdDialog,
        undefined,
        undefined,
        permissionStore.hasPermission("[management-center]USER:EDIT_PASSWORD")
      ),
      TableOperations.buildButtons().newInstance(
        t("user.notify_setting"),
        "primary",
        showMsgConfigDialog,
        undefined,
        undefined,
        permissionStore.hasPermission(
          "[management-center]USER:NOTIFICATION_SETTING"
        )
      ),
      TableOperations.buildButtons().newInstance(
        t("commons.btn.delete"),
        "danger",
        deleteUser,
        undefined,
        undefined,
        permissionStore.hasPermission("[management-center]USER:DELETE"),
        "#F54A45"
      ),
    ])
);

function refreshUser() {
  getUser(props.id, loading).then((res) => {
    user.value = res.data;
  });
}

onMounted(() => {
  refreshUser();

  sourceIdNames().then((res) => {
    sourceNames.value = res.data;
  });
});
</script>

<template>
  <el-container v-loading="loading">
    <el-header>
      <div class="header">
        <span>{{ user?.name }} 详情</span>
        <MoreOptionsButton
          :buttons="tableOperations.buttons"
          :row="user"
          name="操作"
        />
      </div>
    </el-header>
    <el-main ref="create-catalog-container">
      <el-descriptions direction="vertical" :column="3">
        <template #title>
          <DetailFormTitle :title="t('commons.basic_info', '基本信息')" />
        </template>
        <el-descriptions-item :width="itemWidth">
          <template #label>
            <DetailFormLabel :label="$t('user.username')" />
          </template>
          <DetailFormValue :value="user?.username" />
        </el-descriptions-item>
        <el-descriptions-item :width="itemWidth">
          <template #label>
            <DetailFormLabel :label="$t('user.name')" />
          </template>
          <DetailFormValue :value="user?.name" />
        </el-descriptions-item>
        <el-descriptions-item :width="itemWidth">
          <template #label>
            <DetailFormLabel :label="$t('user.status')" />
          </template>
          <DetailFormValue :value="enabledFilter(user?.enabled)" />
        </el-descriptions-item>
        <el-descriptions-item :width="itemWidth">
          <template #label>
            <DetailFormLabel :label="$t('user.email')" />
          </template>
          <DetailFormValue :value="user?.email" />
        </el-descriptions-item>
        <el-descriptions-item :width="itemWidth">
          <template #label>
            <DetailFormLabel :label="$t('user.phone')" />
          </template>
          <DetailFormValue :value="user?.phone ? user?.phone : '-'" />
        </el-descriptions-item>
        <el-descriptions-item :width="itemWidth">
          <template #label>
            <DetailFormLabel :label="$t('user.source')" />
          </template>
          <DetailFormValue :value="sourceFilter(user?.source)" />
        </el-descriptions-item>
        <el-descriptions-item :width="itemWidth">
          <template #label>
            <DetailFormLabel :label="$t('commons.create_time')" />
          </template>
          <DetailFormValue :value="user?.createTime" />
        </el-descriptions-item>
      </el-descriptions>

      <DetailFormTitle
        class="margin-top margin-bottom"
        :title="t('user.has_role')"
      />

      <template v-for="(roleInfo, index) in roleList" :key="index">
        <el-descriptions direction="vertical" :column="1">
          <template #title>
            <div class="role-title">角色{{ index + 1 }}</div>
          </template>
          <el-descriptions-item>
            <template #label>
              <DetailFormLabel :label="$t('user.type')" />
            </template>
            <DetailFormValue :value="roleInfo.roleName" />
          </el-descriptions-item>
          <el-descriptions-item
            v-if="
              roleInfo.parentRole === roleConst.orgAdmin ||
              roleInfo.parentRole === roleConst.user
            "
          >
            <template #label>
              <DetailFormLabel
                v-if="roleInfo.parentRole === roleConst.orgAdmin"
                :label="$t('user.add_org')"
              />
              <DetailFormLabel
                v-else-if="roleInfo.parentRole === roleConst.user"
                :label="$t('user.add_workspace')"
              />
            </template>
            <DetailFormValue>
              <el-space wrap>
                <div
                  class="source-tip"
                  v-for="(s, j) in filterSourceList(roleInfo.list)"
                  :key="j"
                >
                  {{ s }}
                </div>
              </el-space>
            </DetailFormValue>
          </el-descriptions-item>
        </el-descriptions>
      </template>
    </el-main>

    <!-- 修改密码弹出框 -->
    <ModifyPwd ref="modifyPwdRef" :userId="id" style="min-width: 600px" />

    <!-- 通知设置弹出框 -->
    <el-dialog
      v-model="msgConfigDialogVisible"
      :title="$t('user.notify_setting')"
      width="840px"
      destroy-on-close
      :close-on-click-modal="false"
      class="custom-dialog"
      style="min-width: 600px"
    >
      <MsgConfig :userId="id" v-model:visible="msgConfigDialogVisible" />
    </el-dialog>

    <CreateOrEdit ref="editFormRef" @confirm="editConfirmed" :id="id" />
  </el-container>
</template>

<style lang="scss" scoped>
.el-main {
  padding: 16px 0;
}

.margin-top {
  margin-top: 16px;
}
.margin-bottom {
  margin-bottom: 16px;
}
.role-title {
  font-style: normal;
  font-weight: 500;
  font-size: 14px;
  line-height: 22px;
  color: #1f2329;
}

.source-tip {
  height: 24px;
  background: rgba(31, 35, 41, 0.1);
  border-radius: 2px;
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  color: #646a73;
  line-height: 22px;
  padding: 1px 6px;
}

.el-header {
  --el-header-height: 52px;
  --el-header-padding: 0;
  border-bottom: var(--el-border-color) 1px solid;
  color: #1f2329;

  .header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    font-style: normal;
    font-weight: 500;
    font-size: 16px;
    line-height: 24px;
    padding-top: 4px;
  }
}
</style>
