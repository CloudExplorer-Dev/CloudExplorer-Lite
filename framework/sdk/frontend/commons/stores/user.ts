import {login} from "@commons/api/user";
import type {LoginRequest, LoginResponse} from "@commons/api/user/type";
import {defineStore} from "pinia";
import type {Result} from "@commons/request/Result";
import {getToken, removeToken, setToken} from "@commons/utils/auth";
import {getLanguage, setLanguage} from "@commons/base-locales";
import {useRouter} from "vue-router";

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
