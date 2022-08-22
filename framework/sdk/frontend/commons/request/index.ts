import axios from "axios";
import { ElMessage } from "element-plus";
import packageJSON from "@/../package.json";
import nProgress from "nprogress";
import type { Ref } from "vue";
import type { NProgress } from "nprogress";
import type { Result } from "./Result";
import { getToken } from "../utils/auth";
declare global {
  interface ImportMeta {
    env: {
      DEV: boolean;
    };
  }
}
const instance = axios.create({
  baseURL: import.meta.env.DEV ? "" : "/" + packageJSON.name,
  withCredentials: true,
  timeout: 60000,
});

// 设置请求拦截器
instance.interceptors.request.use(
  (config: any) => {
    config.headers["Authorization"] = getToken();
    return config;
  },
  (err: any) => {
    return Promise.reject(err);
  }
);

//设置响应拦截器
instance.interceptors.response.use(
  (responce: any) => {
    if (responce.data) {
      if (responce.data.code !== 200) {
        ElMessage.error(responce.data.message);
      }
    }
    if (responce.headers["content-type"] === "application/octet-stream") {
      return responce;
    }
    return responce;
  },
  (err: any) => {
    return Promise.reject(err);
  }
);

export const request = instance;

/* 简化请求方法，统一处理返回结果，并增加loading处理，这里以{success,data,message}格式的返回值为例，具体项目根据实际需求修改 */
const promise: (
  request: Promise<any>,
  loading?: NProgress | Ref<boolean>
) => Promise<Result<any>> = (request, loading = nProgress) => {
  return new Promise((resolve, reject) => {
    if ((loading as NProgress).start) {
      (loading as NProgress).start();
    } else {
      (loading as Ref).value = true;
    }
    request
      .then((response) => {
        if (response.data.code === 200) {
          resolve(response.data);
        } else {
          reject(response.data);
        }
        if ((loading as NProgress).start) {
          (loading as NProgress).done();
        } else {
          (loading as Ref).value = false;
        }
      })
      .catch((error) => {
        reject(error);
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
  params: unknown,
  loading: NProgress | Ref<boolean>
) => Promise<Result<any>> = (url, params, loading) => {
  return promise(request({ url: url, method: "delete", params }), loading);
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
  if (import.meta.env.MODE !== "development") {
    uri = protocol + window.location.host + "/" + packageJSON.name + url;
  }
  return new WebSocket(uri);
};
export default instance;
