<template v-loading="_loading">
  <div style="width: 100%">
    <template v-if="!confirm">
      <el-form-item :rules="pwdRules" :prop="props.field">
        <ce-regex-tooltip
          :description="'登录密码必须同时符合以下规则'"
          :model-value="_data"
          :rules="pwdRules"
        >
          <el-input :show-password="true" v-bind="$attrs" v-model="_data" />
        </ce-regex-tooltip>
      </el-form-item>
    </template>
    <template v-else>
      <div class="detail-box">
        <el-descriptions :column="3" direction="vertical">
          <el-descriptions-item width="33.33%" />
        </el-descriptions>
      </div>
    </template>
  </div>
</template>
<script setup lang="ts">
import { computed, ref } from "vue";
import { type FormInstance } from "element-plus";
import type { FormView } from "@commons/components/ce-form/type";

const props = defineProps<{
  modelValue?: string;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  otherParams: any;
  formItem: FormView;
  confirm?: boolean;
}>();

const emit = defineEmits(["update:modelValue", "change"]);

// 校验实例对象
const ruleFormRef = ref<FormInstance>();
const _loading = ref<boolean>(false);

const regexByOs = computed(() => {
  if (props.allData.os?.toLowerCase().indexOf("windows") > -1) {
    return {
      regexMessage:
        "Windows系统密码不能包含用户名或用户名的逆序，不能包含用户名中超过两个连续字符的部分。",
      message: "登录密码不符合规则要求",
      trigger: "blur",
      pattern:
        "^(?!.*Adm)(?!.*dmi)(?!.*min)(?!.*ini)(?!.*nis)(?!.*ist)(?!.*str)(?!.*tra)(?!.*rat)(?!.*ato)(?!.*tor)(?!.*rotartsinimd[A|a]).*$",
      regex:
        "^(?!.*Adm)(?!.*dmi)(?!.*min)(?!.*ini)(?!.*nis)(?!.*ist)(?!.*str)(?!.*tra)(?!.*rat)(?!.*ato)(?!.*tor)(?!.*rotartsinimd[A|a]).*$",
      required: true,
    };
  } else {
    return {
      regexMessage: "密码不能包含用户名或用户名的逆序。",
      message: "登录密码不符合规则要求",
      trigger: "blur",
      pattern: "^(?!.*(?:root|\\btoor)\\b).*$",
      regex: "^(?!.*(?:root|\\btoor)\\b).*$",
      required: true,
    };
  }
});

const pwdRules = [
  {
    regexMessage: "8~26个字符。",
    message: "登录密码不符合规则要求",
    trigger: "blur",
    pattern: "^.{8,26}$",
    regex: "^.{8,26}$",
    required: true,
  },
  {
    regexMessage:
      "密码只能包含大写字母、小写字母、数字和特殊宇符(!@$%^-_=+[{}]:,./?~#*)且至少包含四种字符中的三种。",
    message: "登录密码不符合规则要求",
    trigger: "blur",
    pattern:
      "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@$%^\\-_=+[{}\\]:,./?~#*])[a-zA-Z\\d!@$%^\\-_=+[{}\\]:,./?~#*]{8,}$",
    regex:
      "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@$%^\\-_=+[{}\\]:,./?~#*])[a-zA-Z\\d!@$%^\\-_=+[{}\\]:,./?~#*]{8,}$",
    required: true,
  },
  regexByOs.value,
];

const _data = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});
</script>
<style lang="scss" scoped>
.box-content {
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  padding: 18px;
  background-color: #f7f9fc;
  border-radius: 4px;
  .el-form-item {
    margin-bottom: 0;
    margin-right: 0;
    padding: 0px 2px 0px 8px;
    width: 100%;
    .el-form-item__content .custom-popover {
      width: 100%;
    }
  }

  .delete-icon {
    cursor: pointer;
    margin-left: 8px;
  }
}
.add-button {
  margin: 10px;
}
.detail-box {
  margin-top: 8px;
  :deep(.el-descriptions__header) {
    margin-bottom: 0 !important;
  }
}
</style>
