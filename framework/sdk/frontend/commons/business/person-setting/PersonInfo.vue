<script setup lang="ts">
import { getUser, saveUser } from "@/../commons/api/user";
import { onMounted, ref } from "vue";
import { ElMessage } from "element-plus";
import { useI18n } from "vue-i18n";

const { t } = useI18n();

const dialogVisible = ref(false);
defineExpose({
  dialogVisible,
});

const form = ref({
  id: "",
  username: "",
  email: "",
  phone: "",
  weChat: "",
});

const getUserInfo = () => {
  getUser().then((res) => {
    form.value = res.data.userInfo;
  });
};

const saveUserInfo = (userInfo: any) => {
  saveUser(userInfo).then(() => {
    dialogVisible.value = false;
    ElMessage.success(t("commons.msg.save_success"));
  });
};

onMounted(() => {
  getUserInfo();
});
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    :title="$t('commons.personal.personal_info')"
    width="35%"
    destroy-on-close
  >
    <el-form :model="form" label-width="auto" label-position="right">
      <el-form-item label="ID">
        <el-input v-model="form.id" type="text" disabled />
      </el-form-item>
      <el-form-item :label="$t('commons.personal.username')">
        <el-input
          v-model="form.username"
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
      <el-form-item :label="$t('commons.personal.wechat')">
        <el-input v-model="form.weChat" />
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
