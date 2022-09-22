import { defineStore } from "pinia";
import route from "@commons/router";
import authStorage from "@commons/utils/authStorage";
import type { Ref } from "vue";
import { fetchCurrentUser, login } from "@commons/api/user";
import type {
  LoginRequest,
  User,
  UserRole,
  UserStoreObjectWithLoginStatus,
} from "@commons/api/user/type";
import { find, flatMap, has, join } from "lodash";
import type { Role } from "@commons/api/role/type";
import { usePermissionStore } from "@commons/stores/modules/permission";
import type { SimpleMap } from "@commons/api/base/type";

export const languages: SimpleMap<string> = {
  "zh-cn": "中文(简体)",
  "zh-tw": "中文(繁體)",
  en: "English",
};

export const useUserStore = defineStore({
  id: "user",
  state: (): UserStoreObjectWithLoginStatus => ({
    userStoreObject: {},
    login: false,
    lang: "none",
  }),
  getters: {
    currentUser(state: any): User | undefined {
      if (
        authStorage.getToken() != null &&
        state?.userStoreObject?.user == null
      ) {
        console.log("currentUser init");
        state.getCurrentUser();
      }
      return state.userStoreObject?.user;
    },
    currentToken(state: any): string | null {
      return state.userStoreObject?.token
        ? state.userStoreObject?.token
        : authStorage.getToken();
    },
    isLogin(state: any): boolean {
      return state.login;
    },
    currentRole(state: any): string {
      return state?.userStoreObject?.role
        ? state.userStoreObject.role
        : "ANONYMOUS";
    },
    currentSource(state: any): string | undefined | null {
      return state?.userStoreObject?.source;
    },
    /**
     * 返回用户角色名
     * @param state
     */
    currentRoleSourceName(state: any): string | undefined | null {
      const roleList: UserRole[] | undefined =
        state?.userStoreObject?.user?.roleMap[state?.userStoreObject?.role];
      let roles: Role[] | undefined;
      if (
        state?.userStoreObject?.role == "ORGADMIN" ||
        state?.userStoreObject?.role == "USER"
      ) {
        roles = find(roleList, state?.userStoreObject?.source)?.roles;
      } else {
        if (roleList) {
          roles = roleList[0]?.roles;
        }
      }
      //todo add source
      return join(
        flatMap(roles, (o) => o.name),
        ", "
      );
    },
    currentLang(state: any): string {
      if (state.lang === "none") {
        state.changeLang(authStorage.getLang());
      }
      return state.lang;
    },
  },
  actions: {
    async doLogin(loginRequest: LoginRequest, loading?: Ref<boolean>) {
      await login(loginRequest, loading);
      if (authStorage.getToken() == null) {
        return;
      }
      this.userStoreObject.token = authStorage.getToken();
      await this.getCurrentUser();
    },
    doLogout() {
      this.clear();
      route.getRoute()?.router.push({ name: "signin" });
    },
    clear() {
      this.login = false;
      this.userStoreObject = {};
      authStorage.removeToken();
    },
    async getCurrentUser(): Promise<User | null> {
      if (authStorage.getToken() == null) {
        this.login = false;
        return null;
      }
      try {
        const user: User = (await fetchCurrentUser()).data;
        let storedRole = authStorage.getRole();
        let storedSource: string | undefined | null = authStorage.getSource();
        if (user.roleMap == null) {
          //理论上后台必会返回，至少有 ANONYMOUS 角色
          return null;
        }
        let userRoles: UserRole[] | undefined;

        if (storedRole == null || !has(user.roleMap, storedRole)) {
          //如果没有这个role，从admin角色开始判断
          if (has(user.roleMap, "ADMIN")) {
            storedRole = "ADMIN";
          } else if (has(user.roleMap, "ORGADMIN")) {
            storedRole = "ORGADMIN";
          } else if (has(user.roleMap, "USER")) {
            storedRole = "USER";
          } else {
            //ANONYMOUS
            storedRole = "ANONYMOUS";
          }
        }
        if (storedRole == "ORGADMIN" || storedRole == "USER") {
          userRoles = user.roleMap[storedRole];
          console.log(userRoles);
          //判断source
          const findRole = find(userRoles, { source: storedSource });
          if (findRole == undefined) {
            storedSource = userRoles ? userRoles[0].source : null;
          }
        } else {
          storedSource = null;
        }

        if (!storedRole) {
          authStorage.removeRole();
        } else {
          authStorage.setRole(storedRole);
        }
        if (!storedSource) {
          authStorage.removeSource();
        } else {
          authStorage.setSource(storedSource);
        }

        this.login = true;
        this.userStoreObject.user = user;
        this.changeRole(storedRole, storedSource);

        const permissionStore = usePermissionStore();

        //刷新权限
        await permissionStore.refreshPermissions();
        //console.log(permissionStore.userPermissions);

        return this.currentUser;
      } catch (err) {
        console.log(err);
        return null;
      }
    },
    changeRole(role: string, source?: string | null) {
      this.userStoreObject.role = role;
      this.userStoreObject.source = source;
      authStorage.setRole(role);
      if (source) {
        authStorage.setSource(source);
      } else {
        authStorage.removeSource();
      }
    },
    changeLang(lang: string) {
      if (!has(languages, lang)) {
        lang = "zh-cn";
      }
      this.lang = lang;
      authStorage.setLang(lang);
    },
  },
});
