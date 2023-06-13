<script setup lang="ts">
import { ref } from "vue";
import { useUserStore } from "@commons/stores/modules/user";
import ModifyPassword from "./ModifyPassword.vue";
import PersonInfo from "./PersonInfo.vue";
import UserAvatar from "@commons/business/person-setting/UserAvatar.vue";

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

function jumpToDoc() {
  window.open(
    window.location.protocol + "//" + window.location.host + "/swagger-ui.html",
    "_blank"
  );
}
</script>

<template>
  <el-dropdown ref="userDropdown" trigger="contextmenu">
    <UserAvatar @click="handleClickUser" />
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item icon="InfoFilled" @click="showUserInfoDialog">
          {{ $t("commons.personal.personal_info") }}
        </el-dropdown-item>
        <el-dropdown-item icon="Edit" @click="showPasswordDialog">
          {{ $t("commons.personal.edit_pwd") }}
        </el-dropdown-item>
        <el-dropdown-item icon="Document" @click="jumpToDoc">
          {{ $t("commons.personal.api") }}
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
</template>
