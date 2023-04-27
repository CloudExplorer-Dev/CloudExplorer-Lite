import type {
  Organization,
  OrganizationTree,
} from "@commons/api/organization/type";
import type { Workspace } from "@commons/api/workspace/type";

/**
 *树节点
 */
interface TreeNode {
  /**
   *主键id
   */
  id: string;
  /**
   *名称
   */
  name: string;
  /**
   * 组织或者工作空间
   */
  type: "WORKSPACE" | "ORGANIZATION" | string;
}
/**
 * 树
 */
interface Tree {
  children?: Array<TreeNode>;
}
export type { Organization, OrganizationTree, Workspace, TreeNode, Tree };
