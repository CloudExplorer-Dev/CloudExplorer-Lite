<script setup lang="ts">
import { reactive, ref } from "vue";
import { FormInstance, FormRules } from "element-plus";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const dialogVisible = ref(false);
defineExpose({
  dialogVisible,
});
const formRef = ref<FormInstance | undefined>();
const form = reactive({
  email: "",
  phone: "",
  wechat: "",
});

const rules: FormRules = {
  phone: [
    {
      pattern: /^1[3|4|5|7|8][0-9]{9}$/,
      message: t("user.validate.phone_format"),
      trigger: "blur",
    },
  ],
  email: [
    {
      pattern: /^[a-zA-Z0-9_._-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
      message: t("user.validate.email_format"),
      trigger: "blur",
    },
  ],
};

const handleCancel = (formEl: FormInstance) => {
  dialogVisible.value = false;
  formEl.resetFields();
};

const handleSave = (formEl: FormInstance) => {
  if (!formEl) return false;
  formEl.validate((valid) => {
    if (valid) {
      const param = {
        email: form.email,
        phone: form.phone,
        wechat: form.wechat,
      };
      //TODO 更新通知设置
    } else {
      return false;
    }
  });
};
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    :title="$t('通知设置')"
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
      <el-form-item label="Email" prop="email">
        <el-input v-model="form.email" clearable />
      </el-form-item>
      <el-form-item :label="$t('手机号码')" prop="phone">
        <el-input v-model="form.phone" clearable />
      </el-form-item>
      <el-form-item :label="$t('企业微信账号')" prop="wechat">
        <el-input v-model="form.wechat" clearable />
      </el-form-item>
    </el-form>

    <div class="notification_title">
      <span style="font-size: 12px">
        <el-icon>
          <InfoFilled />
        </el-icon>
        邮箱、手机号设置后将与用户基本信息关联。手机号将做为钉钉平台推送标识。企业微信账号参考:<a
          href="https://work.weixin.qq.com/api/doc/90000/90135/90665"
          target="_blank"
          style="color: var(--el-color-primary)"
        >
          https://work.weixin.qq.com/api/doc/90000/90135/90665</a
        ></span
      >
    </div>

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

<style lang="scss">
.notification_title {
  border-radius: 4px;
  background: #dcdfe6;
  padding: 5px;
}
</style>
