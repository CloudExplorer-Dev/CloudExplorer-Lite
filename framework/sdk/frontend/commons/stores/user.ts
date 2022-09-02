import { login } from "@commons/api/user";
import type { LoginRequest, LoginResponse } from "@commons/api/user/type";
import { defineStore } from "pinia";
import type { Result } from "@commons/request/Result";
import { getToken, removeToken, setToken } from "@commons/utils/authStorage";
import { getLanguage, setLanguage } from "@commons/base-locales";
import { useRouter } from "vue-router";
import type { Ref } from "vue";

export const useUserStore = defineStore({
  id: "user1",
  state: () => ({
    token: getToken(),
    language: getLanguage(),
  }),
  getters: {},
  actions: {
    login(loginRequest: LoginRequest, loading?: Ref<boolean>) {
      return login(loginRequest, loading).then(
        (response: Result<LoginResponse>) => {
          /*const token = response.data.token;
          this.token = token;
          setToken(token);*/
          console.log(getToken());
        }
      );
    },
    logout() {
      removeToken();
      useRouter().push({ name: "signin" });
    },
    setLanguage(language: string) {
      this.language = language;
      setLanguage(language);
      //TODO 保存语言信息
    },
  },
});
