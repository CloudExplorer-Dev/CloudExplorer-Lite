<script setup lang="ts">
import {
  computed,
  onBeforeMount,
  onMounted,
  onUnmounted,
  reactive,
  ref,
} from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "@commons/stores/modules/user";
import type { FormInstance, FormRules } from "element-plus";
import type { LoginRequest } from "@commons/api/user/type";
import { useI18n } from "vue-i18n";
import _ from "lodash";
import { ElMessage } from "element-plus";
import { get } from "@commons/request";

const { t } = useI18n();

const userStore = useUserStore();
const router = useRouter();

const loading = ref(false);
const formRef = ref<FormInstance | undefined>();

const form = reactive<LoginRequest>({
  username: "",
  password: "",
});

const rules: FormRules = {
  username: [
    {
      required: true,
      message: t("commons.validate.input", [t("commons.login.username")]),
      trigger: "blur",
    },
  ],
  password: [
    {
      required: true,
      message: t("commons.validate.input", [t("commons.login.password")]),
      trigger: "blur",
    },
    {
      min: 6,
      max: 30,
      message: t("commons.validate.limit", ["6", "30"]),
      trigger: "blur",
    },
  ],
};
const msg = ref("");
const redirect = computed(() => {
  const r = _.split(window.location.href, "redirect=");
  if (r.length > 1) {
    return _.split(r[1], "&")[0];
  }
  return "/";
});

onBeforeMount(() => {
  document.addEventListener("keydown", watchEnter);
});
onUnmounted(() => {
  document.removeEventListener("keydown", watchEnter);
});

// 监控键盘回车事件
const watchEnter = (e: KeyboardEvent) => {
  if (e.key === "Enter") {
    submitForm(formRef.value);
  }
};

const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.validate((valid: boolean) => {
    loading.value = true;
    if (valid) {
      userStore
        .doLogin(form)
        .then(() => {
          router.push(redirect.value);
        })
        .catch((error: any) => {
          msg.value = error.response.data.message;
          ElMessage.error(msg.value);
        })
        .finally(() => {
          loading.value = false;
        });
    } else {
      return false;
    }
  });
};

const loginHint = ref();

function getHint() {
  if (import.meta.env.VITE_APP_NAME === "base") {
    get("login-hint", {}, loading).then((res) => {
      loginHint.value = res.data;
    });
  }
}

onMounted(() => {
  getHint();
});
</script>

<template>
  <div class="login-background">
    <div class="login-container">
      <el-row type="flex" v-loading="loading">
        <el-col :span="12">
          <el-form
            :model="form"
            :rules="rules"
            ref="formRef"
            size="default"
            class="login-form"
          >
            <div class="login-logo">
              <img src="../../assets/CloudExplorer-Lite-01.svg" alt="" />
            </div>
            <div class="login-welcome">{{ $t("commons.login.welcome") }}</div>
            <div class="login-form-item-container">
              <el-form-item prop="username" style="margin-bottom: 28px">
                <el-input
                  v-model.trim="form.username"
                  :placeholder="$t('commons.login.username')"
                  autofocus
                  class="login-form-input"
                />
              </el-form-item>
              <el-form-item prop="password">
                <el-input
                  v-model.trim="form.password"
                  :placeholder="$t('commons.login.password')"
                  show-password
                  maxlength="30"
                  show-word-limit
                  autocomplete="new-password"
                  class="login-form-input"
                />
              </el-form-item>
            </div>
            <div class="login-btn">
              <el-button
                type="primary"
                class="submit"
                @click="submitForm(formRef)"
                size="default"
              >
                <span>{{ $t("commons.btn.login") }}</span>
              </el-button>
            </div>
            <div class="login-hint" v-html="loginHint"></div>
          </el-form>
        </el-col>
        <el-col :span="12">
          <div class="login-image"></div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<style scoped lang="scss">
@mixin login-center {
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-background {
  background-color: #f5f5f5;
  height: 100%;
  @include login-center;
}

.login-container {
  min-width: 900px;
  width: 1200px;
  height: 520px;
  background-color: #ffffff;

  .login-form {
    margin-top: 116px;
    width: 440px;
    margin-left: 80px;
  }

  .login-logo {
    text-align: center;
    img {
      height: 53px;
    }
  }

  .login-welcome {
    margin-top: 12px;
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    line-height: 20px;
    color: #646a73;
    text-align: center;
  }

  .login-hint {
    margin-top: 12px;
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    line-height: 20px;
    color: #646a73;
    text-align: center;
  }

  .login-form-item-container {
    margin-top: 24px;
    .login-form-input {
      height: 40px;
    }
    & :deep(.el-input__inner) {
      font-size: 16px;
    }
    & :deep(.el-form-item__error) {
      font-size: 14px;
      line-height: 22px;
    }
  }

  .login-btn {
    margin-top: 48px;
    .submit {
      width: 100%;
      height: 40px;
      span {
        font-size: 16px;
      }
    }
  }

  .login-msg {
    margin-top: 10px;
    padding: 0 40px;
    color: var(--el-color-danger);
    text-align: center;
  }

  .login-image {
    background: url(@commons/assets/login-desc.jpg) no-repeat;
    background-size: cover;
    width: 100%;
    height: 520px;
  }
}
</style>
