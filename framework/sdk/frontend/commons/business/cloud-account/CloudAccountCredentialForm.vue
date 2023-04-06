<script setup lang="ts">
import { ref, computed, reactive } from "vue";
import type {
  Platform,
  CreateAccount,
  UpdateAccount,
} from "@commons/api/cloud_account/type";
import { useI18n } from "vue-i18n";
import type { FormInstance, FormRules } from "element-plus";
import CeIcon from "@commons/components/ce-icon/index.vue";

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

const { t } = useI18n();
// 校验实例对象
const formRef = ref<FormInstance>();

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

function validate(): Promise<boolean> | undefined {
  return formRef.value?.validate();
}

defineExpose({
  validate,
  formRef,
});

function jumpTo(url: string) {
  if (url) {
    window.open(url, "_blank");
  }
}
</script>
<template>
  <el-form
    ref="formRef"
    :model="form"
    :inline="true"
    status-icon
    label-width="130px"
    label-suffix=":"
    label-position="top"
    :class="{ 'model-form': type === 'model' }"
  >
    <el-form-item
      style="width: 100%"
      :label="t('cloud_account.name', '云账号名称')"
      prop="name"
      :rules="rules.name"
    >
      <el-input
        v-model.trim="form.name"
        autocomplete="new-password"
        :placeholder="t('cloud_account.name_placeholder', '请输入云账号名称')"
      />
    </el-form-item>
    <el-form-item
      style="width: 100%; position: relative"
      v-for="item in activePlatform?.credentialForm"
      :prop="`credential.${item.field}`"
      :key="item.field"
      :label="item.label"
      :rules="[
        {
          message:
            item.label + t('cloud_account.field_is_not_null', '字段不能为空'),
          trigger: 'blur',
          required: item.required,
        },
      ]"
    >
      <template #label>
        {{ item.label }}
        <el-tooltip
          effect="dark"
          v-if="item.hint"
          :key="(hint = JSON.parse(item.hint))"
        >
          <template #content>
            <div class="tooltip-title" v-if="hint.title">
              {{ hint.title }}
            </div>
            <pre class="tooltip-content" v-if="hint.content">{{
              hint.content
            }}</pre>
          </template>
          <CeIcon code="icon-maybe_outlined" size="1em" />
        </el-tooltip>
        <div
          style="float: right; color: var(--el-color-primary); cursor: pointer"
          v-if="item.extraInfo"
          :key="(extraInfo = JSON.parse(item.extraInfo))"
          @click="jumpTo(extraInfo?.url)"
        >
          {{ extraInfo?.text }}
        </div>
      </template>

      <el-input
        v-model.trim="form.credential[item.field]"
        :type="item.inputType === 'Text' ? 'text' : ''"
        :show-password="item.inputType === 'Password'"
        autocomplete="new-password"
        v-if="item.inputType === 'Text' || item.inputType === 'Password'"
      />

      <el-switch
        v-model="form.credential[item.field]"
        v-if="item.inputType === 'SwitchBtn'"
      >
      </el-switch>
    </el-form-item>
  </el-form>
</template>

<style lang="scss" scoped>
.model-form {
  text-align: center;
  margin: 0 auto;
  width: 80%;
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
