interface BillRule {
  /**
   *主键id
   */
  id: string;
  /**
   *名称
   */
  name: string;
  /**
   * 分组规则
   */
  groups: Array<Group>;
  /**
   *过滤
   */
  filters: Array<Filter>;
  /**
   * 创建时间
   */
  createTime: string;
  /**
   * 修改时间
   */
  updateTime: string;
}
interface Group {
  /**
   *分组名称
   */
  name: string;
  /**
   * 分组字段
   */
  field: string;
}

interface Filter {
  /**
   * 名称
   */
  name: string;
  /**
   * 字段
   */
  field: string;
  /**
   * 值
   */
  value: string | number;
}

export type { BillRule, Group, Filter };
