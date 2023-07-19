<script setup lang="ts">
import { ref, computed, reactive } from "vue";
import type {
  Platform,
  CreateAccount,
  UpdateAccount,
} from "@commons/api/cloud_account/type";
import { useI18n } from "vue-i18n";
import type { FormInstance, FormRules } from "element-plus";
import CeForm from "@commons/components/ce-form/index.vue";
import type { FormView } from "@commons/components/ce-form/type";
const props = withDefaults(
  defineProps<{
    modelValue?: CreateAccount | UpdateAccount;
    activePlatform: Platform;
    type?: "dialog" | "model";
  }>(),
  {
    modelValue: () => {
      return {
        platform: "",
        name: "",
        credential: {},
      };
    },
    type: "model",
  }
);
const formViewData = computed(() => {
  return [...props.activePlatform.credentialForm];
});
const { t } = useI18n();
// 校验实例对象
const formRef = ref<FormInstance>();

const accountFormRef = ref<FormInstance>();
const emit = defineEmits(["update:modelValue"]);

const form = computed<CreateAccount | UpdateAccount>({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

// 校验对象
const rules = reactive<FormRules>({
  name: [
    {
      message: t("cloud_account.name_is_not_empty", "云账号名称不为空"),
      trigger: "blur",
      type: "string",
      required: true,
    },
  ],
  platform: [
    {
      message: t("cloud_account.platform_is_not_empty", "云平台不能为空"),
      trigger: "blur",
      type: "string",
      required: true,
    },
  ],
});

function validate(): Promise<boolean | undefined> | undefined {
  return accountFormRef.value?.validate().then(() => {
    return formRef.value?.validate();
  });
}

defineExpose({
  validate,
  formRef,
});
</script>
<template>
  <el-form
    :inline="true"
    status-icon
    label-width="130px"
    label-suffix=""
    :model="form"
    ref="accountFormRef"
    label-position="top"
    :class="{ 'model-form': type === 'model' }"
  >
    <el-form-item
      style="width: 100%; margin-right: 32px"
      :label="t('cloud_account.name', '云账号名称')"
      prop="name"
      :rules="rules.name"
    >
      <el-input
        style="width: 100%; --el-form-inline-content-width: 100%"
        v-model.trim="form.name"
        autocomplete="new-password"
        :placeholder="t('cloud_account.name_placeholder', '请输入云账号名称')"
      />
    </el-form-item>
  </el-form>
  <CeForm
    :inline="true"
    status-icon
    label-width="130px"
    label-suffix=":"
    label-position="top"
    :class="{ 'model-form': type === 'model' }"
    v-model="form.credential"
    ref="formRef"
    default-item-width="100%"
    :form-view-data="formViewData"
    :otherParams="{}"
  ></CeForm>
</template>

<style lang="scss" scoped>
.model-form {
  text-align: center;
  margin: 0 auto;
  width: 100%;
  min-width: 300px;
}
.tooltip-title {
  font-style: normal;
  font-weight: 500;
  font-size: 12px;
  line-height: 20px;
}
.tooltip-content {
  font-style: normal;
  font-weight: 400;
  font-size: 12px;
  line-height: 20px;
}
</style>
