import { defineStore } from "pinia";
import authStorage from "@commons/utils/authStorage";
import type { Ref } from "vue";
import _ from "lodash";
import { fetchCurrentUser, login } from "@commons/api/user";
import { sourceTree } from "@commons/api/organization";
import type {
  LoginRequest,
  User,
  UserRole,
  UserStoreObjectWithLoginStatus,
} from "@commons/api/user/type";
import type { Role } from "@commons/api/role/type";
import { usePermissionStore } from "@commons/stores/modules/permission";
import type { SimpleMap } from "@commons/api/base/type";
import type { SourceTreeObject } from "@commons/api/organization/type";
import type { Router } from "vue-router";

export const languages: SimpleMap<string> = {
  "zh-cn": "中文(简体)",
  "zh-tw": "中文(繁體)",
  en: "English",
};

export const useUserStore = defineStore({
  id: "user",
  state: (): UserStoreObjectWithLoginStatus => ({
    userStoreObject: {},
    sourceTree: null,
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
      //return authStorage.getToken();
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
    currentRoleSourceName(
      state: any
    ): SimpleMap<string | Role[] | undefined> | undefined | null {
      const roleList: UserRole[] | undefined =
        state?.userStoreObject?.user?.roleMap[state?.userStoreObject?.role];
      let roles: Role[] | undefined;
      if (
        state?.userStoreObject?.role == "ORGADMIN" ||
        state?.userStoreObject?.role == "USER"
      ) {
        roles = _.find(
          roleList,
          (r) => r.source === state.currentSource
        )?.roles;
      } else {
        if (roleList) {
          roles = roleList[0]?.roles;
        }
      }

      if (state.currentSource) {
        return {
          roles: roles,
          sourceName: _.head(
            _.filter(state.sourceList, (s) => s.id === state.currentSource)
          )?.name,
        };
      } else {
        return { roles: roles, sourceName: "云管理平台" };
      }
    },
    sourceMap(state: any): Array<SourceTreeObject> {
      if (state.sourceTree == null) {
        state.refreshSourceTree();
      }
      return state.sourceTree;
    },
    sourceList(state: any): Array<SourceTreeObject> {
      return flat(state.sourceMap, []);
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
    setToken(token: string) {
      authStorage.setToken(token);
      if (this.userStoreObject) {
        this.userStoreObject.token = token;
      }
    },
    doLogout(redirect?: string) {
      this.clear();

      if (
        import.meta.env.VITE_APP_NAME === "base" ||
        !window.__MICRO_APP_ENVIRONMENT__
      ) {
        window.location.href =
          window.location.protocol +
          "//" +
          window.location.host +
          "/signin" +
          (redirect ? "?redirect=" + encodeURI(redirect) : "");
      } else {
        const rObj: any = { name: "signin" };
        if (redirect) {
          rObj.query = { redirect: encodeURI(redirect) };
        }
        (window.microApp.router.getBaseAppRouter() as Router)?.push(rObj);
      }
    },
    clear() {
      this.login = false;
      this.userStoreObject = {};
      authStorage.removeToken();
    },
    async getCurrentUser(loading?: Ref<boolean>): Promise<User | null> {
      if (authStorage.getToken() == null) {
        this.login = false;
        return null;
      }
      try {
        const user: User = (await fetchCurrentUser(loading)).data;
        let storedRole = authStorage.getRole();
        let storedSource: string | undefined | null = authStorage.getSource();
        if (user.roleMap == null) {
          //理论上后台必会返回，至少有 ANONYMOUS 角色
          return null;
        }
        let userRoles: UserRole[] | undefined;

        if (storedRole == null || !_.has(user.roleMap, storedRole)) {
          //如果没有这个role，从admin角色开始判断
          if (_.has(user.roleMap, "ADMIN")) {
            storedRole = "ADMIN";
          } else if (_.has(user.roleMap, "ORGADMIN")) {
            storedRole = "ORGADMIN";
          } else if (_.has(user.roleMap, "USER")) {
            storedRole = "USER";
          } else {
            //ANONYMOUS
            storedRole = "ANONYMOUS";
          }
        }
        if (storedRole == "ORGADMIN" || storedRole == "USER") {
          userRoles = user.roleMap[storedRole];
          //判断source
          const findRole = _.find(userRoles, { source: storedSource });
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
        await permissionStore.refreshPermissions(loading);

        return this.currentUser;
      } catch (err) {
        console.error(err);
        return null;
      }
    },
    async refreshSourceTree(
      loading?: Ref<boolean>
    ): Promise<Array<SourceTreeObject> | null> {
      try {
        this.sourceTree = (await sourceTree(loading)).data;
        return this.sourceTree;
      } catch (err) {
        console.error(err);
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
      if (!_.has(languages, lang)) {
        lang = "zh-cn";
      }
      this.lang = lang;
      authStorage.setLang(lang);
    },
  },
});

function flat(
  tree: Array<SourceTreeObject>,
  list: Array<SourceTreeObject>
): Array<SourceTreeObject> {
  _.forEach(tree, (obj) => {
    list.push(obj);
    if (obj.children) {
      flat(obj.children, list);
    }
  });
  return list;
}
