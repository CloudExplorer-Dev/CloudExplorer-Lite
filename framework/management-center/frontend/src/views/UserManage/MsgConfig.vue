<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { useI18n } from "vue-i18n";
import { userNotificationSetting, findUserNotification } from "@/api/user";
import { ElMessage } from "element-plus";

const props = defineProps({
  visible: {
    type: Boolean,
    required: true,
  },
  userId: {
    type: String,
    required: true,
  },
});
const emits = defineEmits(["update:visible"]);

const { t } = useI18n();
const formRef = ref<FormInstance | undefined>();
const form = reactive({
  id: null,
  email: "",
  phone: "",
  wechatAccount: "",
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
      required: true,
      message: t("commons.validate.required", [t("user.email")]),
      trigger: "blur",
    },
    {
      pattern: /^[a-zA-Z0-9_._-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
      message: t("user.validate.email_format"),
      trigger: "blur",
    },
  ],
};

const handleCancel = (formEl: FormInstance) => {
  emits("update:visible", false);
  formEl.resetFields();
};

const getUserNotification = () => {
  if (props.userId) {
    findUserNotification(props.userId).then((res) => {
      form.id = res.data.id;
      form.email = res.data.email;
      form.phone = res.data.phone;
      form.wechatAccount = res.data.wechatAccount;
    });
  }
};

const handleSave = (formEl: FormInstance) => {
  if (!formEl) return false;
  formEl.validate((valid) => {
    if (valid) {
      const param = {
        id: props.userId,
        email: form.email,
        phone: form.phone,
        wechatAccount: form.wechatAccount,
      };
      userNotificationSetting(param)
        .then(() => {
          emits("update:visible", false);
          ElMessage.success(t("commons.msg.save_success"));
        })
        .catch((err) => {
          ElMessage.error(err.response.data.message);
        });
    } else {
      return false;
    }
  });
};

onMounted(() => {
  getUserNotification();
});
</script>

<template>
    <el-form
      :model="form"
      :rules="rules"
      ref="formRef"
      label-width="auto"
      label-position="top"
      require-asterisk-position="right"
    >
      <el-form-item label="Email" prop="email">
        <el-input v-model="form.email" clearable />
      </el-form-item>
      <el-form-item :label="$t('user.phone')" prop="phone">
        <el-input v-model="form.phone" clearable />
      </el-form-item>
      <el-form-item :label="$t('user.wechatAccount')" prop="wechatAccount">
        <el-input v-model="form.wechatAccount" clearable />
      </el-form-item>
    </el-form>

    <div class="notification_title">
      <span style="font-size: 12px">
        <el-icon>
          <InfoFilled />
        </el-icon>
        {{ $t("user.notify_tips") }}:<a
          href="https://work.weixin.qq.com/api/doc/90000/90135/90665"
          target="_blank"
          style="color: var(--el-color-primary)"
        >
          https://work.weixin.qq.com/api/doc/90000/90135/90665</a
        ></span
      >
    </div>

    <div class="dialog_footer footer-btn">
      <el-button @click="handleCancel(formRef)">{{
        $t("commons.btn.cancel")
      }}</el-button>
      <el-button type="primary" @click="handleSave(formRef)">{{
        $t("commons.btn.save")
      }}</el-button>
    </div>
</template>

<style lang="scss">
.notification_title {
  border-radius: 4px;
  background: #dcdfe6;
  padding: 5px;
}
.dialog_footer {
  text-align: right;
  padding-top: 24px;
  padding-bottom: 29px;
}
</style>
