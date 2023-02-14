<script setup lang="ts">
import { updateUser } from "@commons/api/user";
import { ref } from "vue";
import { ElMessage } from "element-plus";
import { useI18n } from "vue-i18n";

import { useUserStore } from "@commons/stores/modules/user";
import { User } from "@commons/api/user/type";

const userStore = useUserStore();

const { t } = useI18n();

const dialogVisible = ref(false);
defineExpose({
  dialogVisible,
});

const defaultUser: User = new User("", "", "", "");

const form = ref(defaultUser);

const loading = ref<boolean>(false);

const saveUserInfo = (userInfo: User) => {
  console.log(userInfo);
  updateUser(userInfo, loading).then(() => {
    dialogVisible.value = false;
    ElMessage.success(t("commons.msg.save_success"));
  });
};

function onOpen() {
  //通过store拿，顺便能刷新权限
  userStore.getCurrentUser(loading).then((result: User | null) => {
    form.value = result ? result : defaultUser;
  });
}
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    :title="$t('commons.personal.personal_info')"
    width="35%"
    @open="onOpen"
    destroy-on-close
  >
    <el-form
      :model="form"
      label-width="auto"
      label-position="right"
      v-loading="loading"
    >
      <el-form-item label="ID">
        <el-input v-model="form.username" type="text" disabled />
      </el-form-item>
      <el-form-item :label="$t('commons.personal.username')">
        <el-input
          v-model="form.name"
          type="text"
          maxlength="30"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="Email">
        <el-input
          v-model="form.email"
          type="text"
          maxlength="50"
          show-word-limit
        />
      </el-form-item>
      <el-form-item :label="$t('commons.personal.phone')">
        <el-input v-model="form.phone" />
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">{{
          $t("commons.btn.cancel")
        }}</el-button>
        <el-button type="primary" @click="saveUserInfo(form)">{{
          $t("commons.btn.save")
        }}</el-button>
      </span>
    </template>
  </el-dialog>
</template>
