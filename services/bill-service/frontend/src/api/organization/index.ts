import { get } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { OrganizationTree } from "./type";

export const tree: (
  treeType?: string,
  loading?: Ref<boolean>
) => Promise<Result<Array<OrganizationTree>>> = (treeType, loading) => {
  const type: string = treeType === undefined ? "ORGANIZATION" : treeType;
  return get("/api/base/organization/tree/" + type, {}, loading);
};

export default {
  tree,
};
