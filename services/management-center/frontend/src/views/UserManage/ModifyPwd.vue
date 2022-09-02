<script setup lang="ts">
import { reactive, ref } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { $tv } from "@commons/base-locales";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const dialogVisible = ref(false);
defineExpose({
  dialogVisible,
});
const formRef = ref<FormInstance | undefined>();
const form = reactive({
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

const rules: FormRules = {
  newPassword: [
    {
      required: true,
      message: $tv("commons.validate.input", "commons.personal.new_password"),
      trigger: "blur",
    },
    {
      pattern: /^(?!.*\s)(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[\W_]).{8,30}$/,
      message: t("commons.validate.pwd"),
      trigger: "blur",
    },
  ],
  confirmPassword: [
    {
      required: true,
      message: $tv(
        "commons.validate.input",
        "commons.personal.confirm_password"
      ),
      trigger: "blur",
    },
    { validator: confirmPwdValidator, trigger: "blur" },
  ],
};

const handleCancel = (formEl: FormInstance) => {
  dialogVisible.value = false;
  formEl.resetFields();
};

const handleSave = (formEl: FormInstance) => {
  if (!formEl) return;
  formEl.validate((valid) => {
    if (valid) {
      const param = {
        userId: "",
        password: form.newPassword,
      };
      //TODO 更新用户密码
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
    width="25%"
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
        :label="$t('commons.personal.new_password')"
        prop="newPassword"
      >
        <el-input
          v-model="form.newPassword"
          type="password"
          clearable
          show-password
          autocomplete="new-password"
        />
      </el-form-item>
      <el-form-item
        :label="$t('commons.personal.confirm_password')"
        prop="confirmPassword"
      >
        <el-input
          v-model="form.confirmPassword"
          type="password"
          clearable
          show-password
          autocomplete="new-password"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="handleCancel(formRef)">{{
          $t("commons.btn.cancel")
        }}</el-button>
        <el-button type="primary" @click="handleSave(formRef)">{{
          $t("commons.btn.save")
        }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style lang="scss"></style>
