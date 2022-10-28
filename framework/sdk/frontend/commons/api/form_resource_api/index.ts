import { get } from "@commons/request";
import type { Ref } from "vue";
const getResourceMyMethod = (
  clazz: string,
  method: string,
  params: unknown,
  loading?: Ref<boolean>
) => {
  return get("/api/base/provider/" + clazz + "/" + method, params, loading);
};

const getResourceMyServiceMethod = (
  clazz: string,
  method: string,
  params: unknown,
  loading?: Ref<boolean>
) => {
  return get("/api/base/service/" + clazz + "/" + method, params, loading);
};

function getResourceMethod(
  serviceMethod: boolean | undefined,
  clazz: string,
  method: string,
  params: unknown,
  loading?: Ref<boolean>
) {
  if (serviceMethod) {
    return getResourceMyServiceMethod(clazz, method, params, loading);
  } else {
    return getResourceMyMethod(clazz, method, params, loading);
  }
}

export default {
  getResourceMyMethod,
  getResourceMyServiceMethod,
  getResourceMethod,
};
