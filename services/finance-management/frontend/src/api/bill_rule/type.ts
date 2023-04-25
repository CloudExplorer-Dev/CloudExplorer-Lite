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
   *分组唯一id 用于前端循环
   */
  id: string;
  /**
   *分组名称
   */
  name: string;
  /**
   * 分组字段
   */
  field: string;
  /**
   *分组未命中的名称
   */
  missName: string;
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

interface BillRuleRequest {
  name: string;
}

/**
 * 账单分组规则
 */
interface BillGroupRule {
  /**
   * 前段用于唯一确认id
   */
  id: string;
  /**
   * 分组字段
   */
  field: string;
  /**
   *名称
   */
  name: string;
}

interface AddRule {
  /**
   * 规则名称
   */
  name: string;
  /**
   *分组
   */
  groups: Array<Group>;
}

interface UpdateRule extends AddRule {
  /**
   * 主键id
   */
  id: string;
}
export type {
  BillRule,
  Group,
  Filter,
  BillRuleRequest,
  BillGroupRule,
  AddRule,
  UpdateRule,
};
