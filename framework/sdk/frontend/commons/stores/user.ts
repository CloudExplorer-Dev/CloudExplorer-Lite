import { login } from "../api/user";
import { LoginRequest, LoginResponse } from "../api/user/type";
import { defineStore } from "pinia";
import type { Result } from "../request/Result";
import { getToken, setToken, removeToken } from "../utils/auth";
import { setLanguage, getLanguage } from "../base-locales";
import { useRouter } from "vue-router";

export const useUserStore = defineStore({
  id: "user",
  state: () => ({
    token: getToken(),
    language: getLanguage(),
  }),
  getters: {},
  actions: {
    login(loginRequest: LoginRequest) {
      return login(loginRequest).then((response: Result<LoginResponse>) => {
        const token = response.data.token;
        this.token = token;
        setToken(token);
      });
    },
    logout() {
      removeToken();
      useRouter().push({ name: "login" });
    },
    setLanguage(language: string) {
      this.language = language;
      setLanguage(language);
      //TODO 保存语言信息
    },
  },
});
