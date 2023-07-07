import { post } from "@commons/request";
import type { Ref } from "vue";

const filterNullValue = (obj: any) => {
  return Object.keys(obj)
    .filter((key) => obj[key] != null && obj[key] != undefined)
    .map((key) => ({
      [key]: obj[key],
    }))
    .reduce((pre, next) => ({ ...pre, ...next }), {});
};
const getResourceMyMethod = (
  clazz: string,
  method: string,
  data: unknown,
  loading?: Ref<boolean>,
  params?: any
) => {
  params = params ? filterNullValue(params) : {};
  return post(`/api/base/provider/${clazz}/${method}`, params, data, loading);
};

const getResourceMyServiceMethod = (
  clazz: string,
  method: string,
  data: unknown,
  loading?: Ref<boolean>,
  params?: any
) => {
  params = params ? filterNullValue(params) : {};
  return post(`/api/base/service/${clazz}/${method}`, params, data, loading);
};

function getResourceMethod(
  serviceMethod: boolean | undefined,
  clazz: string,
  method: string,
  data: unknown,
  loading?: Ref<boolean>,
  params?: any
) {
  if (serviceMethod) {
    return getResourceMyServiceMethod(clazz, method, data, loading, params);
  } else {
    return getResourceMyMethod(clazz, method, data, loading, params);
  }
}

export default {
  getResourceMyMethod,
  getResourceMyServiceMethod,
  getResourceMethod,
};
