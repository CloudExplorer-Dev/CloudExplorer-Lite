interface FormView {
  /**
   *输入类型
   */
  inputType: string;
  /**
   * 字段名称
   */
  field: string;
  /**
   * 提示
   */
  label: string;
  /**
   * 值
   */
  value: unknown;
  /**
   * 是否必填
   */
  required: boolean;
  /**
   * 默认值
   */
  defaultValue: unknown;
  /**
   *描述
   */
  description: string;
  /**
   * 值字段
   */
  valueField?: string;
  /**
   * 文本字段
   */
  textField?: string;
  /**
   * 关联显示
   */
  relationShows: Array<string>;
  /**
   * 关联处理器
   */
  relationTrigger: Array<string>;
  /**
   * 处理器
   */
  clazz?: string | null;
  /**
   * 执行函数
   */
  method?: string | null;
  /**
   * 其他字段
   */
  [propName: string]: any;
}

export type { FormView };
