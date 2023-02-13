<script setup lang="ts">
import { ref } from "vue";
import { useUserStore } from "@commons/stores/modules/user";
import ModifyPassword from "./ModifyPassword.vue";
import SourceChangeDialog from "./SourceChangeDialog.vue";
import PersonInfo from "./PersonInfo.vue";

const userStore = useUserStore();

const handleLogout = () => {
  userStore.doLogout();
};

const userDropdown = ref();
const handleClickUser = () => {
  userDropdown.value.handleOpen();
};

// 父组件通过模板 ref 的方式获取到子组件通过 defineExpose 暴露出来的属性
const modifyPasswordRef = ref();
const showPasswordDialog = () => {
  modifyPasswordRef.value.dialogVisible = true;
};

const userInfoRef = ref();
const showUserInfoDialog = () => {
  userInfoRef.value.dialogVisible = true;
};

const sourceChangeDialogRef = ref();
const showSourceChangeDialog = () => {
  sourceChangeDialogRef.value.dialogVisible = true;
};
</script>

<template>
  <el-dropdown ref="userDropdown" trigger="contextmenu">
    <el-button type="primary" @click="handleClickUser" circle>
      <el-icon :size="15" type="primary">
        <UserFilled />
      </el-icon>
    </el-button>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item icon="UserFilled" @click="showSourceChangeDialog">
          切换角色
        </el-dropdown-item>
        <el-dropdown-item icon="InfoFilled" divided @click="showUserInfoDialog">
          {{ $t("commons.personal.personal_info") }}
        </el-dropdown-item>
        <el-dropdown-item icon="Edit" @click="showPasswordDialog">
          {{ $t("commons.personal.edit_pwd") }}
        </el-dropdown-item>
        <el-dropdown-item icon="Right" divided @click="handleLogout">
          {{ $t("commons.personal.exit_system") }}
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>

  <!--修改密码弹出框-->
  <ModifyPassword ref="modifyPasswordRef" />

  <!--个人信息弹出框-->
  <PersonInfo ref="userInfoRef" />

  <SourceChangeDialog ref="sourceChangeDialogRef" />
</template>
