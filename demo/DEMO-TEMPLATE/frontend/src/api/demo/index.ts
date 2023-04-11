import { get, post } from "@commons/request";
import type Result from "@commons/request/Result";

import type { Ref } from "vue";
import type { DemoObject } from "@/api/demo/type";

function getDemo(loading?: Ref<boolean>): Promise<Result<DemoObject>> {
  return get(`api/demo/demo`, null, loading);
}

function updateDemo(
  value?: string,
  loading?: Ref<boolean>
): Promise<Result<DemoObject>> {
  return post(`api/demo/demo`, null, { value: value }, loading);
}

const DemoApi = {
  getDemo,
  updateDemo,
};
export default DemoApi;
