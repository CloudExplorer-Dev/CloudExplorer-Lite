<script setup lang="ts">
import { reactive, ref } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { $tv } from "@commons/base-locales";
import { useI18n } from "vue-i18n";
import { ElMessage } from "element-plus/es";
import { updatePwd } from "@/api/user";
import type { InternalRuleItem } from "async-validator/dist-types/interface";

const props = defineProps<{
  userId?: string;
}>();

const dialogVisible = ref(false);
defineExpose({
  dialogVisible,
});

const { t } = useI18n();
const formRef = ref<FormInstance | undefined>();
const form = reactive({
  newPassword: "",
  confirmPassword: "",
});

const confirmPwdValidator = (
  rule: InternalRuleItem,
  value: string,
  callback: (error?: string | Error) => void
) => {
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
    if (valid && props.userId) {
      const param = {
        id: props.userId,
        password: form.newPassword,
      };
      updatePwd(param)
        .then(() => {
          ElMessage.success(t("commons.msg.save_success"));
          handleCancel(formEl);
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
