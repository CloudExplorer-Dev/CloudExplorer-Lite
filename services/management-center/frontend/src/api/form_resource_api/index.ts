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
export default { getResourceMyMethod };
