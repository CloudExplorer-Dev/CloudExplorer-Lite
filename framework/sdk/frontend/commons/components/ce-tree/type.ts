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
interface Tree extends TreeNode {
  /**
   * 子数据
   */
  children?: Array<Tree>;
}
export type { TreeNode, Tree };
