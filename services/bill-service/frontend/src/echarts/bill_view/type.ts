interface BillSummary {
  /**
   * 名称
   */
  name: string | "";
  /**
   *分组
   */
  group1: string;
  /**
   *分组
   */
  group2?: string;
  /**
   * 分组三
   */
  group3?: string;
  /**
   *值
   */
  value: number;
  /**
   * 存储一些元数据
   */
  meta?: any;
  /**
   * 其他数据
   */
  [propName: string]: any;
}

interface TrendData {
  /**
   *值
   */
  value: number;
  /**
   *提示
   */
  label: string;
}

export type { BillSummary, TrendData };
