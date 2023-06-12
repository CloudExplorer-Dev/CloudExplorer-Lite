import axios, { type AxiosRequestConfig } from "axios";
import { ElMessage } from "element-plus";
import type { NProgress } from "nprogress";
import type { Ref } from "vue";
import type { Result } from "@commons/request/Result";
import { store } from "@commons/stores";
import { useUserStore } from "@commons/stores/modules/user";
import Config from "@commons/utils/constants";
import _ from "lodash";
import route from "@commons/router";
import { ref, type WritableComputedRef } from "vue";

const axiosConfig = {
  baseURL: import.meta.env.VITE_BASE_PATH,
  withCredentials: false,
  timeout: 60000,
  // headers: {},
};

const instance = axios.create(axiosConfig);

// 设置请求拦截器
instance.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    const userStore = useUserStore(store);
    if (config.headers === undefined) {
      config.headers = {};
    }
    config.headers[Config.CE_TOKEN_KEY] = userStore.currentToken();
    // config.headers[Config.CE_TOKEN_KEY] = localStorage.getItem(
    //   Config.CE_TOKEN_KEY
    // ) as string;
    config.headers[Config.CE_ROLE_KEY] = userStore.currentRole;
    config.headers[Config.CE_SOURCE_KEY] = userStore.currentSource;

    //针对params中的[]符号处理，不使用axios默认的传params，这里直接拼进url里
    if (config.params && _.keysIn(config.params).length > 0) {
      const paramList: Array<string> = [];
      _.forIn(config.params, (value, key) => {
        if (value !== undefined) {
          if (value instanceof Array) {
            _.forEach(value, (v) => {
              paramList.push(
                `${encodeURI(key + "[]")}=${encodeURI(
                  v instanceof Object ? JSON.stringify(v) : v
                )}`
              );
            });
          } else {
            paramList.push(
              `${encodeURI(key)}=${encodeURI(
                value instanceof Object ? JSON.stringify(value) : value
              )}`
            );
          }
        }
      });
      //拼接url
      if (paramList.length > 0) {
        config.url += "?" + _.join(paramList, "&");
        //清空params
        config.params = undefined;
      }
    }

    return config;
  },
  (err: any) => {
    return Promise.reject(err);
  }
);

//设置响应拦截器
instance.interceptors.response.use(
  (response: any) => {
    if (response.data) {
      if (response.data.code !== 200) {
        ElMessage.error(response.data.message);
      } else {
        //取出header中返回的token
        const token = response.headers[Config.CE_TOKEN_KEY];
        if (token != null && token.length > 0) {
          const userStore = useUserStore(store);
          userStore.setToken(token);
        }
      }
    }
    if (response.headers["content-type"] === "application/octet-stream") {
      return response;
    }
    return response;
  },
  (err: any) => {
    if (err.code === "ECONNABORTED") {
      ElMessage.error(err.message);
      console.error(err);
    }
    if (
      err.response?.status === 401 &&
      route.getRoute()?.router?.currentRoute?.value.name !== "signin" //已经是登录页面了没必要再跳转了
    ) {
      //401时清空token
      const userStore = useUserStore(store);

      let path = _.replace(
        window.location.href,
        window.location.protocol + "//" + window.location.host,
        ""
      );

      if (window.__MICRO_APP_ENVIRONMENT__) {
        path =
          "/" +
          import.meta.env.VITE_APP_NAME +
          "?" +
          import.meta.env.VITE_APP_NAME +
          "=" +
          window.microApp.router.encode(path);
      }

      userStore.doLogout(path === "/" ? undefined : path);
    }
    if (err.response?.status === 403) {
      ElMessage.error(
        err.response.data && err.response.data.message
          ? err.response.data.message
          : "没有权限访问"
      );
    }
    return Promise.reject(err);
  }
);

export const request = instance;

/* 简化请求方法，统一处理返回结果，并增加loading处理，这里以{success,data,message}格式的返回值为例，具体项目根据实际需求修改 */
const promise: (
  request: Promise<any>,
  loading?: NProgress | Ref<boolean> | WritableComputedRef<boolean>
) => Promise<Result<any>> = (request, loading = ref(false)) => {
  return new Promise((resolve, reject) => {
    if ((loading as NProgress).start) {
      (loading as NProgress).start();
    } else {
      (loading as Ref).value = true;
    }
    request
      .then((response) => {
        // blob类型的返回状态是response.status
        if (response.data.code === 200 || response.status == 200) {
          resolve(response.data);
        } else {
          reject(response.data);
        }
      })
      .catch((error) => {
        reject(error);
      })
      .finally(() => {
        if ((loading as NProgress).start) {
          (loading as NProgress).done();
        } else {
          (loading as Ref).value = false;
        }
      });
  });
};

/**
 * 发送get请求   一般用来请求资源
 * @param url    资源url
 * @param params 参数
 * @param loading loading
 * @returns 异步promise对象
 */
export const get: (
  url: string,
  params?: unknown,
  loading?: NProgress | Ref<boolean>
) => Promise<Result<any>> = (
  url: string,
  params: unknown,
  loading?: NProgress | Ref<boolean>
) => {
  return promise(request({ url: url, method: "get", params }), loading);
};

/**
 * faso post请求 一般用来添加资源
 * @param url    资源url
 * @param params 参数
 * @param data   添加数据
 * @param loading loading
 * @returns 异步promise对象
 */
export const post: (
  url: string,
  params?: unknown,
  data?: unknown,
  loading?: NProgress | Ref<boolean>
) => Promise<Result<any> | any> = (url, params, data, loading) => {
  return promise(request({ url: url, method: "post", data, params }), loading);
};

/**|
 * 发送put请求 用于修改服务器资源
 * @param url     资源地址
 * @param params  params参数地址
 * @param data    需要修改的数据
 * @param loading 进度条
 * @returns
 */
export const put: (
  url: string,
  params?: unknown,
  data?: unknown,
  loading?: NProgress | Ref<boolean>
) => Promise<Result<any>> = (url, params, data, loading) => {
  return promise(request({ url: url, method: "put", data, params }), loading);
};

/**
 * 删除
 * @param url     删除url
 * @param params  params参数
 * @param loading 进度条
 * @returns
 */
export const del: (
  url: string,
  params?: unknown,
  data?: unknown,
  loading?: NProgress | Ref<boolean>
) => Promise<Result<any>> = (url, params, data, loading) => {
  return promise(
    request({ url: url, method: "delete", data, params }),
    loading
  );
};

export const exportExcel: (
  url: string,
  params?: unknown,
  loading?: NProgress | Ref<boolean>
) => Promise<Result<any>> = (
  url: string,
  params: unknown,
  loading?: NProgress | Ref<boolean>
) => {
  return promise(
    request({ url: url, method: "get", params, responseType: "blob" }),
    loading
  );
};

/**
 * 与服务器建立ws链接
 * @param url websocket路径
 * @returns  返回一个websocket实例
 */
export const socket = (url: string) => {
  let protocol = "ws://";
  if (window.location.protocol === "https:") {
    protocol = "wss://";
  }
  let uri = protocol + window.location.host + url;
  if (!import.meta.env.DEV) {
    uri =
      protocol + window.location.host + import.meta.env.VITE_BASE_PATH + url;
  }
  return new WebSocket(uri);
};
export default instance;
