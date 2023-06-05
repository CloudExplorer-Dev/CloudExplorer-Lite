<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { useI18n } from "vue-i18n";
import { ElMessage } from "element-plus";
import OperatedLogApi, { saveLogClearConfig } from "@/api/operated_log/index";

const props = defineProps({
  visible: {
    type: Boolean,
    required: true,
  },
  paramValue: {
    type: String,
    required: true,
  },
  paramKey: {
    type: String,
    required: true,
  },
});
const emits = defineEmits(["update:visible"]);

const { t } = useI18n();
const formRef = ref<FormInstance | undefined>();
const form = reactive({
  paramKey: props.paramKey,
  paramValue: "",
});

const rules: FormRules = {
  paramValue: [
    {
      pattern: /^[1-9]$|^[0-1][0-9]$|^2[0-4]$/,
      message: "最少1个月,最多24个月",
      trigger: "blur",
    },
  ],
};

const handleCancel = (formEl: FormInstance) => {
  emits("update:visible", false);
  formEl.resetFields();
};

const getLogClearConfig = () => {
  if (props.paramKey) {
    OperatedLogApi.getLogClearConfig({ paramKey: props.paramKey }).then(
      (res) => {
        form.paramValue = res.data;
      }
    );
  }
};

const handleSave = (formEl: FormInstance) => {
  if (!formEl) return false;
  formEl.validate((valid) => {
    if (valid) {
      const param = {
        paramKey: props.paramKey,
        paramValue: form.paramValue,
      };
      OperatedLogApi.saveLogClearConfig(param)
        .then(() => {
          emits("update:visible", false);
          ElMessage.success("保存成功");
        })
        .catch((err) => {
          ElMessage.error(err);
        });
    } else {
      return false;
    }
  });
};

onMounted(() => {
  getLogClearConfig();
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
    <el-form-item label="日志保存月数" prop="paramValue" required>
      <el-input v-model="form.paramValue" clearable />
    </el-form-item>
  </el-form>

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
}
</style>
