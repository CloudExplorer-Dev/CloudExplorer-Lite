<script setup lang="ts">
import { onBeforeMount, onUnmounted, reactive, ref, watch } from "vue";
import type { LocationQuery } from "vue-router";
import { useRoute, useRouter } from "vue-router";
import { useUserStore } from "@commons/stores/modules/user";
import type { FormInstance, FormRules } from "element-plus";
import type { LoginRequest } from "@commons/api/user/type";
import { useI18n } from "vue-i18n";

const { t } = useI18n();

const userStore = useUserStore();
const route = useRoute();
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
const redirect = ref("");
const otherQuery = ref({});

// 获取路径参数
const getOtherQuery = (query: LocationQuery) => {
  return Object.keys(query).reduce((acc: LocationQuery, cur: string) => {
    if (cur !== "redirect") {
      acc[cur] = query[cur];
    }
    return acc;
  }, {});
};

watch(
  route,
  (route) => {
    const query = route.query;
    if (query) {
      redirect.value = query.redirect == null ? "" : query.redirect.toString();
      otherQuery.value = getOtherQuery(query);
    }
  },
  { immediate: true }
);

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
    if (valid) {
      userStore
        .doLogin(form, loading)
        .then(() => {
          router.push({
            path: redirect.value || "/",
            query: otherQuery.value,
          });
        })
        .catch((error: any) => {
          console.log(error);
          msg.value = error.message;
        });
    } else {
      return false;
    }
  });
};
</script>

<template>
  <div class="login-background">
    <div class="login-container">
      <el-row type="flex" v-loading="loading">
        <el-col :span="12">
          <el-form :model="form" :rules="rules" ref="formRef" size="default">
            <div class="login-logo">
              <img src="../../assets/blue-logo.png" alt="" />
            </div>
            <div class="login-title">{{ $t("commons.login.title") }}</div>
            <div class="login-border"></div>
            <div class="login-welcome">{{ $t("commons.login.welcome") }}</div>
            <div class="login-form">
              <el-form-item prop="username">
                <el-input
                  v-model="form.username"
                  :placeholder="$t('commons.login.username')"
                  autofocus
                />
              </el-form-item>
              <el-form-item prop="password">
                <el-input
                  v-model="form.password"
                  :placeholder="$t('commons.login.password')"
                  show-password
                  maxlength="30"
                  show-word-limit
                  autocomplete="new-password"
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
                {{ $t("commons.btn.login") }}
              </el-button>
            </div>
            <div class="login-msg">
              {{ msg }}
            </div>
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
  width: 1280px;
  height: 520px;
  background-color: #ffffff;
  @media only screen and (max-width: 1280px) {
    width: 900px;
    height: 380px;
  }

  .login-logo {
    margin-top: 30px;
    margin-left: 30px;
    @media only screen and (max-width: 1280px) {
      margin-top: 20px;
    }

    img {
      height: 45px;
    }
  }

  .login-title {
    margin-top: 50px;
    font-size: 24px;
    letter-spacing: 0;
    text-align: center;
    color: #999999;

    @media only screen and (max-width: 1280px) {
      margin-top: 20px;
    }
  }

  .login-border {
    height: 2px;
    margin: 20px auto 20px;
    position: relative;
    width: 80px;
    background: var(--el-color);
    @media only screen and (max-width: 1280px) {
      margin: 10px auto 10px;
    }
  }

  .login-welcome {
    margin-top: 50px;
    font-size: 14px;
    color: #999999;
    letter-spacing: 0;
    line-height: 18px;
    text-align: center;
    @media only screen and (max-width: 1280px) {
      margin-top: 20px;
    }
  }

  .login-form {
    margin-top: 30px;
    padding: 0 40px;

    @media only screen and (max-width: 1280px) {
      margin-top: 10px;
    }

    & :deep(.el-input__inner) {
      border-radius: 0;
    }
  }

  .login-btn {
    margin-top: 40px;
    padding: 0 40px;
    @media only screen and (max-width: 1280px) {
      margin-top: 20px;
    }

    .submit {
      width: 100%;
      border-radius: 0;
    }
  }

  .login-msg {
    margin-top: 10px;
    padding: 0 40px;
    color: var(--el-color-danger);
    text-align: center;
  }

  .login-image {
    background: url(../../assets/login-desc.png) no-repeat;
    background-size: cover;
    width: 100%;
    height: 520px;
    @media only screen and (max-width: 1280px) {
      height: 380px;
    }
  }
}
</style>
