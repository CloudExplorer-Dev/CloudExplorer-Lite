interface Tree {
  /**
   * 当前节点唯一id
   */
  id: string;
  /**
   * 数据
   */
  value?: any;
  /**
   *子组件
   */
  children?: Array<Tree>;
  /**
   * 数据条件
   */
  items?: Array<TreeItem>;
}
interface TreeItem {
  /**
   * 节点唯一id
   */
  id: string;
  /**
   * 数据
   */
  value?: any;
}

export type { Tree, TreeItem };
