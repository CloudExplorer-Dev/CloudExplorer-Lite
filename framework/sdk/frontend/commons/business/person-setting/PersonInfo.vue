<script setup lang="ts">
import { updateUser } from "@commons/api/user";
import { ref } from "vue";
import { ElMessage, type FormInstance, type FormRules } from "element-plus";
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

const formRef = ref<FormInstance | undefined>();

// 表单校验规则
const rules: FormRules = {
  name: [
    {
      required: true,
      message: t("commons.validate.required", [t("commons.personal.username")]),
      trigger: "blur",
    },
  ],
  email: [
    {
      required: true,
      message: t("commons.validate.required", [t("commons.personal.email")]),
      trigger: "blur",
    },
    {
      required: true,
      pattern: /^[a-zA-Z0-9_._-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
      message: t("commons.validate.format_error", [
        t("commons.personal.email"),
      ]),
      trigger: "blur",
    },
  ],
  phone: [
    {
      pattern: /^1[3|4|5|7|8][0-9]{9}$/,
      message: t("commons.validate.format_error", [
        t("commons.personal.phone"),
      ]),
      trigger: "blur",
    },
  ],
};

const saveUserInfo = (formEl: FormInstance | undefined, userInfo: User) => {
  if (!formEl) return;
  formEl.validate((valid) => {
    if (valid) {
      updateUser(userInfo, loading)
        .then(() => {
          dialogVisible.value = false;
          ElMessage.success(t("commons.msg.save_success"));
        })
        .catch((err) => {
          ElMessage.error(err.response.data.message);
        });
    }
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
      :rules="rules"
      ref="formRef"
      label-width="auto"
      label-position="right"
      v-loading="loading"
    >
      <el-form-item label="ID">
        <el-input v-model="form.username" type="text" disabled />
      </el-form-item>
      <el-form-item :label="$t('commons.personal.username')" prop="name">
        <el-input
          v-model.trim="form.name"
          type="text"
          maxlength="30"
          show-word-limit
        />
      </el-form-item>
      <el-form-item :label="$t('commons.personal.email')" prop="email">
        <el-input
          v-model.trim="form.email"
          type="text"
          maxlength="50"
          show-word-limit
        />
      </el-form-item>
      <el-form-item :label="$t('commons.personal.phone')" prop="phone">
        <el-input v-model.trim="form.phone" />
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">
          {{ $t("commons.btn.cancel") }}
        </el-button>
        <el-button type="primary" @click="saveUserInfo(formRef, form)">
          {{ $t("commons.btn.save") }}
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>
