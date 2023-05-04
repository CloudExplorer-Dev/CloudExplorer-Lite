import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type {
  OrganizationTree,
  SourceTreeObject,
} from "@commons/api/organization/type";
import { get } from "@commons/request";
import type { SimpleMap } from "@commons/api/base/type";

export function tree(
  treeType?: string,
  loading?: Ref<boolean>
): Promise<Result<Array<OrganizationTree>>> {
  const type: string = treeType === undefined ? "ORGANIZATION" : treeType;
  return get("/api/organization/tree/" + type, {}, loading);
}
export function sourceTree(
  loading?: Ref<boolean>
): Promise<Result<Array<SourceTreeObject>>> {
  return get("/api/organization/sourceTree", {}, loading);
}
export function sourceIdNames(
  loading?: Ref<boolean>
): Promise<Result<SimpleMap<string>>> {
  return get("/api/organization/sourceIdNames", {}, loading);
}
export function idFullNames(
  loading?: Ref<boolean>
): Promise<Result<SimpleMap<string>>> {
  return get("/api/organization/idFullNames", {}, loading);
}

const BaseOrganizationApi = {
  tree,
  sourceTree,
  sourceIdNames,
  idFullNames,
};

export default BaseOrganizationApi;
