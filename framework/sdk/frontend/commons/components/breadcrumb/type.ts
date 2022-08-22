export interface Breadcrumb {
  /**
   * 路由
   */
  to: {
    /**
     * 路由name
     */
    name?: string;
    /**
     * 路由path
     */
    path?: string;
  };
  /**
   * 标题
   */
  title: string;
}
