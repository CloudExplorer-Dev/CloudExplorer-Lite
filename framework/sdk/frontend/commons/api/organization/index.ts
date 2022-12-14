import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type {
  OrganizationTree,
  SourceTreeObject,
} from "@commons/api/organization/type";
import { get } from "@commons/request";

export function tree(
  treeType?: string,
  loading?: Ref<boolean>
): Promise<Result<Array<OrganizationTree>>> {
  const type: string = treeType === undefined ? "ORGANIZATION" : treeType;
  return get("/api/base/organization/tree/" + type, {}, loading);
}
export function sourceTree(
  loading?: Ref<boolean>
): Promise<Result<Array<SourceTreeObject>>> {
  return get("/api/base/organization/sourceTree", {}, loading);
}

const BaseOrganizationApi = {
  tree,
  sourceTree,
};

export default BaseOrganizationApi;
