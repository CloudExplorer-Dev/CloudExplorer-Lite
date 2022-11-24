interface PieType {
  /**
   * 值
   */
  value: number;
  /**
   * 名称
   */
  name: string;
  /**
   * 元数据
   */
  meta?: unknown;
}

/**
 * 获取账单数据图表
 * @param billData 账单数据
 * @returns        图标options
 */
const billViewOptions = (billData: Array<PieType>) => {
  // 默认选中所欲
  const selected = billData
    .map((b) => {
      return { [b.name]: true };
    })
    .reduce((pre, next) => {
      return { ...pre, ...next };
    }, {});
  return;
};

export { billViewOptions };
