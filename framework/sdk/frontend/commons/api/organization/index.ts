import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type {
  OrganizationTree,
  SourceTreeObject,
} from "@commons/api/organization/type";
import { get } from "@commons/request";
import type { SimpleMap } from "@commons/api/base/type";
import type { Tree } from "@commons/components/ce-tree/type";

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

/**
 * 转换为Tree
 * @param organizationWorkspaceTreeData 组织工作空间树对象
 */
export const toTree = (
  organizationWorkspaceTreeData: Array<OrganizationTree>
): Array<Tree> => {
  return organizationWorkspaceTreeData.map((item) => {
    const children: Array<Tree> = item.workspaces
      ? item.workspaces.map((workspace) => ({
          id: workspace.id,
          name: workspace.name,
          type: "WORKSPACE",
        }))
      : [];

    if (item.children && item.children.length > 0) {
      const childrenTree = toTree(item.children);
      childrenTree.forEach((i) => {
        children.push(i);
      });
    }
    return {
      id: item.id,
      name: item.name,
      type: "ORGANIZATION",
      children,
    };
  });
};

const BaseOrganizationApi = {
  tree,
  sourceTree,
  sourceIdNames,
  idFullNames,
  toTree,
};

export default BaseOrganizationApi;
