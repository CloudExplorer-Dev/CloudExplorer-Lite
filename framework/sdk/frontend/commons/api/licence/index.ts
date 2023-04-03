import { get } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";

function getVersion(loading?: Ref<boolean>): Promise<Result<string>> {
  return get(
    (import.meta.env.VITE_APP_NAME === "management-center"
      ? ""
      : "/management-center/") + `api/licence/version`,
    {},
    loading
  );
}

export default { getVersion };
