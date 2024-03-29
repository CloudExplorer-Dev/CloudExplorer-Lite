<script setup lang="ts">
import { reactive, ref } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { ElMessage } from "element-plus";
import { useI18n } from "vue-i18n";
import { updateUserPwd } from "@commons/api/user";
import { useUserStore } from "@commons/stores/modules/user";

const { t } = useI18n();
const userStore = useUserStore();

const dialogVisible = ref(false);
defineExpose({
  dialogVisible, //允许父组件操作此变量
});

const formRef = ref<FormInstance | undefined>();
const form = reactive({
  oldPassword: "",
  newPassword: "",
  confirmPassword: "",
});

const confirmPwdValidator = (rule: any, value: any, callback: any) => {
  if (value !== form.newPassword) {
    callback(new Error(t("commons.validate.confirm_pwd")));
  } else {
    callback();
  }
};

// 表单校验规则
const rules: FormRules = {
  oldPassword: [
    {
      required: true,
      message: t("commons.validate.input", [
        t("commons.personal.old_password"),
      ]),
      trigger: "blur",
    },
  ],
  newPassword: [
    {
      required: true,
      message: t("commons.validate.required", [
        t("commons.personal.new_password"),
      ]),
      trigger: "blur",
    },
    {
      required: true,
      pattern: /^(?!.*\s)(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[\W_]).{8,30}$/,
      message: t("commons.validate.pwd"),
      trigger: "blur",
    },
  ],
  confirmPassword: [
    {
      required: true,
      message: t("commons.validate.input", [
        t("commons.personal.confirm_password"),
      ]),
      trigger: "blur",
    },
    { validator: confirmPwdValidator, trigger: "blur" },
  ],
};

const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.validate((valid) => {
    if (valid) {
      const param = {
        oldPassword: form.oldPassword,
        newPassword: form.newPassword,
      };
      updateUserPwd(param)
        .then(() => {
          dialogVisible.value = false;
          ElMessage.success(t("commons.msg.save_success"));
          userStore.doLogout();
        })
        .catch((err) => {
          ElMessage.error(err.response.data.message);
        });
    } else {
      return false;
    }
  });
};
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    :title="$t('commons.personal.edit_pwd')"
    width="35%"
    destroy-on-close
  >
    <el-form
      :model="form"
      :rules="rules"
      ref="formRef"
      label-width="auto"
      label-position="right"
    >
      <el-form-item
        :label="$t('commons.personal.old_password')"
        prop="oldPassword"
      >
        <el-input
          v-model.trim="form.oldPassword"
          type="password"
          show-password
          autocomplete="new-password"
          autofocus
        />
      </el-form-item>
      <el-form-item
        :label="$t('commons.personal.new_password')"
        prop="newPassword"
      >
        <el-input
          v-model.trim="form.newPassword"
          type="password"
          show-password
          autocomplete="new-password"
        />
      </el-form-item>
      <el-form-item
        :label="$t('commons.personal.confirm_password')"
        prop="confirmPassword"
      >
        <el-input
          v-model.trim="form.confirmPassword"
          type="password"
          show-password
          autocomplete="new-password"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">{{
          $t("commons.btn.cancel")
        }}</el-button>
        <el-button type="primary" @click="submitForm(formRef)">{{
          $t("commons.btn.save")
        }}</el-button>
      </span>
    </template>
  </el-dialog>
</template>
